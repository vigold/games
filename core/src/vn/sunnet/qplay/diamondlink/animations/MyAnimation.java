package vn.sunnet.qplay.diamondlink.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class MyAnimation extends Animation {

	
	
	private PlayMode myMode = PlayMode.NORMAL;
	
	public float frameDuration;
	public float animationDuration;
	
	private int frameCounts = 0;
	
	public MyAnimation (float frameDuration, TextureRegion... keyFrames) {
		super(frameDuration, keyFrames);
	
		frameCounts = keyFrames.length;
		this.frameDuration = frameDuration;
		this.animationDuration = frameCounts * frameDuration;
	}
	
	public MyAnimation (float frameDuration, Array<? extends TextureRegion> keyFrames) {
		super(frameDuration, keyFrames);
		frameCounts = keyFrames.size;
		this.frameDuration = frameDuration;
		this.animationDuration = frameCounts * frameDuration;
	}

	public MyAnimation (float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playType) {
		super(frameDuration, keyFrames, playType);
		
		frameCounts = keyFrames.size;
		this.frameDuration = frameDuration;
		this.animationDuration = frameCounts * frameDuration;
	}
	
	@Override
	public void setPlayMode(PlayMode playMode) {
		// TODO Auto-generated method stub
		myMode = playMode;
		super.setPlayMode(playMode);
	}
	
	@Override
	public TextureRegion getKeyFrame(float stateTime) {
		// TODO Auto-generated method stub
		if (myMode == PlayMode.REVERSED) return super.getKeyFrame(1);
		return super.getKeyFrame(stateTime);
	}
	
	public PlayMode getPlayMode() {
		return myMode;
	}
	
	public int getFrameNum() {
		return frameCounts;
	}
	
	public float getAnimationDuration() {
		return animationDuration;
	}
	
	public float getFrameDuration() {
		return frameDuration;
	}
}
