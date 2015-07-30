package vn.sunnet.qplay.diamondlink.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MySprite {
	
	private float frameDuration = 1/60f;
	
	private TextureRegion[] keyFrames;
	
	private int playType = 0;
	
	public MySprite (float frameDuration, TextureRegion... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}
	
	public MySprite (float frameDuration, Array<? extends TextureRegion> keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = new TextureRegion[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.keyFrames[i] = keyFrames.get(i);
		}
	}

	public MySprite (float frameDuration, Array<? extends TextureRegion> keyFrames, int playType) {
		this.frameDuration = frameDuration;
		this.keyFrames = new TextureRegion[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.keyFrames[i] = keyFrames.get(i);
		}
		this.playType = playType;
	}
	
	public TextureRegion getKeyFrame(int frameID) {
		if (frameID > keyFrames.length - 1 || frameID < 0) 
			throw new GdxRuntimeException("outOfSpriteBounds "+frameID);
		return keyFrames[frameID];
	}
	
	public float getDuration() {
		return frameDuration;
	}
	
}
