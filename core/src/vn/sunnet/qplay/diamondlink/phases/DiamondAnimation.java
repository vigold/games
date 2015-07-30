package vn.sunnet.qplay.diamondlink.phases;

import java.util.List;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.modules.FallModule;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.jndi.ldap.Ber;



// cong viec code hop vien + swap trong animation
public class DiamondAnimation extends Phase {

	public GameScreen screen;
	private boolean isFalling;
	public DiamondLink Instance = null;
	// cac thong so lien quan den su doi cho 2 o
	protected int sCell = -1, dCell = -1;
	protected boolean backFlag = false;
	protected boolean swapFlag = true;// co cho phep swap hay khong ? true neu co the
	private int account = 0;
	
	private Sound move;
	private Sound moveFail;
	protected Sound select;
	
	public DiamondAnimation(GameScreen screen) {
		this.screen = screen;
		Instance = screen.gGame;
		
		
	}
	
	/*********************************Implements Phases' Methods*********************************/
	
	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		sCell = -1 ; dCell = -1;
		backFlag = false;
		swapFlag = true;
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		Gdx.app.log("test", "DiamondAnimation OnBegin");
		branch = -1;
		sCell = -1; dCell = -1;
//		screen.selection = -1;
		screen.addInputProcessor(this);
		move = screen.manager.get(SoundAssets.MOVE_SOUND, Sound.class);
		moveFail = screen.manager.get(SoundAssets.MOVE_FAIL_SOUND, Sound.class);
		select = screen.manager.get(SoundAssets.SELECT_SOUND, Sound.class);
	}

	@Override
	public void onRunning() {
		// TODO Auto-generated method stub
		EventHandle();
		ActionHandle();
	}

	@Override
	public void onEnd() {
		screen.logic.numberCombo = 0;
		if (swapFlag) {
			screen.removeInputProcessor(this);
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
//			Log.d("test", "Animation->Rest");
		} else {
			setState(Phase.ON_RUNNING);
		}
	}
	
	public void EventHandle() {}
	
	public void ActionHandle() {
		// swap Move
		
		// logic up date
		
		// Fall Begin
		
		// Diamond Move - certaincell and fall + butterfly Move
		
		//Effect Move
		
		if (getState() == ON_RUNNING) {
			
			boolean isDoneLogic = false;
			isFalling = false;
			if (!swapFlag) {
				boolean continueSwap = Operator.hasOnly(Effect.TWO_ASPECT_SWAP,
						screen.inGridFlag[sCell / 8][sCell % 8])
						&& Operator.hasOnly(Effect.TWO_ASPECT_SWAP,
								screen.inGridFlag[dCell / 8][dCell % 8]);
				if (continueSwap) {
					isDoneLogic = SwapMove();
				} else {
					resetSwap();
				}
			}
			
			if (!isDoneLogic) {
				screen.logic.update(deltaTime);
				
			}
			
			if (isFall()) screen.fall.state = FallModule.ON_BEGIN;
			screen.fall.update(deltaTime);
			DiamondsMove();
			EffectsMove();
		}
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return !(screen.fall.isFallEnd()
				&& screen.logic.state == GameLogic.ON_END
				&& (screen.checkCellEffect.size() == 0) && swapFlag);
	}
	
	public void baseMove() {
		for (int i = 0 ; i < Math.min(screen.diamonds.size() , 64) ; i++) {
			IDiamond diamond = screen.diamonds.get(i);
			int u = CellRow(i);
			int v = CellCol(i);
			if (i != sCell && i != dCell)
			if (certainCell(screen.inGridFlag[u][v]))
				diamond.update(deltaTime);
		}
	}
	
	public void extensionMove() {}
	
	public void DiamondsMove() {
		baseMove();
		extensionMove();
	}
	
	public void EffectsMove() {
		for (int i = 0 ; i < screen.logic.effects.size() ; i++) {
			Effect effect = (Effect) screen.logic.effects.get(i);
			effect.update(deltaTime);	
		}
	}
	
	public void beginSwap(int sou, int des) {
		IDiamond diamond1 = screen.diamonds.get(sou);
		IDiamond diamond2 = screen.diamonds.get(des);
//		Log.d("Game", "beginSwap in Animation");
		diamond1.setSource(diamond1.getPosX(), diamond1.getPosY());
		diamond1.setDestination(diamond2.getPosX(), diamond2.getPosY());
		diamond1.setAction(IDiamond.FRAME_CHANGE | IDiamond.TWO_ASPECT_SWAP);
		
		int row1 = CellRow(sou); int col1 = CellCol(sou);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.TWO_ASPECT_SWAP, screen.inGridFlag[row1][col1]);
		
		diamond2.setSource(diamond2.getPosX(), diamond2.getPosY());
		diamond2.setDestination(diamond1.getPosX(), diamond1.getPosY());
		diamond2.setAction(IDiamond.TWO_ASPECT_SWAP);
	
		row1 = CellRow(des); col1 = CellCol(des);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.TWO_ASPECT_SWAP, screen.inGridFlag[row1][col1]);
		
		swapCellValue(sou, des);
		Assets.playSound(move);
	}
	
	
	public boolean SwapMove() {// tra ve true neu thuc hien 1 lan logic con lai tra ve false
		screen.timeSwap = 0;
		IDiamond diamond1 = null;
		IDiamond diamond2 = null;
		diamond1 = screen.diamonds.get(sCell);
		diamond2 = screen.diamonds.get(dCell);
		diamond1.update(deltaTime);
		diamond2.update(deltaTime);
//		Log.d("Game", "SwapMove in Animation chieu quay ve"+backFlag);
		float x1 = diamond1.getSourX();
		float y1 = diamond1.getSourY();
		float x2 = diamond1.getDesX();
		float y2 = diamond1.getDesY();
		float x = diamond1.getPosX();
		float y = diamond1.getPosY();
//		Log.d("Game", "kc nguon "+x1+" "+y1+" to "+x2+" "+y2+" cur = "+x+" "+y+" flag = "+screen.inGridFlag[sCell / 8][sCell % 8]);
		x1 = diamond2.getSourX();
		y1 = diamond2.getSourY();
		x2 = diamond2.getDesX();
		y2 = diamond2.getDesY();
		x = diamond2.getPosX();
		y = diamond2.getPosY();
//		Log.d("Game", "kc dich "+x1+" "+y1+" to "+x2+" "+y2+" cur = "+x+" "+y+" flag = "+screen.inGridFlag[dCell / 8][dCell % 8]);
		boolean isDestination = diamond1.isDestination() && diamond2.isDestination();
		if (isDestination)
			if (!backFlag ) {
				goAspect(); 
				return true;
			}else {
				returnAspect();
				return true;
			}
		return false;
	}
	
	public boolean goAspect() {// tra ve true neu co quay lai false neu phat sinh hieu ung
		IDiamond diamond1 = screen.diamonds.get(sCell);
		IDiamond diamond2 = screen.diamonds.get(dCell);
		IEffect effect = null;
		float xx = 0 , yy = 0;
		int row1 = CellRow(sCell); int col1 = CellCol(sCell);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row1][col1]);
		row1 = CellRow(dCell); col1 = CellCol(dCell);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row1][col1]);
		effect = (IEffect) screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(sCell);
		effect.setSwapFlag(true);
		screen.checkCellEffect.add(effect);
		effect = (IEffect) screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(dCell);
		effect.setSwapFlag(true);
		screen.checkCellEffect.add(effect);
		
		screen.logic.init();
		
		screen.logic.update(deltaTime);
		
		if (!screen.logic.isChange()) {// khong phat sinh hieu ung
			//swapCellValue(sCell, dCell);
			
			xx = diamond1.getDesX();
			yy = diamond1.getDesY();
			diamond1.setDestination(diamond1.getSourX(), diamond1.getSourY());
			
			diamond1.setSource(xx, yy);
			
			xx = diamond2.getDesX();
			yy = diamond2.getDesY();
			diamond2.setDestination(diamond2.getSourX(), diamond2.getSourY());
			diamond2.setSource(xx, yy);
			diamond1.setAction(IDiamond.TWO_ASPECT_SWAP);
			diamond2.setAction(IDiamond.TWO_ASPECT_SWAP);
			backFlag = true;
			
			row1 = CellRow(sCell); col1 = CellCol(sCell);
			screen.inGridFlag[row1][col1] = 0;
			screen.inGridFlag[row1][col1] = Operator.onBit(Effect.TWO_ASPECT_SWAP, screen.inGridFlag[row1][col1]);
			row1 = CellRow(dCell); col1 = CellCol(dCell);
			screen.inGridFlag[row1][col1] = 0;
			screen.inGridFlag[row1][col1] = Operator.onBit(Effect.TWO_ASPECT_SWAP, screen.inGridFlag[row1][col1]);
			Assets.playSound(moveFail);
			
			return true;
		} else {//phat sinh hieu ung
			
			float time1 = diamond1.getActiveTime();
			float time2 = diamond2.getActiveTime();
			int value1 = diamond1.getDiamondValue();
			int value2 = diamond2.getDiamondValue();
			
			xx = CordXOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond1.setSource(xx, yy);
			diamond1.setDestination(xx, yy);
			xx = CordXOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond2.setSource(xx, yy);
			diamond2.setDestination(xx, yy);
			diamond1.setDiamondValue(value2);
			diamond2.setDiamondValue(value1);
			backFlag = false;
			
			float time = diamond1.getActiveTime();
			diamond1.setActivieTime(time2);
			diamond2.setActivieTime(time1);
			
			swapFlag = true;
			
		}
		return false;
	}
	
	public boolean returnAspect() {// tra ve false neu phat sinh hieu ung
//		Log.d("Game", "SwapMove return Aspect in Animation");
		IDiamond diamond1 = screen.diamonds.get(sCell);
		IDiamond diamond2 = screen.diamonds.get(dCell);
		diamond1.setAction(Diamond.REST);
		diamond2.setAction(Diamond.REST);
		// danh dau lai gridtype
		int row1 = CellRow(sCell); int col1 = CellCol(sCell);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row1][col1]);
		row1 = CellRow(dCell); col1 = CellCol(dCell);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row1][col1]);
		swapCellValue(sCell, dCell);
		// de phong xay ra khi quay lai co hieu ung
		IEffect effect = (IEffect) screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(sCell);
		effect.setSwapFlag(true);
		screen.checkCellEffect.add(effect);
		effect = (IEffect) screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(dCell);
		effect.setSwapFlag(true);
		screen.logic.init();
		screen.logic.update(deltaTime);
		
		backFlag = false;
		
		swapFlag = true;
		sCell = -1; dCell = -1;
		return !screen.logic.isChange;
	}
	
	public void beginChain(int sou, int des) {
		int source = isChain(sou, des);
		Effect effect = null;
		if (diamondType(screen.grid[source / 8][source % 8]) == IDiamond.FIVE_COLOR_DIAMOND)
			effect = screen.logic.allocateEffect(Effect.CHAIN_THUNDER);
		else
			effect = screen.logic.allocateEffect(Effect.EXTRA_CHAIN_THUNDER);
		if (isChain(sou, des) == sou) effect.setSource(sou,des);
		else effect.setSource(des,sou);
		screen.checkCellEffect.add((IEffect) effect);
		if (screen.logic.state == GameLogic.ON_END)
			screen.logic.state = GameLogic.ON_RUNNING;
		swapFlag = true;
		
	}
		
	public int isChain(int sou, int des) {
		int row = CellRow(sou); int col = CellCol(sou);
		int type = diamondType(screen.grid[row][col]);
		if (isSpecialDiamond(sou) && isValidCell(des / 8, des % 8)) return sou;
		row = CellRow(des); col = CellCol(des);
		type = diamondType(screen.grid[row][col]);
		if (isSpecialDiamond(des) && isValidCell(sou / 8, sou % 8)) return des;
		return - 1;
	}
	
	private boolean isSpecialDiamond(int cell) {
		int row = CellRow(cell); int col = CellCol(cell);
		int type = diamondType(screen.grid[row][col]);
		return (type == IDiamond.FIVE_COLOR_DIAMOND)
				|| (type == IDiamond.HYPER_CUBE);
	}
	
	private boolean isValidCell(int row, int col) {
		if (screen.grid[row][col] == -1)
			return false;
		int dType = screen.logic.diamondType(screen.logic.grid[row][col]);
		return

		dType != Diamond.LAVA && dType != Diamond.BLUE_GEM
				&& dType != Diamond.DEEP_BLUE_GEM && dType != Diamond.PINK_GEM
				&& dType != Diamond.RED_GEM && dType != Diamond.MARK_DIAMOND
				&& dType != Diamond.SOIL_DIAMOND
				&& dType != Diamond.ROCK_DIAMOND
				&& dType != IDiamond.BOX_DIAMOND
				&& dType != IDiamond.PEARL_DIAMOND;
	}
	
	/********************************Implements InputProcessor's Methods*********************************/
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected boolean touchDownInTouchGame(int screenX, int screenY,
			int pointer, int button) {
		// TODO Auto-generated method stub
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		
		for (int i = 0 ; i < screen.skillArrs.length; i++) {
			if (screen.skillArrs[i] != null)
			if (screen.skillArrs[i].collision(screen.curPoint.x, screen.curPoint.y)) {
				if (!screen.skillArrs[i].isExist()) {
					screen.createToast("No more items", 2f);
					return false;
				} 
				if (screen.skillArrs[i].type == Skill.CLOCK) {
					screen.skillArrs[i].dec();
					screen.showItem(10, "ClockItem",
							screen.skillArrs[i].getBound().x,
							screen.skillArrs[i].getBound().y,
							DiamondLink.WIDTH / 2, screen.gridPos.y - 30);
				} else {
					if (screen.skillArrs[i].type > 3) return false;
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

	@Override
	protected boolean touchDownInTouchItem(int screenX, int screenY,
			int pointer, int button) {
		// TODO Auto-generated method stub
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		screen.curSkill.set(screen.curPoint);
		return false;
	}

	@Override
	protected boolean touchUpInTouchGame(int screenX, int screenY, int pointer,
			int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchUpInTouchItem(int screenX, int screenY, int pointer,
			int button) {
		// TODO Auto-generated method stub
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		if (screen.gTouchMode == GameScreen.TOUCH_ITEM) {
    		if (screen.gridTable.contains(screen.curPoint.x , screen.curPoint.y)) {
    			touchCell = touchCell(screen.curPoint);
    			int row = touchCell / 8;
    			int col = touchCell % 8;
    			screen.selectedSkill.dec();
    			screen.selectedSkill = null;
    			screen.curSkill.generateAt(row, col, screen);
    			screen.curSkill = null;
    			if (screen.logic.state == GameLogic.ON_END) screen.logic.state = GameLogic.ON_RUNNING;
    			screen.gTouchMode= GameScreen.TOUCH_GAME;
    		} else {
    			screen.selectedSkill = null;
    			screen.curSkill = null;
    			screen.gTouchMode= GameScreen.TOUCH_GAME;
    		}
    	}
		return false;
	}

	@Override
	protected boolean touchDraggedInTouchGame(int screenX, int screenY,
			int pointer) {
		// TODO Auto-generated method stub
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		if (screen.selection != -1) {
			Vector2 temp1 = new Vector2(screen.lastPoint.x, screen.lastPoint.y);
			Vector2 temp2 = new Vector2(screen.curPoint.x, screen.curPoint.y);
			temp2.sub(temp1);

			if (temp2.angle() >= 45 && temp2.angle() < 45 + 90) {
				touchCell = screen.selection + 8;
				if (touchCell > 63 || touchCell < 0)
					touchCell = -1;
			} else if (temp2.angle() >= 135 && temp2.angle() < 135 + 90) {
				touchCell = screen.selection - 1;
				if (screen.selection % 8 == 0)
					touchCell = -1;
			} else if (temp2.angle() >= 225 && temp2.angle() < 225 + 90) {
				touchCell = screen.selection - 8;
				if (touchCell > 63 || touchCell < 0)
					touchCell = -1;
			} else {
				touchCell = screen.selection + 1;
				if (touchCell % 8 == 0)
					touchCell = -1;

				if (touchCell > 63 || touchCell < 0)
					touchCell = -1;
			}
			if (temp2.len() > 30) {
				if (touchCell != -1)
					if (canTouchCell(touchCell)
							&& canTouchCell(screen.selection) && swapFlag
							&& screen.logic.SpecialEffect <= 0) {
						swapFlag = false;
						sCell = screen.selection;
						dCell = touchCell;
						if (isChain(sCell, dCell) > -1)
							beginChain(sCell, dCell);
						else {
							beginSwap(sCell, dCell);
							// Log.d("Game", "SwapMove beginSwap in Animation");
						}
						screen.selection = -1;
						// setState(Phase.ON_END);
					} else {
						if (screen.selection != -1) {
							IDiamond diamond1 = screen.diamonds
									.get(screen.selection);
							if (canTouchCell(screen.selection))
								diamond1.setAction(Diamond.REST);
						}
						screen.selection = -1;
					}
			}
		}
		return false;
	}

	@Override
	protected boolean touchDraggedInTouchItem(int screenX, int screenY,
			int pointer) {
		// TODO Auto-generated method stub
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		screen.curSkill.set(screen.curPoint);
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
		if (screen.isOverTime()) return false;
		if (getState() == ON_END) return false;
		if (pointer > 0) return false;
		if (!(swapFlag && screen.logic.SpecialEffect == 0)) return false;
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (screen.gTouchMode) {
				case GameScreen.TOUCH_GAME:
					touchDownInTouchGame(screenX, screenY, pointer, button);
					break;
				case GameScreen.TOUCH_ITEM:
					touchDownInTouchItem(screenX, screenY, pointer, button);
					break;
				}
			}
		});
		
		
		return true;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
		if (screen.isOverTime()) return false;
		if (getState() == ON_END) return false;
		if (pointer > 0) return false;
		if (!(swapFlag && screen.logic.SpecialEffect == 0)) return false;
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (screen.gTouchMode) {
				case GameScreen.TOUCH_GAME:
					touchUpInTouchGame(screenX, screenY, pointer, button);
					break;
				case GameScreen.TOUCH_ITEM:
					touchUpInTouchItem(screenX, screenY, pointer, button);
					break;
				}
			}
		});
		
		
		return false;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
		// TODO Auto-generated method stub
		if (screen.isOverTime()) return false;
		if (getState() == ON_END) return false;
		if (pointer > 0 ) return false;
		if (!(swapFlag && screen.logic.SpecialEffect == 0)) return false;
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (screen.gTouchMode) {
				case GameScreen.TOUCH_GAME:
					touchDraggedInTouchGame(screenX, screenY, pointer);
					break;
				case GameScreen.TOUCH_ITEM:
					touchDraggedInTouchItem(screenX, screenY, pointer);
					break;
				}
			}
		});
		
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/********************************Draw Methods**********************************/
	
	public void draw(float deltaTime) {
		int count = 0;
		if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1); else 
			if (screen.curStep != GameScreen.GAME_OVER) screen.batch.setColor(1, 1, 1, 1);
		
		screen.fall.draw(deltaTime);
		if (screen.logic.SpecialEffect > 0) {
//			System.out.print(arg0)
		}
        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	
        	TextureRegion region = null;
        	
        	int value = 0;
        	
        	if (i < 64)
        	value = screen.logic.gridFlag[CellRow(i)][CellCol(i)];
//        	if (Operator.getBit(Effect.FOUR_ROW_COMBINE, value) > 0) screen.batch.setColor(0.5f, 0.5f, 0.5f, 1);
//        	else if (Operator.getBit(Effect.FOUR_COl_COMBINE, value) > 0) screen.batch.setColor(0.7f, 0.7f, 0.7f, 1);
//        	else if (Operator.getBit(Effect.FIVE_ROW_COMBINE, value) > 0) screen.batch.setColor(0.1f, 0.1f, 0.1f, 1);
//        	else if (Operator.getBit(Effect.FIVE_COL_COMBINE, value) > 0) screen.batch.setColor(0.3f, 0.3f, 0.3f, 1);
        	if (isDrawingCell(i))  {
        		
        		if (screen.logic.SpecialEffect > 0) diamond.setColorMode(GameObject.BATCH_COLOR);
        		else diamond.setColorMode(GameObject.OBJECT_COLOR);
				if (screen.grid[i / 8][i % 8] == -1)
					screen.batch.setColor(0, 0, 0, 1);
        		diamond.draw(deltaTime, screen.batch);	
        	} else {
        	}
        }
        
        for (int i = 0 ; i < screen.logic.effects.size(); i++) {
        	Effect effect = (Effect) screen.logic.effects.get(i);
        	effect.draw(deltaTime);
        }
        
		if (screen.selection > -1)
			drawAnimation(
					screen.gSelection,
					CordXOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT), screen.DIAMOND_WIDTH,
					screen.DIAMOND_HEIGHT);
	
//		screen.comboFont.draw(screen.batch, screen.fall.isFallEnd()+" "+(screen.logic.state == GameLogic.ON_END)+" "+(screen.checkCellEffect.size() == 0) +" "+swapFlag+" "+getState(), 0, screen.gridPos.y + screen.gridTable.height);
//		if (screen.curSkill != null) screen.curSkill.draw(screen.batch, deltaTime);
	}
	
	protected void drawAnimation(TextureRegion region, float x, float y, float width, float height) {
		
		screen.batch.draw(region, x - width / 2, y - height / 2, width, height);
		
	}
	
	protected void drawAnimation(TextureRegion region, float x, float y, float width, float height, float angle) {
		screen.batch.draw(region, x - width / 2, y - height / 2, width / 2 , height / 2 ,
				width, height, 1f, 1f, angle, true);
	}
	
	protected void drawAnimation(TextureRegion region, Vector2 pos, Rectangle bound) {
		
		screen.batch.draw(region,pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width, bound.height);
		
	}
	
	protected void drawAnimation(TextureRegion region, Vector2 pos, Rectangle bound, float angle) {
		screen.batch.draw(region, pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width / 2  , bound.height / 2 , 
				bound.width, bound.height, 1f, 1f, angle, true);
	}
	
	/********************************Logic Methods**********************************/
	
	public void resetSwap() {
		if (!swapFlag) {
			swapFlag = true;
			backFlag = false;
			screen.inGridFlag[sCell / 8][sCell % 8] = Operator.offBit(
					Effect.TWO_ASPECT_SWAP,
					screen.inGridFlag[sCell / 8][sCell % 8]);
			screen.inGridFlag[dCell / 8][dCell % 8] = Operator.offBit(
					Effect.TWO_ASPECT_SWAP,
					screen.inGridFlag[dCell / 8][dCell % 8]);
			if (certainCell(screen.inGridFlag[sCell / 8][sCell % 8])) {
				IDiamond diamond = screen.diamonds.get(sCell);
				diamond.setDiamondValue(screen.grid[sCell / 8][sCell % 8]);
				diamond.setAction(Diamond.REST);
			}
			if (certainCell(screen.inGridFlag[dCell / 8][dCell % 8])) {
				IDiamond diamond = screen.diamonds.get(dCell);
				diamond.setDiamondValue(screen.grid[dCell / 8][dCell % 8]);
				diamond.setAction(Diamond.REST);
			}
			
			sCell = -1;
			dCell = -1;
		}
	}
	
	protected boolean isFall() {
		// TODO Auto-generated method stub
		if (screen.logic.isSpecialEffect()) return false;
		//Log.d("test", "check Hieght");
		for (int i = 0; i < 8; i++) 
			if (screen.colHeight[i] < 8) return true;
		return false;
	}
	
	public boolean isComplete() {
		if (screen.logic.effects.size() > 0) {
			if (account != screen.logic.effects.size()) {
				for (int i = 0 ; i < screen.logic.effects.size() ; i++) {
					Effect effect = (Effect) screen.logic.effects.get(i);				
				}
			}
			account = screen.logic.effects.size();
			return false;
		}
		for (int i = 0; i < 8; i++) {
			if (screen.colHeight[i] < 8) {			
				return false;
			}
		}
		
		return true;
	}
	
	public int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	public int diamondColor(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
	}
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}

	public int touchCell(Vector3 curPoint) {// loi
		int j = (int) (curPoint.x - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		int i = (int) (curPoint.y - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		j = (int) (curPoint.x - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		i = (int) (curPoint.y - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		return i * 8 + j;
	}
	
	public int touchCell(Vector2 curPoint) {
		int j = (int) (curPoint.x - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		int i = (int) (curPoint.y - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		j = (int) (curPoint.x - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		i = (int) (curPoint.y - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		return i * 8 + j;
	}
	
	protected float CordXOfCell(int cell, int width, int height) {
		return CordXOfCell(cell / 8, cell % 8, width, height);
	}
	
	protected float CordYOfCell(int cell, int width, int height) {
		return CordYOfCell(cell / 8, cell % 8, width, height);
	}
	
	protected float CordXOfCell(int row, int col, int width, int height) {
		float result = 0;
		result = screen.gridPos.x + col * width + width / 2;
		
		return result;
	}
	
	protected float CordYOfCell(int row, int col, int width, int height) {
		float result = 0;
		result = screen.gridPos.y + row * height + height / 2;
		
		return result;
	}
	
	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public boolean inGrid(int i , int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
	
	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
	} 
	
	public boolean canTouchCell(int cell) {
		int row = CellRow(cell);
		int col = CellCol(cell);
		return canTouchCell(row, col);
	}
	
	public boolean canTouchCell(int row, int col) {
		int value = screen.grid[row][col];
		int status = screen.inGridFlag[row][col];
		int type = screen.logic.diamondType(screen.grid[row][col]);
		return (value != -1) && (certainCell(status))
				&& (type != IDiamond.SOIL_DIAMOND)
				&& (type != IDiamond.ROCK_DIAMOND)
				&& (type != IDiamond.BOX_DIAMOND) 
				&& (type != IDiamond.LAVA)
				&& (type != IDiamond.BLUE_GEM)
				&& (type != IDiamond.DEEP_BLUE_GEM)
				&& (type != IDiamond.PINK_GEM) 
				&& (type != IDiamond.RED_GEM)
				&& (type != IDiamond.MARK_DIAMOND);
	}
	
	public void swapCellValue(int cell1, int cell2) {
		int middle = 0;
		middle = screen.grid[CellRow(cell1)][CellCol(cell1)];
		screen.grid[CellRow(cell1)][CellCol(cell1)] = screen.grid[CellRow(cell2)][CellCol(cell2)];
		screen.grid[CellRow(cell2)][CellCol(cell2)] = middle;
	}
	
	public boolean isSwapCell(int cell) {
		return (!swapFlag) && (cell == sCell | cell == dCell);
	}
	
	public boolean isDrawingCell(int cell) {
		if (cell < 64) {
			int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
			if (certainCell(value) || isSwapCell(cell)) return true;
		} else return true;
		return false;
	}
	
	@Override
	public void save(IFunctions iFunctions) {
		// TODO Auto-generated method stub
		super.save(iFunctions);
		iFunctions.putFastInt("phaseState "+screen.GAME_ID, phaseState);
		iFunctions.putFastInt("branch "+screen.GAME_ID, branch);
		if (sCell != -1 && dCell != -1) {
			iFunctions.putFastBool("animation state data", true);
			iFunctions.putFastBool("animation state backFlag "+screen.GAME_ID, backFlag);
			IDiamond diamond1 = screen.diamonds.get(sCell);
			IDiamond diamond2 = screen.diamonds.get(dCell);
			iFunctions.putFastString("animation state sCell " + screen.GAME_ID, sCell
					+ "|" + diamond1.getDiamondValue()
					+ "|" + diamond1.getSourX() + "|" + diamond1.getSourY() 
					+ "|" + diamond1.getPosX() + "|" + diamond1.getPosY() 
					+ "|" + diamond1.getDesX() + "|" + diamond1.getDesY());
			iFunctions.putFastString("animation state dCell " + screen.GAME_ID, dCell
					+ "|" + diamond2.getDiamondValue()
					+ "|" + diamond2.getSourX() + "|" + diamond2.getSourY()
					+ "|" + diamond2.getPosX() + "|" + diamond2.getPosY() 
					+ "|" + diamond2.getDesX() + "|" + diamond2.getDesY());
		} else iFunctions.putFastBool("animation state data", false);
	}
	
	@Override
	public void parse(IFunctions iFunctions) {
		// TODO Auto-generated method stub
		super.parse(iFunctions);
		move = screen.manager.get(SoundAssets.MOVE_SOUND, Sound.class);
		moveFail = screen.manager.get(SoundAssets.MOVE_FAIL_SOUND, Sound.class);
		select = screen.manager.get(SoundAssets.SELECT_SOUND, Sound.class);
		screen.addInputProcessor(this);
		phaseState = iFunctions.getFastInt("phaseState "+screen.GAME_ID, 0);
		branch = iFunctions.getFastInt("branch "+screen.GAME_ID, 0);
		if (iFunctions.getFastBool("animation state data", false)) {
			backFlag = iFunctions.getFastBool("animation state backFlag "+screen.GAME_ID, false);
			String data = iFunctions.getFastString("animation state sCell " + screen.GAME_ID, "");
			String split[] = data.split("\\|");
			sCell = Integer.parseInt(split[0]);
			IDiamond diamond1 = screen.diamonds.get(sCell);
			diamond1.setDiamondValue(Integer.parseInt(split[1]));
			
			diamond1.setSource(Float.parseFloat(split[2]), Float.parseFloat(split[3]));
			diamond1.setCenterPosition(Float.parseFloat(split[4]), Float.parseFloat(split[5]));
			diamond1.setDestination(Float.parseFloat(split[6]), Float.parseFloat(split[7]));
			diamond1.setAction(Diamond.TWO_ASPECT_SWAP);
			
			data = iFunctions.getFastString("animation state dCell " + screen.GAME_ID, "");
			split = data.split("\\|");
			dCell = Integer.parseInt(split[0]);
			IDiamond diamond2 = screen.diamonds.get(dCell);
			diamond2.setDiamondValue(Integer.parseInt(split[1]));
			
			diamond2.setSource(Float.parseFloat(split[2]), Float.parseFloat(split[3]));
			diamond2.setCenterPosition(Float.parseFloat(split[4]), Float.parseFloat(split[5]));
			diamond2.setDestination(Float.parseFloat(split[6]), Float.parseFloat(split[7]));
			diamond2.setAction(Diamond.TWO_ASPECT_SWAP);
			swapFlag = false;
		}
	}
}
