package vn.sunnet.qplay.diamondlink.minerdiamond;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sun.xml.internal.bind.v2.model.core.ID;

import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



public class DiamondOfMiner extends Diamond implements IDiamond{
	
	public MyAnimation soilUp = null;
	public MyAnimation soilDown = null;
	public MyAnimation soilLeft = null;
	public MyAnimation soilRight = null;
	public int value = 0;
	
	public DiamondOfMiner(float x, float y, float width, float height, GameScreen screen) { // x y la toa do tam
		super(x, y, width, height, screen);
	}
	
	@Override
	public void recycleDiamond(float x, float y, float width, float height,
			GameScreen screen) {
		// TODO Auto-generated method stub
		super.recycleDiamond(x, y, width, height, screen);
		value = 0;
	}
	
	public void setAction(int Action) {
		float vX = 0, vY = 0;
		float speedX = 0, speedY = 0;
		
		super.setAction(Action);
		
		if (isAction == REST) return;
		if (containsAction(FLY)) {
			aniamationTime = 0;
			speedY = 20f/0.5f;		
			if (position.x < destination.x) vX = speedX;
			else if (position.x > destination.x) vX = -speedX;
			
			if (position.y < destination.y) vY = speedY;
			else if (position.y > destination.y) vY = -speedY;
			velocity.set(vX, vY);
		}
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (isAction == REST) return;
		if (containsAction(FLY)){
			fly(deltaTime);
		}
	}
	
	private void fly(float deltaTime) {
		float xx = position.x;
		float yy = position.y;
		
		if (velocity.y > 0) 
			yy = Math.min(destination.y, yy + deltaTime * velocity.y);
		else if (velocity.y < 0) 
			yy = Math.max(destination.y, yy + deltaTime * velocity.y);
		setCenter(xx, yy); 
		
		if (isDestination()) setFinish(FLY);
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	@Override
	public void setDiamondValue(int value) {
		// TODO Auto-generated method stub
		super.setDiamondValue(value);
		int type = getDiamondType();
		if (isGemOrMarkOrSoil()) {
			soilLeft = screen.assets.getEffectAnimation("SoilDiamondLeft", 0.05f);
			soilRight = screen.assets.getEffectAnimation("SoilDiamondRight", 0.05f);
			soilUp = screen.assets.getEffectAnimation("SoilDiamondUp", 0.05f);
		}
	}
	
	@Override
	public void draw(float deltaTime, SpriteBatch pBatch) {
		// TODO Auto-generated method stub
		super.draw(deltaTime, pBatch);
		int type = getDiamondType();
		if (isGemOrMarkOrSoil()) {
			TextureRegion region = null;
			switch (getAction()) {
			case FALL:
				region = soilUp.getKeyFrame(aniamationTime);
				screen.batch.draw(region, getPosX() - DIAMOND_WIDTH / 2,
						getPosY() + DIAMOND_HEIGHT / 2, DIAMOND_WIDTH,
						region.getRegionHeight());
				break;
			case FLY:
				
				break;
			default:
				break;
			}
		}
	}

}
