package vn.sunnet.qplay.diamondlink.phases;




import java.util.List;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;

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





public class DiamondMove extends Phase {
	
	public GameScreen screen;
	protected boolean backFlag = false;
	protected int sCell = -1;
	protected int dCell = -1;
	
	public DiamondLink Instance = null;
	
	private Sound move;
	private Sound moveFail;
	private Sound select;
	
	
	public DiamondMove(GameScreen pScreen) {
		// TODO Auto-generated constructor stub
		this.screen = pScreen;
		Instance = (DiamondLink) screen.gGame;
	
		
		
	}
	
	/*********************************Implements Phases' Methods*********************************/
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		DiamondRest phase = (DiamondRest) screen.gamePhase[GameScreen.DIAMOND_REST];
		sCell = phase.sCell; dCell = phase.dCell;
		branch = -1;
		screen.selection = -1;
		screen.addInputProcessor(this);
		Gdx.app.log("test", "Begin Move" + screen.selection);
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
		//Log.d("Game", "before Move go to Animation "+screen.stateGame);
		screen.removeInputProcessor(this);
		if (branch == GameScreen.DIAMOND_REST && screen.checkCellEffect.size() > 0) branch = GameScreen.DIAMOND_ANIMATION;
		screen.gamePhase[branch].setState(Phase.ON_BEGIN);
		screen.stateGame = branch;
		screen.gamePhase[branch].update(0);
		//Log.d("Game", "behind Move go to Animation "+screen.stateGame);
	}
	
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void EventHandle() {}
	
	public void ActionHandle() {
		IDiamond diamond = null;
		// TODO Auto-generated method stub
		if (getState() == ON_RUNNING) {
			for (int i = 0 ; i < screen.diamonds.size() ; i++) {
				diamond = screen.diamonds.get(i);
				diamond.update(deltaTime);
				if (diamond.containsAction(IDiamond.FLY)) {
					if (diamond.isFinished(IDiamond.FLY)) {
						screen.diamonds.remove(i);
						i--;
					}
				}
			}
			SwapMove();
		}
	}
	
	public void SwapMove() {
		screen.timeSwap = 0;
		IDiamond diamond1 = null;
		IDiamond diamond2 = null;
		diamond1 = screen.diamonds.get(sCell);
		diamond2 = screen.diamonds.get(dCell);
		//diamond1.update(deltaTime);
		//diamond2.update(deltaTime);
//		Log.d("frame", "SwapProcess in Move");
		boolean isDestination = diamond1.isDestination() && diamond2.isDestination();
		System.out.println("moving "+sCell+" = "+diamond1.getDiamondValue()+" "+dCell+" = "+diamond2.getDiamondValue());
		if (isDestination)
			if (!backFlag) goAspect(); else returnAspect();
	}
	
	public boolean goAspect() {
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
		for (int i = 0 ; i < screen.checkCellEffect.size() ; i++) {
			effect = screen.checkCellEffect.get(i);
		}
		
		screen.logic.init();
		
		screen.logic.update(deltaTime);
		for (int i = 0 ; i < screen.checkCellEffect.size() ; i++) {
			effect = screen.checkCellEffect.get(i);
		}
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
		} else {//pha sinh hieu ung // den day moi doi animation thuc su
			
			float time1 = diamond1.getActiveTime();
			float time2 = diamond2.getActiveTime();
			int value1 = diamond1.getDiamondValue();
			int value2 = diamond2.getDiamondValue();
			System.out.println("phat sinh hieu ung "+sCell+" = "+value1+" "+dCell+" = "+value2);
			
			xx = CordXOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond1.setSource(xx, yy);
			diamond1.setDestination(xx, yy);
			xx = CordXOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			
			diamond2.setSource(xx, yy);
			diamond2.setDestination(xx, yy);
			
//			MyAnimation animation = screen.assets.getDiamondAnimation(value2, screen.getGameID());
//			diamond1.setDiamondValue(screen.grid[CellRow(sCell)][CellCol(sCell)]);
			diamond1.setDiamondValue(value2);
//			diamond1.setSprite(animation);
			
//			animation = screen.assets.getDiamondAnimation(value1, screen.getGameID());
//			diamond2.setDiamondValue(screen.grid[CellRow(dCell)][CellCol(dCell)]);
			diamond2.setDiamondValue(value1);
//			diamond2.setSprite(animation);
			
			float time = diamond1.getActiveTime();
			diamond1.setActivieTime(time2);
			diamond2.setActivieTime(time1);
			
			backFlag = false;
//			sCell = -1; dCell = -1;
			
			phaseState = ON_END;
			branch = GameScreen.DIAMOND_ANIMATION;
			
//			Log.d("test", "has change");
			return false;
		}
		return true;
	}
	
	public boolean returnAspect() {// tra ve false neu phat sinh hieu ung
//		Log.d("test", "BackPosition");
		IDiamond diamond1 = screen.diamonds.get(sCell);
		IDiamond diamond2 = screen.diamonds.get(dCell);
		diamond1.setAction(Diamond.REST);
		diamond2.setAction(Diamond.REST);
		
		//diamond1.setSource(screen.gr, yy);
		// danh dau lai gridtype
		int row1 = CellRow(sCell); int col1 = CellCol(sCell);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row1][col1]);
		row1 = CellRow(dCell); col1 = CellCol(dCell);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row1][col1]);
		swapCellValue(sCell, dCell);
		
		/*
		int middle = 0;
		middle = screen.gridFur[CellRow(sCell)][CellCol(sCell)];
		screen.gridFur[CellRow(sCell)][CellCol(sCell)] = screen.gridFur[CellRow(dCell)][CellCol(dCell)];
		screen.gridFur[CellRow(dCell)][CellCol(dCell)] = (byte) middle;
		*/
		// loai bo khoi danh sach checkCell
		//screen.checkCellEffect.clear();
		//screen.gridType[]
		// xet de phong
		IEffect effect = (IEffect) screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(sCell);
		effect.setSwapFlag(true);
		screen.checkCellEffect.add(effect);
		effect = (IEffect) screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(dCell);
		effect.setSwapFlag(true);
		
		screen.logic.init();
		
		screen.logic.update(deltaTime);
		
		if (!screen.logic.isChange) {
			backFlag = false;
			phaseState = ON_END;
			branch = GameScreen.DIAMOND_REST;
//			sCell = -1; dCell = -1;
		} else {
			backFlag = false;
			phaseState = ON_END;
			branch = GameScreen.DIAMOND_ANIMATION;
//			sCell = -1; dCell = -1;
			return false;
		}
		
		return true;
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
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		IDiamond diamond1;
		if (screen.gridTable.contains(screen.curPoint.x, screen.curPoint.y)) {
			touchCell = touchCell(screen.curPoint);
			int row = CellRow(touchCell);
			int col = CellCol(touchCell);
			if (certainCell(screen.inGridFlag[row][col])) {
				if (screen.selection == -1) {
					screen.selection = touchCell;
					diamond1 = screen.diamonds.get(touchCell);
					diamond1.setAction(Diamond.FRAME_CHANGE);
//					screen.touchDownX = screen.curPoint.x; screen.touchDownY = screen.curPoint.y;
					screen.lastPoint.set(screen.curPoint);
					Assets.playSound(select);
				} else 
				if (screen.selection != touchCell) {
					 // khac khu vuc lan can
					 diamond1 = screen.diamonds.get(screen.selection);
					 diamond1.setAction(Diamond.REST);
					 screen.selection = touchCell;
					 diamond1 = screen.diamonds.get(touchCell);
					 diamond1.setAction(Diamond.FRAME_CHANGE);	
					 screen.lastPoint.set(screen.curPoint);
				} else {
					diamond1 = screen.diamonds.get(screen.selection);
					diamond1.setAction(Diamond.REST);
					screen.selection = -1;
				}
			}
		} 
		else{
			if (screen.selection != -1) {
				diamond1 = screen.diamonds.get(screen.selection);
				diamond1.setAction(Diamond.REST);
			}
			screen.selection = -1;
		}
		return false;
	}
	
	protected boolean touchDownInTouchItem(int screenX, int screenY, int pointer, int button) {
	
		return false;
	}
	
	protected boolean touchUpInTouchGame(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	protected boolean touchUpInTouchItem(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	protected boolean touchDraggedInTouchGame(int screenX, int screenY, int pointer) {
		return false;
	}
	
	protected boolean touchDraggedInTouchItem(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
		// TODO Auto-generated method stub
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
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
		
//		if (((MissionDiamond)screen).mission != null) {
//			Mission mission = ((MissionDiamond)screen).mission;
//			if (mission.getType() == Mission.FIND_SHODE) {
//				FindShoe findShoe = (FindShoe) mission;
//				screen.batch.end();
//				for (int i = 0; i < findShoe.positions.size(); i++) {
//					int cell = findShoe.positions.get(i);
//					int row = cell / 8;
//					int col = cell % 8;
//					screen.renderer.begin(ShapeType.Filled);
//					screen.renderer.rect(screen.gridPos.x + col
//							* screen.DIAMOND_WIDTH, screen.gridPos.y + row
//							* screen.DIAMOND_HEIGHT, screen.DIAMOND_WIDTH,
//							screen.DIAMOND_HEIGHT);
//					screen.renderer.end();
//				}
//				screen.batch.begin();
//			}
//		}
        
        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	
        	IDiamond diamond = screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	diamond.draw(deltaTime, screen.batch);
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
//		Gdx.app.log("FallModule", "Da thanh cong");
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

	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
	} 
	
	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
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
	
	public int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	public int diamondColor(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
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
	
	public void swapCellValue(int cell1, int cell2) {
		int middle = 0;
		middle = screen.grid[CellRow(cell1)][CellCol(cell1)];
		screen.grid[CellRow(cell1)][CellCol(cell1)] = screen.grid[CellRow(cell2)][CellCol(cell2)];
		screen.grid[CellRow(cell2)][CellCol(cell2)] = middle;
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		backFlag = false;
		sCell = -1;
		dCell = -1;
	}

	@Override
	public void save(IFunctions iFunctions) {
		// TODO Auto-generated method stub
		super.save(iFunctions);
		iFunctions.putFastInt("phaseState "+screen.GAME_ID, phaseState);
		iFunctions.putFastInt("branch "+screen.GAME_ID, branch);
		if (sCell != -1 && dCell != -1) {
			iFunctions.putFastBool("move state data", true);
			iFunctions.putFastBool("move state backFlag "+screen.GAME_ID, backFlag);
			IDiamond diamond1 = screen.diamonds.get(sCell);
			IDiamond diamond2 = screen.diamonds.get(dCell);
			iFunctions.putFastString("move state sCell " + screen.GAME_ID, sCell
					+ "|" + diamond1.getDiamondValue()
					+ "|" + diamond1.getSourX() + "|" + diamond1.getSourY() 
					+ "|" + diamond1.getPosX() + "|" + diamond1.getPosY() 
					+ "|" + diamond1.getDesX() + "|" + diamond1.getDesY());
			iFunctions.putFastString("move state dCell " + screen.GAME_ID, dCell
					+ "|" + diamond2.getDiamondValue()
					+ "|" + diamond2.getSourX() + "|" + diamond2.getSourY() 
					+ "|" + diamond2.getPosX() + "|" + diamond2.getPosY() 
					+ "|" + diamond2.getDesX() + "|" + diamond2.getDesY());
		} else iFunctions.putFastBool("move state data", false);
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
		if (iFunctions.getFastBool("move state data", false)) {
			backFlag = iFunctions.getFastBool("move state backFlag "+screen.GAME_ID, false);
			String data = iFunctions.getFastString("move state sCell " + screen.GAME_ID, "");
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length; i++)
				System.out.println("split "+split[i]);
			sCell = Integer.parseInt(split[0]);
			IDiamond diamond1 = screen.diamonds.get(sCell);
			diamond1.setDiamondValue(Integer.parseInt(split[1]));
			
			diamond1.setSource(Float.parseFloat(split[2]), Float.parseFloat(split[3]));
			diamond1.setCenterPosition(Float.parseFloat(split[4]), Float.parseFloat(split[5]));
			diamond1.setDestination(Float.parseFloat(split[6]), Float.parseFloat(split[7]));
			diamond1.setAction(Diamond.TWO_ASPECT_SWAP);
			
			
			data = iFunctions.getFastString("move state dCell " + screen.GAME_ID, "");
			split = data.split("\\|");
			System.out.println("++++++++++++++++++++++");
			for (int i = 0; i < split.length; i++)
				System.out.println("split "+split[i]);
			dCell = Integer.parseInt(split[0]);
			IDiamond diamond2 = screen.diamonds.get(dCell);
			diamond2.setDiamondValue(Integer.parseInt(split[1]));
			
			diamond2.setSource(Float.parseFloat(split[2]), Float.parseFloat(split[3]));
			diamond2.setCenterPosition(Float.parseFloat(split[4]), Float.parseFloat(split[5]));
			diamond2.setDestination(Float.parseFloat(split[6]), Float.parseFloat(split[7]));
			diamond2.setAction(Diamond.TWO_ASPECT_SWAP);
			
			System.out.println("parse move state "+sCell+" = "+diamond1.getDiamondValue()+" "+dCell+" = "+diamond2.getDiamondValue());
		}
	}
}
