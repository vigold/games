package vn.sunnet.qplay.diamondlink.classicdiamond;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.modules.FindSolution;
import vn.sunnet.qplay.diamondlink.phases.DiamondChange;
import vn.sunnet.qplay.diamondlink.phases.DiamondRest;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



public class FirstClassic extends DiamondRest {
	
	DiamondChange phase; 
	private boolean firstTime = true;

	public FirstClassic(ClassicDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
		firstTime = true;
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		super.onBegin();
		Gdx.app.log("test", "trang thai cua solution "+screen.solution.getStatus());
//		debug();
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
