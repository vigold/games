package vn.sunnet.qplay.diamondlink.phases;

import java.util.ArrayList;
import java.util.Random;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.FallModule;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class GemRest extends Phase {

	protected GameScreen screen;
	public int sCell = -1;
	public int dCell = -1;
	
	public DiamondLink Instance = null;
	
	private Assets gAssets;
	private AssetManager gAssetManager;
	private Random random = new Random();
//	ArrayList<EsObject> createdEffect = new ArrayList<EsObject>();
	
	public GemRest(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		Instance = screen.gGame;
		this.gAssets = Instance.getAssets();
		this.gAssetManager = Instance.getAssetManager();
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
		if (screen.gTouchMode == GameScreen.TOUCH_ITEM) {
			screen.itemPoint.set(screen.curPoint);
			return false;
		}
		IDiamond diamond1;
		if (screen.gridTable.contains(screen.curPoint.x, screen.curPoint.y)) { // khi an trong man hinh
 			touchCell = touchCell(screen.curPoint);
 			int row = touchCell / 8;
 			int col = touchCell % 8;
 			if (canTouchCell(touchCell)) {
     			if (screen.selection == -1) { // khi truoc do la khong an
     				screen.selection = touchCell; 
     				diamond1 = screen.diamonds.get(screen.selection);
     				diamond1.setAction(Diamond.FRAME_CHANGE);
     				screen.lastPoint.set(screen.curPoint);
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
// 			MissionDiamond pScreen = (MissionDiamond) screen;
// 			for (int i = 0 ; i < 4 ; i++) {
// 				int position = DiamondGameInfo.myPosition * 4 + i;
// 				float x = pScreen.gItemsX[position];
// 				float y = pScreen.gItemsY[position];
// 				float width = pScreen.gItemsW[position];
// 				float height = pScreen.gItemsH[position];
// 				if (x < screen.curPoint.x && screen.curPoint.x < x + width && y < screen.curPoint.y && screen.curPoint.y < y + height) {
// 					if (PlayerInfo.coin < pScreen.gItemCosts[pScreen.gCurItems[i]])
//						((ISunnetLib) pScreen.gGame.iFunctions)
//								.createToast("Không đủ xu để dùng vật phẩm này");
//					else
//						useItem(pScreen.gCurItems[i], i);
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
		
//		if (screen.playMode != screen.GAME) return false;
		if (pointer > 0 || screen.gTouchMode == GameScreen.TOUCH_UI) return false;
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
	
		if (screen.gTouchMode == GameScreen.TOUCH_ITEM) {
			screen.itemPoint.set(screen.curPoint);
// 			MissionDiamond pScreen = (MissionDiamond) screen;
//    		if (screen.gridTable.contains(screen.itemPoint.x, screen.itemPoint.y)) {
//    			touchCell = touchCell(screen.itemPoint);
//    			int row = touchCell / 8;
//    			int col = touchCell % 8;
//
//				if (pScreen.gUsingItem == MissionDiamond.RC_THUNER) {
//					screen.logic.newEffect(row, col, Effect.RCTHUNDER_ITEM,
//							null);
//					PlayerInfo.coin -= pScreen.gItemCosts[pScreen.gUsingItem];
//				} else if (pScreen.gUsingItem == MissionDiamond.CHAINED_THUNER) {
//					screen.logic.newEffect(row, col, Effect.CHAIN_THUNDER_ITEM,
//							null);
//					PlayerInfo.coin -= pScreen.gItemCosts[pScreen.gUsingItem];
//				} else if (pScreen.gUsingItem == MissionDiamond.NINE_EXPLODE) {
//					screen.logic.newEffect(row, col, Effect.EXPLODE_ITEM, null);
//					PlayerInfo.coin -= pScreen.gItemCosts[pScreen.gUsingItem];
//				}
//    			screen.gTouchMode = GameScreen.TOUCH_GAME;
//    			pScreen.gUsingItem = -1;
//    			branch = GameScreen.DIAMOND_ANIMATION;
//    			setState(Phase.ON_END);
//    		} else screen.gTouchMode = GameScreen.TOUCH_GAME;
    	} 
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		
//		if (screen.playMode != screen.GAME) return false;
		if (pointer > 0 || screen.gTouchMode == GameScreen.TOUCH_UI) return false;
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		if (screen.gTouchMode == GameScreen.TOUCH_GAME) {
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
	        			if (canTouchCell(touchCell) && canTouchCell(screen.selection)) {
	        				sCell = screen.selection; dCell = touchCell;
	     					if (isChain(sCell, dCell) > -1) branch = GameScreen.DIAMOND_ANIMATION;
	     					else branch = GameScreen.DIAMOND_MOVE;
	     					if (screen.selection != -1) {
	     		 				IDiamond diamond1 = screen.diamonds.get(screen.selection);
	     						diamond1.setAction(Diamond.REST);
	     		 			}
	     					screen.selection = -1;
	     					setState(Phase.ON_END);
	        			} else {
	        				if (screen.selection != -1) {
	     		 				IDiamond diamond1 = screen.diamonds.get(screen.selection);
	     						diamond1.setAction(Diamond.REST);
	     		 			}
	        				screen.selection = -1;
	        			}
        		} 
        		temp1 = null; temp2 = null;
        	}
    	} else {
    		screen.itemPoint.set(screen.curPoint);
    	}
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
		branch = -1; sCell = -1; dCell = -1;
		
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
		if (branch == GameScreen.DIAMOND_MOVE) {
			beginSwap(sCell, dCell);
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
//			Log.d("Game", "Rest go to Move "+screen.stateGame);
		} else 
		if (branch == GameScreen.DIAMOND_ANIMATION){
			//screen.logic.SpecialEffect++;
			if (sCell != -1)
			beginChain(sCell, dCell);
			else {// truong hop item
				screen.logic.init();
				screen.logic.update(deltaTime);
			}
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
//			Log.d("Game", "Rest go to Animation "+screen.stateGame);
		}
	}

	public void EventHandle() {
	}
	
	public void ActionHandle() {
		if (getState() == ON_RUNNING) { 
			//Log.d("Game", "Action in REST");
			for (int i = 0 ; i < screen.diamonds.size() ; i++) { // chuyen dong cua o chua selection
				IDiamond diamond = screen.diamonds.get(i);
				diamond.update(deltaTime);
				//diamond.update(deltaTime);
			}
		
			screen.logic.update(deltaTime);
				
		
			
			if (isFall()) screen.fall.state = FallModule.ON_BEGIN;
			screen.fall.update(deltaTime);
			EffectsMove();
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

/********************************Methods Before Transfering to DiamondAnimation**********************************/
	
	public void beginSwap(int sou, int des) {
		//Gdx.app.log("DiamondRest", "move "+sou+"  to "+des);
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
		if (screen.GAME_ID == GameScreen.PALAESTRA)
		beginMove();
	}
	
	public void beginChain(int sou, int des) {
		Effect effect = screen.logic.allocateEffect(Effect.CHAIN_THUNDER);
		if (isChain(sou, des) == sou) effect.setSource(sou,des);
		else effect.setSource(des,sou);
		screen.checkCellEffect.add((IEffect) effect);
		screen.logic.init();
		screen.logic.update(deltaTime);
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
			if (isDrawingCell(i)) {
				if (diamond.getRearEffect() != null) {
					PooledEffect effect = diamond.getRearEffect();
					effect.setPosition(diamond.getPosX(), diamond.getPosY());
					effect.draw(screen.batch, deltaTime);
				}
				// if (diamond.isType == Diamond.FIRE_DIAMOND) Log.d("test",
				// "vien lua");
				if (diamond.getRearSprite() != null) {
					// Log.d("test", "DiamondAnimation REST"+i);
					region = diamond.getRearSprite().getKeyFrame(
							diamond.getTime());

					Rectangle bounds = new Rectangle(40, 40, 120, 120);
					drawAnimation(region, diamond.getCenterPosition(), bounds);
					bounds = null;
				}
				int value = diamond.getDiamondValue();
				
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
        
        if (screen.gTouchMode == GameScreen.TOUCH_ITEM) {
//        	MissionDiamond lScreen = (MissionDiamond) screen;
//        	int cell = touchCell(screen.itemPoint);
//        	
//        	float x = 0;
//        	float y = 0;
//        	TextureRegion region = null;
//        	if (lScreen.gUsingItem == MissionDiamond.RC_THUNER) {
//        		region = lScreen.gRCThunder;
//        		
//        		if (screen.gridTable.contains(screen.itemPoint.x, screen.itemPoint.y)) {
//        			x = CordXOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//        			y = CordYOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//        			float w = screen.DIAMOND_WIDTH;
//        			float h = screen.DIAMOND_HEIGHT;
//	        		screen.batch.draw(region, screen.gridPos.x , y - (1.5f * h) / 2, w * 4, (1.5f * h) / 2, w * 8, 1.5f * h, 1f, 1f, 0 , false);
//	    			screen.batch.draw(region, x - (1.5f * w) / 2, screen.gridPos.y, 1.5f * w, h * 8);
//        		} else {
//        			x = screen.itemPoint.x;
//        			y = screen.itemPoint.y;
//        			float w = screen.DIAMOND_WIDTH;
//        			float h = screen.DIAMOND_HEIGHT;
//        			screen.batch.draw(region, x - w * 4 , y - (1.5f * h) / 2, w * 4, (1.5f * h) / 2, w * 8, 1.5f * h, 1f, 1f, 0 , false);
//	    			screen.batch.draw(region, x - (1.5f * w) / 2, y - h * 4, 1.5f * w, h * 8);
//        		}
//        	} 
//        	
//        	if (lScreen.gUsingItem == MissionDiamond.CHAINED_THUNER) {
//        		region = lScreen.gChainThunder;
//        		
//        		if (screen.gridTable.contains(screen.itemPoint.x, screen.itemPoint.y)) {
//        			x = CordXOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//        			y = CordYOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//	        		screen.batch.draw(region, x - screen.DIAMOND_WIDTH / 2 , y - screen.DIAMOND_HEIGHT / 2, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//	    			
//        		} else {
//        			x = screen.itemPoint.x;
//        			y = screen.itemPoint.y;
//        			screen.batch.draw(region, x - screen.DIAMOND_WIDTH / 2 , y - screen.DIAMOND_HEIGHT / 2, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//        		}
//        	}
//        	
//        	if (lScreen.gUsingItem == MissionDiamond.NINE_EXPLODE) {
//        		region = lScreen.gExplode;
//        		
//        		if (screen.gridTable.contains(screen.itemPoint.x, screen.itemPoint.y)) {
//        			x = CordXOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//        			y = CordYOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//	        		screen.batch.draw(region, x - 3 * screen.DIAMOND_WIDTH / 2 , y - 3 * screen.DIAMOND_HEIGHT / 2, 3 * screen.DIAMOND_WIDTH, 3 * screen.DIAMOND_HEIGHT);
//	    			
//        		} else {
//        			x = screen.itemPoint.x;
//        			y = screen.itemPoint.y;
//        			screen.batch.draw(region, x - 3 * screen.DIAMOND_WIDTH / 2 , y - 3 * screen.DIAMOND_HEIGHT / 2, 3 * screen.DIAMOND_WIDTH, 3 * screen.DIAMOND_HEIGHT);
//        		}
//        	}
        }
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
	
	public boolean canTouchCell(int cell) {
		int row = CellRow(cell);
		int col = CellCol(cell);
		return canTouchCell(row, col);
	}
	
	public boolean canTouchCell(int row, int col) {
		int type = screen.logic.diamondType(screen.grid[row][col]);
		return (screen.grid[row][col] != -1) && (certainCell(screen.inGridFlag[row][col])) && 
				(type != IDiamond.SOIL_DIAMOND) && (type != IDiamond.ROCK_DIAMOND);
	}
	
	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
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
	
	public boolean certainCell(int value) {
		return (Operator.getBit(Effect.FIXED_POS, value) > 0);// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
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
//		case MissionDiamond.ADD_TIME:
//			if (pScreen.gItemLimits[pPosition] == 0) {
//				pScreen.gInform = MissionDiamond.NOT_USE_TIME;
//				return;
//			}
//			pScreen.timeRemain = Math.min(pScreen.timeRemain + 20, pScreen.timeLevel);
//			pScreen.gItemLimits[pPosition]--;
//			PlayerInfo.coin -= pScreen.gItemCosts[pItem];
//			break;
//		case MissionDiamond.DOUBBLE_EXP:
//			if (pScreen.timeRemain > 20) {
//				pScreen.gInform = MissionDiamond.NOT_USE_X2;
//				return;
//			}
//			PlayerInfo.coin -= pScreen.gItemCosts[pItem];
//			pScreen.gMulScore = true;
//			break;
//		case MissionDiamond.SIXTEEN_CELL_EXPLODE:
//			PlayerInfo.coin -= pScreen.gItemCosts[pItem];
//			for (int i = 0 ; i < 16; i++) {
//				int cell = (4 * (i + 1) - 1) - random.nextInt(4);
//				int row = cell / 8;
//				int col = cell % 8;
////				Gdx.app.log("test", "Rest tao cell explode tai "+row+" "+col);
//				screen.logic.newEffect(row, col, Effect.CELL_EXPLODE, null);
//			}
//			if (screen.logic.state == GameLogic.ON_END) screen.logic.state = GameLogic.ON_RUNNING;
//			branch = GameScreen.DIAMOND_ANIMATION;
//			setState(Phase.ON_END);
//			break;
//		case MissionDiamond.CHAINED_THUNER:
//			screen.gTouchMode = GameScreen.TOUCH_ITEM;
//			screen.itemPoint.set(screen.curPoint);
//			pScreen.gUsingItem = pScreen.gCurItems[pPosition];
//			break;
//		case MissionDiamond.RC_THUNER:
//			screen.gTouchMode = GameScreen.TOUCH_ITEM;
//			screen.itemPoint.set(screen.curPoint);
//			pScreen.gUsingItem = pScreen.gCurItems[pPosition];
//			break;
//		case MissionDiamond.NINE_EXPLODE:
//			screen.gTouchMode = GameScreen.TOUCH_ITEM;
//			screen.itemPoint.set(screen.curPoint);
//			pScreen.gUsingItem = pScreen.gCurItems[pPosition];
//			break;
//		}
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		sCell = -1;
		dCell = -1;
		
	}
	
	private void beginMove() {
//		VsDiamond game = (VsDiamond) screen;
//		game.messagesInFrame++;
//		short shortArr[]  = null;
//		byte byteArr[] = null;
//		MyEsObject message = game.myEsObjectPool.obtain();
////		message.setString(ActionNames.ACTION, MyEsObject.BEGIN_MOVE);
//		message.setString(FieldNames.ACTION, FieldValues.SEND_MESSAGE);
//		message.setString(FieldNames.TYPE, FieldValues.BEGIN_MOVE);
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
