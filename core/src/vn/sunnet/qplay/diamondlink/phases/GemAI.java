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
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class GemAI extends Phase {
	
	public GameScreen screen;
	public Assets gAssets;
	//protected boolean isFalling;
	
	public DiamondLink Instance = null;
	// cac thong so lien quan den su doi cho 2 o
	protected int sCell = -1, dCell = -1;
	protected boolean backFlag = false;
	protected boolean swapFlag = true;// co cho phep swap hay khong ? true neu co the
	public int account = 0;
	
	public Sound DiamondMove;
	private Random random = new Random();
//	ArrayList<EsObject> createdEffect = new ArrayList<EsObject>();
	
	public GemAI(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		Instance = screen.gGame;
		this.gAssets = Instance.getAssets();
		//createdEffect.clear();
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
	public void onReset() {
		// TODO Auto-generated method stub
		sCell = -1 ; dCell = -1;
		backFlag = false;
		swapFlag = true;
	}

	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		branch = -1;
		sCell = -1; dCell = -1;		
		screen.addInputProcessor(this);
	}

	@Override
	public void onRunning() {
		// TODO Auto-generated method stub
		ActionHandle();
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub

	}
	
	public void ActionHandle() {
		// swap Move
		// logic up date
		// Fall Begin
		// Diamond Move - certaincell and fall + butterfly Move
		//Effect Move
		//AI Move
		if (getState() == ON_RUNNING) {
			
			boolean isDoneLogic = false;
			//isFalling = false;
			if (!swapFlag) {
				
				isDoneLogic = SwapMove(); 
			}
			
			if (!isDoneLogic) {
				screen.logic.update(deltaTime);
				
			}
			
			if (isFall()) screen.fall.state = FallModule.ON_BEGIN;
			screen.fall.update(deltaTime);
			
			DiamondsMove();
			
			EffectsMove();
			
			screen.ai.update(deltaTime);
		}
	}
	
	public void EffectsMove() {

		boolean needSendScore = false;

		for (int i = 0; i < screen.logic.effects.size(); i++) {
			Effect effect = (Effect) screen.logic.effects.get(i);
			effect.update(deltaTime);
//			if (effect.getType() == Effect.LITTLE_DISAPPEAR) {
//				for (int j = 0; j < effect.mirrorTarget.size(); j++) {
//					System.out.println("an 3 "+effect.getSource(0)+" "+effect.mirrorTarget.get(j));
//				}
//			}
			if (screen.GAME_ID == GameScreen.PALAESTRA) {
				if (effect.isFinished()
						&& effect.getType() != Effect.TEMP_EFFECT) {
//					if (effect.getOnlineStatus() == Effect.NEED_TO_SEND) {
//						effect.setOnlineStatus(Effect.SENDED);
//						endEffect(effect);
//						needSendScore = true;
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
	
	public boolean SwapMove() {// tra ve true neu thuc hien 1 lan logic con lai tra ve false
		screen.timeSwap = 0;
		IDiamond diamond1 = null;
		IDiamond diamond2 = null;
		diamond1 = screen.diamonds.get(sCell);
		diamond2 = screen.diamonds.get(dCell);
		diamond1.update(deltaTime);
		diamond2.update(deltaTime);
		float x1 = diamond1.getSourX();
		float y1 = diamond1.getSourY();
		float x2 = diamond1.getDesX();
		float y2 = diamond1.getDesY();
		float x = diamond1.getPosX();
		float y = diamond1.getPosY();
		x1 = diamond2.getSourX();
		y1 = diamond2.getSourY();
		x2 = diamond2.getDesX();
		y2 = diamond2.getDesY();
		x = diamond2.getPosX();
		y = diamond2.getPosY();
		Gdx.app.log("test", diamond1.getAction()+" "+diamond2.getAction());
		boolean isDestination = diamond1.isDestination() && diamond2.isDestination();
		if (isDestination) {
			goAspect(); 
			screen.ai.finishSwap();
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
		for (int i = 0 ; i < screen.checkCellEffect.size() ; i++) {
			effect = screen.checkCellEffect.get(i);
		}
		
		if (screen.GAME_ID == GameScreen.PALAESTRA)
			endMove();
		
		changeStatusBehindSwap(sCell / 8, sCell % 8);
		changeStatusBehindSwap(dCell / 8, dCell % 8);
		screen.logic.init();
		
		screen.logic.update(deltaTime);
		if (!screen.logic.isChange()) {// khong phat sinh hieu ung
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
			
			swapFlag = true;
			return true;
		} else {//phat sinh hieu ung
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
			swapFlag = true;
			
		}
		return false;
	}
	
	public void beginSwap(int sou, int des) {
		swapFlag = false;
		sCell = sou; dCell = des;
		IDiamond diamond1 = screen.diamonds.get(sou);
		IDiamond diamond2 = screen.diamonds.get(des);
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
		Gdx.app.log("test", sCell+" swap and "+dCell);
		changeStatusBeforeSwap(sCell / 8, sCell % 8);
		changeStatusBeforeSwap(dCell / 8, dCell % 8);
		swapCellValue(sou, des);
	}
	
	public void changeStatusBeforeSwap(int row, int col) {
		screen.ai.removeCell(row, col);
	}
	
	public void changeStatusBehindSwap(int row, int col) {
		screen.ai.addCell(row, col);
	}
	
	public void beginChain(int sou, int des) {
		swapFlag = false;
		Effect effect = screen.logic.allocateEffect(Effect.CHAIN_THUNDER);
		if (isChain(sou, des) == sou) effect.setSource(sou,des);
		else effect.setSource(des,sou);
		screen.checkCellEffect.add((IEffect) effect);
		if (screen.logic.state == GameLogic.ON_END)
			screen.logic.state = GameLogic.ON_RUNNING;
		swapFlag = true;
	}
	
	public void swapCellValue(int cell1, int cell2) {
		int middle = 0;
		middle = screen.grid[CellRow(cell1)][CellCol(cell1)];
		screen.grid[CellRow(cell1)][CellCol(cell1)] = screen.grid[CellRow(cell2)][CellCol(cell2)];
		screen.grid[CellRow(cell2)][CellCol(cell2)] = middle;
	}
	
	public int isChain(int sou, int des) {
		int row = CellRow(sou); int col = CellCol(sou);
		int beginLimit = Diamond.FIVE_COLOR_DIAMOND * screen.COLOR_NUM - 1;
		int endLimit = (Diamond.FIVE_COLOR_DIAMOND + 1) * screen.COLOR_NUM;
		if (screen.grid[row][col] > beginLimit && screen.grid[row][col] < endLimit) 
			return sou;
		row = CellRow(des); col = CellCol(des);
		if (screen.grid[row][col] > beginLimit && screen.grid[row][col] < endLimit) return des;
		return - 1;
	}
	
	
	public void draw(float deltaTime) {
		int count = 0;
		if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1); else 
		if (screen.curStep != GameScreen.GAME_OVER) screen.batch.setColor(1, 1, 1, 1);
		
		screen.fall.draw(deltaTime);
        for (int i = 0 ; i < screen.diamonds.size() ; i++)
        {
        	IDiamond diamond = screen.diamonds.get(i);
        	
        	TextureRegion region = null;
        	
        	int value = 0;
        	
        	if (i < 64)
        	value = screen.logic.gridFlag[CellRow(i)][CellCol(i)];

        	if (isDrawingCell(i))  {
//        		count++;
        		
        		if (diamond.getRearEffect() != null) {
            		PooledEffect effect = diamond.getRearEffect();
            		effect.setPosition(diamond.getPosX() , diamond.getPosY() );
            		effect.draw(screen.batch, deltaTime);
            	}
        		
        		if (diamond.getRearSprite() != null) {
        			region = diamond.getRearSprite().getKeyFrame(diamond.getTime());
        			
        			Rectangle bounds = new Rectangle(40, 40, 120, 120);
        			drawAnimation(region, diamond.getCenterPosition(), bounds);
        			bounds = null;
        		}
        		
        		value = diamond.getDiamondValue();
        		
            	region = diamond.getSprite().getKeyFrame(diamond.getTime());
         
            	drawAnimation(region, diamond.getCenterPosition(), diamond.getBound());
 
            	
            	
            	
            	
        		if (diamond.getFrontSprite() != null) {
        			region = diamond.getFrontSprite().getKeyFrame(diamond.getTime());
        			
        			drawAnimation(region, diamond.getCenterPosition(), diamond.getBound());
        		}
        		
        		
        		
        	} else {
//        		Log.d("NO EMPTY", "row = "+(i/8)+" col = "+(i%8)+" "+screen.inGridFlag[i/8][i % 8]);
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
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}
	
	public boolean certainCell(int value) {
		return (Operator.getBit(Effect.FIXED_POS, value) > 0);// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public boolean isDrawingCell(int cell) {
		if (cell < 64) {
			int value = screen.logic.gridFlag[CellRow(cell)][CellCol(cell)];
			if (certainCell(value) || isSwapCell(cell)) return true;
		} else return true;
		return false;
	}
	
	public boolean isSwapCell(int cell) {
		return (!swapFlag) && (cell == sCell | cell == dCell);
	}
	
	protected boolean isFall() {
		// TODO Auto-generated method stub
		if (screen.logic.isSpecialEffect()) return false;
		//Log.d("test", "check Hieght");
		for (int i = 0; i < 8; i++) 
			if (screen.colHeight[i] < 8) return true;
		return false;
	}
	
	private void beginMove() {
//		VsDiamond game = (VsDiamond) screen;
//		short shortArr[]  = null;
//		byte byteArr[] = null;
//		MyEsObject message = game.myEsObjectPool.obtain();
//		//message.setString(ActionNames.ACTION, MyEsObject.BEGIN_MOVE);
//		message.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		message.setString(FieldNames.TYPE, FieldValues.BEGIN_MOVE);
//		shortArr = new short[2];
//		shortArr[0] = (short) sCell;
//		shortArr[1] = (short) dCell;
//		message.setShortArray(FieldNames.POSITION, shortArr);
//
//		message.setFloat(FieldNames.TIME, game.eclapsedTime);
//		message.setInteger(FieldNames.CONCURRENT, 0);
////		message.setString(ActionNames.FROM, game.gGameInfo.userNames[game.gGameInfo.myPosition]);
////		message.setString(ActionNames.TO, game.gGameInfo.userNames[game.gGameInfo.opponentPosition]);
////		message.setInteger(ActionNames.ABOUT, MyEsObject.MAIN_BOARD);
////		game.gGameInfo.mySavedMessages.add(message);
//		game.gGameInfo.myCurMessages.add(message);
	}
	
	private void endMove() {
//		VsDiamond game = (VsDiamond) screen;
//		short shortArr[]  = null;
//		byte byteArr[] = null;
//		MyEsObject message = game.myEsObjectPool.obtain();
//		//message.setString(ActionNames.ACTION, MyEsObject.END_MOVE);
//		message.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		message.setString(FieldNames.TYPE, FieldValues.END_MOVE);
//		shortArr = new short[2];
//		shortArr[0] = (short) sCell;
//		shortArr[1] = (short) dCell;
//		message.setShortArray(FieldNames.POSITION, shortArr);
//
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
