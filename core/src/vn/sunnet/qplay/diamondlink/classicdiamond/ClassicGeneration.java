package vn.sunnet.qplay.diamondlink.classicdiamond;





import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import vn.sunnet.qplay.diamondlink.butterflydiamond.ButterflyDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.minerdiamond.DiamondOfMiner;
import vn.sunnet.qplay.diamondlink.modules.GenerationModule;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class ClassicGeneration extends GenerationModule{// tang no + 5 vien dau tien + bo mau trang
	
	public boolean color[] = new boolean[7];
	
	public final int FIRE_POSIBILITY = 4;
	public final int COIN_POSIBILITY = 5;
	public final int CLOCK_POSIBILITY = 3;
	public float fireP = 4;
	public float coinP = 5;
	public float clockP = 3;
	public int countBomb = 0;
	
	private final int CLOCK_LIMIT_TIME = 3 * 60;
	
	public ClassicGeneration(GameScreen screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}


	static int[][] grid = new int[8][8];
	private static Random random = new Random();
	
	
	public int[][] affectedCell = {
			{-1, -1, - 1, -2},
			{-2,  0,  -3,  0},
			{-1,  1,  -1,  2},
			
			{ 0, -2,   0, -3},
			{-1, -1,  -2, -1},
			
			{-1,  1,  -2,  1},
			
			{ 0, -2,  -1, -1},
			{-2,  0,  -1, -1},
			
			{-2,  0,  -1,  1},
			
			{-1,  -2,  0, -1},
			
			{-2,  -1, -1,  0},
			
			{-2,   1, -1,  0},
			
			{-1,   0, -3,  0},
			
			{ 0,  -3,  0, -1},
			
			{-1, -1, -1, 1}
	};
	
	public  int[][] generateAll() {
		Gdx.app.log("Generate", "sinh all");
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++)
				gridBuffer[i][j] = -1;
		int solutions = 10;
		int numberSpecial = 0;
		if (is5Gems()) numberSpecial += 5;
		
		for (int i = 0 ; i < 8 ; i ++) 
			for (int j = 0 ; j < 8 ; j++) {
				for (int k = 0 ; k < 7 ; k++)
					color[k] = false;
				
				if (i > 1)
					if (diamondColor(gridBuffer[i - 1][j]) == diamondColor(gridBuffer[i - 2][j]))
						color[diamondColor(gridBuffer[i - 1][j])] = true;
				if (j > 1)
					if (diamondColor(gridBuffer[i][j - 1]) == diamondColor(gridBuffer[i][j - 2]))
						color[diamondColor(gridBuffer[i][j - 2])] = true;
				if (isDeletedWhite())
					color[Diamond.WHITE] = true;
				if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) color[Diamond.ORANGE] = true;
				
				int isChosen = MathUtils.random(0, 1);
				boolean hasSol = false;
				if (64 - (i * 8 + j) == solutions ) isChosen = 1;
				if (solutions == 0) isChosen = 0;
				if (isChosen == 1)
				for (int k = 0 ; k < affectedCell.length ; k++) {
					int i1 = i + affectedCell[k][0];
					int j1 = j + affectedCell[k][1];
					int i2 = i + affectedCell[k][2];
					int j2 = j + affectedCell[k][3];
						if (isInGrid(i1, j1) && isInGrid(i2, j2)) {
							if (diamondColor(gridBuffer[i1][j1]) == diamondColor(gridBuffer[i2][j1])
									&& !color[diamondColor(gridBuffer[i1][j1])]) {
								// Log.d("Generate",
								// "duoc chinh "+i+" "+j+" "+grid[i1][j1]);

								gridBuffer[i][j] = generateValue(diamondColor(gridBuffer[i1][j1]));
								solutions--;
								hasSol = true;
								break;
							}
						}
				}
				if (!hasSol) {
					int number = 6;
					for (int k = 0 ; k < 7 ; k++)
						if (color[k]) number--;
					int index = MathUtils.random(0, number);
					number = -1;
					for (int k = 0 ; k < 7 ; k++)
						if (!color[k]) {
							number++;
							if (number == index) {
								gridBuffer[i][j] = generateValue(k);
								//Log.d("Generate", "ko duoc chinh "+i+" "+j+" "+grid[i][j]);
								hasSol = true;
								break;
							}
						}
				}
				
				if (gridBuffer[i][j] < 7 && numberSpecial > 0) {
					int result = random.nextInt(2);
					if (numberSpecial == 64 - (i * 8 + j)) result = 0;
					if (result == 1) {
						numberSpecial--;
						gridBuffer[i][j] = screen.logic.getDiamondValue(MathUtils.random(
								Diamond.FIRE_DIAMOND, Diamond.CT_DIAMOND), gridBuffer[i][j], 0);
					}
				}
				if (!hasSol) Gdx.app.log("Generate", "loi o "+i+" "+j);
			}
		
//		gridBuffer[0][3] = Diamond.BUTTERFLY_DIAMOND * screen.COLOR_NUM + Diamond.GREEN;
//		gridBuffer[1][0] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + 0;
//		gridBuffer[1][1] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + 0;
//		gridBuffer[2][2] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + 0;
//		gridBuffer[1][3] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + 0;
//		gridBuffer[1][4] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + 0;
//		gridBuffer[4][3] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + Diamond.GREEN;
//		gridBuffer[5][3] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM + Diamond.GREEN;
		
//		gridBuffer[1][0] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[1][1] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[2][0] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[2][1] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[0][2] = Diamond.BUTTERFLY_DIAMOND * screen.COLOR_NUM + 1;
//		gridBuffer[3][2] = Diamond.NORMAL_DIAMOND * screen.COLOR_NUM;
		
//		gridBuffer[5][2] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[5][3] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[5][4] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[5][5] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[5][6] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[5][7] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
		return gridBuffer;
	}
	
	
	@Override
	public int[][] genertateAll(int[][] grid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (diamondType(screen.grid[i][j]) != IDiamond.NORMAL_DIAMOND) 
					gridBuffer[i][j] = grid[i][j];
				else gridBuffer[i][j] = -1;
			}
		int solutions = 10;
		
		for (int i = 0 ; i < 8 ; i ++) 
			for (int j = 0 ; j < 8 ; j++) {
				if (gridBuffer[i][j] != -1) continue;
				for (int k = 0 ; k < 7 ; k++)
					color[k] = false;
				if (i > 1) 
					if (isDiamond(gridBuffer[i - 1][j])
							&& isDiamond(gridBuffer[i - 2][j]))
						if (diamondColor(gridBuffer[i - 1][j]) == diamondColor(gridBuffer[i - 2][j]))
							color[diamondColor(gridBuffer[i - 1][j])] = true;
				if (j > 1)
					if (isDiamond(gridBuffer[i][j - 1])
							&& isDiamond(gridBuffer[i][j - 2]))
						if (diamondColor(gridBuffer[i][j - 1]) == diamondColor(gridBuffer[i][j - 2]))
							color[diamondColor(gridBuffer[i][j - 2])] = true;
				if (isDeletedWhite()) {
					color[Diamond.WHITE] = true;
				}
				if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) {
					color[Diamond.ORANGE] = true;
					System.out.println("co loai bo");
				}
				int isChosen = MathUtils.random(0, 1);
				boolean hasSol = false;
				if (64 - (i * 8 + j) == solutions ) isChosen = 1;
				if (solutions == 0) isChosen = 0;
				if (isChosen == 1)
					for (int k = 0; k < affectedCell.length; k++) {
						int i1 = i + affectedCell[k][0];
						int j1 = j + affectedCell[k][1];
						int i2 = i + affectedCell[k][2];
						int j2 = j + affectedCell[k][3];
						if (isInGrid(i1, j1) && isInGrid(i2, j2)) {
							if (isDiamond(gridBuffer[i1][j1])
									&& isDiamond(gridBuffer[i2][j2]))
								if (diamondColor(gridBuffer[i1][j1]) == diamondColor(gridBuffer[i2][j1])
										&& !color[diamondColor(gridBuffer[i1][j1])]) {
									gridBuffer[i][j] = screen.logic
											.getDiamondValue(
													IDiamond.NORMAL_DIAMOND,
													diamondColor(gridBuffer[i1][j1]),
													0);

									solutions--;
									hasSol = true;
									break;
								}
						}
				}
				if (!hasSol) {
					int number = 6;
					for (int k = 0 ; k < 7 ; k++)
						if (color[k]) number--;
					int index = MathUtils.random(0, number);
					number = -1;
					for (int k = 0 ; k < 7 ; k++)
						if (!color[k]) {
							number++;
							if (number == index) {
								gridBuffer[i][j] = screen.logic
										.getDiamondValue(
												IDiamond.NORMAL_DIAMOND, k, 0);
								//Log.d("Generate", "ko duoc chinh "+i+" "+j+" "+grid[i][j]);
								hasSol = true;
								break;
							}
						}
				}
				if (!hasSol) Gdx.app.log("Generate", "loi o "+i+" "+j);
			}
//		gridBuffer[0][0] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[0][1] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
		return gridBuffer;
	}
	

	public void swap(int x1, int y1, int x2, int y2) {
		int temp = gridBuffer[x1][y1];
		gridBuffer[x1][y1] = gridBuffer[x2][y2];
		gridBuffer[x2][y2] = temp;
	}
	
	public int[][] generatePart(int[] colHeight, int[][] gridFurture,
			int[][] gridFlag) {
		// TODO Auto-generated method stub
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++)
				if (colHeight[j] - 1 < i) {
					grid[i][j] = generateValue(random.nextInt(7));
					int type = diamondType(grid[i][j]);
					int color = diamondColor(grid[i][j]);
					if (isDeletedWhite()) 
						if (color == Diamond.WHITE)
							grid[i][j] = screen.logic.getDiamondValue(type, 0, 0);
					if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
						if (color == Diamond.ORANGE) grid[i][j] = screen.logic.getDiamondValue(type, 6 - Diamond.ORANGE, 0);
					
				}
				else grid[i][j] = -1;
		return grid;
	}
	
	private int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	private int diamondColor(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
	}
	
	private boolean isInGrid(int i, int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
	
	private int generateValue(int color) {
		int p = random.nextInt(100);
		fireP = FIRE_POSIBILITY;
		coinP = COIN_POSIBILITY;
		clockP = CLOCK_POSIBILITY;
		if (isDoubleFire())
			fireP = 2f * fireP;
		
		if (random.nextInt(2) == 0) {
			if (p > 100 - clockP) {
				if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND)
					return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND,
							color, 0);
				if (screen.gameTime > CLOCK_LIMIT_TIME)
					return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND,
							color, 0);
				return screen.logic.getDiamondValue(IDiamond.CLOCK_DIAMOND, color,
						0);
			} else if (p > 100 - fireP - coinP) {
				return screen.logic
						.getDiamondValue(IDiamond.COIN_DIAMOND, color, 0);
			} else if (p > 100 - fireP - coinP - clockP) {
				return screen.logic
						.getDiamondValue(IDiamond.FIRE_DIAMOND, color, 0);
			}
		} else {
			if (p < fireP) {
				return screen.logic
						.getDiamondValue(IDiamond.FIRE_DIAMOND, color, 0);
			} else if (p < fireP + coinP) {
				return screen.logic
						.getDiamondValue(IDiamond.COIN_DIAMOND, color, 0);
			} else if (p < fireP + coinP + clockP) {
				if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND)
					return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND,
							color, 0);
				if (screen.gameTime > CLOCK_LIMIT_TIME) 
					return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND,
							color, 0);
				return screen.logic.getDiamondValue(IDiamond.CLOCK_DIAMOND, color,
						0);
			}
		}
		return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND, color, 0);
	}

	private boolean isDiamond(int i){
		int type = diamondType(i);
		return (type == DiamondOfMiner.NORMAL_DIAMOND
				|| type == DiamondOfMiner.FIRE_DIAMOND
				|| type == DiamondOfMiner.BLINK_DIAMOND
				|| type == DiamondOfMiner.RT_DIAMOND || type == DiamondOfMiner.CT_DIAMOND);
	}
	
	
	
	
}
