package vn.sunnet.qplay.diamondlink.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class SpriteSheet extends Widget {
	
	public static final int NORMAL = 0;
	public static final int REVERSED = 1;
	public static final int LOOP = 2;
	public static final int LOOP_REVERSED = 3;
	public static final int LOOP_PINGPONG = 4;
	public static final int LOOP_RANDOM = 5;
	
	private Scaling scaling;
	private int align = Align.center;
	private float imageX, imageY, imageWidth, imageHeight;
	private Drawable drawable[];
	
	float eclapsedTime = 0;
	private float frameDuration;
	private float animationDuration;
	private TextureRegion[] keyFrames;
	private int playMode;
	
	
	public SpriteSheet (float frameDuration, Array<? extends TextureRegion> keyFrames, int playType) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.size * frameDuration;
		this.keyFrames = new TextureRegion[keyFrames.size];
		this.drawable = new TextureRegionDrawable[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.keyFrames[i] = keyFrames.get(i);
			this.drawable[i] = new TextureRegionDrawable(this.keyFrames[i]);
		}
		this.playMode = playType;
		
		this.scaling = Scaling.stretch;
		this.align = Align.center;
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());
	}
	
	public SpriteSheet (float frameDuration, Array<? extends TextureRegion> keyFrames) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.size * frameDuration;
		this.keyFrames = new TextureRegion[keyFrames.size];
		this.drawable = new TextureRegionDrawable[keyFrames.size];
		for (int i = 0, n = keyFrames.size; i < n; i++) {
			this.keyFrames[i] = keyFrames.get(i);
			this.drawable[i] = new TextureRegionDrawable(this.keyFrames[i]);
		}
		this.playMode = NORMAL;
		Gdx.app.log("test", "so luon frame"+keyFrames.size);
		this.scaling = Scaling.stretch;
		this.align = Align.center;
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());
	}
	
	public SpriteSheet (float frameDuration, TextureRegion... keyFrames) {
		this.frameDuration = frameDuration;
		this.animationDuration = keyFrames.length * frameDuration;
		this.keyFrames = keyFrames;
		this.drawable = new TextureRegionDrawable[keyFrames.length];
		for (int i = 0, n = keyFrames.length; i < n; i++) {
			this.drawable[i] = new TextureRegionDrawable(this.keyFrames[i]);
		}
		this.playMode = NORMAL;
		
		this.scaling = Scaling.stretch;
		this.align = Align.center;
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());
	}
	
	public float getPrefWidth () {
		if (drawable != null) {
			float max = -1;//getKeyFrame(eclapsedTime).getMinHeight();

			for (int i = 0; i < drawable.length; i++) {
				if (max < drawable[i].getMinWidth()) {
					max = drawable[i].getMinWidth();
				}
			}

			return max;
		}
		return 0;
	}

	public float getPrefHeight () {
		if (drawable != null){
			float max = -1;//getKeyFrame(eclapsedTime).getMinHeight();

			for (int i = 0; i < drawable.length; i++) {
				if (max < drawable[i].getMinHeight()) {
					max = drawable[i].getMinHeight();
				}
			}

			return max;
		}
		return 0;
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		eclapsedTime += delta;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX();
		float y = getY();
		float scaleX = getScaleX();
		float scaleY = getScaleY();
		Drawable lDrawable = getKeyFrame(eclapsedTime);
		
		if (lDrawable != null) {
			if (lDrawable.getClass() == TextureRegionDrawable.class) {
				TextureRegion region = ((TextureRegionDrawable)lDrawable).getRegion();
				float rotation = getRotation();
				if (scaleX == 1 && scaleY == 1 && rotation == 0)
					batch.draw(region, x + imageX, y + imageY, imageWidth, imageHeight);
				else {
					batch.draw(region, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY, imageWidth, imageHeight,
						scaleX, scaleY, rotation);
				}
			} else
				lDrawable.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		}
	}
	
	public void layout () {
		if (drawable == null) return;
		Drawable lDrawable = getKeyFrame(eclapsedTime);
		float regionWidth = lDrawable.getMinWidth();
		float regionHeight = lDrawable.getMinHeight();
		float width = getWidth();
		float height = getHeight();

		Vector2 size = scaling.apply(regionWidth, regionHeight, width, height);
		imageWidth = size.x;
		imageHeight = size.y;

		if ((align & Align.left) != 0)
			imageX = 0;
		else if ((align & Align.right) != 0)
			imageX = (int)(width - imageWidth);
		else
			imageX = (int)(width / 2 - imageWidth / 2);

		if ((align & Align.top) != 0)
			imageY = (int)(height - imageHeight);
		else if ((align & Align.bottom) != 0)
			imageY = 0;
		else
			imageY = (int)(height / 2 - imageHeight / 2);
	}
	
	public Drawable getKeyFrame (float stateTime) {
		int frameNumber = getKeyFrameIndex (stateTime);
		return drawable[frameNumber];
	}
	
	/** Returns the current frame number.
	 * @param stateTime
	 * @return current frame number */
	public int getKeyFrameIndex (float stateTime) {
		int frameNumber = (int)(stateTime / frameDuration);

		if(keyFrames.length == 1)
         return 0;
		
		switch (playMode) {
		case NORMAL:
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			break;
		case LOOP:
			frameNumber = frameNumber % keyFrames.length;
			break;
		case LOOP_PINGPONG:
			frameNumber = frameNumber % ((keyFrames.length * 2) - 2);
         if (frameNumber >= keyFrames.length)
            frameNumber = keyFrames.length - 2 - (frameNumber - keyFrames.length);
         break;
		case LOOP_RANDOM:
			frameNumber = MathUtils.random(keyFrames.length - 1);
			break;
		case REVERSED:
			frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
			break;
		case LOOP_REVERSED:
			frameNumber = frameNumber % keyFrames.length;
			frameNumber = keyFrames.length - frameNumber - 1;
			break;

		default:
			// play normal otherwise
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			break;
		}
		
		return frameNumber;
	}

	/** Sets the animation play mode.
	 * 
	 * @param playMode can be one of the following: Animation.NORMAL, Animation.REVERSED, Animation.LOOP, Animation.LOOP_REVERSED,
	 *           Animation.LOOP_PINGPONG, Animation.LOOP_RANDOM */
	public void setPlayMode (int playMode) {
		this.playMode = playMode;
	}

	/** Whether the animation would be finished if played without looping (PlayMode Animation#NORMAL), given the state time.
	 * @param stateTime
	 * @return whether the animation is finished. */
	public boolean isAnimationFinished (float stateTime) {
		int frameNumber = (int)(stateTime / frameDuration);
		return drawable.length - 1 < frameNumber;
	}
	
	public int getMaxFrame() {
		return keyFrames.length;
	}
	
	public void setFrameDuration(float duration) {
		this.frameDuration = duration;
		this.animationDuration = getMaxFrame() * duration;
	}
}
