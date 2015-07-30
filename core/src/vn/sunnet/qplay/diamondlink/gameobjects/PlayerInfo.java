package vn.sunnet.qplay.diamondlink.gameobjects;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.items.Item;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.utils.Fields;
import vn.sunnet.qplay.diamondlink.utils.ModeLeaderBoard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;


public class PlayerInfo {
	
	
	public static int coin = 0;
	public static long gem = 0;
	public static long playTurns = 0;
	public static int leftDays = 0;
	
	
	public static String serverMsg = "";
	/*offline*/
	public static String offlineName = "";
	
	public static float lastScore = 0;
	public static float highScores[] = null;
	public static float bestScore;
	
	public static String names_const[] = {"Razor", "Mirana", "Sven", "Destroyer", "Barathrum","Laya","Prophet","Riki","Lina","Anti Mage"};
	public static float scores_const[] = {100000, 90000, 80000, 70000, 60000, 50000, 40000, 30000, 20000, 10000};
	
	public static int getPosition(float bestScore) {
		int i = 0;
		for (i = 0; i < scores_const.length; i++) {
			if (bestScore > scores_const[i]) break; 
		}
		return i + 1;
	}
	
	public static String[] getNames(float bestScore) {
		String names[] = new String[10];
		int pos = getPosition(bestScore);
		for (int i = 0; i < pos - 1; i++)
			names[i] = names_const[i];
		for (int i = pos - 1; i < 10; i++) {
			if (i == pos - 1) names[i] = "You";
			else names[i] = names_const[i - 1];
		}
		return names;
	}
	
	public static float[] getScores(float bestScore) {
		float scores[] = new float[10];
		int pos = getPosition(bestScore);
		for (int i = 0; i < pos - 1; i++)
			scores[i] = scores_const[i];
		for (int i = pos - 1; i < 10; i++) {
			if (i == pos - 1) scores[i] = bestScore;
			else scores[i] = scores_const[i - 1];
		}
		return scores;
	}
	
	public static float getBestScore(IFunctions iFunctions, int mode) {
		return iFunctions.getFloat("mode_"+mode, 0);
	}
	
	public static void updateBestScore(IFunctions iFunctions, int mode, float score) {
		float best = getBestScore(iFunctions, mode);
		if (best < score) {
			iFunctions.putFloat("mode_"+mode, score);
		}
	}
	
	public static void readOfflineScores(IFunctions iFunctions) {
		PlayerInfo.offlineName = iFunctions.getString("offlinename", "");
		PlayerInfo.lastScore = iFunctions.getFloat("lastscore", 0);
		int scores = iFunctions.getInt("scores", 0);
		if (scores > 0) {
			PlayerInfo.highScores = new float[scores];
			for (int i = 0; i < scores; i++) {
				PlayerInfo.highScores[i] = iFunctions.getFloat("score"+i, 0);
			}
		}
	}
	
	public static void saveOfflineScores(IFunctions iFunctions, float lastScore, float scores[]) {
		iFunctions.putString("offlinename", PlayerInfo.offlineName);
		iFunctions.putFloat("lastscore", lastScore);
		iFunctions.putInt("scores", scores.length);
		for (int i = 0; i < scores.length; i++) {
			iFunctions.putFloat("score"+i, scores[i]);
		}
	}
	
	public static void updateHighScores(float lastScore) {
		if (PlayerInfo.highScores == null) {
			PlayerInfo.highScores = new float[1];
			PlayerInfo.highScores[0] = lastScore;
		} else {
			int length = PlayerInfo.highScores.length;
			if (lastScore > PlayerInfo.highScores[length - 1] && length < 10) {
				length = length + 1;
			}
			float cost[] = new float[length];
			int number = -1;
			for (int i = 0; i < PlayerInfo.highScores.length; i++) {
				if (lastScore < PlayerInfo.highScores[i]) {
					number++;
					cost[number] = PlayerInfo.highScores[i];
				}
			}
			if (number < cost.length - 1) {
				number++;
				cost[number] = lastScore;
			}
			
			if (number < cost.length - 1) {
				for (int i = 0; i < PlayerInfo.highScores.length; i++) {
					if (lastScore >= PlayerInfo.highScores[i]) {
						number++;
						if (number == cost.length - 1) break;
						cost[number] = PlayerInfo.highScores[i];
					}
				}
			}
			
			highScores = cost;
		}
		
	}
	
	public static ModeLeaderBoard getOfflineMode(int modeID, IFunctions iFunctions) {
		return new ModeLeaderBoard(modeID, PlayerInfo.getNames(PlayerInfo.getBestScore(
				iFunctions, modeID)), PlayerInfo.getScores(PlayerInfo
				.getBestScore(iFunctions, modeID)), "You",
				(int)PlayerInfo.getBestScore(iFunctions, modeID),
				PlayerInfo.getPosition(PlayerInfo.getBestScore(iFunctions,
						modeID)));
	}
	
	public static ModeLeaderBoard parseModeData(int modeID, String result) {
		ModeLeaderBoard leader = new ModeLeaderBoard(modeID);
		leader.parseData(new JsonReader().parse(result));
		return leader;
	}
	
	public static ModeLeaderBoard[] parseModesData(String result) {
		JsonValue value = new JsonReader().parse(result);
		ModeLeaderBoard[] leaders = new ModeLeaderBoard[value.size];
		JsonValue current = value.child;
		int index = -1;
		while (current != null) {
			index++;
			int modeID = Integer.parseInt(current.getString("modeid")) - 1;
			ModeLeaderBoard leader = new ModeLeaderBoard(modeID);
			JsonValue data = current.child.next;
			leader.parseData(data);
			leaders[index] = leader;
			current = current.next;
		}
		
		return leaders;
	} 
	
}
