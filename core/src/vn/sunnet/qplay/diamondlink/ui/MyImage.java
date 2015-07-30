package vn.sunnet.qplay.diamondlink.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class MyImage extends Image {
	/** Creates an image with no region or patch, stretched, and aligned center. */
	public MyImage () {
		this((Drawable)null);
	}

	/** Creates an image stretched, and aligned center.
	 * @param patch May be null. */
	public MyImage (NinePatch patch) {
		this(new NinePatchDrawable(patch), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center.
	 * @param region May be null. */
	public MyImage (TextureRegion region) {
		this(new TextureRegionDrawable(region), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center. */
	public MyImage (Texture texture) {
		this(new TextureRegionDrawable(new TextureRegion(texture)));
	}

	/** Creates an image stretched, and aligned center. */
	public MyImage (Skin skin, String drawableName) {
		this(skin.getDrawable(drawableName), Scaling.stretch, Align.center);
	}

	/** Creates an image stretched, and aligned center.
	 * @param drawable May be null. */
	public MyImage (Drawable drawable) {
		this(drawable, Scaling.stretch, Align.center);
	}

	/** Creates an image aligned center.
	 * @param drawable May be null. */
	public MyImage (Drawable drawable, Scaling scaling) {
		this(drawable, scaling, Align.center);
	}

	/** @param drawable May be null. */
	public MyImage (Drawable drawable, Scaling scaling, int align) {
		setDrawable(drawable);
		setScaling(scaling);
		setAlign(align);
		setWidth(getPrefWidth());
		setHeight(getPrefHeight());
	}
	
	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub
		super.setWidth(width);
		layout();
	}
	
	@Override
	public void setHeight(float height) {
		// TODO Auto-generated method stub
		super.setHeight(height);
		layout();
	}
	
	@Override
	public void setBounds(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
		layout();
	}
}
