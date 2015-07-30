package vn.sunnet.qplay.diamondlink.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.LoadingDialog;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libutils.constant.EsFields;


import vn.sunnet.game.electro.rooms.ElectroRoomInfo;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.loaders.Slogan;

import vn.sunnet.qplay.diamondlink.screens.groups.BuyItemTable;
import vn.sunnet.qplay.diamondlink.screens.groups.InfoTable;
import vn.sunnet.qplay.diamondlink.screens.groups.Shop;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.MyImage;

import vn.sunnet.qplay.diamondlink.utils.Fields;

public class FrontGame extends NodeScreen implements InputProcessor {
	
	private AssetManager manager;
	private Assets assets;
	private TweenManager tween;
	
	private DiamondLink Instance;
	
	private InputMultiplexer multiplexer;
	
	private Stage stage;
	
	private InfoTable info;
	
	private Shop shop;
	
	
	private Button toShop;
	private Button freeCoin;
	
	GameScreen toScreen;
	private Sound press;
	private BuyItemTable buy;
	
	private Music music;

	public FrontGame(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
		Instance = (DiamondLink) game;
		manager = Instance.getAssetManager();
		assets = Instance.getAssets();
		tween = Instance.getMoveSystem();
		multiplexer = new InputMultiplexer(this);
		stage = new Stage(new StretchViewport(width, height));
		multiplexer.addProcessor(stage);
	}
	
	/********************************Network********************************************/
	
	@Override
	public void createDialog(String title, String context,
			ButtonDescription positive, ButtonDescription negative,
			ButtonDescription neutral, final InputProcessor toInputProcessor) {
		// TODO Auto-generated method stub
		if (Gdx.app.getType() == ApplicationType.Android) {
			Command command = new Command() {

				@Override
				public void execute(Object data) {
					// TODO Auto-generated method stub

					if (toInputProcessor == null) {
						Gdx.input.setInputProcessor(preProcessor);
						System.out.println("quay tro ve man hinh chinh");
					} else {

						System.out.println("quay tro ve man hinh khac");
					}
				}
			};
			Instance.iFunctions.createDialog(title, context, positive, negative,
				neutral, command);
		} else
		super.createDialog(title, context, positive, negative, neutral,
				toInputProcessor);
	}
	
	@Override
	public void createToast(String slogan, float duration) {
		// TODO Auto-generated method stub
		if (Gdx.app.getType() == ApplicationType.Android) {
			Instance.iFunctions.createToast(slogan, duration);
		} else
		super.createToast(slogan, duration);
	}
	
	/*******************************************UIMethods********************************/
	
	private void initTextures() {
		
	}
	
	private void initAudio() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
		music = manager.get(MusicAssets.MENU_MUSIC, Music.class);
		music.setLooping(true);
	}
	
	private void initInterfaces() {
		Texture texture = manager.get(UIAssets.LOGIN_LAYER_0, Texture.class);
		stage.addActor(new Image(texture));
		texture = manager.get(UIAssets.LOGIN_LAYER_1, Texture.class);
		final Image im = new Image(texture);
		im.addAction(Actions.repeat(-1, Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				im.setBounds(240 - im.getPrefWidth() / 2, -im.getPrefHeight(), im.getPrefWidth(), im.getPrefHeight());
				return true;
			}
		}, Actions.moveTo(0, 800, 15, Interpolation.linear))));
		stage.addActor(im);
		
		TextureAtlas atlas = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		
		buy = new BuyItemTable(Instance, this, new Runnable() {
			
			@Override
			public void run() {
				Instance.setScreen(Instance.getLogin());
			}
		});
		stage.addActor(buy);
		
		atlas = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		TextureRegionDrawable imageUp = new TextureRegionDrawable(atlas.findRegion("Play", 0));
		TextureRegionDrawable imageDown = new TextureRegionDrawable(atlas.findRegion("Play", 1));
		Button button = new ImageButton(imageUp, imageDown);
		button.setBounds(480 / 2 - button.getPrefWidth() / 2, 12, button.getPrefWidth(), button.getPrefHeight());
		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				Assets.playSound(press);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				
				Instance.setScreen(toScreen);
				Skill skills[] = buy.getSelectedSkills();
				if (skills != null)
				for (int i = 0; i < skills.length; i++) {
					if (skills[i] != null) {
						Instance.iFunctions.track("In Items Shop", "Purchase "+skills[i].getName(), skills[i].getName()+" "+skills[i].get(), 1);
					}
				}
				toScreen.sendSkills(buy.getSelectedSkills());
				PlayerInfo.coin -= buy.getPayment();
				Instance.iFunctions.putInt("coins", (int)PlayerInfo.coin);	
				
			}
		});
		
		stage.addActor(button);
		
//		button = new ImageButton(new TextureRegionDrawable(atlas.findRegion(
//				"Back", 0)), new TextureRegionDrawable(atlas.findRegion("Back",
//				1)));
//
//		button.setBounds(47, 129 - 75, button.getPrefWidth(), button.getPrefHeight());
//		button.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				if (DiamondLink.SOUND == 1)
//					Assets.playSound(press);
//				return true;
//			}
//
//			@Override
//			public void touchUp(InputEvent event, float x, float y,
//					int pointer, int button) {
//				Instance.setScreen(Instance.getLogin());
//			}
//		});
//
//		stage.addActor(button);
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Assets.pauseMusic(music);
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Assets.playMusic(music);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Assets.pauseMusic(music);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		if (firstTime) {
			firstTime = false;
			initAudio();
			initInterfaces();
			initTextures();
		} else {
			buy.reset();
		}
		Assets.playMusic(music);
		
//		createToast("Kết nối "+esAdapter.isConnected(), 2f);
		
//		bag = Instance.getMenu().getBag();
//		bag.hide();
//		bag.setFocusScreen(this);
//		shop = Instance.getMenu().getShop();
//		shop.hide();
//		shop.setFocusScreen(this);
		
		Gdx.input.setInputProcessor(multiplexer);
		setProcessorAfterDismissingLoading(multiplexer);
		
//		Instance.iFunctions.track("ItemsShop", "Click", ""+Instance.getMoment(), 1);
	}
	
	public void setToScreen(NodeScreen screen) {
		toScreen = (GameScreen) screen;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		tween.update(delta);
		super.render(delta);
	}

	@Override
	public boolean keyDown(int keyCode) {
		// TODO Auto-generated method stub
		if (keyCode == Keys.BACK || keyCode == Keys.ESCAPE) {
			Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Instance.setScreen(Instance.getLogin());
				}
			});
		}
		return true;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void playMusic() {
		Assets.playMusic(music);
	}

	@Override
	public void pauseMusic() {
		Assets.pauseMusic(music);
	}

	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return multiplexer;
	}

}
;