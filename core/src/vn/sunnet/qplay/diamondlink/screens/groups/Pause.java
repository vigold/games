package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Pause extends Group {
	DiamondLink game;
	GameScreen screen;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	Sound press;
	
	private Image musicBut;
	private Image soundBut;
	private Image vibartorBut;
	private Image notificationBut;
	
	private EventListener continueListener;
	private EventListener exitListener;
	private EventListener soundListener;
	private EventListener musicListener;
	private EventListener vibatorListener; 
	private EventListener notificationListener;
	
	public Pause(DiamondLink game, GameScreen screen,
			EventListener continueListener, EventListener exitListener,
			EventListener soundListener, EventListener musicListener,
			EventListener vibatorListener, EventListener notificationListener) {
		// TODO Auto-generated constructor stub
		this.game = game;
		this.screen = screen;

		manager = game.getAssetManager();
		assets = game.getAssets();
		moveSystem = game.getMoveSystem();

		this.continueListener = continueListener;
		this.exitListener = exitListener;
		this.soundListener = soundListener;
		this.musicListener = musicListener;
		this.vibatorListener = vibatorListener; 
		this.notificationListener = notificationListener;

		initPrivateAssets();
		initContent();
	}

	private void initContent() {
		// TODO Auto-generated method stub
		TextureAtlas atlas2 = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		Image im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
		im.setBounds(0, 0, 480, 800);
		addActor(im);
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		im = new Image(atlas.findRegion("PauseFrame"));
		im.setBounds(12, 291, im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
//		im = new Image(atlas.findRegion("SettingFrame"));
//		im.setBounds(14, 183, 450, 441);
//		addActor(im);
//		
//		im = new Image(atlas.findRegion("MusicText"));
//		im.setBounds(248 + 15, 444, 163, 37);
//		
//		addActor(im);
//		
//		im = new Image(atlas.findRegion("SoundText"));
//		im.setBounds(60 + 15, 444, 163, 37);
//		addActor(im);
//
//		musicBut = new Image(atlas.findRegion("Music", (1 - DiamondLink.MUSIC) * 2));
//		musicBut.setBounds(60, 430, 74, 74);
//		musicBut.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				if (DiamondLink.SOUND == 1)
//					press.play(1f);
//				return true;
//			}
//			
//			@Override
//			public void touchUp(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				DiamondLink.MUSIC = 1 - DiamondLink.MUSIC;
//				musicBut.setDrawable(new TextureRegionDrawable(atlas
//						.findRegion("Music", (1 - DiamondLink.MUSIC) * 2)));
//				
//				if (DiamondLink.MUSIC == 0) {
//					screen.pauseMusic();
//				} else {
//					screen.playMusic();
//				}
//			}
//		});
//		
//		addActor(musicBut);
//		
//		soundBut = new Image(atlas.findRegion("Sound", (1 - DiamondLink.SOUND) * 2));
//		soundBut.setBounds(248, 430, 74, 74);
//		addActor(soundBut);
//		soundBut.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				if (DiamondLink.SOUND == 1)
//					press.play(1f);
//				return true;
//			}
//			
//			@Override
//			public void touchUp(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				DiamondLink.SOUND = 1 - DiamondLink.SOUND;
//				soundBut.setDrawable(new TextureRegionDrawable(atlas
//						.findRegion("Sound", (1 - DiamondLink.SOUND) * 2)));
//			}
//		});
//		
//		
//		
//		im = new Image(atlas.findRegion("VibratorText"));
//		im.setBounds(60 + 15, 365, 163, 37);
//		addActor(im);
//		
//		vibartorBut = new Image(atlas.findRegion("Vibrator", (1 - DiamondLink.ADS) * 2));
//		vibartorBut.setBounds(60, 350, 74, 74);
//		addActor(vibartorBut);
//		vibartorBut.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				Assets.playSound(press);
//				return true;
//			}
//			
//			@Override
//			public void touchUp(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				DiamondLink.ADS = 1 - DiamondLink.ADS;
//				vibartorBut.setDrawable(new TextureRegionDrawable(atlas
//						.findRegion("Vibrator", (1 - DiamondLink.ADS) * 2)));
//				
//			}
//		});
//		
//		
//		im = new Image(atlas.findRegion("NotificationText"));
//		im.setBounds(248 + 15, 365, 163, 37);
//		addActor(im);
//		
//		notificationBut = new Image(atlas.findRegion("Notification", (1 - DiamondLink.NOTIFICATION) * 2));
//		notificationBut.setBounds(248, 350, 74, 74);
//		addActor(notificationBut);
//		notificationBut.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				Assets.playSound(press);
//				return true;
//			}
//			
//			@Override
//			public void touchUp(InputEvent event, float x, float y,
//					int pointer, int button) {
//				// TODO Auto-generated method stub
//				DiamondLink.NOTIFICATION = 1 - DiamondLink.NOTIFICATION;
//				notificationBut.setDrawable(new TextureRegionDrawable(atlas
//						.findRegion("Notification", (1 - DiamondLink.NOTIFICATION) * 2)));
//			}
//		});
		
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Ok", 0)), new TextureRegionDrawable(
				atlas.findRegion("Ok", 1)));
		button.addListener(exitListener);
		button.setBounds(150, 196, button.getPrefWidth(), button.getPrefHeight());
		addActor(button);
		
		button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Cancel", 0)), new TextureRegionDrawable(
				atlas.findRegion("Cancel", 1)));
		button.addListener(continueListener);
		
		button.setBounds(245, 196, button.getPrefWidth(), button.getPrefHeight());
		addActor(button);
	}

	public void updateSettings() {
//		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
//		musicBut.setDrawable(new TextureRegionDrawable(atlas.findRegion(
//				"Music", (1 - DiamondLink.MUSIC) * 2)));
//		soundBut.setDrawable(new TextureRegionDrawable(atlas.findRegion(
//				"Sound", (1 - DiamondLink.SOUND) * 2)));
//		vibartorBut.setDrawable(new TextureRegionDrawable(atlas.findRegion(
//				"Vibrator", (1 - DiamondLink.ADS) * 2)));
//		notificationBut.setDrawable(new TextureRegionDrawable(
//				(atlas.findRegion("Notification",
//						(1 - DiamondLink.NOTIFICATION) * 2))));
	}
	
	private void initPrivateAssets() {
		// TODO Auto-generated method stub
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	public void show(Group father) {
		father.addActor(this);
		updateSettings();
	}
	
	public void hide() {
		remove();
	}
}
