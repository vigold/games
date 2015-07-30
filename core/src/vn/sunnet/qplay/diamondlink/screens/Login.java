package vn.sunnet.qplay.diamondlink.screens;






import java.util.ArrayList;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.RegisterLoginScreen;
import vn.sunnet.game.electro.libutils.Codes;
import vn.sunnet.lib.bmspriter.BMSAnimationBlueprint;
import vn.sunnet.lib.bmspriter.BMSRenderObjectProducer;
import vn.sunnet.lib.bmspriter.BMSSCMLFile;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxAnimationInstance;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxROP_TextureAtlas;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxRenderer;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.screens.groups.ModeGroup;
import vn.sunnet.qplay.diamondlink.screens.groups.NameDialog;
import vn.sunnet.qplay.diamondlink.screens.groups.Setting;
import vn.sunnet.qplay.diamondlink.screens.groups.Shop;
import vn.sunnet.qplay.diamondlink.screens.groups.WorldList;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.utils.CustomHttpClient;
import vn.sunnet.qplay.diamondlink.utils.GetLeaderBoardAdapter;
import vn.sunnet.qplay.diamondlink.utils.GetLeaderBoardEventListener;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class Login extends RegisterLoginScreen implements InputProcessor {
	
	Stage loginStage;
	Label loginlogoutBut = null;
	Button getFriends = null;
	
	SpriteBatch batch = new SpriteBatch();
	
	TweenManager tween = new TweenManager();
	
	DiamondLink instance = null;
	
	boolean firstLogin = true;
	private Sound press;
	private Music music;
	
	
	private float targetScale[] = {0.75f, 1, 0.75f, 0.5f};
	private float wayPointX[] = new float[4];
	private float wayPointY[] = new float[4];
	private float targetX[] = new float[4];
	private float targetY[] = new float[4];
	
	float eclapsedTime = 0;
	private boolean canTouch = true;
	ParticleEffect effect = new ParticleEffect();
	
	int loginNum = 0;
	float loginDelay = 0;
	private ArrayList<BMSLibgdxAnimationInstance> bmses = new ArrayList<BMSLibgdxAnimationInstance>();
	private ArrayList<Vector2> positions = new ArrayList<Vector2>();
	private BMSLibgdxAnimationInstance idle;
	private BMSLibgdxRenderer renderer;
	private OrthographicCamera camera;
	
	private Image playTab;
	private Button playBut;
	private ModeGroup modeTab;
	private InputMultiplexer multiplexer;
	
	private Button about;
	private Button leader;
	private Button setting;
	private Button shop;
	private TweenManager moveSystem;
	private Table serverSlogan;
	private Label serverStr;
	private BitmapFont msgFont;
	private Setting setGroup;
	private Shop shopGroup;
	private WorldList leaderGroup;

	public Login(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
		loginStage = new Stage(new StretchViewport(width, height));
		instance = (DiamondLink) game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(loginStage);
		multiplexer.addProcessor(this);
		moveSystem = instance.getMoveSystem();
		System.out.println("khoi tao");
	}

	private void getServerStr() {
		if (PlayerInfo.serverMsg.length() == 0) return;
		if (PlayerInfo.serverMsg.equals("[]")) return;
		JsonValue value = new JsonReader().parse(PlayerInfo.serverMsg); 
		int size = value.size;
		int index = MathUtils.random(0, size - 1);
		value = value.get(index);
		serverStr.setText(value.getString("Description")+" on "+value.getString("time"));
		final float width =  msgFont.getBounds(serverStr.getText()).width;
		serverStr
		.setBounds(
				0,
				30 / 2 - serverStr
						.getPrefHeight() / 2,
						width,
				serverStr
						.getPrefHeight());
		serverStr.setVisible(false);
	}
	
	private void getServerMsg() {
		System.out.println("serverMsg = |"+PlayerInfo.serverMsg+"|"+PlayerInfo.serverMsg.length());
		if (PlayerInfo.serverMsg.length() > 0) {
			System.out.println("show ngay");
			getServerStr();
			showServerMsg();
			return;
		} else
			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("lay ngay");
					CustomHttpClient httpClient = new CustomHttpClient(
							"http://viguysentertainment.com/manager/index.php/services/announcement");

					String str = "";
					try {
						str = httpClient.request();
					} catch (Exception e) {
						str = "";
						System.out.println(e.getMessage());
					}

					PlayerInfo.serverMsg = str;
					System.out.println("lay duoc "+PlayerInfo.serverMsg);
					getServerStr();
					Gdx.app.postRunnable(new Runnable() {

						@Override
						public void run() {
							showServerMsg();
						}
					});

				}
			}).start();
	}
	
	private void showServerMsg() {
		System.out.println("getText "+serverStr.getText());
		if (modeTab.isVisible()) return;
		if (serverStr.getText().length() > 0) {
			serverSlogan.setVisible(true);
			final float width =  msgFont.getBounds(serverStr.getText()).width;
			
			serverStr.setVisible(false);
			serverStr.clearActions();
			serverSlogan.clearActions();
			serverSlogan.setColor(1f, 1f, 1f, 0);
			serverSlogan.addAction(Actions.sequence(
					Actions.alpha(1f, 1f, Interpolation.linear), new Action() {

						@Override
						public boolean act(float delta) {

							serverStr.addAction(Actions.repeat(-1,
									Actions.sequence(new Action() {

										@Override
										public boolean act(float delta) {
											serverStr
													.setBounds(
															400,
															57 / 2 - serverStr
																	.getPrefHeight() / 2,
															serverStr
																	.getPrefWidth(),
															serverStr
																	.getPrefHeight());
											serverStr.setVisible(true);
											return true;
										}
									}, Actions
											.moveTo(-width, 57 / 2 - serverStr
													.getPrefHeight() / 2, 15f,
													Interpolation.linear))));
							return true;
						}
					}));
//			serverSlogan.addAction(Actions.repeat(-1, Actions.sequence(
//					Actions.alpha(1f, 1f, Interpolation.linear), new Action() {
//
//						@Override
//						public boolean act(float delta) {
//							serverStr.clearActions();
//							
//							serverStr.addAction(Actions.repeat(-1,
//									Actions.sequence(
//											new Action() {
//
//												@Override
//												public boolean act(float delta) {
//													serverStr
//															.setBounds(
//																	480 - 20,
//																	30 / 2 - serverStr
//																			.getPrefHeight() / 2,
//																	serverStr
//																			.getPrefWidth(),
//																	serverStr
//																			.getPrefHeight());
//													serverStr.setVisible(true);
//													return true;
//												}
//											},
//											Actions.moveTo(
//													-width,
//													30 / 2 - serverStr
//															.getPrefHeight() / 2,
//													15f, Interpolation.linear))));
//							return true;
//						}
//					}
//					, Actions.delay(30), Actions.alpha(0f, 1f,
//							Interpolation.linear), new Action() {
//
//						@Override
//						public boolean act(float delta) {
//							serverStr.setVisible(false);
//							serverStr
//							.setBounds(
//									480 - 20,
//									30 / 2 - serverStr
//											.getPrefHeight() / 2,
//									serverStr
//											.getPrefWidth(),
//									serverStr
//											.getPrefHeight());
//							serverStr.clearActions();
//							return true;
//						}
//					}, Actions.delay(1 * 30)))
//					);
		} else serverSlogan.setVisible(false);
	}
	
	@Override
	public void show() {
		System.out.println("showwwwwwwwwwwwwwwwwwww"+firstTime);
		if (firstTime) {
			firstTime = false;
			initLoginStage();
			initAudio();
		} else resetUI();
		System.out.println("showwwwwwwwwwwwwwwwwwww");
		Gdx.input.setInputProcessor(multiplexer);
		setProcessorAfterDismissingLoading(multiplexer);
		super.show();
		
		loginNum = 0;
		loginDelay = 4f;
		
		Assets.playMusic(music);
		
		getServerMsg();
		
		PlayerInfo.offlineName = instance.iFunctions.getString("name", "");
		System.out.println("name = "+PlayerInfo.offlineName);
		String str = new String(instance.iFunctions.getString("name", ""));
		if (PlayerInfo.offlineName.equals("") || str.trim().length() == 0) {
			new NameDialog(instance, this, new Runnable() {
				
				@Override
				public void run() {
					if (DiamondLink.hello) {
						createToast("Welcome to Jewelry", 2);
						DiamondLink.hello = false;
					}
				}
			}).show(loginStage.getRoot());
			
		} else {
			if (DiamondLink.hello) {
				createToast("Welcome to Jewelry", 2);
				DiamondLink.hello = false;
			}
		}
		System.out.println("showwwwwwwwwwwwwwwwwwww");
		
		TextureAtlas atlas = instance.getAssetManager().get(Assets.ATLAS,
				TextureAtlas.class);
		BMSSCMLFile file = instance.getAssetManager().get(Assets.STAR_BMS, BMSSCMLFile.class);
		BMSRenderObjectProducer producer = new BMSLibgdxROP_TextureAtlas(atlas);
		BMSAnimationBlueprint print = file.getEntity(0).getAnimation("NewAnimation");
		idle = new BMSLibgdxAnimationInstance(print, producer);
		idle.setLooping(false);
		
		
		bmses.clear();
		positions.clear();
		
		renderer = new BMSLibgdxRenderer(batch);
		System.out.println("render++++++++++++++++++++++++++++++++++++++++++++"+renderer.getSpriteBatch());
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		Assets.pauseMusic(music) ;
		super.hide();
	}
	
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Assets.playMusic(music);
	}
	
	@Override
	public void pause() {
		if (music != null)
			Assets.pauseMusic(music);
		super.pause();
	}
	
	private void initAudio() {
		press = instance.getAssetManager().get(SoundAssets.PRESS_SOUND, Sound.class);
		music = instance.getAssetManager().get(MusicAssets.LOGIN_MUSIC, Music.class);
		music.setLooping(true);
		music.setVolume(1f);
	}
	
	private void showPlayTab() {
		playBut.addAction(Actions.moveTo(116, 800 - 623, 1f, Interpolation.sine));
		playTab.addAction(Actions.moveTo(0, 0, 1f, Interpolation.sine));
		modeTab.addAction(Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				modeTab.setTouchable(Touchable.disabled);
				return true;
			}
		}, Actions.moveTo(480, 160, 1f, Interpolation.sine), new Action() {
			
			@Override
			public boolean act(float arg0) {
				modeTab.setVisible(false);
				getServerMsg();
				return true;
			}
		}));
	}
	
	private void resetUI() {
		playBut.setBounds(116, 800 - 623, playBut.getPrefWidth(), playBut.getPrefHeight());
		playTab.setX(0); playTab.setY(0);
		modeTab.setX(480); modeTab.setY(160);
		modeTab.setVisible(false);
	}
	
	
	private void hidePlayTab() {
		serverSlogan.clearActions();
		serverSlogan.addAction(Actions.alpha(0, 1f, Interpolation.linear));
//		serverSlogan.setVisible(false);
		playBut.addAction(Actions.moveTo(116 - 480, 800 - 623, 1f, Interpolation.sine));
		playTab.addAction(Actions.moveTo(0, -playTab.getHeight(), 1f, Interpolation.sine));
		modeTab.addAction(Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				modeTab.setVisible(true);
				modeTab.setTouchable(Touchable.disabled);
				
				return true;
			}
		}, Actions.moveTo(480 / 2 - modeTab.getWidth() / 2, 160, 1f, Interpolation.sine), new Action() {
			
			@Override
			public boolean act(float arg0) {
				modeTab.setTouchable(Touchable.enabled);
				modeTab.reset();
				return true;
			}
		}));
	}
	
	private void initPlayStage() {
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		Texture texture = instance.getAssetManager().get(UIAssets.LOGIN_LAYER_0, Texture.class);
		loginStage.addActor(new Image(texture));
		texture = instance.getAssetManager().get(UIAssets.LOGIN_LAYER_1, Texture.class);
		final Image im = new Image(texture);
		im.addAction(Actions.repeat(-1, Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				im.setBounds(240 - im.getPrefWidth() / 2, -im.getPrefHeight(), im.getPrefWidth(), im.getPrefHeight());
				return true;
			}
		}, Actions.moveTo(0, 800, 15, Interpolation.linear))));
		loginStage.addActor(im);
		
		
		
		texture = instance.getAssetManager().get(UIAssets.LOGIN_LAYER_2, Texture.class);
		playTab = new Image(texture);
		loginStage.addActor(playTab);
		
		TextureAtlas atlas = instance.getAssetManager().get(UIAssets.LOGIN_FG, TextureAtlas.class);
		Image im2 = new Image(atlas.findRegion("Logo"));
		im2.setBounds(480 / 2 - im2.getPrefWidth() / 2, 800 - 270, im2.getPrefWidth(), im2.getPrefHeight());
		loginStage.addActor(im2);
		
		
		playBut = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Play", 0)), new TextureRegionDrawable(
				atlas.findRegion("Play", 1)));
		playBut.setBounds(116, 800 - 623, playBut.getPrefWidth(), playBut.getPrefHeight());
		playBut.addListener(new ClickListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					press.play();
				return true;
			}
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				hidePlayTab();
			}
			
		});
		loginStage.addActor(playBut);
		
		leader = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("LeaderBoard", 0)), new TextureRegionDrawable(
				atlas.findRegion("LeaderBoard", 1)));
		leader.setBounds(480 / 2 - 113 / 2 - 10 - 113, 13, leader.getPrefWidth(), leader.getPrefHeight());
		leader.addListener(new ClickListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					press.play();
				return true;
			}
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				instance.iFunctions.track("In Menu", "Click Leaderboard", "Board "+instance.getMoment(), 1);
				
				if (instance.iFunctions.isInternet()) 
				{
					instance.iFunctions.showWaitingDialog();
					GetLeaderBoardAdapter adapter = new GetLeaderBoardAdapter(new GetLeaderBoardEventListener() {
						
						@Override
						public void onGetLeaderBoardSuccess(final String resultString) {
							instance.iFunctions.hideWaitingDialog();
							Gdx.app.postRunnable(new Runnable() {
								
									@Override
									public void run() {
										if (instance.getScreen() == Login.this) {
											leader.setVisible(false);
											shop.setVisible(false);
											setting.setVisible(false);
											about.setVisible(false);
											leaderGroup = new WorldList(instance, new Runnable() {
												
												@Override
												public void run() {
													leader.setVisible(true);
													shop.setVisible(true);
													setting.setVisible(true);
													about.setVisible(true);
												}
											}).updateDatas(PlayerInfo.parseModesData(resultString));
											leaderGroup.show(loginStage.getRoot());
									}
								}
							});
							
						}
						
						@Override
						public void onGetLeaderBoardFailure(String resultString) {
							instance.iFunctions.hideWaitingDialog();
						}
					});
					adapter.getLeaderBoard(instance.iFunctions.getDeviceId(), "1", "1|2|3");
					
				} else 
				{
					leader.setVisible(false);
					shop.setVisible(false);
					setting.setVisible(false);
					about.setVisible(false);
					createToast("Open Internet to get online leaderboards", 2f);
					leaderGroup = new WorldList(instance, new Runnable() {

						@Override
						public void run() {
							leader.setVisible(true);
							shop.setVisible(true);
							setting.setVisible(true);
							about.setVisible(true);
						}
					}).updateDatas(
							PlayerInfo.getOfflineMode(0, instance.iFunctions),
							PlayerInfo.getOfflineMode(1, instance.iFunctions),
							PlayerInfo.getOfflineMode(2, instance.iFunctions));
					leaderGroup.show(loginStage.getRoot());
				}
			}
			
		});
		loginStage.addActor(leader);
		
		about = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("About", 0)), new TextureRegionDrawable(
				atlas.findRegion("About", 1)));
		about.setBounds(480 / 2 - 113 / 2 - 10 - 113, 13, about.getPrefWidth(), about.getPrefHeight());
		about.addListener(new ClickListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					press.play();
				return true;
			}
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				
			}
			
		});
//		loginStage.addActor(about);
		
		shop = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Shop", 0)), new TextureRegionDrawable(
				atlas.findRegion("Shop", 1)));
		shop.setBounds(480 / 2 - shop.getPrefWidth() / 2, 13, shop.getPrefWidth(), shop.getPrefHeight());
		shop.addListener(new ClickListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					press.play();
				instance.iFunctions.track("In Menu", "Click CoinShop", "CoinShop "+instance.getMoment(), 1);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				leader.setVisible(false);
				shop.setVisible(false);
				setting.setVisible(false);
				about.setVisible(false);
				shopGroup = new Shop(instance, Login.this, null, new Runnable() {
					
					@Override
					public void run() {
						leader.setVisible(true);
						shop.setVisible(true);
						setting.setVisible(true);
						about.setVisible(true);
					}
				});
				shopGroup.show(loginStage.getRoot());
			}
			
		});
		loginStage.addActor(shop);
		
		setting = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Setting", 0)), new TextureRegionDrawable(
				atlas.findRegion("Setting", 1)));
		setting.setBounds(480 / 2 + 113 / 2 + 10, 13, setting.getPrefWidth(), setting.getPrefHeight());
		setting.addListener(new ClickListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					press.play();
				return true;
			}
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				leader.setVisible(false);
				shop.setVisible(false);
				setting.setVisible(false);
				about.setVisible(false);
				setGroup = new Setting(instance, Login.this, new Runnable() {
					
					@Override
					public void run() {
						leader.setVisible(true);
						shop.setVisible(true);
						setting.setVisible(true);
						about.setVisible(true);
					}
				});
				setGroup.show(loginStage.getRoot());
			}
			
		});
		loginStage.addActor(setting);
	}
	
	private void initModeStage() {
		modeTab = new ModeGroup(instance, this);
		modeTab.setX(480); modeTab.setY(160);
	
		loginStage.addActor(modeTab);
		modeTab.setVisible(false);
	}
	
	private void initLoginStage() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++1");
		initPlayStage();
		initModeStage();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++2");
		FreeTypeFontGenerator generator = instance.getAssetManager().get(UIAssets.DUC_GENERATOR, FreeTypeFontGenerator.class);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++3");
//		msgFont = generator.generateFont(30, AbstractScreen.FONT_CHARACTERS, false);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		msgFont = generator.generateFont(parameter);
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++4");
		msgFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
		serverSlogan = new Table();
		serverSlogan.setBounds(0, 400 - 57 / 2, 480, 57);
		serverSlogan.setClip(true);
		TextureAtlas atlas = instance.getAssetManager().get(UIAssets.LOGIN_FG, TextureAtlas.class);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
		Image image = new Image(atlas.findRegion("SloganFrame"));
		image.setBounds(0, 0, 480, 57);
		serverSlogan.addActor(image);
		
		Table table = new Table();
		table.setClip(true);
		table.setBounds(40, 0, 480 - 80, 57);
		serverSlogan.addActor(table);
		
		
		serverStr = new Label("", new Label.LabelStyle(msgFont, msgFont.getColor()));
//		serverStr.setWrap(true);
//		serverStr.setAlignment(Align.center, Align.center);
		table.add(serverStr);
		
		
		serverSlogan.setTransform(true);
		serverSlogan.setColor(1f, 1f, 1f, 0);
		loginStage.addActor(serverSlogan);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		loginStage.act(delta);
		loginStage.draw();
//		System.out.println("modeTab"+modeTab.getT)
		
		if (bmses.size() == 0) {
			for (int i = 0; i < MathUtils.random(5); i++)
			{
				TextureAtlas atlas = instance.getAssetManager().get(Assets.ATLAS,
						TextureAtlas.class);
				BMSSCMLFile file = instance.getAssetManager().get(Assets.STAR_BMS, BMSSCMLFile.class);
				BMSRenderObjectProducer producer = new BMSLibgdxROP_TextureAtlas(atlas);
				BMSAnimationBlueprint print = file.getEntity(0).getAnimation("NewAnimation");
				idle = new BMSLibgdxAnimationInstance(print, producer);
				idle.seek(MathUtils.random(idle.blueprint.getLength() / 2));
				idle.setLooping(false);
				bmses.add(idle);
				
				Vector2 pos = new Vector2(MathUtils.random(30, 450), MathUtils.random(30, 800 - 30));
				positions.add(pos);
			}
		} else {
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			for (int i = 0; i < bmses.size(); i++) {
				bmses.get(i).animate((int)(delta * 1000 * 0.4f));
				if (renderer != null) 
					renderer.render(bmses.get(i), positions.get(i).x, positions.get(i).y);
//				System.out.println("getTime"+ bmses.get(i).getTime());
				if (bmses.get(i).getTime() == bmses.get(i).blueprint.getLength() - 1) {
//					System.out.println("remove"+ i);
					bmses.remove(i);
					positions.remove(i);
					i--;
				}
			}
			batch.end();
			
			
		}
		moveSystem.update(delta);
		
		super.render(delta);
	}
	
	/***********************************Network*******************************/
	
	private void login() {
		System.out.println("login in Login");
		setMapRoot("diamondlink", "city", "diamondlink");
		register_login();
	}
	
	@Override
	protected boolean isFirstLogin() {
		// TODO Auto-generated method stub
		return instance.getAssets().getBoolean("login");
	}
	
	@Override
	public boolean haveInternet() {
		// TODO Auto-generated method stub
		return instance.iFunctions.isInternet();
	}
	
	@Override
	public void createToast(String slogan, float duration) {
		// TODO Auto-generated method stub
		if (Gdx.app.getType() == ApplicationType.Android) {
			instance.iFunctions.createToast(slogan, duration);
		} else
		super.createToast(slogan, duration);
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
			instance.iFunctions.createDialog(title, context, positive, negative,
				neutral, command);
		} else
		super.createDialog(title, context, positive, negative, neutral,
				toInputProcessor);
	}
	
	protected void showConnectionErrorDialog() {
		// prepare the alert box
		createDialog("Lá»—i káº¿t ná»‘i",
				"Ä�Ã£ cÃ³ lá»—i trong quÃ¡ trÃ¬nh káº¿t ná»‘i. Xin kiá»ƒm tra káº¿t ná»‘i vÃ  thá»­ láº¡i!", new ButtonDescription("Tiáº¿p tá»¥c", new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						System.out.println("Thá»±c hiá»‡n tiáº¿p tá»¥c");
						login();
					}
				}), new ButtonDescription("KhÃ´ng", new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
					}
				}), null, null);
	}
	
	protected void showNoNetworkErrorDialog() {
		// prepare the alert box
		createDialog("Lá»—i káº¿t ná»‘i",
				"ChÆ°a káº¿t ná»‘i máº¡ng! Báº¡n hÃ£y báº­t káº¿t ná»‘i máº¡ng Ä‘á»ƒ chÆ¡i.",
				new ButtonDescription("Tiáº¿p tá»¥c", new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						System.out.println("Thá»±c hiá»‡n tiáº¿p tá»¥c");
						login();
					}
				}), null, null, null);
	}
	
	protected void showWrongInfoToast(int code) {
		String title = "ThÃ´ng bÃ¡o";
		String context = "";
		switch (code) {
		case Codes.CODE_LOGIN_WRONG_INFO:
			context = "Lá»—i khi Ä‘Äƒng nháº­p. Vui lÃ²ng thá»­ láº¡i!";

			break;
		case Codes.CODE_LOGIN_CONFLICT:
			context = "TÃ i khoáº£n Ä‘ang Ä‘Æ°á»£c sá»­ dá»¥ng";

			break;
		case Codes.CODE_LOGIN_ERROR:
			context = "Lá»—i khi kiá»ƒm tra tÃ i khoáº£n, vui lÃ²ng thá»­ láº¡i sau";

			break;
		case Codes.CODE_DISCONNECT_SERVER:
			context = "Lá»—i káº¿t ná»‘i mÃ¡y chá»§, xin vui lÃ²ng thá»­ láº¡i sau";

		default:
			break;
		}
		
		createDialog(title,
				context,
				new ButtonDescription("Tiáº¿p tá»¥c", new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						System.out.println("Thá»±c hiá»‡n tiáº¿p tá»¥c");
						login();
					}
				}), null, null, null);
	}

	/*******************************Input************************************/
	@Override
	public boolean keyDown(int keyCode) {
		// TODO Auto-generated method stub
		if (keyCode == Keys.ESCAPE || keyCode == Keys.BACK) {
			if (setGroup != null)
				if (setGroup.getParent() != null) {
					setGroup.remove();
					leader.setVisible(true);
					shop.setVisible(true);
					setting.setVisible(true);
					about.setVisible(true);
					return true;
				}
			if (shopGroup != null)
				if (shopGroup.getParent() != null) {
					shopGroup.remove();
					leader.setVisible(true);
					shop.setVisible(true);
					setting.setVisible(true);
					about.setVisible(true);
					return true;
				}
			if (leaderGroup != null)
				if (leaderGroup.getParent() != null) {
					leaderGroup.remove();
					leader.setVisible(true);
					shop.setVisible(true);
					setting.setVisible(true);
					about.setVisible(true);
					return true;
				}
			if (modeTab.isVisible()) {
				showPlayTab();
				return true;
			}
			
			
			createDialog("Exit", "Do you want to quit?",
					new ButtonDescription("Ok", new Command() {

						@Override
						public void execute(Object data) {
							Gdx.app.exit();
						}
					}), new ButtonDescription("Cancel", new Command() {

						@Override
						public void execute(Object data) {
							// TODO Auto-generated method stub
						}
					}), null, null);
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
		if (canTouch) {
			canTouch = false;
		}
		return true;
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
		return this;
	}

	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		loginStage.dispose();
		msgFont.dispose();
	}
}
