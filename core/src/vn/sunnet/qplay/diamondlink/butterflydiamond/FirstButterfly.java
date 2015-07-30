package vn.sunnet.qplay.diamondlink.butterflydiamond;

import java.util.ArrayList;
import java.util.Random;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.graphiceffects.OpenGLEffect;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.FindSolution;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.phases.DiamondRest;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;





public class FirstButterfly extends DiamondRest {

	Random random = new Random();
	ArrayList<Integer> selections = new ArrayList<Integer>();
	
	public FirstButterfly(ButterflyDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}
	
	protected boolean touchDownInTouchGame(int screenX, int screenY, int pointer, int button) {
		IDiamond diamond1 = null;
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
		
		if (screen.gridTable.contains(screen.curPoint.x, screen.curPoint.y)) { // khi an trong man hinh
 			touchCell = touchCell(screen.curPoint);
// 			Gdx.app.log("test", "Touch Down 0" + touchCell+" "+screen.selection);
 			if (canTouchCell(touchCell)) {
				if (diamondType(screen.grid[touchCell / 8][touchCell % 8]) == IDiamond.LASER_DIAMOND) {
					branch = GameScreen.DIAMOND_ANIMATION;
					screen.logic.newEffect(touchCell / 8, touchCell % 8,
							Effect.CROSS_LASER, null);
					screen.selection = -1;
					setState(Phase.ON_END);
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							initButterflyMove();
						}
					});
				} else
     			if (screen.selection == -1) { // khi truoc do la khong an
//     				Gdx.app.log("test", "Touch Down 1");
     				screen.selection = touchCell; 
     				diamond1 = screen.diamonds.get(screen.selection);
     				diamond1.setAction(Diamond.FRAME_CHANGE);
     				screen.lastPoint.set(screen.curPoint);
     				Assets.playSound(select);
     				// tu dong active
					
     			} else 
     			if (screen.selection != touchCell) {// an phim
     				if (neighbourCell(screen.selection, touchCell) && canTouchCell(screen.selection)) { // la phim lan can
     					
     					sCell = screen.selection; dCell = touchCell;
     					
     					if (isChain(sCell, dCell) > -1) branch = GameScreen.DIAMOND_ANIMATION;
     					else branch = GameScreen.DIAMOND_MOVE;

     					
     					screen.selection = -1;
     					setState(Phase.ON_END);		
     				} else {// khong la phim lan can
     					diamond1 = screen.diamonds.get(screen.selection);
     					diamond1.setAction(Diamond.REST);
     					screen.selection = touchCell;
     					diamond1 = screen.diamonds.get(screen.selection);
     					diamond1.setAction(Diamond.FRAME_CHANGE);
     				}
     				screen.lastPoint.set(screen.curPoint);
     			} else {
     				diamond1 = screen.diamonds.get(screen.selection);
 					diamond1.setAction(Diamond.REST);
     				screen.selection = -1;
     			} // an trung phim selection
 			} else screen.selection = -1;// tro vao o khong tro duoc
 		} 
 		else {
 			if (screen.selection != -1) {
 				diamond1 = screen.diamonds.get(screen.selection);
				diamond1.setAction(Diamond.REST);
 			}
 			screen.selection = -1;
 		}
		return false;
	}
	
	@Override
	public void ActionHandle() {
		// TODO Auto-generated method stub
		ButterflyDiamond game = (ButterflyDiamond) screen;
		if (getState() == ON_RUNNING) { 
			for (int i = 0 ; i < screen.diamonds.size() ; i++) { // chuyen dong cua o chua selection
				IDiamond diamond = screen.diamonds.get(i);
				diamond.update(deltaTime);
			}
			
			for (int i = 0 ; i < game.butterflyOut.size() ; i++) {
				IDiamond diamond = game.butterflyOut.get(i);
				diamond.update(deltaTime);
				if (diamond.isFinished(IDiamond.FLY)) {
					game.butterflyNum++;
					game.butterflyOut.remove(i);
					i--;
				}
			}	
		}
		
		if (game.beginLevel) {
			initButterflyMove();
			game.beginLevel = false;
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
	
	// 30-> 1| 500| 30 -> 70 2 | 1000 | > 70 3| 5000  
	public void initButterflyMove() {
		Gdx.app.log("test", "tao buom");
		selections.clear();
		ButterflyDiamond game = (ButterflyDiamond) screen;
		int numberFree = 0;
		Spider spider = game.spider;
		for (int i = 0; i < 8; i++)
			if (certainCell(screen.inGridFlag[0][i])
					&& diamondType(screen.grid[0][i]) != IDiamond.BUTTERFLY_DIAMOND) {
				numberFree++;
				selections.add(i);
			}
		
		int generate = 1;
		if (game.butterflyNum < 20) generate = 1;
		else
		if (game.butterflyNum < 50) generate = 2;
		else generate = 3;
		
		System.out.println("sinh buom diamond rest "+generate+" "+numberFree);
		
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
		
		if (spider.state == Spider.SPIDER_INIT) {
			spider.state = Spider.SPIDER_BEGIN;
		}
		spider.butterflyMove = true;
		
	}
	
	@Override
	public void onRunning() {
		// TODO Auto-generated method stub
		super.onRunning();
		if (getState() == ON_RUNNING) {
			if (screen.solution.update(deltaTime) == FindSolution.NEW_GRID) {
				debug();
			}
		}
	}
	
	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		super.onEnd();
		if (branch != GameScreen.DIAMOND_CHANGE) {
			screen.solution.resetTime();
		}
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
        	diamond.draw(deltaTime, screen.batch);
		}
			
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		ButterflyDiamond game = (ButterflyDiamond) screen;
		return super.isActive() || game.butterflyOut.size() > 0;
	}
	
	public boolean isUpToGrid(int cell) {
		int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
		return (Operator.getBit(Effect.UP_TO_GRID, value) > 0);
	}
	
	private void debug() {
		setState(Phase.ON_END);
		branch = GameScreen.DIAMOND_CHANGE;
		
//		screen.setStepGame(GameScreen.GAME_PAUSED);
//		if (Gdx.app.getType() == ApplicationType.Android)
//		screen.gGame.iFunctions.showDebugDialog(new Runnable() {
//			
//			@Override
//			public void run() {
//				Gdx.app.postRunnable(new Runnable() {
//					
//					@Override
//					public void run() {
//						screen.setStepGame(GameScreen.GAME_RUNNING);
//						screen.solution.resetTime();
//						setState(Phase.ON_END);
//						branch = GameScreen.DIAMOND_CHANGE;
//					}
//				});
//				
//			}
//		}); 
//		else screen.createDialog("Debug", "Hết nước làm mới bàn", new ButtonDescription("Đồng ý", new Command() {
//			
//			@Override
//			public void execute(Object data) {
//				Gdx.app.postRunnable(new Runnable() {
//					
//					@Override
//					public void run() {
//						screen.setStepGame(GameScreen.GAME_RUNNING);
//						screen.solution.resetTime();
//						setState(Phase.ON_END);
//						branch = GameScreen.DIAMOND_CHANGE;
//					}
//				});
//			}
//		}), null, null, null);
	}

}
