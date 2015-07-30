package vn.sunnet.qplay.diamondlink.modules;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.minerdiamond.DiamondOfMiner;
import vn.sunnet.qplay.diamondlink.phases.DiamondChange;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FindSolution {
	
	private GameScreen screen;
	
	MyAnimation hint;
	
	private int cells[] = new int[3];
	private int resultIndex = -1;
	private int resultAspect = -1;
	
	public final static int IDLE = 0;
	public final static int FIND = 1;
	public final static int NEW_GRID = 2;
	public final static float LIMIT = 2;
	public final float FINT_LIMIT = 0.5f;
	private int status = 0;
	
	private int[][] affectedCell = {
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
			
			{-1, -1, -1, 1},
			
			{0, -1, -1, 1}
			
	};
	
	public int[] posResults = {0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 1, 1, 2, 1, 0, 2};
	public int[] posAspects = {2, 2, 2, 3, 3, 1, 0, 1, 3, 0, 1, 3, 0, 1, 2, 0};
	public int[] aspectCells = {8, 1, -8, -1};
	
	
	float eclapsedTime = 0;
	float findTime = FINT_LIMIT;
	
	public FindSolution(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
//		Gdx.app.log("test", "find solution "+affectedCell.length / 4);
	}
	
	public void onCreated() {
		status = IDLE;
		for (int i = 0 ; i < cells.length; i++)
			cells[i] = -1;
		eclapsedTime = 0;
		TextureAtlas lAtlas = screen.manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		hint = new MyAnimation(0.2f, lAtlas.findRegions("Hint"), Animation.PlayMode.LOOP);
//		if (hint != null) System.out.println("Ton tai HInt");
	}
	
	public void resetTime() {
		eclapsedTime = 0;
		findTime = FINT_LIMIT;
		status = IDLE;
	}
	
	public float getTime() {
		return eclapsedTime;
	}
	
	public int update(float delta) {
		eclapsedTime += delta;
		if (status == IDLE && eclapsedTime > LIMIT) {
			if (!findSolution()) {
				DiamondChange phase = (DiamondChange) screen.gamePhase[GameScreen.DIAMOND_CHANGE];
				phase.setDesGrid(screen.generate.genertateAll(screen.grid));
				status = NEW_GRID;
			} else status = FIND;
		} else {
			findTime += delta;
			if (findTime > 0.5f) {
				findTime = 0;
//				System.out.println("find Solution");
				if (!findSolution()) {
					DiamondChange phase = (DiamondChange) screen.gamePhase[GameScreen.DIAMOND_CHANGE];
					phase.setDesGrid(screen.generate.genertateAll(screen.grid));
					status = NEW_GRID;
				}
			}
		} 
//		Gdx.app.log("test", "status "+status+" "+eclapsedTime);
		return status;
	}
	
	public void draw(float delta) {
		switch (status) {
		case FIND:
			if (resultAspect < aspectCells.length) {
				int row1 = cells[resultIndex] / 8;
				int col1 = cells[resultIndex] % 8;
				int row2 = (cells[resultIndex] + aspectCells[resultAspect]) / 8;
				int col2 = (cells[resultIndex] + aspectCells[resultAspect]) % 8;
				float x1 = screen.gridPos.x + col1 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
				float y1 = screen.gridPos.y + row1 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
				float x2 = screen.gridPos.x + col2 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
				float y2 = screen.gridPos.y + row2 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
				float x = (x1 + x2) / 2;
				float y = (y1 + y2) / 2;
				 
	//			Gdx.app.log("test", "status "+status+" "+cells[resultIndex]+" "+aspectCells[resultIndex]);
				TextureRegion region = hint.getKeyFrame(eclapsedTime);
				if (row1 == row2) {
					x = (float) ((x1 + x2) / 2 + 5 * Math.sin(2 * Math.PI
							* eclapsedTime / 2f));
					screen.batch.draw(region, x - 45 / 2, y - 45 / 2, 45, 45);
				} else {
					y = (float) ((y1 + y2) / 2 + 5 * Math.sin(2 * Math.PI
							* eclapsedTime / 2f));
					screen.batch.draw(region, x - 45 / 2, y - 45 / 2, 45 / 2,
							45 / 2, 45, 45, 1f, 1f, 90);
				}
//				String str = diamondType(screen.grid[row1][col1]) + " "+diamondType(screen.grid[row2][col2]) + " "+row1+" "+col1+" "+row2+" "+col2;
//				screen.comboFont.draw(screen.batch, str, x - 45 / 2, y - 45 / 2);
			} else {
				int row1 = cells[resultIndex] / 8;
				int col1 = cells[resultIndex] % 8;
				float x1 = screen.gridPos.x + col1 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
				float y1 = screen.gridPos.y + row1 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
				float x = (x1);
				float y = (y1);
				TextureRegion region = hint.getKeyFrame(eclapsedTime);
				screen.batch.draw(region, x - 45 / 2, y - 45 / 2, 45 / 2,
						45 / 2, 45, 45, 1f, 1f, 90 + 50 * eclapsedTime);
			}
			break;

		
		}
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public boolean findSolution() {
		if (checkValid(cells)) return true;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++){
				if (isFiveColorOrHyperCube(screen.grid[i][j])) {
					cells[0] = cells[1] = cells[2] = i * 8 + j;
					resultIndex = 0;
					for (int k = 0; k < aspectCells.length; k++) {
						int row = cells[resultIndex] / 8;
						int col = cells[resultIndex] % 8;
						row = (cells[resultIndex] / 8) + (aspectCells[k]) / 8;
						col = (cells[resultIndex] % 8) + (aspectCells[k]) % 8;
						if (isInGrid(row, col))
							if (isDiamond(screen.grid[row][col])) {
								resultAspect = k;
								Gdx.app.log("test", "sinh ngu sac"+cells[0]);
								return true;
							}
					}
				}
				
				if (diamondType(screen.grid[i][j]) == IDiamond.LASER_DIAMOND) {
					cells[0] = cells[1] = cells[2] = i * 8 + j;
					resultIndex  = 0;
					resultAspect = 4;
					return true;
				}
				
				for (int k = 0 ; k < affectedCell.length; k++) {
					int i1 = i + affectedCell[k][0];
					int j1 = j + affectedCell[k][1];
					int i2 = i + affectedCell[k][2];
					int j2 = j + affectedCell[k][3];
					
					if (isInGrid(i1, j1) && isInGrid(i2, j2)){
						if (diamondColor(screen.grid[i1][j1]) == diamondColor(screen.grid[i2][j2]) 
							&& diamondColor(screen.grid[i1][j1]) == diamondColor(screen.grid[i][j])
							&& isDiamond(screen.grid[i1][j1]) 
							&& isDiamond(screen.grid[i2][j2])
							&& isDiamond(screen.grid[i][j])) {
								cells[0] = i * 8 + j;
								cells[1] = i1 * 8 + j1;
								cells[2] = i2 * 8 + j2;
								resultIndex = posResults[k];
								resultAspect = posAspects[k];
								int i3 = (cells[resultIndex] + aspectCells[resultAspect]) / 8;
								int j3 = (cells[resultIndex] + aspectCells[resultAspect]) % 8;
								if (isDiamond(screen.grid[i3][j3])) {
//									Gdx.app.log("test", "ket qua"+ cells[0] +" "+cells[1]+" "+cells[2]);
									return true;
								} else {
									resultIndex = -1;
									resultAspect = -1;
								}
						}
					}
				}
			}
		
		return false;
	}
	
	public int getTargetCell() {
		return cells[resultIndex];
	}
	
	public boolean checkValid(int[] cells) {
		return false;
//		for (int i = 0 ; i  < 3 ; i++)
//			if (cells[i] == -1) return false;
//		for (int i = 0 ; i  < 3 ; i++) {
//			int row = cells[i] / 8;
//			int col = cells[i] % 8;
//			if (!isDiamond(screen.grid[row][col])) return false;
//		}
//		int row = cells[0] / 8;
//		int col = cells[0] % 8;
//		int color = diamondColor(screen.grid[row][col]);
//		for (int i = 1 ; i  < 3 ; i++) {
//			row = cells[i] / 8;
//			col = cells[i] % 8;
//			if (diamondColor(screen.grid[row][col]) != color) return false;
//		}
//		if (resultIndex == -1 || resultAspect == -1) {
//			return false;
//		} else {
//			row = (cells[resultIndex] + aspectCells[resultAspect]) / 8;
//			col = (cells[resultIndex] + aspectCells[resultAspect]) % 8;
//			if (!isDiamond(screen.grid[row][col])) return false;
//		}
		
	}
	
	private boolean isFiveColorOrHyperCube(int i) {
		if (i == -1) return false;
		int type = diamondType(i);
		return (type == DiamondOfMiner.FIVE_COLOR_DIAMOND
				|| type == DiamondOfMiner.HYPER_CUBE);
	}
	
	private boolean isDiamond(int i){
		if (i == -1) return false;
		int type = diamondType(i);
		return (type == DiamondOfMiner.NORMAL_DIAMOND
				|| type == IDiamond.BUTTERFLY_DIAMOND
				|| type == IDiamond.CLOCK_DIAMOND
				|| type == IDiamond.COIN_DIAMOND
				|| type == IDiamond.X_SCORE_GEM
				|| type == DiamondOfMiner.FIRE_DIAMOND
				|| type == DiamondOfMiner.BLINK_DIAMOND
				|| type == DiamondOfMiner.RT_DIAMOND || type == DiamondOfMiner.CT_DIAMOND);
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
}
