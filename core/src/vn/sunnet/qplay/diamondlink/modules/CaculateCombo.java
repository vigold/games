package vn.sunnet.qplay.diamondlink.modules;

import vn.sunnet.qplay.diamondlink.logiceffects.Explode;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class CaculateCombo {
	
	public final static float DEFAULT_LIMIT = 2.5f;
	private float LIMIT = DEFAULT_LIMIT; 
	int curCombo = 1;
	float counter = 0;
	public int totalCombo = 0;
	public int comboMax = 0;
	
	GameScreen screen;
	
	public CaculateCombo(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		counter = LIMIT;
		curCombo = 0;
		comboMax = 0;
	}
	
	public void setDurationCombo(float duration) {
		this.LIMIT = duration;
	}
	
	public void update(float delta) {
		if (screen.logic.SpecialEffect == 0)
		counter = Math.max(counter - delta, 0);
		if (counter == 0) {
			curCombo = 0;
			if (screen.vip != null) {
				screen.vip.combo = 0;
//				Explode.countExplode = 0;
			}
		}
	}
	
	public void inc(int combo) {
		counter = LIMIT;
		curCombo += combo;
		comboMax = Math.max(curCombo, comboMax);
		if (curCombo / 7 > 0 && (curCombo - 7) % 5 == 0) totalCombo++;
	}
	
	public void dec(int combo) {
		curCombo -= combo;
	}
	
	public int get() {
		return curCombo;
	}
	
	public void set(int combo) {
		curCombo = combo;
	}
	
	public int getTotalCombo() {
		return totalCombo;
	}
	
	public int getComboMax() {
		return comboMax;
	}
}
