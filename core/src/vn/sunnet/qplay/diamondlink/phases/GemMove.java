package vn.sunnet.qplay.diamondlink.phases;

import java.util.ArrayList;
import java.util.Random;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.FallModule;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;




public class GemMove extends Phase {
	
	public GameScreen screen;
	protected boolean backFlag = false;
	protected int sCell = -1;
	protected int dCell = -1;
	
	public DiamondLink Instance = null;
	
	public Assets gAssets = null;

	public Sound DiamondMove;
	Random random = new Random();
//	ArrayList<EsObject> createdEffect = new ArrayList<EsObject>();
	
	public GemMove(GameScreen pScreen) {
		// TODO Auto-generated constructor stub
		this.screen = pScreen;
		Instance = (DiamondLink) screen.gGame;
		this.gAssets = Instance.getAssets();
//		createdEffect.clear();
	}
	
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
//		if (screen.playMode != screen.GAME) return false;
		if (pointer > 0 || screen.gTouchMode == GameScreen.TOUCH_UI) return false;
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
				} else 
				if (screen.selection != touchCell) {
					 // khac khu vuc lan can
					 diamond1 = screen.diamonds.get(screen.selection);
					 diamond1.setAction(Diamond.REST);
					 screen.selection = touchCell;
					 diamond1 = screen.diamonds.get(touchCell);
					 diamond1.setAction(Diamond.FRAME_CHANGE);	
//					 screen.touchDownX = screen.curPoint.x; screen.touchDownY = screen.curPoint.y;
					 screen.lastPoint.set(screen.curPoint);
				} else {
					diamond1 = screen.diamonds.get(screen.selection);
					diamond1.setAction(Diamond.REST);
					screen.selection = -1;
				}
			}
		} 
		else{
//			MissionDiamond pScreen = (MissionDiamond) screen;
// 			for (int i = 0 ; i < 4 ; i++) {
// 				float x = pScreen.gItemsX[i];
// 				float y = pScreen.gItemsY[i];
// 				float width = pScreen.gItemsW[i];
// 				float height = pScreen.gItemsH[i];
// 				if (x < screen.curPoint.x && screen.curPoint.x < x + width && y < screen.curPoint.y && screen.curPoint.y < y + height) {
// 					useItem(pScreen.gCurItems[i], i);
// 					break;
// 				}
// 			}
			if (screen.selection != -1) {
				diamond1 = screen.diamonds.get(screen.selection);
				diamond1.setAction(Diamond.REST);
			}
			screen.selection = -1;
		}
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

	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		GemRest phase = (GemRest) screen.gamePhase[GameScreen.DIAMOND_REST];
		sCell = phase.sCell; dCell = phase.dCell;
		//Gdx.app.log("GemMove", "begin with "+sCell+" "+dCell);
		branch = -1;
		screen.selection = -1;
		screen.addInputProcessor(this);
	}

	@Override
	public void onRunning() {
		// TODO Auto-generated method stub
		EventHandle();
		ActionHandle();
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		screen.removeInputProcessor(this);
		screen.gamePhase[branch].setState(Phase.ON_BEGIN);
		screen.stateGame = branch;
		screen.gamePhase[branch].update(0);
//		if (branch == GameScreen.DIAMOND_REST)
//		Gdx.app.log("GemMove", "end to GemRest");
//		else Gdx.app.log("GemMove", "end to GemAnimation");
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
		
			
			boolean isDoneLogic = false;
			
	
			isDoneLogic = SwapMove(); 
	
			
			if (!isDoneLogic) {
				screen.logic.update(deltaTime);
				
			}
			
			if (isFall()) screen.fall.state = FallModule.ON_BEGIN;
			screen.fall.update(deltaTime);
			
			DiamondsMove();
			
			EffectsMove();
		}
	}
	
	public void DiamondsMove() {
		for (int i = 0 ; i < Math.min(screen.diamonds.size() , 64) ; i++) {
			IDiamond diamond = screen.diamonds.get(i);
			int u = CellRow(i);
			int v = CellCol(i);
			if (i != sCell && i != dCell)
			if (certainCell(screen.inGridFlag[u][v]))
				diamond.update(deltaTime);
		}
	}
	
	public void EffectsMove() {
		
		boolean needSendScore = false;

		for (int i = 0; i < screen.logic.effects.size(); i++) {
			Effect effect = (Effect) screen.logic.effects.get(i);
			effect.update(deltaTime);
			if (screen.GAME_ID == GameScreen.PALAESTRA) {
				if (effect.isFinished()
						&& effect.getType() != Effect.TEMP_EFFECT) {
////					if (effect.getOnlineStatus() == Effect.NEED_TO_SEND) {
////						effect.setOnlineStatus(Effect.SENDED);
////						endEffect(effect);
////						needSendScore = true;
//					}
				}
			}
		}

		if (needSendScore)
			sendScore();

//		if (screen.GAME_ID == GameScreen.PALAESTRA) {
//			if (createdEffect.size() > 0)
//				sendEffect(null);
//		}
		
	}
	
	public boolean SwapMove() {// true la co xet logic roi
		screen.timeSwap = 0;
		IDiamond diamond1 = null;
		IDiamond diamond2 = null;
		diamond1 = screen.diamonds.get(sCell);
		diamond2 = screen.diamonds.get(dCell);
		//diamond1.update(deltaTime);
		//diamond2.update(deltaTime);
//		Log.d("frame", "SwapProcess in Move");
		boolean isDestination = diamond1.isDestination() && diamond2.isDestination();
		if (isDestination) {
			goAspect();
			return true;
		}
		return false;
	}
	
	public boolean goAspect() {
		IDiamond diamond1 = screen.diamonds.get(sCell);
		IDiamond diamond2 = screen.diamonds.get(dCell);
		IEffect effect = null;
		float xx = 0 , yy = 0;
//		Log.d("test", "beginLogic2");
//		swapCellValue(sCell, dCell);
		/*
		int middle = 0;
		middle = screen.grid[CellRow(sCell)][CellCol(sCell)];
		screen.grid[CellRow(sCell)][CellCol(sCell)] = screen.grid[CellRow(dCell)][CellCol(dCell)];
		screen.grid[CellRow(dCell)][CellCol(dCell)] = middle;
		*/
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
//			Log.d("Level", "At DiamondMove before"+effect.getSource(0)+" type = "+effect.getType());
		}
		
		if (screen.GAME_ID == GameScreen.PALAESTRA)
			endMove();
		screen.logic.init();
		
		screen.logic.update(deltaTime);
		for (int i = 0 ; i < screen.checkCellEffect.size() ; i++) {
			effect = screen.checkCellEffect.get(i);
//			Log.d("Level", "At DiamondMove behind"+effect.getSource(0)+" type = "+effect.getType());
		}
		if (!screen.logic.isChange()) {// khong phat sinh hieu ung
			// doi Animation
			xx = CordXOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond1.setSource(xx, yy);
			diamond1.setDestination(xx, yy);
//			Gdx.app.log("TestMove", "o 1 "+sCell+" "+xx+" "+yy);
			xx = CordXOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			
			diamond2.setSource(xx, yy);
			diamond2.setDestination(xx, yy);
//			MyAnimation animation = gAssets.getDiamondAnimation(screen.grid[CellRow(sCell)][CellCol(sCell)], screen.getGameID());
			diamond1.setDiamondValue(screen.grid[CellRow(sCell)][CellCol(sCell)]);
//			diamond1.setSprite(animation);
			
//			animation = gAssets.getDiamondAnimation(screen.grid[CellRow(dCell)][CellCol(dCell)], screen.getGameID());
			diamond2.setDiamondValue(screen.grid[CellRow(dCell)][CellCol(dCell)]);
//			diamond2.setSprite(animation);
			
//			Assets.playSound(Assets.NoChange, 0);
			phaseState = ON_END;
			branch = GameScreen.DIAMOND_REST;
		} else {//pha sinh hieu ung // den day moi doi animation thuc su
			xx = CordXOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond1.setSource(xx, yy);
			diamond1.setDestination(xx, yy);

			xx = CordXOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			
			diamond2.setSource(xx, yy);
			diamond2.setDestination(xx, yy);

//			MyAnimation animation = gAssets.getDiamondAnimation(screen.grid[CellRow(sCell)][CellCol(sCell)], screen.getGameID());
			diamond1.setDiamondValue(screen.grid[CellRow(sCell)][CellCol(sCell)]);
//			diamond1.setSprite(animation);
			
//			animation = gAssets.getDiamondAnimation(screen.grid[CellRow(dCell)][CellCol(dCell)], screen.getGameID());
			diamond2.setDiamondValue(screen.grid[CellRow(dCell)][CellCol(dCell)]);
//			diamond2.setSprite(animation);
	
			phaseState = ON_END;
			branch = GameScreen.DIAMOND_ANIMATION;
			
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
		} else {
			backFlag = false;
			phaseState = ON_END;
			branch = GameScreen.DIAMOND_ANIMATION;
			return false;
		}
		
		return true;
	}
	
/********************************Draw Methods**********************************/
	
	public void draw(float deltaTime) {
		if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1); else 
			if (screen.curStep != GameScreen.GAME_OVER) screen.batch.setColor(1, 1, 1, 1);
		screen.fall.draw(deltaTime);
        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	
        	IDiamond diamond = screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	TextureRegion region = null;
        	
        	int value = 0;
        	
        	if (i < 64)
        	value = screen.logic.gridFlag[CellRow(i)][CellCol(i)];
			if (isDrawingCell(i)) {
				if (diamond.getRearEffect() != null) {
					PooledEffect effect = diamond.getRearEffect();
					effect.setPosition(diamond.getPosX(), diamond.getPosY());
					effect.draw(screen.batch, deltaTime);
				}
				if (diamond.getRearSprite() != null) {
					region = diamond.getRearSprite().getKeyFrame(
							diamond.getTime());
					Rectangle bounds = new Rectangle(40, 40, 120, 120);
					drawAnimation(region, diamond.getCenterPosition(), bounds);
					bounds = null;
				}
				value = diamond.getDiamondValue();
				
				region = diamond.getSprite().getKeyFrame(diamond.getTime());
				drawAnimation(region, diamond.getCenterPosition(), diamond.getBound());
			
				if (diamond.getFrontSprite() != null) {
					region = diamond.getFrontSprite().getKeyFrame(
							diamond.getTime());
					drawAnimation(region, diamond.getCenterPosition(),
							diamond.getBound());
				}
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
	public boolean isSwapCell(int cell) {
		return (cell == sCell | cell == dCell);
	}
	
	public boolean isDrawingCell(int cell) {
		if (cell < 64) {
			int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
			if (certainCell(value) || isSwapCell(cell)) return true;
		} else return true;
		return false;
	}
	
	protected boolean isFall() {
		// TODO Auto-generated method stub
		if (screen.logic.isSpecialEffect()) return false;
		//Log.d("test", "check Hieght");
		for (int i = 0; i < 8; i++) 
			if (screen.colHeight[i] < 8) return true;
		return false;
	}

	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
	} 
	
	public boolean certainCell(int value) {
		return (Operator.getBit(Effect.FIXED_POS, value) > 0);// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
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

	private void useItem(int pItem, int pPosition) {
//		MissionDiamond pScreen = (MissionDiamond) screen;
//		switch (pItem) {
//		case 0:
//			if (pScreen.gItemLimits[pPosition] == 0) return;
//			pScreen.timeRemain = Math.min(pScreen.timeRemain + 20, pScreen.timeLevel);
//			break;
//		case 1:
//			if (pScreen.timeRemain > 20) return;
//			pScreen.gMulScore = true;
//			break;
//		
//		}
	}
	
	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		backFlag = false;
		sCell = -1;
		dCell = -1;
	}
	
	private void endMove() {
//		VsDiamond game = (VsDiamond) screen;
//		game.messagesInFrame++;
//		short shortArr[]  = null;
//		byte byteArr[] = null;
//		MyEsObject message = game.myEsObjectPool.obtain();
////		message.setString(ActionNames.ACTION, MyEsObject.END_MOVE);
//		message.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		message.setString(FieldNames.TYPE, FieldValues.END_MOVE);
//		shortArr = new short[2];
//		shortArr[0] = (short) sCell;
//		shortArr[1] = (short) dCell;
//		message.setShortArray(FieldNames.POSITION, shortArr);
////		shortArr[0] = (short) game.grid[CellRow(sCell)][CellCol(sCell)];
////		shortArr[1] = (short) game.grid[CellRow(dCell)][CellCol(dCell)];
////		message.setShortArray(ActionNames.GRID, shortArr);
////		shortArr[0] = (short) game.inGridFlag[CellRow(sCell)][CellCol(sCell)];
////		shortArr[1] = (short) game.inGridFlag[CellRow(dCell)][CellCol(dCell)];
////		message.setShortArray(ActionNames.FLAG, shortArr);
//		message.setFloat(FieldNames.TIME, game.eclapsedTime);
//		message.setInteger(FieldNames.CONCURRENT, 0);
////		message.setString(ActionNames.FROM, game.gGameInfo.userNames[game.gGameInfo.myPosition]);
////		message.setString(ActionNames.TO, game.gGameInfo.userNames[game.gGameInfo.opponentPosition]);
////		message.setInteger(ActionNames.ABOUT, MyEsObject.MAIN_BOARD);
////		game.gGameInfo.mySavedMessages.add(message);
//		game.gGameInfo.myCurMessages.add(message);
	}
	
	private void endEffect(Effect effect) {
//		VsDiamond game = (VsDiamond) screen;
//		EsObject esObject = game.esObjectPool.obtain();
//		esObject.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		esObject.setString(FieldNames.TYPE, FieldValues.END_EFFECT);
//		
//		esObject.setByte(FieldNames.CHILDREN_TYPE, (byte)effect.getType());
//		esObject.setByte(FieldNames.CHILDREN_DEPTH, (byte)effect.getDepthBFS());
//		byte byteArr[] = new byte[effect.source.length];
//		for (int i = 0 ; i < byteArr.length; i++)
//			byteArr[i] = (byte) effect.source[i];
//		esObject.setByteArray(FieldNames.CHILDREN_SOURCE, byteArr);
//		esObject.setFloat(FieldNames.TIME, screen.eclapsedTime);
//		createdEffect.add(esObject);
	}
	
	private void sendEffect(Effect effect) {
//		VsDiamond gGame = (VsDiamond) screen;
//		gGame.messagesInFrame++;
//		MyEsObject myEsObject = gGame.myEsObjectPool.obtain();
//		myEsObject.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		myEsObject.setString(FieldNames.TYPE, FieldValues.END_EFFECT);
//		
//		EsObject temp[] = new EsObject[createdEffect.size()];
//		for (int i = 0 ; i < createdEffect.size(); i++) {
//			EsObject esObject = createdEffect.get(i);
//			temp[i] = esObject;
////			esObject.setInteger(ActionNames.CONCURRENT, createdEffect.size());
//		}
//		myEsObject.setEsObjectArray(FieldNames.EFFECTS, temp);
//		myEsObject.setFloat(FieldNames.TIME, gGame.eclapsedTime);
//		myEsObject.setInteger(FieldNames.CONCURRENT, 0);
//		
//		gGame.gGameInfo.myCurMessages.add(myEsObject);
//		createdEffect.clear();
	}
	
	private void sendScore() {
//		VsDiamond gGame = (VsDiamond) screen;
//		gGame.messagesInFrame++;
//		MyEsObject myEsObject = gGame.myEsObjectPool.obtain();
//		myEsObject.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		myEsObject.setString(FieldNames.TYPE, FieldValues.UPDATE_SCORE);
//		myEsObject.setFloat(FieldNames.SCORE, gGame.levelScore);
//		
//		myEsObject.setFloat(FieldNames.TIME, gGame.eclapsedTime);
//		myEsObject.setInteger(FieldNames.CONCURRENT, 0);
//		gGame.gGameInfo.myCurMessages.add(myEsObject);
	}

	@Override
	protected boolean touchDownInTouchGame(int screenX, int screenY,
			int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchDownInTouchItem(int screenX, int screenY,
			int pointer, int button) {
		// TODO Auto-generated method stub
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
		return false;
	}

	@Override
	protected boolean touchDraggedInTouchGame(int screenX, int screenY,
			int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchDraggedInTouchItem(int screenX, int screenY,
			int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
