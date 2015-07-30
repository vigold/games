package vn.sunnet.qplay.diamondlink.butterflydiamond;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import vn.sunnet.lib.bmspriter.BMSAnimationBlueprint;
import vn.sunnet.lib.bmspriter.BMSRenderObjectProducer;
import vn.sunnet.lib.bmspriter.BMSSCMLFile;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxAnimationInstance;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxROP_TextureAtlas;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.actions.BezierAction;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.math.BezierConfig;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;




public class DiamondOfButterfly extends Diamond {
	
	public BezierAction bezierAction = null;
	
	public IDiamond preButterfly = null;
	
	public IDiamond nextButterfly = null;
	
	private BMSLibgdxAnimationInstance swings; 
	
	private int upStep = 0;
	
	public DiamondOfButterfly(float x, float y, float width, float height,
			GameScreen screen) {
		super(x, y, width, height, screen);
	}

	@Override
	public void recycleDiamond(float x, float y, float width, float height,
			GameScreen screen) {
		// TODO Auto-generated method stub
		super.recycleDiamond(x, y, width, height, screen);
		bezierAction = null;
		preButterfly = null;
		nextButterfly = null;
		upStep = 0;
	}
	
	@Override
	public void setDiamondValue(int value) {
		// TODO Auto-generated method stub
		super.setDiamondValue(value);
		if (isType == Diamond.BUTTERFLY_DIAMOND) {
			TextureAtlas atlas = screen.gGame.getAssetManager().get(Assets.ATLAS,
					TextureAtlas.class);
			BMSSCMLFile file = screen.gGame.getAssetManager().get(Assets.BUT_BMS, BMSSCMLFile.class);
			BMSRenderObjectProducer producer = new BMSLibgdxROP_TextureAtlas(atlas);
			BMSAnimationBlueprint print = file.getEntity(0).getAnimation("NewAnimation_000");
			swings = new BMSLibgdxAnimationInstance(print, producer);
			swings.setLooping(true);
		}
		
	}
	
	public void setAction(int Action) {
		float vX = 0, vY = 0;
		float speedX = 0, speedY = 0;
		super.setAction(Action);
		
		if (containsAction(FLY)) {
			aniamationTime = 0;
			speedX = speedY = 15f / 0.05f;
			animation.setPlayMode(Animation.PlayMode.LOOP);
			if (bezierAction != null) bezierAction = null;
			BezierConfig config = null;
			if (source.x > 240)
			config = new BezierConfig(new Vector2(source), new Vector2(destination), new Vector2(120, 800) , new Vector2(120, 800 - 60 - 360));
			else
			config = new BezierConfig(new Vector2(source), new Vector2(destination), new Vector2(360, 800) , new Vector2(360, 800 - 60 - 360));
			bezierAction = new BezierAction(2, config);
			config = null;
			
		}
		
		if (containsAction(UP_TO_GRID)) {
			aniamationTime = 0;
			velocity.x = 0; velocity.y = 10f / 0.05f;
		}
	}
	
	public void update(float deltaTime) {
		//Log.d("frame", "deltaTime"+position.x+" "+position.y);
		super.update(deltaTime);
		if (isType == BUTTERFLY_DIAMOND)
			swings.animate((int)(deltaTime * 1000 * 0.2f));
		if (containsAction(FLY)) {
//			Log.d("Diamond", "update Fly");
			fly(deltaTime);
		}
		if (containsAction(UP_TO_GRID)) {
			up_to_grid(deltaTime);
		}
	}
	
	@Override
	public void behindDiamond(float delta, SpriteBatch pBatch) {
		// TODO Auto-generated method stub
		super.behindDiamond(delta, pBatch);
		if (isType == BUTTERFLY_DIAMOND) {
			screen.bmsRenderer.render(swings, getPosX(), getPosY() + 20);
		}
	}
	
	public void up_to_grid(float deltaTime) {
		float xx = position.x;
		float yy = position.y;
		//Log.d("frame", "swap deltaTime"+position.x+" "+position.y+" "+velocity.x+" "+velocity.y);
		if (velocity.x > 0) xx = Math.min(destination.x, xx + deltaTime * velocity.x);
		else if (velocity.x < 0) xx = Math.max(destination.x, xx + deltaTime * velocity.x);
		if (velocity.y > 0) yy = Math.min(destination.y, yy + deltaTime * velocity.y);
		else if (velocity.y < 0) yy = Math.max(destination.y, yy + deltaTime * velocity.y);
		setCenter(xx, yy); 
		if (isDestination()) setFinish(UP_TO_GRID);
	}
	
	public void setUpStep(int upStep) {
		this.upStep = Math.max(upStep, 0);
	}
	
	public int getUpStep() {
		return upStep;
	}
	
	public void fly(float deltaTime) {
		bezierAction.update(deltaTime, position);
		setCenter(position.x, position.y);
//		Log.d("Diamond", "Fly"+source.x+" "+source.y+" "+position.x+" "+position.y+" "+destination.x+" "+destination.y);
		if (isDestination()) setFinish(FLY);
	}

}
