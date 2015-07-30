package vn.sunnet.qplay.diamondlink.butterflydiamond;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Random;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;


import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.logiceffects.TempEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.phases.DiamondAnimation;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;







public class ThirdButterfly extends DiamondAnimation {

	Random random = new Random();
	ArrayList<Integer> selections = new ArrayList<Integer>();
	
	public ThirdButterfly(ButterflyDiamond screen) {
		super(screen);
	}
	
	@Override
	public void onBegin() {
		super.onBegin();
		screen.solution.resetTime();
	}
	
	protected boolean touchDownInTouchGame(int screenX, int screenY,
			int pointer, int button) {
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		
		for (int i = 0 ; i < screen.skillArrs.length; i++) {
			if (screen.skillArrs[i] != null)
			if (screen.skillArrs[i].collision(screen.curPoint.x, screen.curPoint.y)) {
				if (!screen.skillArrs[i].isExist()) {
					screen.createToast("Bạn đã hết vật phẩm này", 2f);
					return false;
				} 
				if (screen.skillArrs[i].type == Skill.CLOCK) {
					screen.skillArrs[i].dec();
					screen.showItem(10, "ClockItem",
							screen.skillArrs[i].getBound().x,
							screen.skillArrs[i].getBound().y,
							DiamondLink.WIDTH / 2, screen.gridPos.y - 30);
				} else {
					screen.selectedSkill = screen.skillArrs[i];
					screen.curSkill = new Skill(screen.skillArrs[i].type, screen.gGame, null);
					screen.curSkill.set(screen.curPoint);
					screen.gTouchMode = GameScreen.TOUCH_ITEM;
				}
				return false;
			}
		}
		
		IDiamond diamond1;
		if (screen.gridTable.contains(screen.curPoint.x, screen.curPoint.y)) { // khi an trong man hinh
 			touchCell = touchCell(screen.curPoint);
 			if (canTouchCell(touchCell) && swapFlag && screen.logic.SpecialEffect <= 0) {
				if (diamondType(screen.grid[touchCell / 8][touchCell % 8]) == IDiamond.LASER_DIAMOND) {
					branch = GameScreen.DIAMOND_ANIMATION;
					screen.logic.newEffect(touchCell / 8, touchCell % 8,
							Effect.CROSS_LASER, null);
					screen.selection = -1;
					if (screen.logic.state == GameLogic.ON_END)
						screen.logic.state = GameLogic.ON_RUNNING;
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							initButterflyMove();
						}
					});
				} else
     			if (screen.selection == -1) { // khi truoc do la khong an
     				screen.selection = touchCell; 
     				diamond1 = screen.diamonds.get(screen.selection);
     				diamond1.setAction(Diamond.FRAME_CHANGE);
//     				screen.touchDownX = screen.curPoint.x; screen.touchDownY = screen.curPoint.y;
     				screen.lastPoint.set(screen.curPoint);
     				Assets.playSound(select);
     			} else 
     			if (screen.selection != touchCell) {// an phim
     				if (neighbourCell(screen.selection, touchCell) && canTouchCell(screen.selection)) { // la phim lan can
     					swapFlag = false;
     					// swap two neighbour cell
     					sCell = screen.selection; dCell = touchCell;
     					if (isChain(sCell, dCell) > -1) beginChain(sCell, dCell);
     					else {
     						beginSwap(sCell, dCell);
     					}
     					
     					screen.selection = -1;
     					//setState(Phase.ON_END);		
     				} else {// khong la phim lan can
     					diamond1 = screen.diamonds.get(screen.selection);
     					diamond1.setAction(Diamond.REST);
     					screen.selection = touchCell;
     					diamond1 = screen.diamonds.get(screen.selection);
     					diamond1.setAction(Diamond.FRAME_CHANGE);
     				}
//     				screen.touchDownX = screen.curPoint.x; screen.touchDownY = screen.curPoint.y;
     				screen.lastPoint.set(screen.curPoint);
     			} else {
     				diamond1 = screen.diamonds.get(screen.selection);
     				if (canTouchCell(screen.selection))
 					diamond1.setAction(Diamond.REST);
     				screen.selection = -1;
     			} // an trung phim selection
 			} else screen.selection = -1;// tro vao o khong tro duoc
 		} 
		else {
 			if (screen.selection != -1) {
 				diamond1 = screen.diamonds.get(screen.selection);
 				if (canTouchCell(screen.selection))
					diamond1.setAction(Diamond.REST);
 			}
 			screen.selection = -1;
 		}
		return false;
	}
	
	public void ActionHandle() {
		super.ActionHandle();
		if (getState() == ON_RUNNING) {
//			System.out.println("gridFlag "+screen.inGridFlag[2][3] +" "+screen.inGridFlag[3][3]);
			ButterflyDiamond game = (ButterflyDiamond) screen;
			for (int i = 0 ; i < game.butterflyOut.size() ; i++) {
				IDiamond diamond = game.butterflyOut.get(i);
				diamond.update(deltaTime);
				if (diamond.isFinished(IDiamond.FLY)) {
					game.butterflyNum++;
					game.butterflyOut.remove(i);
					i--;
				}
				diamond = null;
			}
			continueButterflyMove();
		}
//		
		if (isComplete() && swapFlag) {// lenh chuyen pha
			branch = ButterflyDiamond.DIAMOND_REST;
			phaseState = ON_END;
			//Log.d("Game", "Animation to Rest"+swapFlag);
//			printButterflyList(screen, "when transfer Animation to Rest ");
		}
	}

	public void baseMove() {
		ButterflyDiamond game = (ButterflyDiamond) screen;
		boolean generateE = false;
		boolean isButtflyUp = false;
		for (int i = 0 ; i < Math.min(screen.diamonds.size() , 64) ; i++) {
			IDiamond diamond = screen.diamonds.get(i);
			int u = CellRow(i);
			int v = CellCol(i);
			
			int value = screen.inGridFlag[u][v];
			if ((Operator.getBit(Effect.ONE_ASPECT_SWAP, value) > 0) || certainCell(value) || isUpToGrid(i)) {
				diamond.update(deltaTime);
			}
			if (Operator.getBit(Effect.ONE_ASPECT_SWAP, value) > 0) {
			
				if (diamond.containsAction(IDiamond.ONE_ASPECT_SWAP)) {
					if (diamond.isFinished(IDiamond.ONE_ASPECT_SWAP)) {
						int cell = touchCell(diamond.getCenterPosition());
						int row = CellRow(cell);
						int col = CellCol(cell);
						diamond.setAction(IDiamond.REST);
						
						DiamondOfButterfly butterfly = (DiamondOfButterfly) diamond;
						butterfly.setUpStep(butterfly.getUpStep() - 1);
						
//						if (butterfly.getUpStep() <= 0) {
						screen.inGridFlag[row][col] = Operator.offBit(
								Effect.ONE_ASPECT_SWAP,
								screen.inGridFlag[row][col]);
						screen.inGridFlag[row][col] = Operator.onBit(
								Effect.FIXED_POS, screen.inGridFlag[row][col]);
						IEffect temp = new TempEffect(screen.logic, screen);
						temp.setSource(cell);
						if (butterfly.getUpStep() <= 0) {
							screen.checkCellEffect.add(temp);
							generateE = true;
						}
//						}
						
					}
				}
			}
			if (Operator.getBit(Effect.UP_TO_GRID, value) > 0) {
				if (diamond.containsAction(IDiamond.UP_TO_GRID)) {
					if (diamond.isFinished(IDiamond.UP_TO_GRID)) {
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
	
	
	
	public boolean SwapMove() {// tra ve true neu thuc hien 1 lan logic con lai tra ve false
		IDiamond diamond1 = null;
		IDiamond diamond2 = null;
		diamond1 = screen.diamonds.get(sCell);
		diamond2 = screen.diamonds.get(dCell);
		diamond1.update(deltaTime);
		diamond2.update(deltaTime);
		//Log.d("Game", "SwapMove in Animation"+sCell+" "+dCell);
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
					initButterflyMove(); 
				}
				return true;
			}else {
				returnAspect();
				
				return true;
			}
		return false;
	}
	
	public void continueButterflyMove() {
		ButterflyDiamond game = (ButterflyDiamond) screen;
		DiamondOfButterfly diamond1 = null;
		DiamondOfButterfly diamond2 = null;
		ObjectMap<Integer, IDiamond> butterflies = game.spider.butterflies;
		boolean remove[] = new boolean[64];
		boolean add[] = new boolean[64];
		for (int i = 0; i < 64; i++) {
			remove[i] = false;
			add[i] = false;
		}
		game.spider.repairButterfly();
		
		boolean generateE = false;
		
		for (Integer cell: game.spider.butterflies.keys()) {
			int i = cell / 8; int j = cell % 8;
			if (isExistDiamod(i, j)) {
				if (i == 7) {
					if (!isCertainCol(i, j)) {
						Gdx.app.log("test", "bi chat chan o "+i+" "+j);
						continue;
					}

					diamond1 = (DiamondOfButterfly) butterflies.get(cell);
					if (diamond1.getUpStep() <= 0) continue;
					
					float x1 = screen.gridPos.x + j * 60 + 30;
					float y1 = screen.gridPos.y + i * 60 + 30;
					diamond1.setDiamondValue(screen.grid[i][j]);
					diamond1.setSource(x1, y1);
					diamond1.setDestination(x1, y1 + 30);
					diamond1.setAction(IDiamond.UP_TO_GRID);
					screen.inGridFlag[i][j] = Operator.offBit(Effect.FIXED_POS, screen.inGridFlag[i][j]);
					screen.inGridFlag[i][j] = Operator.onBit(Effect.UP_TO_GRID, screen.inGridFlag[i][j]);
					game.spider.huntFlag = true;
				} else {
					if (isExistDiamod(i + 1, j)
							&& diamondType(screen.grid[i + 1][j]) != IDiamond.BUTTERFLY_DIAMOND) {
						diamond1 = (DiamondOfButterfly) butterflies.get(cell);
						diamond2 = (DiamondOfButterfly) screen.diamonds.get((i + 1) * 8 +j);
						if (diamond1.getUpStep() <= 0) continue; 
//						System.out.println("continueMove+++++++++++++++++++++++++++++++");
						int value = screen.grid[i][j];
						screen.grid[i][j] = screen.grid[i + 1][j];
						screen.grid[i + 1][j] = value;
						
						diamond1.setDiamondValue(screen.grid[i][j]);
						diamond2.setDiamondValue(screen.grid[i + 1][j]);
						
						diamond2.setUpStep(diamond1.getUpStep());
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
					} else {
					
						diamond1 = (DiamondOfButterfly) butterflies.get(cell);
						if (diamond1.getUpStep() > 0) {
							diamond1.setUpStep(0);
							IEffect temp = new TempEffect(screen.logic, screen);
							temp.setSource(cell);
							screen.checkCellEffect.add(temp);
							generateE = true;
						}
					}
				}
			}
		}
		
		if (generateE) 
			if (screen.logic.state == GameLogic.ON_END)
				screen.logic.state = GameLogic.ON_RUNNING;
		
		for (int i = 0; i < 64; i++) {
			if (remove[i]) game.spider.removeButterfly(i);
			if (add[i]) game.spider.addButterfly(i);
		}
	}
	
	public void initButterflyMove() {
		ButterflyDiamond game = (ButterflyDiamond) screen;
		DiamondOfButterfly diamond1 = null;
		DiamondOfButterfly diamond2 = null;
		ObjectMap<Integer, IDiamond> butterflies = game.spider.butterflies;
		boolean remove[] = new boolean[64];
		boolean add[] = new boolean[64];
		for (int i = 0; i < 64; i++) {
			remove[i] = false;
			add[i] = false;
		}
		game.spider.repairButterfly();
		
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
//					Gdx.app.log("test", "UpToGrid"+i+" "+j);
					diamond1 = (DiamondOfButterfly) butterflies.get(cell);
					float x1 = screen.gridPos.x + j * 60 + 30;
					float y1 = screen.gridPos.y + i * 60 + 30;
					diamond1.setDiamondValue(screen.grid[i][j]);
					diamond1.setSource(x1, y1);
					diamond1.setDestination(x1, y1 + 30);
					diamond1.setAction(IDiamond.UP_TO_GRID);
					screen.inGridFlag[i][j] = Operator.offBit(Effect.FIXED_POS, screen.inGridFlag[i][j]);
					screen.inGridFlag[i][j] = Operator.onBit(Effect.UP_TO_GRID, screen.inGridFlag[i][j]);
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
		
//		System.out.println("sinh buom diamond animation "+generate+" "+numberFree);
		
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
		super.draw(deltaTime);
		ButterflyDiamond game = (ButterflyDiamond) screen;
		for (int i = 0 ; i < game.butterflyOut.size(); i++) {
			IDiamond diamond = game.butterflyOut.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	diamond.draw(deltaTime, screen.batch);
		}
			
	}
	
	public boolean isOneAspectSwapCell(int cell) {
		int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
		return (Operator.getBit(Effect.ONE_ASPECT_SWAP, value) > 0);
	}

	public boolean isUpToGrid(int cell) {
		int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
		return (Operator.getBit(Effect.UP_TO_GRID, value) > 0);
	}
	
	public boolean isDrawingCell(int cell) {
		if (cell < 64) {
			int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
			if (certainCell(value) || isSwapCell(cell) || isOneAspectSwapCell(cell) || isUpToGrid(cell)) return true;
		} else return true;
		return false;
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		ButterflyDiamond game = (ButterflyDiamond) screen;
		return super.isActive() || game.butterflyOut.size() > 0;
	}
	
	private boolean isExistDiamod(int i, int j) {
		return (certainCell(screen.inGridFlag[i][j]));
	}
	
	private boolean isOnAspectSwapInCell(int i, int j) {
		int temp = 0;
		return screen.inGridFlag[i][j] == Operator.onBit(Effect.ONE_ASPECT_SWAP, temp);
	}
	
	private boolean isCertainCol(int i, int j) {
		for (int k = i - 1; k > -1; k--) {
			if (!isExistDiamod(k, j) && !isOnAspectSwapInCell(k , j)) {
				return false;
			}
		}
		return true;	
	}
}
