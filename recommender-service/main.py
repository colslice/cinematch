from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict
from recommend import recommend

app = FastAPI(title="CineMatch Recommender API")

class UserInput(BaseModel):
    userID: str

@app.post("/recommend", response_model=List[Dict])
async def get_recommendations(user: UserInput):
    try:
        recs = await recommend({
            "userID": user.userID
        })
# 	return json.dumps({})
        return recs

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
