package vn.sunnet.game.electro.libgdx.screens;

import vn.sunnet.game.electro.libgdx.scene2d.ui.SpriteSheet;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.items.Skill;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class LoadingDialog extends Table {
	public static final float TIME_OUT = 30f;
	
	
	public static final int NO_TIME_OUT = 0;
	public static final int NEED_TIME_OUT = 1;
	private int type = NO_TIME_OUT;
	
	private float timeOut = 0;
	
	private SpriteSheet loadingSymbol = null;
	private Label loadingSlogan = null;
	private BitmapFont font = null;
	private Runnable hanleWhenTimeOut = null;
	
	
	public LoadingDialog(Array<AtlasRegion> keyFrames, LabelStyle style) {
		// TODO Auto-generated constructor stub
		super();
		loadingSymbol = new SpriteSheet(0.05f, keyFrames, SpriteSheet.LOOP);
		loadingSlogan = new Label("", style);
		this.font = style.font;
		this.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.timeOut = TIME_OUT;
	}
	
	public LoadingDialog(Array<AtlasRegion> keyFrames, Skin skin) {
		// TODO Auto-generated constructor stub
		this(keyFrames, skin.get(LabelStyle.class));
	}
	
	public void show(String slogan, Stage stage, Runnable handleWhenTimeOut, int type, float timeOut) {
		hide();
		clear();
		
		this.type = type;
		this.hanleWhenTimeOut = handleWhenTimeOut;
		
		TextBounds bounds = font.getBounds(slogan);
		loadingSlogan.setText(slogan);
		float width = stage.getWidth();
		float height = Math.max(bounds.height, loadingSymbol.getPrefHeight());
		loadingSlogan.setBounds(stage.getWidth() / 2 - bounds.width / 2, 0,
				bounds.width, bounds.height);
		loadingSymbol.setBounds(
				stage.getWidth() / 2 - loadingSymbol.getPrefWidth() / 2, 0,
				loadingSymbol.getPrefWidth(), loadingSymbol.getPrefHeight());
		addActor(loadingSymbol);
		addActor(loadingSlogan);
		setBounds(0, stage.getHeight() / 2 - height / 2, stage.getWidth(), height);
		
		if (timeOut <= 0)
			this.timeOut = TIME_OUT;
		else
			this.timeOut = timeOut;
		
		stage.addActor(this);
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		if (type == NEED_TIME_OUT) {
			timeOut = Math.max(0, timeOut - delta);
			if (timeOut == 0) {
				if (hanleWhenTimeOut != null)
					hanleWhenTimeOut.run();
				hide();
			}
		}
	}
	
	public void hide() {
		remove();
	}
	
}
