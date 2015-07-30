package vn.sunnet.qplay.diamondlink.phases;





import java.text.BreakIterator;
import java.util.List;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.graphiceffects.Glaze;
import vn.sunnet.qplay.diamondlink.graphiceffects.OpenGLEffect;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.screens.GameScreen;





import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.xml.internal.bind.v2.model.core.ID;

public class DiamondRest extends Phase {

	public int sCell = -1;
	public int dCell = -1;
	
	protected GameScreen screen;
	protected DiamondLink Instance = null;
	
	private Sound move;
	private Sound moveFail;
	protected Sound select;
	
	protected float glazeToLimit = 7f;
	
	public DiamondRest(GameScreen screen) {
		this.screen = screen;
		Instance = screen.gGame;
	}
	
	/*********************************Implements Phases' Methods*********************************/
	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		sCell = -1;
		dCell = -1;
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		branch = -1; sCell = -1; dCell = -1;
		screen.selection = -1;
		screen.addInputProcessor(this);
		Gdx.app.log("test", "Begin Rest" + screen.selection);
		move = screen.manager.get(SoundAssets.MOVE_SOUND, Sound.class);
		moveFail = screen.manager.get(SoundAssets.MOVE_FAIL_SOUND, Sound.class);
		select = screen.manager.get(SoundAssets.SELECT_SOUND, Sound.class);
		
		
	}

	@Override
	public void onRunning() {
		EventHandle();		
		ActionHandle(); 
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		screen.removeInputProcessor(this);
		screen.glaze.setStatus(OpenGLEffect.END);
		screen.batch.setShader(null);
		if (branch == GameScreen.DIAMOND_MOVE) {
			beginSwap(sCell, dCell);
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
		} else if (branch == GameScreen.DIAMOND_ANIMATION) {
			if (sCell != -1)
				beginChain(sCell, dCell);
			else {// truong hop item
				screen.logic.init();
				screen.logic.update(deltaTime);
			}
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
		} else {
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
			screen.curSkill = null;
		}
	}
	
	public void EventHandle() {
	}
	
	public void ActionHandle() {
		if (getState() == ON_RUNNING) { 
			for (int i = 0 ; i < screen.diamonds.size() ; i++) { // chuyen dong cua o chua selection
				IDiamond diamond = screen.diamonds.get(i);
				diamond.update(deltaTime);
			}
		}
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
	
	protected boolean touchDownInTouchGame(int screenX, int screenY, int pointer, int button) {
		IDiamond diamond1 = null;
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
		
		if (screen.gridTable.contains(screen.curPoint.x, screen.curPoint.y)) { // khi an trong man hinh
 			touchCell = touchCell(screen.curPoint);
 			Gdx.app.log("test", "Touch Down 0" + touchCell+" "+screen.selection);
 			if (canTouchCell(touchCell)) {
				if (diamondType(screen.grid[touchCell / 8][touchCell % 8]) == IDiamond.LASER_DIAMOND) {
					branch = GameScreen.DIAMOND_ANIMATION;
					screen.logic.newEffect(touchCell / 8, touchCell % 8,
							Effect.CROSS_LASER, null);
					screen.selection = -1;
					setState(Phase.ON_END);
				} else
     			if (screen.selection == -1) { // khi truoc do la khong an
     				Gdx.app.log("test", "Touch Down 1");
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
	
	protected boolean touchDownInTouchItem(int screenX, int screenY, int pointer, int button) {
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		screen.curSkill.set(screen.curPoint);
		return false;
	}
	
	protected boolean touchUpInTouchGame(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	protected boolean touchUpInTouchItem(int screenX, int screenY, int pointer, int button) {
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		Gdx.app.log("test", "Touch up Item");
		if (screen.gTouchMode == GameScreen.TOUCH_ITEM) {
    		if (screen.gridTable.contains(screen.curPoint.x, screen.curPoint.y)) {
    			touchCell = touchCell(screen.curPoint);
    			int row = touchCell / 8;
    			int col = touchCell % 8;
    			screen.selectedSkill.dec();
    			Gdx.app.log("test", "Touch up Item"+screen.curSkill);
    			screen.selectedSkill = null;
    			screen.curSkill.generateAt(row, col, screen);
    			screen.curSkill = null;
    			screen.gTouchMode = GameScreen.TOUCH_GAME;
    			branch = GameScreen.DIAMOND_ANIMATION;
    			setState(Phase.ON_END);
    		} else {
    			screen.curSkill = null;
    			screen.selectedSkill = null;
    			screen.gTouchMode = GameScreen.TOUCH_GAME;
    		}
    	} 
		return false;
	}
	
	protected boolean touchDraggedInTouchGame(int screenX, int screenY, int pointer) {
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		
		if (screen.selection != -1) {
    		Vector2 temp1 = new Vector2(screen.lastPoint.x, screen.lastPoint.y);
    		Vector2 temp2 = new Vector2(screen.curPoint.x, screen.curPoint.y);
    		temp2.sub(temp1);
    	
    		
			if (temp2.angle() >= 45 && temp2.angle() < 45 + 90) {
				touchCell = screen.selection + 8;
				if (touchCell > 63 || touchCell < 0) touchCell = -1;
			}
			else
			if (temp2.angle() >= 135 && temp2.angle() < 135 + 90) {
				touchCell = screen.selection - 1;
				if (screen.selection % 8 == 0) touchCell = -1;
				if (touchCell > 63 || touchCell < 0) touchCell = -1;
			}
			else
			if (temp2.angle() >= 225 && temp2.angle() < 225 + 90) {
				touchCell = screen.selection - 8;
				if (touchCell > 63 || touchCell < 0) touchCell = -1;
			}
			else
			{
				touchCell = screen.selection + 1;
				if (touchCell % 8 == 0) touchCell = -1;
			
				
				if (touchCell > 63 || touchCell < 0) touchCell = -1;
			}
    		if (temp2.len() > 30) {		
    			if (touchCell != -1) 
        			if (canTouchCell(touchCell) && canTouchCell(screen.selection) ) {
        				sCell = screen.selection; dCell = touchCell;
     					if (isChain(sCell, dCell) > -1) branch = GameScreen.DIAMOND_ANIMATION;
     					else branch = GameScreen.DIAMOND_MOVE;
     					screen.selection = -1;
     					setState(Phase.ON_END);
        			} else screen.selection = -1;
    		} 
    		temp1 = null; temp2 = null;
    	}
		return false;
	}
	
	protected boolean touchDraggedInTouchItem(int screenX, int screenY, int pointer) {
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		screen.curSkill.set(screen.curPoint);
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
		if (getState() == ON_END) return false;
		if (screen.isOverTime()) return false;
		if (pointer > 0) return false;
		
		
		
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
		
		return false;
	}

	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
		if (getState() == ON_END) return false;
		if (screen.isOverTime()) return false;
		if (pointer > 0) return false;
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
		if (getState() == ON_END) return false;
		if (screen.isOverTime()) return false;
		if (pointer > 0 || screen.gTouchMode == GameScreen.TOUCH_UI) return false;
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
	
	/********************************Methods Before Transfering to DiamondAnimation**********************************/
	
	public void beginSwap(int sou, int des) {
		screen.timeSwap = 0;
		IDiamond diamond1 = screen.diamonds.get(sou);
		IDiamond diamond2 = screen.diamonds.get(des);
//		Log.d("frame", "beginSwap in Rest");
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
		screen.logic.init();
		screen.logic.update(deltaTime);
		
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
	
	/********************************Draw Methods**********************************/
	
	public void draw(float deltaTime) {
		
		screen.batch.setColor(1f, 1f, 1f, 1f);

        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	diamond.behindDiamond(deltaTime, screen.batch);
        }
        
        screen.batch.end();
        /**/
        screen.batch.setShader(screen.glaze.shader);
        screen.batch.begin();
        screen.glaze.update(deltaTime);
        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	diamond.intoDiamond(deltaTime, screen.batch);
        }
        screen.batch.end();
        
        /**/
        screen.batch.setShader(null);
        screen.batch.begin();
        
        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	diamond.inFrontOfDiamond(deltaTime, screen.batch);
        }

		if (screen.selection > -1)
			drawAnimation(
					screen.gSelection,
					CordXOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT), screen.DIAMOND_WIDTH,
					screen.DIAMOND_HEIGHT);
        
		
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
//		Gdx.app.log("FallModule", "Da thanh cong");
		screen.batch.draw(region, pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width / 2  , bound.height / 2 , 
				bound.width, bound.height, 1f, 1f, angle, true);
	}
	
	/********************************Logic Methods**********************************/

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
	
	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
	} 
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}
	
	public int touchCell(Vector3 curPoint) {// loi
		int i = 0;
		int j = 0;
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
	
	public int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	public int diamondColor(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
	}
	
	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public void swapCellValue(int cell1, int cell2) {
		int middle = 0;
		middle = screen.grid[CellRow(cell1)][CellCol(cell1)];
		screen.grid[CellRow(cell1)][CellCol(cell1)] = screen.grid[CellRow(cell2)][CellCol(cell2)];
		screen.grid[CellRow(cell2)][CellCol(cell2)] = middle;
	}

	@Override
	public void save(IFunctions iFunctions) {
		super.save(iFunctions);
		iFunctions.putFastInt("phaseState "+screen.GAME_ID, phaseState);
		iFunctions.putFastInt("branch "+screen.GAME_ID, branch);
	}
	
	@Override
	public void parse(IFunctions iFunctions) {
		// TODO Auto-generated method stub
		super.parse(iFunctions);
		screen.addInputProcessor(this);
		phaseState = iFunctions.getFastInt("phaseState "+screen.GAME_ID, 0);
		branch = iFunctions.getFastInt("branch "+screen.GAME_ID, 0);
		move = screen.manager.get(SoundAssets.MOVE_SOUND, Sound.class);
		moveFail = screen.manager.get(SoundAssets.MOVE_FAIL_SOUND, Sound.class);
		select = screen.manager.get(SoundAssets.SELECT_SOUND, Sound.class);
	}
	
}
