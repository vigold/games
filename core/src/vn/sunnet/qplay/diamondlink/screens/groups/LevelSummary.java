package vn.sunnet.qplay.diamondlink.screens.groups;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;

public class LevelSummary extends Group {
	
	DiamondLink game;
	GameScreen screen;
	
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;

	private Image congratulation;
	private Image levelupText;
	private Label level;
	private SpriteSheet butterfly;
	
	Sound press;
	
	private EventListener okListener;
	private EventListener shareListener;
	
	public LevelSummary(DiamondLink game, GameScreen screen, EventListener shareListener,
			EventListener okListener) {
		// TODO Auto-generated constructor stub
		this.game = game;
		this.screen = screen;
		this.manager = game.getAssetManager();
		this.assets = game.getAssets();
		this.moveSystem = game.getMoveSystem();
		this.screen = screen;
		this.shareListener = shareListener;
		this.okListener = okListener;
		initPrivateAssets();
		initContent();
	}

	private void initContent() {
		// TODO Auto-generated method stub
		TextureAtlas atlas = manager.get(UIAssets.SUMMARY_FG, TextureAtlas.class);

		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		Image im = new Image(manager.get(UIAssets.GAME_BG, Texture.class));
		if (screen.GAME_ID == GameScreen.MINE_DIAMOND) 
			im = new Image(manager.get(UIAssets.MINER_BG, Texture.class));
		addActor(im);
		
		im = new Image(atlas.findRegion("LevelupFrame"));
		im.setBounds(30, 200, 426, 366);
		addActor(im);
		
		im = new Image(atlas.findRegion("Light"));
		im.setBounds(DiamondLink.WIDTH / 2 - 627/ 2,
				DiamondLink.HEIGHT / 2 - 629 / 2,
				627, 629);
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		Tween.to(im, ActorAccessor.ROTATION, 4).targetRelative(360).ease(Linear.INOUT)
		.repeat(Tween.INFINITY, 0).start(moveSystem);
		addActor(im);
		
		congratulation = new Image(atlas.findRegion("Congratulation"));
		congratulation.setBounds(DiamondLink.WIDTH / 2 - 287 / 2, 622,
				287, 88);
		congratulation.setOrigin(congratulation.getWidth() / 2, congratulation.getHeight() / 2);
		addActor(congratulation);
		
		levelupText = new Image(atlas.findRegion("UpLevelText"));
		levelupText.setBounds(DiamondLink.WIDTH / 2 - 444 / 2, 511,
				444, 167);
		levelupText.setOrigin(levelupText.getWidth() / 2, levelupText.getHeight() / 2);
		addActor(levelupText);
		
		im = new Image(atlas.findRegion("LevelFrame"));
		im.setBounds(48, 231, 387, 160);
		addActor(im);
		
		atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		im = new Image(atlas.findRegion("Symbol"));
		im.setBounds(104, 423, 270, 101);
		addActor(im);
		
		atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		
		im = new Image(atlas.findRegion("Star"));
		im.setBounds(359, 315, 25, 25);
		addActor(im);
		
		im = new Image(atlas.findRegion("Gem"));
		im.setBounds(359, 273, 25, 25);
		addActor(im);
		
		BitmapFont font = manager.get(UIAssets.EXP_FONT, BitmapFont.class);
		LabelStyle style = new LabelStyle(font, font.getColor());
		level = new Label("", style);
		level.setBounds(359 - 60, 315, 60, 60);
		level.setWrap(true);
		level.setAlignment(Align.center|Align.center);
		addActor(level);
		
		Label label = new Label("1", style);
		label.setBounds(359 - 60, 273, 60, 60);
		label.setWrap(true);
		label.setAlignment(Align.center|Align.center);
		addActor(label);
		
		atlas = manager.get(UIAssets.SUMMARY_FG, TextureAtlas.class);
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Share", 0)), new TextureRegionDrawable(
				atlas.findRegion("Share", 1)));
		button.addListener(shareListener);	
		button.setBounds(240, 47, 201, 58);
		addActor(button);
		
		
		button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Ok", 0)), new TextureRegionDrawable(
				atlas.findRegion("Ok", 1)));
		button.addListener(okListener);
		button.setBounds(42, 47, 170, 58);
		addActor(button);
		
		SpriteSheet sheet = new SpriteSheet(0.1f, atlas.findRegions("Up"), SpriteSheet.LOOP);
		sheet.setBounds(62, 282, 46, 66);
		addActor(sheet);
		
		atlas = manager.get(AnimationAssets.CHARACTERS, TextureAtlas.class);
		butterfly = new SpriteSheet(0.05f, atlas.findRegions("BlueButterfly"), SpriteSheet.LOOP);
		butterfly.setOrigin(butterfly.getPrefWidth() / 2, butterfly.getPrefHeight() / 2);
		addActor(butterfly);
	}

	private void initPrivateAssets() {
		// TODO Auto-generated method stub
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	public void updateContent(float exp, float addExp) {
		level.setText(""+assets.getLevelOfExp(exp + addExp));
	}
	
	public void show(Group father) {
		father.addActor(this);
		moveSystem.killTarget(levelupText);
		moveSystem.killTarget(congratulation);
		moveSystem.killTarget(butterfly);
		
		congratulation.setScale(1f);
		levelupText.setScale(1f);
		congratulation.getColor().a = 1;
		levelupText.getColor().a = 1;
		Timeline.createParallel()
				.push(Tween.from(levelupText, ActorAccessor.SCALE_XY, 0.5f)
						.target(10f, 10f).ease(Bounce.INOUT))
				.push(Tween.from(levelupText, ActorAccessor.OPACITY, 0.5f)
						.target(0).ease(Bounce.INOUT)).start(moveSystem);
		Timeline.createParallel()
		.push(Tween.from(congratulation, ActorAccessor.SCALE_XY, 1f)
				.target(10f, 10f).ease(Bounce.INOUT))
		.push(Tween.from(congratulation, ActorAccessor.OPACITY, 1f)
				.target(0).ease(Bounce.INOUT)).start(moveSystem);
		butterfly.setRotation(15);
		butterfly.setScale(1.5f);
		butterfly.setX(314 - butterfly.getWidth() / 2); butterfly.setY(131 - butterfly.getHeight() / 2);
		Tween.to(butterfly, ActorAccessor.CPOS_XY, 3).target(426, 180)
				.ease(Linear.INOUT).waypoint(314, 180).waypoint(426, 131)
				.path(TweenPaths.catmullRom).start(moveSystem);
	}
	
	public void hide() {
		remove();
	}
	
}
