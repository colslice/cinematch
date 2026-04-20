# recommend.py
import httpx
import asyncio
import math
import random
from typing import List, Dict, Set, Tuple
from collections import defaultdict

BASE_URL = "https://cop4331project.xyz/api"
TMDB_API_KEY = "a8723920bedcd11fd3ef05c34f141af0"
TMDB_BASE = "https://api.themoviedb.org/3"

GENRE_MAP = {
    28: "Action", 12: "Adventure", 16: "Animation", 35: "Comedy",
    80: "Crime", 99: "Documentary", 18: "Drama", 10751: "Family",
    14: "Fantasy", 36: "History", 27: "Horror", 10402: "Music",
    9648: "Mystery", 10749: "Romance", 878: "Sci-Fi",
    10770: "TV Movie", 53: "Thriller", 10752: "War", 37: "Western"
}

# TMDB's internal provider IDs for the US
PROVIDER_ID_MAP = {
    "netflix": 8,
    "hulu": 15,
    "amazon prime video": 9,
    "amazon prime": 9,
    "prime": 9,
    "disney+": 337,
    "disney plus": 337,
    "max": 1899,
    "hbo max": 1899,
    "peacock": 386,
    "peacock premium": 386,
    "apple tv+": 350,
    "apple tv plus": 350,
    "paramount+": 531,
    "paramount plus": 531,
    "tubi": 73,
    "pluto tv": 300,
    "mubi": 11,
    "shudder": 99,
    "criterion channel": 258,
}

# How much randomness to apply to scores.
# At 1.5, films with a score difference smaller than 1.5 can trade places,
# producing meaningful variety across calls while still letting clearly
# superior matches win.
JITTER_SCALE = 1.5

# Maximum results that share the same PRIMARY genre in the final output.
# Only the first genre TMDB lists for a film counts against the cap,
# so a multi-genre film like Nimona (8 tags) doesn't consume 8 slots.
GENRE_CAP = 2

# Minimum quality thresholds — filters out obscure or poor-quality films.
MIN_VOTE_AVERAGE = 7.0
MIN_VOTE_COUNT = 100

# Number of /discover pages to fetch per call.
# Each page is ~20 results, so 5 pages = ~100 candidates per call.
DISCOVER_PAGES = 5


# --- HELPERS ---

def normalize(text: str) -> str:
    return text.lower().strip()


def genre_ids_to_names(ids: List[int]) -> List[str]:
    return [GENRE_MAP[i] for i in ids if i in GENRE_MAP]


def normalize_fav_genres(raw) -> Set[str]:
    """Handle genre IDs (int), name strings, or dicts from the API."""
    names = set()
    for g in raw:
        if isinstance(g, int) and g in GENRE_MAP:
            names.add(GENRE_MAP[g])
        elif isinstance(g, str):
            names.add(g)
        elif isinstance(g, dict):
            if "name" in g:
                names.add(g["name"])
            elif "id" in g and g["id"] in GENRE_MAP:
                names.add(GENRE_MAP[g["id"]])
    return names


def resolve_provider_ids(raw_services: List[str]) -> List[int]:
    """Map user's service names to TMDB provider IDs, dropping unrecognized ones."""
    ids = []
    for s in raw_services:
        pid = PROVIDER_ID_MAP.get(normalize(s))
        if pid is not None:
            ids.append(pid)
    return list(set(ids))


# --- API HELPERS ---

async def fetch_json(client, url, params=None):
    r = await client.get(url, params=params, timeout=10)
    r.raise_for_status()
    return r.json()


async def get_user_data(client, user_id: str):
    return await asyncio.gather(
        fetch_json(client, f"{BASE_URL}/users/{user_id}/genres/"),
        fetch_json(client, f"{BASE_URL}/reviews/user/{user_id}"),
        fetch_json(client, f"{BASE_URL}/users/{user_id}/services/"),
    )


async def get_movie_genres(client, movie_id: int) -> List[str]:
    """Fetch full genre list for a specific movie from TMDB."""
    try:
        data = await fetch_json(
            client,
            f"{TMDB_BASE}/movie/{movie_id}",
            params={"api_key": TMDB_API_KEY},
        )
        return [g["name"] for g in data.get("genres", [])]
    except Exception:
        return []


async def fetch_providers_for_movie(client, movie_id: int) -> List[str]:
    """
    Fetch the US flatrate (subscription) streaming providers for a single movie.
    Called only on the final result set — never on the full candidate pool.
    """
    try:
        data = await fetch_json(
            client,
            f"{TMDB_BASE}/movie/{movie_id}/watch/providers",
            params={"api_key": TMDB_API_KEY},
        )
        us = data.get("results", {}).get("US", {})
        return [p["provider_name"] for p in us.get("flatrate", [])]
    except Exception:
        return []


async def batch_fetch_providers(
    client, movie_ids: List[int]
) -> Dict[int, List[str]]:
    """
    Fetch providers for a list of movie IDs in parallel.
    Only called against the final results (~10 movies), not the full
    candidate pool, so the request count stays small.
    """
    results = await asyncio.gather(
        *[fetch_providers_for_movie(client, mid) for mid in movie_ids],
        return_exceptions=True,
    )
    return {
        mid: (res if not isinstance(res, Exception) else [])
        for mid, res in zip(movie_ids, results)
    }


async def discover_movies(
    client,
    provider_ids: List[int],
    genre_ids: List[int],
    pages: int = DISCOVER_PAGES,
) -> List[Dict]:
    """
    Fetch movies from TMDB /discover that are:
      - Available on the user's streaming services (OR logic)
      - In the targeted genres (OR logic)
      - English language only
      - Above minimum vote count
      - Sorted by vote_average descending for quality-first candidates
    Fetches multiple pages in parallel for a wider candidate pool.
    """
    page_results = await asyncio.gather(
        *[
            fetch_json(
                client,
                f"{TMDB_BASE}/discover/movie",
                params={
                    "api_key": TMDB_API_KEY,
                    "watch_region": "US",
                    "region": "US",
                    "with_watch_providers": "|".join(str(p) for p in provider_ids),
                    "with_genres": "|".join(str(g) for g in genre_ids),
                    "sort_by": "vote_average.desc",
                    "vote_count.gte": MIN_VOTE_COUNT,
                    "with_original_language": "en",
                    "language": "en-US",
                    "page": page,
                },
            )
            for page in range(1, pages + 1)
        ],
        return_exceptions=True,
    )

    movies = []
    for r in page_results:
        if not isinstance(r, Exception):
            movies.extend(r.get("results", []))
    return movies


async def discover_by_providers_only(
    client,
    provider_ids: List[int],
    pages: int = DISCOVER_PAGES,
) -> List[Dict]:
    """
    Fallback: fetch popular English-language movies on the user's services
    with no genre filter. Sorted by popularity for culturally familiar films.
    Fetches multiple pages in parallel to pad the candidate pool.
    """
    page_results = await asyncio.gather(
        *[
            fetch_json(
                client,
                f"{TMDB_BASE}/discover/movie",
                params={
                    "api_key": TMDB_API_KEY,
                    "watch_region": "US",
                    "region": "US",
                    "with_watch_providers": "|".join(str(p) for p in provider_ids),
                    "sort_by": "popularity.desc",
                    "vote_count.gte": MIN_VOTE_COUNT,
                    "with_original_language": "en",
                    "language": "en-US",
                    "page": page,
                },
            )
            for page in range(1, pages + 1)
        ],
        return_exceptions=True,
    )

    movies = []
    for r in page_results:
        if not isinstance(r, Exception):
            movies.extend(r.get("results", []))
    return movies


# --- GENRE PROFILE BUILDER ---

async def build_genre_profile(
    client, reviews: List[Dict]
) -> Tuple[Dict[str, float], Dict[str, float]]:
    """
    Fetch actual TMDB genre data for every reviewed movie and build
    liked/disliked genre weight maps.

    Rating -> weight:
      5 -> +1.0,  4 -> +0.5,  3 -> skip,  2 -> -0.5,  1 -> -1.0

    A genre only enters the disliked map if:
      - At least 2 different films contributed the signal, AND
      - It isn't already more strongly liked
    This prevents one bad film from tanking a genre the user demonstrably enjoys.
    """
    genre_lists = await asyncio.gather(
        *[get_movie_genres(client, r["movieId"]) for r in reviews],
        return_exceptions=True,
    )

    liked_weights: Dict[str, float] = defaultdict(float)
    disliked_raw: Dict[str, float] = defaultdict(float)
    disliked_counts: Dict[str, int] = defaultdict(int)

    for review, genres in zip(reviews, genre_lists):
        if isinstance(genres, Exception) or not genres:
            continue

        rating = review["rating"]
        if rating == 3:
            continue

        weight = (rating - 3) / 2.0  # 5->+1.0, 4->+0.5, 2->-0.5, 1->-1.0

        for genre in genres:
            if weight > 0:
                liked_weights[genre] += weight
            else:
                disliked_raw[genre] += abs(weight)
                disliked_counts[genre] += 1

    disliked_weights: Dict[str, float] = {
        g: w
        for g, w in disliked_raw.items()
        if disliked_counts[g] >= 2
        and liked_weights.get(g, 0.0) < w
    }

    return dict(liked_weights), disliked_weights


# --- MAIN RECOMMENDER ---

async def recommend(user: Dict, max_results: int = 10) -> List[Dict]:
    user_id = str(user["userID"])

    async with httpx.AsyncClient() as client:
        fav_genres_raw, reviews, services_raw = await get_user_data(client, user_id)

        provider_ids = resolve_provider_ids(services_raw)
        fav_genre_names: Set[str] = normalize_fav_genres(fav_genres_raw)
        rated_ids: Set[int] = {r["movieId"] for r in reviews}

        NAME_TO_ID = {v: k for k, v in GENRE_MAP.items()}

        # --- Cold start: no reviews yet ---
        if not reviews:
            if not provider_ids:
                return []

            if fav_genre_names:
                fav_genre_ids = [NAME_TO_ID[g] for g in fav_genre_names if g in NAME_TO_ID]
                candidates = await discover_movies(client, provider_ids, fav_genre_ids)
            else:
                candidates = await discover_by_providers_only(client, provider_ids)

            candidates = _apply_quality_filter(candidates)
            random.shuffle(candidates)
            final = candidates[:max_results]
            provider_map = await batch_fetch_providers(client, [m["id"] for m in final])
            return _format_results(final, provider_map)

        # --- Build genre profile from rating history ---
        liked_genre_weights, disliked_genre_weights = await build_genre_profile(
            client, reviews
        )

        # Boost declared favourite genres (lighter than earned signal)
        for g in fav_genre_names:
            liked_genre_weights[g] = liked_genre_weights.get(g, 0.0) + 0.75

        # --- Candidate generation via /discover ---
        top_genres = sorted(
            liked_genre_weights.items(), key=lambda x: x[1], reverse=True
        )
        top_genre_names = [g for g, _ in top_genres[:4]]
        top_genre_ids = [NAME_TO_ID[g] for g in top_genre_names if g in NAME_TO_ID]

        # Fetch genre-targeted and broad candidates in parallel.
        # Every result is already on the user's services — no provider
        # filtering needed anywhere downstream.
        targeted, broad = await asyncio.gather(
            discover_movies(client, provider_ids, top_genre_ids),
            discover_by_providers_only(client, provider_ids),
        )

        # Merge: targeted first for priority, broad as padding
        raw_candidates = targeted + broad

        # Deduplicate, strip already-rated, and apply quality floor
        seen: Set[int] = set()
        candidates: List[Dict] = []
        for m in raw_candidates:
            mid = m["id"]
            if mid in seen or mid in rated_ids:
                continue
            seen.add(mid)
            candidates.append(m)

        candidates = _apply_quality_filter(candidates)

        # --- Score ---
        scored: List[Tuple[float, Dict]] = []

        for m in candidates:
            movie_genres = set(genre_ids_to_names(m.get("genre_ids", [])))

            liked_score = sum(liked_genre_weights.get(g, 0.0) for g in movie_genres)
            disliked_penalty = sum(
                disliked_genre_weights.get(g, 0.0) for g in movie_genres
            )

            # Skip movies with no positive genre signal
            if liked_score == 0.0 and not (movie_genres & fav_genre_names):
                continue

            # Skip movies where dislike signal dominates with no liked offset
            if disliked_penalty > 0 and liked_score == 0.0:
                continue

            vote_score = m.get("vote_average", 0.0) / 10.0
            pop_score = math.log1p(m.get("popularity", 0.0)) / 10.0

            score = (
                liked_score * 3.0
                - disliked_penalty * 2.5
                + vote_score * 1.5
                + pop_score * 0.5
                + random.uniform(-JITTER_SCALE, JITTER_SCALE)
            )

            scored.append((score, m))

        scored.sort(key=lambda x: x[0], reverse=True)

        # --- Build results with genre diversity cap ---
        # The cap applies to a film's PRIMARY genre only (first in TMDB's list).
        # This prevents a multi-genre film from consuming multiple cap slots
        # and starving the rest of the results.
        results_raw: List[Dict] = []
        used: Set[int] = set()
        genre_counts: Dict[str, int] = defaultdict(int)

        for _, m in scored:
            if m["id"] in used:
                continue

            all_genres = genre_ids_to_names(m.get("genre_ids", []))
            primary_genre = all_genres[0] if all_genres else None

            if primary_genre and genre_counts[primary_genre] >= GENRE_CAP:
                continue

            used.add(m["id"])
            if primary_genre:
                genre_counts[primary_genre] += 1

            results_raw.append(m)

            if len(results_raw) >= max_results:
                break

        # --- Fetch providers for final results only ---
        # Targeted batch of ~10 requests rather than the entire candidate pool.
        provider_map = await batch_fetch_providers(
            client, [m["id"] for m in results_raw]
        )

        return _format_results(results_raw, provider_map)


# --- HELPERS ---

def _apply_quality_filter(movies: List[Dict]) -> List[Dict]:
    """Remove films below the minimum vote average or vote count thresholds."""
    return [
        m for m in movies
        if m.get("vote_average", 0.0) >= MIN_VOTE_AVERAGE
        and m.get("vote_count", 0) >= MIN_VOTE_COUNT
    ]


def _format_movie(m: Dict, providers: List[str]) -> Dict:
    return {
        "id": m["id"],
        "title": m.get("title"),
        "overview": m.get("overview"),
        "poster_path": m.get("poster_path"),
        "backdrop_path": m.get("backdrop_path"),
        "genres": genre_ids_to_names(m.get("genre_ids", [])),
        "release_date": m.get("release_date"),
        "vote_average": m.get("vote_average"),
        "providers": providers,
    }


def _format_results(movies: List[Dict], provider_map: Dict[int, List[str]]) -> List[Dict]:
    return [_format_movie(m, provider_map.get(m["id"], [])) for m in movies]


if __name__ == "__main__":
    async def test():
        recs = await recommend({"userID": "123"})
        for r in recs:
            print(f"{r['title']} ({r['vote_average']}) — {r['genres']} — {r['providers']}")

    asyncio.run(test())
