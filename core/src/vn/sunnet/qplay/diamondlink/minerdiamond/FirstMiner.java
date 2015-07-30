package vn.sunnet.qplay.diamondlink.minerdiamond;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.graphiceffects.Glaze;
import vn.sunnet.qplay.diamondlink.modules.FindSolution;
import vn.sunnet.qplay.diamondlink.phases.DiamondRest;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;




public class FirstMiner extends DiamondRest {
	
	private MinerDiamond instance;
	
	public FirstMiner(MinerDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
		instance = screen;
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		super.onBegin();
		if (screen.isOverTime()) {
			instance.canOver = true;
		}
	}
	
	@Override
	public void onRunning() {
		// TODO Auto-generated method stub
		super.onRunning();
		if (screen.isOverTime()) {
			instance.canOver = true;
		}
		if (getState() == ON_RUNNING) {
			if (screen.solution.update(deltaTime) == FindSolution.NEW_GRID) {
//				setState(Phase.ON_END);
//				branch = GameScreen.DIAMOND_CHANGE;
				debug();
			}
		}
	}
	
	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		super.onEnd();
		if (screen.isOverTime()) {
			instance.canOver = true;
		}
		
		if (branch != GameScreen.DIAMOND_CHANGE) {
			screen.solution.resetTime();
		}
	}
	
	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		drawOthers(deltaTime);
		drawSoilOrMarkOrGem(deltaTime);

		if (screen.selection > -1)
			drawAnimation(
					screen.gSelection,
					CordXOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT), screen.DIAMOND_WIDTH,
					screen.DIAMOND_HEIGHT);
        
		
		if (screen.curSkill != null) {
			screen.curSkill.draw(screen.batch, deltaTime);
		}
	}
	
	public void drawOthers(float delta) {
//		System.out.println("drawOthers in FirstMiner");
		screen.batch.setColor(1f, 1f, 1f, 1f);
		for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	if (!isSoilOrMarkOrGem(diamond.getDiamondValue()))
        		diamond.behindDiamond(deltaTime, screen.batch);
//			if (i == screen.diamonds.size() - 1)
//				System.out.println((screen.diamonds.size() - 1) + " value = "
//						+ diamond.getDiamondValue() + " "
//						+ isSoilOrMarkOrGem(diamond.getDiamondValue()) + " "
//						+ diamond.getPosX() + " " + diamond.getPosY());
        }
        
        screen.batch.end();
        /**/
        screen.batch.setShader(screen.glaze.shader);
        screen.batch.begin();
        screen.glaze.update(deltaTime);
		for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	if (!isSoilOrMarkOrGem(diamond.getDiamondValue()))
        		diamond.intoDiamond(deltaTime, screen.batch);
        }
        screen.batch.end();
        
        /**/
        screen.batch.setShader(null);
        screen.batch.begin();
        
        for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	if (!isSoilOrMarkOrGem(diamond.getDiamondValue()))
        		diamond.inFrontOfDiamond(deltaTime, screen.batch);
        }
		
	}
	
	public void drawSoilOrMarkOrGem(float delta) {
		 for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--) {
			 Diamond diamond = (Diamond) screen.diamonds.get(i);
	        	int row = i / 8;
	        	int col = i % 8;
	        	if (isSoilOrMarkOrGem(diamond.getDiamondValue()))
	        		diamond.draw(deltaTime, screen.batch);
		 }
		 drawExposureSoilDiamond();
	}
	
	private void drawExposureSoilDiamond(){		
		Diamond diamond;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (certainCell(screen.inGridFlag[i][j]))
					if (isSoilOrMarkOrGem(screen.grid[i][j])) {
						diamond = (Diamond) screen.diamonds.get(i * 8 + j);
						if (i + 1 < 8)
							if (!isSoilOrMarkOrGem(screen.grid[i + 1][j]) || !certainCell(screen.inGridFlag[i + 1][j])) {

								screen.batch.draw(((MinerDiamond)screen).soilUp,
										diamond.getX(), diamond.getY()
												+ screen.DIAMOND_HEIGHT,
										screen.DIAMOND_WIDTH, 15);

							}

						if (j - 1 >= 0)
							if (!isSoilOrMarkOrGem(screen.grid[i][j - 1]) || !certainCell(screen.inGridFlag[i][j - 1])) {
								screen.batch.draw(((MinerDiamond)screen).soilLeft,
										diamond.getX() - 16,
										diamond.getY() - 5, 16, 69);
							}

						if (j + 1 < 8)
							if (!isSoilOrMarkOrGem(screen.grid[i][j + 1]) || !certainCell(screen.inGridFlag[i][j + 1])) {
								screen.batch.draw(((MinerDiamond)screen).soilRight,
										diamond.getX() + screen.DIAMOND_WIDTH,
										diamond.getY() - 5, 12, 69);
							}
					}

			}
	}
	
	private boolean isSoilOrMarkOrGem(int value) {
		if (value == -1) return false;
		int dType = screen.logic.diamondType(value);
		return dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM || dType == Diamond.MARK_DIAMOND
				|| dType == Diamond.SOIL_DIAMOND;
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
