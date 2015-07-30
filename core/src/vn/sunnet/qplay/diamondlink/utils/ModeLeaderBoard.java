package vn.sunnet.qplay.diamondlink.utils;

import com.badlogic.gdx.utils.JsonValue;

public class ModeLeaderBoard {
	
	int modeId = -1;
	String myName = "";
	float score = 0;
	int rank = -1;
	float scores[] = null;
	String names[] = null;
	
	public ModeLeaderBoard(int modeID) {
		this.modeId = modeID;
	}
	
	public ModeLeaderBoard(int modeID, String names[], float scores[], String me, float myScore, int myRank) {
		this.modeId = modeID;
		this.names = names;
		this.scores = scores;
		this.myName = me;
		this.score = myScore;
		this.rank = myRank;
	}
	
	public int getModeId() {
		return modeId % 3;
	}
	
	public void parseData(JsonValue data) {
		JsonValue current = data.child;
		int index = -1;
		if (current != null) {
			scores = new float[data.size];
			names = new String[data.size];
		}
		while (current != null) {
			index++;
			names[index] = current.getString("name");
			scores[index] = Float.parseFloat(current.getString("score"));
			int stt = Integer.parseInt(current.getString("stt"));
			boolean me = Boolean.parseBoolean(current.getString("me"));
			System.out.println(names[index]+"|"+scores[index]+"|"+stt+"|"+me);
			if (me) {
				myName = names[index];
				rank = stt;
				score = scores[index];
			}
			current = current.next;
		}
	}
	
	public boolean isEmpty() {
		return scores == null;
	}
	
	public float[] getScores() {
		return scores;
	}
	
	
	public String[] getNames() {
		return names;
	}
	
	public String getName() {
		return myName; 
	}
	
	public float getScore() {
		return score;
	}
	
	public int getRank() {
		return rank;
	}

}
