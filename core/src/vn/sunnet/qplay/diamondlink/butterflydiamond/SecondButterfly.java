package vn.sunnet.qplay.diamondlink.butterflydiamond;


import java.util.ArrayList;
import java.util.Random;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.logiceffects.TempEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.phases.DiamondMove;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



public class SecondButterfly extends DiamondMove {

	ArrayList<Integer> selections = new ArrayList<Integer>();
	Random random = new Random();
	
	public SecondButterfly(ButterflyDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		super.onBegin();
		screen.solution.resetTime();
	}

	public void ActionHandle() {
		IDiamond diamond = null;
		// TODO Auto-generated method stub
		super.ActionHandle();
		if (getState() == ON_RUNNING) {
			ButterflyDiamond game = (ButterflyDiamond) screen;
			for (int i = 0 ; i < game.butterflyOut.size() ; i++) {
				diamond = game.butterflyOut.get(i);
				diamond.update(deltaTime);
				if (diamond.isFinished(IDiamond.FLY)) {
					game.butterflyNum++;
					game.butterflyOut.remove(i);
					i--;
				}
			}
		}
		if (branch == GameScreen.DIAMOND_ANIMATION) {
			initButterflyMove();
		}
	}
	
	public void baseMove() {
		ButterflyDiamond game = (ButterflyDiamond) screen;
		boolean generateE = false;
		for (int i = 0 ; i < Math.min(screen.diamonds.size() , 64) ; i++) {
			IDiamond diamond = screen.diamonds.get(i);
			int u = CellRow(i);
			int v = CellCol(i);
			
			int value = screen.inGridFlag[u][v];
			if (certainCell(value) || isUpToGrid(i)) {
				diamond.update(deltaTime);
				//if (Operator.getBit(Effect.ONE_ASPECT_SWAP, value) > 0)
				//Log.d("ButterflyList", "base Move out"+value);
			}
			
			if (Operator.getBit(Effect.UP_TO_GRID, value) > 0) {
				if (diamond.containsAction(IDiamond.UP_TO_GRID)) {
					if (diamond.isFinished(IDiamond.UP_TO_GRID)) {
//						Gdx.app.log("test", "end to UpToGrid");
						game.spider.huntFlag = true;
					}
				}
			}
		}
		
		if (generateE) {
			if (screen.logic.state == GameLogic.ON_END)
				screen.logic.state = GameLogic.ON_RUNNING;
			game.spider.huntFlag = true;
		}
	}
    
	public void SwapMove() {// tra ve true neu thuc hien 1 lan logic con lai tra ve false
		IDiamond diamond1 = null;
		IDiamond diamond2 = null;
		diamond1 = screen.diamonds.get(sCell);
		diamond2 = screen.diamonds.get(dCell);
		//diamond1.update(deltaTime);
		//diamond2.update(deltaTime);
		
		boolean isDestination = diamond1.isDestination() && diamond2.isDestination();
		if (isDestination)
			if (!backFlag ) {
				if (!goAspect()) {
					Gdx.app.log("Game", "End go Aspect in Animation");
					ButterflyDiamond game = (ButterflyDiamond) screen;
					if (diamond1.getDiamondType() == IDiamond.BUTTERFLY_DIAMOND &&
							diamond2.getDiamondType() != IDiamond.BUTTERFLY_DIAMOND) {
							game.spider.addButterfly(sCell);
							game.spider.removeButterfly(dCell);
							Gdx.app.log("test", "add"+sCell+"remove"+dCell);
						} else 
						if (diamond1.getDiamondType() != IDiamond.BUTTERFLY_DIAMOND &&
							diamond2.getDiamondType() == IDiamond.BUTTERFLY_DIAMOND) {
							game.spider.addButterfly(dCell);
							game.spider.removeButterfly(sCell);
							Gdx.app.log("test", "add"+dCell+"remove"+sCell);
						}
				}; 
			} else {
				Gdx.app.log("test", "End return Aspect in Animation");
				returnAspect();
			}
	}
	
	public void initButterflyMove() {
		ButterflyDiamond game = (ButterflyDiamond) screen;
		DiamondOfButterfly diamond1 = null;
		DiamondOfButterfly diamond2 = null;
		ObjectMap<Integer, IDiamond> butterflies = game.spider.butterflies;
		boolean remove[] = new boolean[64];
		boolean add[] = new boolean[64];
		game.spider.repairButterfly();
		for (int i = 0; i < 64; i++) {
			remove[i] = false;
			add[i] = false;
		}
		
		int generate = 1;
		game.butterflyStep = 1;
		if (game.butterflyNum < 20) generate = 1;
		else
		if (game.butterflyNum < 50) generate = 2;
		else {
			generate = 3;
			if (game.butterflyNum < 100) game.butterflyStep = 1;
			else if (game.butterflyNum < 200) game.butterflyStep = 2;
			else game.butterflyStep = 3;
		}
//		game.butterflyStep = 2;
//		Gdx.app.log("test", "+++++++++++++++++++++++++++++");
		for (Integer cell: game.spider.butterflies.keys()) {
			int i = cell / 8; int j = cell % 8;
			if (isExistDiamod(i, j)) {
				if (i == 7) {
					if (!isCertainCol(i, j)) {
						Gdx.app.log("test", "bi chat chan o "+i+" "+j);
						continue;
					}
					diamond1 = (DiamondOfButterfly) butterflies.get(cell);
					float x1 = screen.gridPos.x + j * 60 + 30;
					float y1 = screen.gridPos.y + i * 60 + 30;
					diamond1.setDiamondValue(screen.grid[i][j]);
					diamond1.setSource(x1, y1);
					diamond1.setDestination(x1, y1 + 30);
					diamond1.setAction(IDiamond.UP_TO_GRID);
					screen.inGridFlag[i][j] = Operator.offBit(Effect.FIXED_POS, screen.inGridFlag[i][j]);
					screen.inGridFlag[i][j] = Operator.onBit(Effect.UP_TO_GRID, screen.inGridFlag[i][j]);
					Gdx.app.log("test", "UpToGrid"+i+" "+j);
					game.spider.huntFlag = true;
				} else {
					if (isExistDiamod(i + 1, j)
							&& diamondType(screen.grid[i + 1][j]) != IDiamond.BUTTERFLY_DIAMOND) {
						int value = screen.grid[i][j];
						screen.grid[i][j] = screen.grid[i + 1][j];
						screen.grid[i + 1][j] = value;
						diamond1 = (DiamondOfButterfly) butterflies.get(cell);
						diamond2 = (DiamondOfButterfly) screen.diamonds.get((i + 1) * 8 +j);
						diamond1.setDiamondValue(screen.grid[i][j]);
						diamond2.setDiamondValue(screen.grid[i + 1][j]);
						
						diamond2.setUpStep(game.butterflyStep);
						diamond1.setUpStep(0);
						
						float x1 = screen.gridPos.x + j * 60 + 30;
						float y1 = screen.gridPos.y + i * 60 + 30;
						float x2 = screen.gridPos.x + j * 60 + 30;
						float y2 = screen.gridPos.y + (i + 1) * 60 + 30;
						
						diamond1.setSource(x2, y2);
						diamond1.setDestination(x1, y1);
						diamond1.setAction(IDiamond.ONE_ASPECT_SWAP);
						
						diamond2.setSource(x1, y1);
						diamond2.setDestination(x2, y2);
						diamond2.setAction(IDiamond.ONE_ASPECT_SWAP);
						
						screen.inGridFlag[i][j] = Operator.offBit(Effect.FIXED_POS, screen.inGridFlag[i][j]); 
						screen.inGridFlag[i + 1][j] = Operator.offBit(Effect.FIXED_POS, screen.inGridFlag[i + 1][j]);
						screen.inGridFlag[i][j] = Operator.onBit(Effect.ONE_ASPECT_SWAP, screen.inGridFlag[i][j]);
						screen.inGridFlag[i + 1][j] = Operator.onBit(Effect.ONE_ASPECT_SWAP, screen.inGridFlag[i + 1][j]);
						remove[i * 8 + j] = true;
						add[(i + 1) * 8 + j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 64; i++) {
			if (remove[i]) game.spider.removeButterfly(i);
			if (add[i]) game.spider.addButterfly(i);
		}

		selections.clear();
		int numberFree = 0;
		for (int i = 0; i < 8; i++)
			if (certainCell(screen.inGridFlag[0][i])
					&& diamondType(screen.grid[0][i]) != IDiamond.BUTTERFLY_DIAMOND) {
				numberFree++;
				selections.add(i);
			}
		
		
		
		System.out.println("sinh buom o diamond move "+generate+" "+numberFree);
		
		generate = Math.min(generate, numberFree);
		
		for (int i = 0; i < generate; i++) {
			int col = selections.remove(random.nextInt(selections.size()));
			int dType = screen.logic.diamondType(screen.grid[0][col]);
			int dColor = screen.logic.diamondColor(screen.grid[0][col]);
			dType = IDiamond.BUTTERFLY_DIAMOND;
			IDiamond diamond = screen.diamonds.get(col);
			diamond.setDiamondValue(screen.logic.getDiamondValue(dType, dColor, 0));
			diamond.setAction(IDiamond.FRAME_CHANGE);
			screen.grid[0][col] = screen.logic.getDiamondValue(dType, dColor, 0);
			game.spider.addButterfly(col);
			game.spider.butterflyMove = true;
			Assets.playSound(game.butterflyAppear);
		}
		
		game.spider.butterflyMove = true;
	
	}
	
	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		ButterflyDiamond game = (ButterflyDiamond) screen;
		super.draw(deltaTime);
		for (int i = 0 ; i < game.butterflyOut.size(); i++) {
			IDiamond diamond = game.butterflyOut.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	TextureRegion region = diamond.getSprite().getKeyFrame(diamond.getTime());
        	diamond.draw(deltaTime, screen.batch);
		}
	}
	
	public boolean isUpToGrid(int cell) {
		int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
		return (Operator.getBit(Effect.UP_TO_GRID, value) > 0);
	}
	
	private boolean isOnAspectSwapInCell(int i, int j) {
		int temp = 0;
		return screen.inGridFlag[i][j] == Operator.onBit(Effect.ONE_ASPECT_SWAP, temp);
	}
	
	private boolean isExistDiamod(int i, int j) {
		return (certainCell(screen.inGridFlag[i][j]));
	}
	
	private boolean isCertainCol(int i, int j) {
		for (int k = i - 1; k > -1; k--) {
			if (!isExistDiamod(k, j) && !isOnAspectSwapInCell(k , j)) return false;
		}
		return true;	
	}
}
