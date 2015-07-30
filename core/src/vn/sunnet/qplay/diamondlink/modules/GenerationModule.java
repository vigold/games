package vn.sunnet.qplay.diamondlink.modules;

import java.util.Random;

import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;





public abstract class GenerationModule {
	
	public static int MAX_SCORE_GEM = 1;
	public static int MAX_TIMES_SCORE = 8;
	
	public int[][] gridBuffer = new int[8][8];
	private Random random = new Random();
	public GameScreen screen = null;
	
	public int xScoreNum = 0;
	public int xScoreValue = 2;
	
	public GenerationModule(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
	}
	
	public abstract int[][] generateAll(); 
	
	public abstract int[][] genertateAll(int[][] grid);
	
	public abstract int[][] generatePart(int[] colHeight, int[][] grid,
			int[][] gridFlag);
	
	public boolean isDeletedWhite() {
		if (screen.getSkills() == null) return false;
		Skill skills[] = screen.getSkills();
		for (int i = 0; i < 4; i++)
			if (skills[i] != null)
				if (skills[i].type == Skill.LACKCOLOR) {
//					System.out.println("La TRUUUUUUUUUUUUUUUUUUUUUUUUUUU");
					return true;
				}
		return false;
	}
	
	
	protected boolean isDoubleFire() {
		if (screen.getSkills() == null) return false;
		Skill skills[] = screen.getSkills();
		for (int i = 0; i < 4; i++)
			if (skills[i] != null)
				if (skills[i].type == Skill.EXPLODE) return true;
		return false;
	}
	
	protected boolean is5Gems() {
		if (screen.getSkills() == null) return false;
		Skill skills[] = screen.getSkills();
		for (int i = 0; i < 4; i++)
			if (skills[i] != null)
				if (skills[i].type == Skill.SPECIAL) return true;
		return false;
	}
	
	public boolean isPercentGold() {
		if (screen.getSkills() == null) return false;
		Skill skills[] = screen.getSkills();
		for (int i = 0; i < 4; i++)
			if (skills[i] != null)
				if (skills[i].type == Skill.GOLD) return true;
		return false;
	}
}
