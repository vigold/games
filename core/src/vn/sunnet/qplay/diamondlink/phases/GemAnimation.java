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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;




public class GemAnimation extends Phase {
	
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
	
	
	public GemAnimation(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		Instance = screen.gGame;
		this.gAssets = Instance.getAssets();
		
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
		if (!(swapFlag && screen.logic.SpecialEffect == 0)) return false;
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
 			if (canTouchCell(touchCell) && swapFlag && screen.logic.SpecialEffect <= 0) {
     			if (screen.selection == -1) { // khi truoc do la khong an
     				screen.selection = touchCell; 
     				diamond1 = screen.diamonds.get(screen.selection);
     				diamond1.setAction(Diamond.FRAME_CHANGE);
//     				screen.touchDownX = screen.curPoint.x; screen.touchDownY = screen.curPoint.y;
     				screen.lastPoint.set(screen.curPoint);
     			} else 
     			if (screen.selection != touchCell) {// an phim
     				if (neighbourCell(screen.selection, touchCell) && canTouchCell(screen.selection)) { // la phim lan can
//     					Gdx.app.log("GemAnimation", "touch down swap from "+screen.selection+" to "+touchCell);
     					swapFlag = false;
     					// swap two neighbour cell
     					sCell = screen.selection; dCell = touchCell;
//     					Log.d("Game", "appear change in Animation");
     					if (isChain(sCell, dCell) > -1) beginChain(sCell, dCell);
     					else {
     						beginSwap(sCell, dCell);
//     						Log.d("Game", "SwapMove beginSwap in Animation"+sCell+" "+dCell);
     					}
     					//beginSwap(screen.selection, touchCell);
     					
     					screen.selection = -1;
     					//setState(Phase.ON_END);		
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
     				if (canTouchCell(screen.selection))
 					diamond1.setAction(Diamond.REST);
     				screen.selection = -1;
     			} // an trung phim selection
 			} else {
 			
 				if (screen.selection != -1) {
					diamond1 = screen.diamonds.get(screen.selection);
					diamond1.setAction(Diamond.REST);
		 		}
 				screen.selection = -1;// tro vao o khong tro duoc
 			}
 		} 
		else {
//			MissionDiamond pScreen = (MissionDiamond) screen;
//			for (int i = 0 ; i < 4 ; i++) {
// 				int position = DiamondGameInfo.myPosition * 4 + i;
// 				float x = pScreen.gItemsX[position];
// 				float y = pScreen.gItemsY[position];
// 				float width = pScreen.gItemsW[position];
// 				float height = pScreen.gItemsH[position];
// 				if (x < screen.curPoint.x && screen.curPoint.x < x + width && y < screen.curPoint.y && screen.curPoint.y < y + height) {
//					if (PlayerInfo.coin < pScreen.gItemCosts[pScreen.gCurItems[i]])
//						((ISunnetLib) pScreen.gGame.iFunctions)
//								.createToast("Không đủ xu để dùng vật phẩm này");
//					else
//						useItem(pScreen.gCurItems[i], i);
// 					break;
// 				}
// 			}
		
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
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		
//		if (screen.playMode != screen.GAME) return false;
		if (pointer > 0 || screen.gTouchMode == GameScreen.TOUCH_UI) return false;
		if (!(swapFlag && screen.logic.SpecialEffect == 0)) return false;
		int touchCell = 0;
		screen.curPoint.set(screenX, screenY, 0);
		screen.gCamera.unproject(screen.curPoint);
		if (screen.gTouchMode == GameScreen.TOUCH_ITEM) {
			screen.itemPoint.set(screen.curPoint);
//			PlayerBoxScreen pBox = screen.gGame.getPlayerBoxScreen();
// 			MissionDiamond pScreen = (MissionDiamond) screen;
// 			
//    		if (screen.gridTable.contains(screen.itemPoint.x , screen.itemPoint.y)) {
//    			touchCell = touchCell(screen.itemPoint);
//    			int row = touchCell / 8;
//    			int col = touchCell % 8;
//    			
//    			if (pScreen.gUsingItem == MissionDiamond.RC_THUNER) {
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
//    			
//    			if (screen.logic.state == GameLogic.ON_END) screen.logic.state = GameLogic.ON_RUNNING;
//    			screen.gTouchMode = GameScreen.TOUCH_GAME;
//    			pScreen.gUsingItem = -1;
////    			pBox.loadData(pScreen.gGameData);
////				pBox.useItem(12);
//				
//    		} else screen.gTouchMode= GameScreen.TOUCH_GAME;
    	}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
	
//		if (screen.playMode != screen.GAME) return false;
		if (pointer > 0 || screen.gTouchMode == GameScreen.TOUCH_UI) return false;
		if (!(swapFlag && screen.logic.SpecialEffect == 0)) return false;
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
	        			if (canTouchCell(touchCell) && canTouchCell(screen.selection) && swapFlag && screen.logic.SpecialEffect <= 0) {
//	        				Gdx.app.log("GemAnimation", "touch drage swap from "+screen.selection+" to "+touchCell);
	        				swapFlag = false;
	        				sCell = screen.selection; dCell = touchCell;
	        				if (isChain(sCell, dCell) > -1) beginChain(sCell, dCell);
	     					else {
	     						beginSwap(sCell, dCell);
//	     						Log.d("Game", "SwapMove beginSwap in Animation");
	     					}
	        				
	     					screen.selection = -1;
	     					//setState(Phase.ON_END);
	        			} else {
	        				if (screen.selection != -1 ) {
		        				IDiamond diamond1 = screen.diamonds.get(screen.selection);
		        				if (canTouchCell(screen.selection))
		     					diamond1.setAction(Diamond.REST);
	        				}
		     				screen.selection = -1;
	        			}
        		} 
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
		branch = -1;
		sCell = -1; dCell = -1;		
//		Gdx.app.log("GemAnimation", "begin"+swapFlag);
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
		if (swapFlag) {
			screen.removeInputProcessor(this);
			screen.gamePhase[branch].setState(Phase.ON_BEGIN);
			screen.stateGame = branch;
			screen.gamePhase[branch].update(0);
		} else {
			setState(Phase.ON_RUNNING);
		}
//		Gdx.app.log("GemAnimation", "To Rest "+swapFlag+" "+branch);
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
			
		}
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
		
		boolean needSendScore = false;
		
		for (int i = 0; i < screen.logic.effects.size(); i++) {
			Effect effect = (Effect) screen.logic.effects.get(i);
			effect.update(deltaTime);
			if (screen.GAME_ID == GameScreen.PALAESTRA) {
				if (effect.isFinished() && effect.getType() != Effect.TEMP_EFFECT) {
//					if (effect.getOnlineStatus() == Effect.NEED_TO_SEND) {
//						effect.setOnlineStatus(Effect.SENDED);
//						endEffect(effect);
//						needSendScore = true;
//					}
				}
			}
		}
		
		if (needSendScore) sendScore();
		
//		if (screen.GAME_ID == GameScreen.PALAESTRA) {
//			if (createdEffect.size() > 0) sendEffect(null);
//		}
	}
	
	public void beginSwap(int sou, int des) {
//		Gdx.app.log("DiamondAnimation", "move "+sou+"  to "+des);
		swapFlag = false;
		sCell = sou; dCell =des;
		IDiamond diamond1 = screen.diamonds.get(sou);
		IDiamond diamond2 = screen.diamonds.get(des);
//		Log.d("Game", "beginSwap in Animation");
		diamond1.setSource(diamond1.getPosX(), diamond1.getPosY());
		diamond1.setDestination(diamond2.getPosX(), diamond2.getPosY());
		diamond1.setAction(IDiamond.FRAME_CHANGE | IDiamond.TWO_ASPECT_SWAP);
//		Gdx.app.log("DiamondAnimation", diamond1.getSourX()+" "+diamond1.getSourY()+" "+diamond1.getDesX()+" "+diamond1.getDesY());
		int row1 = CellRow(sou); int col1 = CellCol(sou);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.TWO_ASPECT_SWAP, screen.inGridFlag[row1][col1]);
		
		diamond2.setSource(diamond2.getPosX(), diamond2.getPosY());
		diamond2.setDestination(diamond1.getPosX(), diamond1.getPosY());
		diamond2.setAction(IDiamond.TWO_ASPECT_SWAP);
//		Gdx.app.log("DiamondAnimation", diamond2.getSourX()+" "+diamond2.getSourY()+" "+diamond2.getDesX()+" "+diamond2.getDesY());
		row1 = CellRow(des); col1 = CellCol(des);
		screen.inGridFlag[row1][col1] = 0;
		screen.inGridFlag[row1][col1] = Operator.onBit(Effect.TWO_ASPECT_SWAP, screen.inGridFlag[row1][col1]);
//		Gdx.app.log("DiamondAnimation", " "+swapFlag);
		swapCellValue(sou, des);
		if (screen.GAME_ID == GameScreen.PALAESTRA)
		beginMove();
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
//		Gdx.app.log("DiamondAnimation", "kc nguon "+x1+" "+y1+" to "+x2+" "+y2+" cur = "+x+" "+y+" flag = "+screen.inGridFlag[sCell / 8][sCell % 8]);
		x1 = diamond2.getSourX();
		y1 = diamond2.getSourY();
		x2 = diamond2.getDesX();
		y2 = diamond2.getDesY();
		x = diamond2.getPosX();
		y = diamond2.getPosY();
//		Gdx.app.log("DiamondAnimation", "kc dich "+x1+" "+y1+" to "+x2+" "+y2+" cur = "+x+" "+y+" flag = "+screen.inGridFlag[dCell / 8][dCell % 8]);
		boolean isDestination = diamond1.isDestination() && diamond2.isDestination();
		if (isDestination) {
//			Gdx.app.log("test", " finish Move");
			goAspect(); 
			return true;
		}
		return false;
	}
	
	public boolean goAspect() {// tra ve true neu co quay lai false neu phat sinh hieu ung
		IDiamond diamond1 = screen.diamonds.get(sCell);
		IDiamond diamond2 = screen.diamonds.get(dCell);
		IEffect effect = null;
		float xx = 0 , yy = 0;
//		Log.d("Game", "SwapMove go Aspect in Animation");
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
			xx = CordXOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond1.setSource(xx, yy);
			diamond1.setDestination(xx, yy);
//			Gdx.app.log("TestAnimation", "o 1 "+sCell+" "+xx+" "+yy);
			xx = CordXOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond2.setSource(xx, yy);
			diamond2.setDestination(xx, yy);
			
//			MyAnimation animation = gAssets.getDiamondAnimation(screen.grid[CellRow(sCell)][CellCol(sCell)], screen.getGameID());
			diamond1.setDiamondValue(screen.grid[CellRow(sCell)][CellCol(sCell)]);
//			diamond1.setSprite(animation);
//			Gdx.app.log("TestAnimation", "o 1 "+sCell+" "+xx+" "+yy);
//			animation = gAssets.getDiamondAnimation(screen.grid[CellRow(dCell)][CellCol(dCell)], screen.getGameID());
			diamond2.setDiamondValue(screen.grid[CellRow(dCell)][CellCol(dCell)]);
//			diamond2.setSprite(animation);
//			Gdx.app.log("TestAnimation", "o 2 "+dCell+" "+xx+" "+yy);
			swapFlag = true;
			return true;
		} else {//phat sinh hieu ung
			xx = CordXOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(sCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond1.setSource(xx, yy);
			diamond1.setDestination(xx, yy);
//			Gdx.app.log("TestAnimation", "o 1 "+sCell+" "+xx+" "+yy);
			xx = CordXOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			yy = CordYOfCell(dCell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			diamond2.setSource(xx, yy);
			diamond2.setDestination(xx, yy);
//			Gdx.app.log("TestAnimation", "o 2 "+dCell+" "+xx+" "+yy);
//			MyAnimation animation = gAssets.getDiamondAnimation(screen.grid[CellRow(sCell)][CellCol(sCell)], screen.getGameID());
			diamond1.setDiamondValue(screen.grid[CellRow(sCell)][CellCol(sCell)]);
//			diamond1.setSprite(animation);
			
//			animation = gAssets.getDiamondAnimation(screen.grid[CellRow(dCell)][CellCol(dCell)], screen.getGameID());
			diamond2.setDiamondValue(screen.grid[CellRow(dCell)][CellCol(dCell)]);
//			diamond2.setSprite(animation);
//			backFlag = false;
			
			swapFlag = true;
			
		}
		return false;
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
	
/********************************Draw Methods**********************************/
	
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
		int type = screen.logic.diamondType(screen.grid[row][col]);
		return (screen.grid[row][col] != -1) && (certainCell(screen.inGridFlag[row][col])) && 
				(type != IDiamond.SOIL_DIAMOND) && (type != IDiamond.ROCK_DIAMOND);
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
		sCell = -1 ; dCell = -1;
		backFlag = false;
		swapFlag = true;
	}
	
	private void beginMove() {
//		VsDiamond game = (VsDiamond) screen;
//		game.messagesInFrame++;
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
//		game.messagesInFrame++;
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
//		
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
