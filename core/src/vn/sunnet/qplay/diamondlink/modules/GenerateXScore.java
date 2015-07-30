package vn.sunnet.qplay.diamondlink.modules;

import java.util.ArrayList;

import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.xml.internal.bind.v2.model.core.ID;

public class GenerateXScore {
	
	public static int IDLE = 0;
	public static int GENERATE = 1;
	private int status = GENERATE;
	private final float DELAY_TIME = 3f;
	private float delay = 0;
	private int xScoreValue = 2;
	
	private GameScreen screen;
	
	public GenerateXScore(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		onCreated();
	}
	
	public void onCreated() {
		delay = DELAY_TIME;
		xScoreValue = 2;
		status = GENERATE;
	}
	
	public void update(float delta) {
		if (status == GENERATE) {
			delay = Math.max(delay - delta, 0);
			if (delay == 0) {
				delay = DELAY_TIME;
				status = IDLE;
				generateXScore();
			}
		}
//		System.out.println("XScore "+status);
	}
	
	public void draw(float delta) {
		
	}
	
	private void generateXScore() {
		ArrayList<Integer> selections = new ArrayList<Integer>();
		selections.clear();
		for (int i = 0; i < 64; i++) {
			if (screen.logic.certainCell(screen.inGridFlag[i / 8][i % 8])
					&& screen.logic.diamondType(screen.grid[i / 8][i % 8]) == IDiamond.NORMAL_DIAMOND) {
				selections.add(i);
			}
		}
		int length = selections.size();
		if (length > 0) {
			int cell = selections.get(MathUtils.random(0, length - 1));
		
			int value = screen.grid[cell / 8][cell % 8];
			int dType = IDiamond.X_SCORE_GEM;
			int dColor = screen.logic.diamondColor(value);
			screen.grid[cell / 8][cell % 8] = screen.logic.getDiamondValue(dType, dColor, xScoreValue);
			IDiamond diamond = screen.diamonds.get(cell);
			diamond.setDiamondValue(screen.grid[cell / 8][cell % 8]);
			diamond.setAction(IDiamond.REST);
		}
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void activeGenerate() {
		xScoreValue  = Math.min(xScoreValue + 1, 9);
		status = GENERATE;
		delay = DELAY_TIME;
	}
	
}
