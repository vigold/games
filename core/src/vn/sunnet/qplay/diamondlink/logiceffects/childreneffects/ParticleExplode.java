package vn.sunnet.qplay.diamondlink.logiceffects.childreneffects;

import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParticleExplode {
	public float time = 0;
	MyAnimation animation;
	ParticleEffect effect;
	public float x , y;
	public boolean isNew = true;
	public ParticleExplode(MyAnimation animation, ParticleEffect effect, float x, float y) {
		this.animation = animation;
		this.animation.setPlayMode(Animation.PlayMode.NORMAL);
		this.effect = effect;
		this.effect.reset();
		effect.setPosition(x, y);
		this.x = x;
		this.y = y;
	}
	
	public void update(float delta) {
		time += delta;
	}
	
	public void draw(SpriteBatch batch, float delta) {
		TextureRegion region = animation.getKeyFrame(time);
		if (!effect.isComplete())
			if (isNew)
				effect.draw(batch, delta);
			else {
				effect.draw(batch, time);
				isNew = true;
			}
		if (!animation.isAnimationFinished(time))
			batch.draw(region, x - 2 * Diamond.DIAMOND_WIDTH / 2, y
					- 2 * Diamond.DIAMOND_HEIGHT / 2,
					2 * Diamond.DIAMOND_WIDTH,
					2 * Diamond.DIAMOND_HEIGHT);
	}
	
	public boolean isComplete() {
		return effect.isComplete() && animation.isAnimationFinished(time);
	}
	
	public boolean isAnimationComplete() {
		return animation.isAnimationFinished(time);
	}
	
	public float getTime() {
		return time;
	}
}
