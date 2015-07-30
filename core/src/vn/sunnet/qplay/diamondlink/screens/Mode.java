package vn.sunnet.qplay.diamondlink.screens;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.MyImage;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Mode extends NodeScreen implements InputProcessor {
	
	private DiamondLink Instance;
	private InputMultiplexer multiplexer;
	private Stage stage;
	private TweenManager moveSystem;
	private Button buttons[] = new Button[4];
	private Image modes[] = new Image[GameScreen.MISSION_DIAMOND + 1];
	
	private Music music;
	private Sound press;
	private float eclapsedTime = 0;
	private int slectType = -1;
	private Label life;
	
	private int turnCost = 0;
	private int gemCost = 0;
	private int coinCost = 0;
	
	public Mode(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
		multiplexer = new InputMultiplexer();
		stage = new Stage(new StretchViewport(width, height));
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(this);
		Instance = (DiamondLink) game;
		this.moveSystem = Instance.getMoveSystem();
	}
	
	/*********************************InitGame***********************************/
	
	private void initInterface() {
		Tween.registerAccessor(Actor.class, new ActorAccessor());
//		Texture texture = Instance.getAssetManager().get(UIAssets.MENU_BG, Texture.class);
//		Image im = new Image(texture);
//		stage.addActor(im);
		
		TextureAtlas atlas = Instance.getAssetManager().get(UIAssets.MODE_FG, TextureAtlas.class);
		modes[GameScreen.CLASSIC_DIAMOND] = new MyImage(atlas.findRegion("ClassicMode"));
		modes[GameScreen.CLASSIC_DIAMOND].addListener(new ClickListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer > 0) return false;
				enterGame(GameScreen.CLASSIC_DIAMOND);
				return true;
			}
			
		});
		
		modes[GameScreen.BUTTERFLY_DIAMOND] = new MyImage(atlas.findRegion("ButterflyMode"));
		modes[GameScreen.BUTTERFLY_DIAMOND].addListener(new ClickListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer > 0) return false;
				enterGame(GameScreen.BUTTERFLY_DIAMOND);
				return true;
			}
		});
	
		
		modes[GameScreen.MINE_DIAMOND] = new MyImage(atlas.findRegion("MinerMode"));
		modes[GameScreen.MINE_DIAMOND].addListener(new ClickListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer > 0) return false;
				enterGame(GameScreen.MINE_DIAMOND);
				return true;
			}
			
		});
				
		modes[GameScreen.MISSION_DIAMOND] = new MyImage(atlas.findRegion("MissionMode"));
		modes[GameScreen.MISSION_DIAMOND].addListener(new ClickListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer > 0) return false;
				createToast("Hiện đang phát triển", 2f);
				return true;
			}
			
		});
		
		
		
		for (int i = 0; i < modes.length; i++) {
			stage.addActor(modes[i]);
			modes[i].setBounds(0, 0, 120, 120);
			modes[i].setOrigin(modes[i].getWidth() / 2, modes[i].getHeight() / 2);
		}
		
		if (Instance.paymentVer == DiamondLink.OTHER_VER) {
			Image image = new Image(new TextureRegion(Instance.getAssetManager()
					.get(UIAssets.LIFE, Texture.class)));
			stage.addActor(image);
			BitmapFont font = Instance.getAssetManager().get(UIAssets.MENU_NAME_FONT, BitmapFont.class);
			life = new Label("", new LabelStyle(font, font.getColor()));
			life.setBounds(image.getPrefWidth(), 0, 480, 40);
			life.setWrap(true);
			life.setAlignment(Align.left|Align.center);
			stage.addActor(life);
		}
	}
	
	private void beginInterfaces() {
		eclapsedTime = 0;
		slectType = -1;
		turnCost = 0;
		gemCost = 0;
		coinCost = 0;
		for (int i = 0; i < modes.length; i++) {
			modes[i].setScale(1);
			modes[i].getColor().a = 1;
			moveSystem.killTarget(modes[i]);
			modes[i].setColor(1f, 1f, 1f, 1f);
		}
	}
	
	private void initAudio() {
		music = Instance.getAssetManager().get(MusicAssets.MODE_MUSIC, Music.class);
		press = Instance.getAssetManager().get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	private void initTextures() {
		
	}
	
	/***************************Libgdx Lifrecycle Methods************************/
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		if (firstTime) {
			firstTime = false;
			initTextures();
			initAudio();
			initInterface();
		} else {
			
		}
		
		
//		createToast("Kết nối "+esAdapter.isConnected(), 2f);
		
		if (Instance.paymentVer == DiamondLink.OTHER_VER) {
			life.setText(""+PlayerInfo.playTurns);
		}
		
		Gdx.input.setInputProcessor(multiplexer);
		setProcessorAfterDismissingLoading(multiplexer);
		Assets.playMusic(music);
		beginInterfaces();
	}
	
	
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Assets.playMusic(music);
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Assets.pauseMusic(music);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Assets.pauseMusic(music);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		updateUI(delta);
		super.render(delta);
	}
	
	private void enterGame(int gameID) {
		slectType = gameID;
		createAnimations(slectType);
	}
	
	

	/************************************Input***************************************/
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
	
	/**/
	private void createAnimations(final int mode) {
		Timeline.createSequence()
				.push(Tween.to(modes[mode], ActorAccessor.CPOS_XY, 1f)
						.target(DiamondLink.WIDTH / 2, DiamondLink.HEIGHT / 2)
						.ease(Linear.INOUT))
				.push(Timeline
						.createParallel()
						.push(Tween.to(modes[mode], ActorAccessor.SCALE_XY, 2f)
								.target(10f, 10f).ease(Quad.INOUT))
						.push(Tween.to(modes[mode], ActorAccessor.OPACITY, 2f)
								.target(0).ease(Linear.INOUT))).setCallback(new TweenCallback() {
									
									@Override
									public void onEvent(int type, BaseTween<?> source) {
										// TODO Auto-generated method stub
										toScreen(mode);
									}
								}).start(moveSystem);
		for (int i = 0; i < modes.length; i++) {
			if (i != mode) {
				Tween.to(modes[i], ActorAccessor.OPACITY, 1f).target(0)
						.ease(Linear.INOUT).start(moveSystem);
			}
		}
	}
	
	private void toScreen(final int type) {
		switch (type) {
		case GameScreen.CLASSIC_DIAMOND:
		case GameScreen.BUTTERFLY_DIAMOND:
		case GameScreen.MINE_DIAMOND:
			Instance.getGame(type).setGameOver();
//			Instance.getGame(type).setElectroRoomInfo(
//					roomInfo);
			//Instance.getFrontGame().setToScreen(Instance.getGame(type));

//			Instance.getSelectAvatar().setElectroRoomInfo(
//					Instance.getMenu().getRoomInfo());
			Instance.getFrontGame().setToScreen(Instance.getGame(type));
//			Instance.getFrontGame().setElectroRoomInfo(getRoomInfo());

			Instance.setScreen(Instance.getFrontGame());
			
//			Instance.setScreen(Instance.getSelectAvatar());
			break;
		case GameScreen.MISSION_DIAMOND:
			
			break;
		}
	}
	
	private void updateUI(float delta) {
		if (slectType != -1) {
			moveSystem.update(delta);
			return;
		}
		float a = 160;
		float b = 160;
		eclapsedTime += 1 * delta;
		float phi = 0;
		float xc = DiamondLink.WIDTH / 2;
		float yc = DiamondLink.HEIGHT / 2;
		float preY = 0;
		// blue
		double t = eclapsedTime;
		float x = (float) (xc + a * Math.cos(t) * Math.cos(phi) - b
				* Math.sin(t) * Math.sin(phi));
		float y = (float) (yc + a * Math.cos(t) * Math.sin(phi) + b
				* Math.sin(t) * Math.cos(phi));
		float sclace = (float) (0.25f * Math.sin(-t) + 0.75);
		
		modes[0].setX(x - modes[0].getWidth() / 2); modes[0].setY(y - modes[0].getHeight() / 2);
		
		
		t = eclapsedTime - 2 * Math.PI / 3;
		x = (float) (xc + a * Math.cos(t) * Math.cos(phi) - b
				* Math.sin(t) * Math.sin(phi));
		y = (float) (yc + a * Math.cos(t) * Math.sin(phi) + b
				* Math.sin(t) * Math.cos(phi));
		sclace = (float) (0.25f * Math.sin(-t) + 0.75);
		
		modes[1].setX(x - modes[1].getWidth() / 2); modes[1].setY(y - modes[1].getHeight() / 2);
		
		
		t = eclapsedTime + 2 * Math.PI / 3;
		x = (float) (xc + a * Math.cos(t) * Math.cos(phi) - b
				* Math.sin(t) * Math.sin(phi));
		y = (float) (yc + a * Math.cos(t) * Math.sin(phi) + b
				* Math.sin(t) * Math.cos(phi));
		sclace = (float) (0.25f * Math.sin(-t) + 0.75);
		
		modes[2].setX(x - modes[2].getWidth() / 2); modes[2].setY(y - modes[2].getHeight() / 2);
		
		y = (float) (yc + 5 * Math.sin(2 * Math.PI * eclapsedTime / 1.5f));
		
//		sclace = (float) (1f + 0.5f * Math.sin(2 * Math.PI * eclapsedTime / 1.5f));
//		modes[3].setScale(sclace);
		modes[3].setX(xc - modes[3].getWidth() / 2); modes[3].setY(y - modes[3].getHeight() / 2);
	}

	/*****************************Network*************************************/
	
	private void showSelectDialog(final String title, final String slogan, final int modes, final int gem, final int coin) {
		ButtonDescription positive = new ButtonDescription("Bằng vàng", new Command() {
			
			@Override
			public void execute(Object data) {
				// TODO Auto-generated method stub
				
				
			}
		});
		
		ButtonDescription negative = new ButtonDescription("Không", null);
		
		ButtonDescription neutral = new ButtonDescription("Bằng ngọc", new Command() {
			
			@Override
			public void execute(Object data) {
				// TODO Auto-generated method stub
				
			}
		});
		createDialog(title, slogan, positive, negative, neutral, null);
	}
	
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

	@Override
	public void playMusic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseMusic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return multiplexer;
	}
	
	public int getTurnCost() {
		return turnCost;
	}
	

}
