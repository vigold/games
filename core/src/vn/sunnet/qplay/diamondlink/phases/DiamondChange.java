package vn.sunnet.qplay.diamondlink.phases;

import com.badlogic.gdx.Gdx;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.GameObjectAccessor;

public class DiamondChange extends Phase {
	
	private GameScreen screen;
	private int[][] desGrid = new int[8][8];
	
	public DiamondChange(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
	}

	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		Gdx.app.log("test", "beginChange");
		screen.addInputProcessor(this);
		screen.showNoMoreMove(null);
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (screen.logic.diamondType(screen.grid[i][j]) == IDiamond.NORMAL_DIAMOND) {

					Diamond diamond = (Diamond) screen.diamonds.get(i * 8 + j);
					diamond.setOrigin(Diamond.DIAMOND_WIDTH / 2,
							Diamond.DIAMOND_HEIGHT / 2);
					screen.moveSystem.killTarget(diamond);
					final int cell = i * 8 + j;
					Timeline.createSequence()
							.push(Tween
									.to(diamond, GameObjectAccessor.SCALE_XY,
											1f).target(0.1f, 0.1f)
									.ease(Linear.INOUT))
							.push(Tween.call(new TweenCallback() {

								@Override
								public void onEvent(int type,
										BaseTween<?> source) {
									// TODO Auto-generated method stub
									Diamond diamond = (Diamond) screen.diamonds
											.get(cell);
									int row = cell / 8;
									int col = cell % 8;
									diamond.setAction(Diamond.REST);
									diamond.setDiamondValue(desGrid[row][col]);

									screen.grid[row][col] = desGrid[row][col];

								}
							}))
							.push(Tween
									.to(diamond, GameObjectAccessor.SCALE_XY,
											1f).target(1f, 1f)
									.ease(Linear.INOUT))
							.push(Tween.call(new TweenCallback() {

								@Override
								public void onEvent(int type,
										BaseTween<?> source) {
									// TODO Auto-generated method stub
									setState(ON_END);
									Diamond diamond = (Diamond) screen.diamonds
											.get(cell);
									screen.moveSystem.killTarget(diamond);
									
									int row = cell / 8;
									int col = cell % 8;
									Effect effect = screen.logic.allocateEffect(Effect.TEMP_EFFECT);
									effect.setSource(row * 8 + col);
									screen.logic.effects.add(effect);
								}
							})).start(screen.moveSystem);
				}
	}

	@Override
	public void onRunning() {
		screen.timeRemain += deltaTime;
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		Gdx.app.log("test", "Chuyen tu Change sang Animation");
		screen.logic.state = GameLogic.ON_RUNNING;
		
		branch = GameScreen.DIAMOND_ANIMATION;
		screen.removeInputProcessor(this);
		screen.gamePhase[branch].setState(Phase.ON_BEGIN);
		screen.stateGame = branch;
		screen.gamePhase[branch].update(0);
		screen.solution.resetTime();
	}
	
	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		for (int i = 0; i < screen.diamonds.size(); i++) {
			IDiamond diamond = screen.diamonds.get(i);
			int row = i / 8;
			int col = i % 8;
			diamond.draw(deltaTime, screen.batch);
		}
	}
	
	public void setDesGrid(int[][] grid) {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				desGrid[i][j] = grid[i][j];
			}
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
