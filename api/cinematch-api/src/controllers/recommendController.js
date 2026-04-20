const RECOMMENDER_URL = "http://localhost:8000/recommend";

exports.getRecommendations = async (req, res) => {
  try {
    const { userId } = req.params;

    const response = await fetch(RECOMMENDER_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ userID: userId })
    });

    if (!response.ok) {
      const text = await response.text();
      throw new Error(`Recommender error: ${text}`);
    }

    const data = await response.json();

    res.json(data);

  } catch (error) {
    console.error("Recommendation error:", error.message);
    res.status(500).json({ error: error.message });
  }
};
