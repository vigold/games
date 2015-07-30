package vn.sunnet.qplay.diamondlink.screens;

import java.util.ArrayList;
import java.util.Random;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.GameNodeScreen;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxRenderer;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.graphiceffects.OpenGLEffect;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.modules.CaculateCombo;
import vn.sunnet.qplay.diamondlink.modules.DiamondAI;
import vn.sunnet.qplay.diamondlink.modules.FallModule;
import vn.sunnet.qplay.diamondlink.modules.FindSolution;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.modules.GenerateXScore;
import vn.sunnet.qplay.diamondlink.modules.GenerationModule;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.groups.FrontStage;
import vn.sunnet.qplay.diamondlink.screens.groups.Pause;
import vn.sunnet.qplay.diamondlink.screens.groups.Running;
import vn.sunnet.qplay.diamondlink.screens.groups.WorldList;
import vn.sunnet.qplay.diamondlink.ui.ParticleEffectActor;
import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;
import vn.sunnet.qplay.diamondlink.utils.SubmitScoreAdapter;
import vn.sunnet.qplay.diamondlink.utils.SubmitScoreEventListener;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class GameScreen extends GameNodeScreen {
	
	public int gScreenType = 0;	
	
	public static int MAX_GAME = 4;
	public static final int CLASSIC_DIAMOND = 0;
	public static final int BUTTERFLY_DIAMOND = 1;
	public static final int MINE_DIAMOND = 2;
	public static final int MISSION_DIAMOND = 3;
	public static final int ADVENTURE_DIAMOND = 4;
	public static final int REVIEW_LITERATURE = 5;
	public static final int TRAIN_KUNGFU = 6;
	public static final int PALAESTRA = 7;
	public int GAME_ID = 0;
	
	public static final int GAME_READY = 0;    
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_LEVEL_END = 3;
	public static final int GAME_OVER = 4;
	public static final int GAME_SUMMARY = GAME_OVER + 1;
	public static final int GAME_RANKING = GAME_OVER + 2;
	
	public static final int STEP_BEGIN = 0;
	public static final int STEP_RUNNING = 1;
	public static final int STEP_END = 2;
	public int stateOfStep = STEP_BEGIN;
	
	public int preStep = GAME_OVER;
	public int curStep = GAME_LEVEL_END;	

	public static final int TOUCH_GAME = 0;
	public static final int TOUCH_UI = 2;
	public static final int TOUCH_ITEM = 1;
	public int gTouchMode = 0;
	
	public DiamondLink gGame;
	public TweenManager moveSystem = null;
	
	public OrthographicCamera gCamera;
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	public AssetManager manager;
	public Assets assets;
	
	public Stage stage;
	public FrontStage frontStage;
	
	public OpenGLEffect curEffect;
	public OpenGLEffect glaze;
	
	protected InputMultiplexer multiplexer;
	
	// Modules
    public FallModule fall = null;
    public GameLogic logic = null;
    public GenerationModule generate = null;
    public FindSolution solution = null;
    public DiamondAI ai = null;
    public CaculateCombo combo;
    public GenerateXScore xScore;
	
    // Constant Sprites , TextureRegions and Animations
    public TextureRegion gSelection;
    public TextureRegion gGameBackGround;
    public TextureRegion gGridTable;
    public MyAnimation hintSymbol;
    
    
    public BitmapFont comboFont;
    public BitmapFont diamondFont;
	
    protected float runningScore = 0;
	protected float eclapsedTime = 0;
	
    public boolean initGame = true;
	public int gameLevel = 0;
	public float targetScore = 0;
	public float totalScore = 0;
	public float levelScore = 0;
	public float timeLevel = 0;
	public float timeRemain = 0;
	public float timeSwap = 0;
	public int addCoins = 0;
	
	// Variables and arrays belong to the grid state
	public int[][] inGridFlag = new int[8][8];
	public int[][] grid = new int[8][8];
	//public int[][] gridFur = new int[8][8];
    public int[][] gridBuf = new int[8][8];
	public int[] colHeight = new int[8];
    // Indexes relate to color and type
	public int TYPE_NUM = -1;
	public int COLOR_NUM = -1;
	public int DIAMOND_WIDTH = 60;
	public int DIAMOND_HEIGHT = 60;
	public Skill[] skillArrs = new Skill[4];
    // Game State in step Running
   
	public static final int DIAMOND_REST = 0;
	public static final int DIAMOND_MOVE = 1;
	public static final int DIAMOND_ANIMATION = 2;
	public static final int DIAMOND_AI = 4;
	public static final int DIAMOND_UP = 4;
	public static final int DIAMOND_CHANGE = 3;
	public int stateGame;

    // item

    public boolean useItem = false;
    public Vector3 itemPoint;
    public Skill curSkill;
    public Skill selectedSkill;
    
    public VipCard vip;
    public Avatar avatars[];
    //
    public Vector3 curPoint;
    public Vector3 lastPoint;
    public Rectangle gridTable;
    
    public Vector3 gridPos;
    public Vector3 fallPos;
    public int selection = -1;
    // Variables and Arrays belong to animation
  
    public ArrayList<IDiamond> diamonds = new ArrayList<IDiamond>();
    public ArrayList<IEffect> checkCellEffect = new ArrayList<IEffect>();
    
    // Time and numbers belong to Fall
    public int[] fixedNum = new int[8];// so luong co dinh
	public int[] fallingNum = new int[8];
    /// relate phases in GameRunning
    public int MAX_PHASE = -1;
    public Phase[] gamePhase = null;
    // Variables in Game Over Step 
    public boolean questSaveScore = false;
    public boolean firstCreated = true;
    public int eatClock = 0; 
    public float totalTime = 0;
    public long lastFriendsRank = 0;
    public boolean shareComplete = false;
    public float gameTime;
    
    public BMSLibgdxRenderer bmsRenderer;
    
    protected int canShowAd = 0;
    protected boolean saving = false;
    protected Group saveDialog = null;
    protected Pause pause;
    protected Running runningUI;
    
	public GameScreen(int width, int height, DiamondLink pGame) {
		// TODO Auto-generated constructor stub
		super(width, height, pGame);
		this.gGame = pGame;
		
		gCamera = new OrthographicCamera();
		gCamera.setToOrtho(false, DiamondLink.getFixedWith(), DiamondLink.getFixedHeight());
		stage = new Stage(new StretchViewport(DiamondLink.getFixedWith(), DiamondLink.getFixedHeight()));
		
		
		
	    batch = new SpriteBatch();
	    bmsRenderer = new BMSLibgdxRenderer(batch);
	    renderer = new ShapeRenderer();
	    multiplexer = new InputMultiplexer();
	    

	    multiplexer.addProcessor(stage);
	    manager = gGame.getAssetManager();
	    assets = gGame.getAssets();
	    moveSystem = gGame.getMoveSystem();
	    Tween.setWaypointsLimit(1000);
		initParamsGame();
		initPhases();
		initModules();
		
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
		batch.disableBlending();
		batch.enableBlending();
		present(delta);
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		Gdx.input.setInputProcessor(multiplexer);
		setProcessorAfterDismissingLoading(multiplexer);
		if (firstCreated) {
			initAudio();
			initTextures();
			initInterface();
			firstCreated = false;
		} 
		
		gGame.iFunctions.hideAdView();
	}

	@Override
	public void hide() {
		super.hide();
	
		gGame.iFunctions.showAdView(true);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		batch.dispose();
		renderer.dispose();
	}
	
	public abstract void initTextures();
	
	public abstract void initAudio();
	
	public abstract void initParamsGame();
	
	public abstract void initLevel();
	
	public abstract void initPhases();
	
	public abstract void initInterface();
	
	public abstract void initModules();
	
	public abstract void updateOver(float deltaTime);

	public abstract void updateLevelEnd(float deltaTime);

	public abstract void updatePaused(float deltaTime);

	public abstract void updateRunning(float deltaTime);

	public abstract void updateReady(float deltaTime);
	
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		gCamera.update();
		switch (curStep) {
			case GAME_READY: updateReady(deltaTime); break;
			case GAME_RUNNING: updateRunning(deltaTime); break;
			case GAME_PAUSED: updatePaused(deltaTime); break;
			case GAME_LEVEL_END: updateLevelEnd(deltaTime); break;
			case GAME_OVER: updateOver(deltaTime); break;
		}
		stage.act(deltaTime);
		moveSystem.update(deltaTime);
	}
	
	public abstract void presentRunning(float deltaTime);

	public abstract void presentOver(float deltaTime);

	public abstract void presentLevelEnd(float deltaTime);

	public abstract void presentPaused(float deltaTime);

	public abstract void presentReady(float deltaTime);
	
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setProjectionMatrix(gCamera.combined);
		batch.setProjectionMatrix(gCamera.combined);

		batch.disableBlending();
		batch.enableBlending();
		switch (curStep) {
			case GAME_READY: presentReady(deltaTime); break;
			case GAME_RUNNING: presentRunning(deltaTime); break;
			case GAME_PAUSED: presentPaused(deltaTime); break;
			case GAME_LEVEL_END: presentLevelEnd(deltaTime); break;
			case GAME_OVER: presentOver(deltaTime); break;	
		}

		stage.draw();
	}
	
	public void setStepGame(int step) {
		preStep = curStep;
		curStep = step;
		stateOfStep = STEP_BEGIN;
		eclapsedTime = 0;
		if ((preStep == GAME_OVER || preStep == GAME_RUNNING)
				&& curStep == GAME_LEVEL_END)
			initGame = true;
	}
	
	public void setGameOver() {
		setStepGame(GAME_OVER);
		setStepGame(GAME_LEVEL_END);
	}
	
	public abstract void setGamePaused();
	
	public abstract boolean isOverTime();
	
	public void setGameID(int GameID) {
		GAME_ID = GameID;
	}
	
	public int getGameID() {
		return GAME_ID;
	}
	
	public void removeInputProcessor(InputProcessor pInputProcessor) {
		multiplexer.removeProcessor(pInputProcessor);
	}
	
	public void addInputProcessor(InputProcessor pInputProcessor) {
		multiplexer.addProcessor(pInputProcessor);
	}
	
	public void changeInputProcessor(InputProcessor pSInputProcessor, InputProcessor pDInputProcessor) {
		if (pSInputProcessor != null) {
			multiplexer.removeProcessor(pSInputProcessor);
			for (int i = 0; i < multiplexer.size(); i++) {
				InputProcessor inputProcessor = multiplexer.getProcessors().get(i);
			}
		}
		
	}
	
	public void sendAvatars(Avatar avatars[], int maxAvatar) {
		this.avatars = avatars;
		if (this.avatars != null) {
			for (int i = 0; i < this.avatars.length; i++)
				if (this.avatars[i] != null) 
					if (this.avatars[i].isExpired()) this.avatars[i] = null;
		}
	}
	
	public void sendSkills(Skill skills[]) {
		this.skillArrs = skills;
	}
	
	public void sendVips(VipCard vip) {
		this.vip = vip;
	}
	
	
	
	public void showNoMoreMove(Runnable complete) {
		((FrontStage) frontStage).noMoreMoveAction(complete);
	}
	
	public void showItem(int add, final String about, float fromX, float fromY, float desX, float desY) {
		float timeMove = 1f;
		if (isOverTime() || timeRemain < 2f) return;
		TextureRegion region = null;
		ParticleEffectActor particle = null;
		Random random = new Random();
		if (about.equals("CoinDiamond")) {
			particle = new ParticleEffectActor(ParticleAssets.COIN_SPRAY, this);
		} else if (about.equals("ClockDiamond")) {
			particle = new ParticleEffectActor(ParticleAssets.TIME_SPRAY, this);
		} else if (about.equals("ClockItem")) {
			particle = new ParticleEffectActor(ParticleAssets.TIME_SPRAY, this);
		} 
		
		particle.setX(fromX); particle.setY(fromY);
		
		final ParticleEffectActor target = particle;
		
		((FrontStage) frontStage).showItem(new Vector2(fromX, fromY),
				new Vector2(desX, desY), particle, new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						if (about.equals("CoinDiamond")) {
							addCoins += 15;
						} else if (about.equals("ClockDiamond")) {
							timeRemain = Math.min(timeRemain + 5,  timeLevel);
						} else if (about.equals("ClockItem")) {
							timeRemain = Math.min(timeRemain + 10,  timeLevel);
						}
					}
				});
	}
	
	public void showSense(int combo, float x, float y) {
		if (isOverTime()) return;
		if (vip != null) {
			if (vip.isShowed()) return;
		}
		
		if (combo < 3) return;
		Image im = null;
		switch (combo) {
		case 3:
			im = new Image(manager.get(UIAssets.COOL, Texture.class));
			break;
		case 4:
			im = new Image(manager.get(UIAssets.GREAT, Texture.class));
			break;
		case 5:
			im = new Image(manager.get(UIAssets.GOOD, Texture.class));
			break;
		case 6:
			im = new Image(manager.get(UIAssets.EXCELLENT, Texture.class));
			break;
		default:
			if (combo / 7 > 0 && (combo - 7) % 5 == 0)
				im = new Image(manager.get(UIAssets.PERFECT, Texture.class));
			else
				return;
			break;
		}
		x = Math.min(x, DiamondLink.WIDTH - im.getPrefWidth() / 2);
		x = Math.max(x, im.getPrefWidth() / 2);
		y = Math.min(y, DiamondLink.HEIGHT - im.getPrefHeight() / 2);
		y = Math.max(y, im.getPrefHeight() / 2);
		im.setBounds(x - im.getPrefWidth() / 2, y - im.getPrefHeight() / 2,
				im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		final Image target = im;
		
		((FrontStage) frontStage).showSense(im, null);
	}
	
	public void showComboScore(int time, int score, int color, float x, float y) {
		
		String combo = "x"+time+" "+score;
		if (time == 1) combo = ""+score;
		Color fontColor = null;
		switch (color) {
		case Diamond.BLUE:
			fontColor = Color.BLUE;
			break;
		case Diamond.RED:
			fontColor = Color.RED;
			break;
		case Diamond.YELLOW:
			fontColor = Color.YELLOW;
			break;
		case Diamond.GREEN:
			fontColor = Color.GREEN;
			break;
		case Diamond.ORANGE:
			fontColor = Color.ORANGE;
			break;
		case Diamond.WHITE:
			fontColor = Color.WHITE;
			break;
		case Diamond.PINK:
			fontColor = Color.PINK;
			break;
		default:
			break;
		}
		LabelStyle style = new LabelStyle(comboFont, fontColor);
		Label label = new Label(combo, style);
		TextBounds bounds = comboFont.getBounds(combo);
		label.setBounds(x - bounds.width / 2, y - bounds.height / 2, bounds.width, bounds.height);
		final Label target = label;
		
		x = Math.min(x, DiamondLink.WIDTH - bounds.width / 2);
		x = Math.max(x, bounds.width / 2);
		y = Math.min(y, DiamondLink.HEIGHT - bounds.height / 2);
		y = Math.max(y, bounds.height / 2);
		
		((FrontStage) frontStage).showComboScore(label, null);
	}
	
	public void showSaving() {
		if (saveDialog == null) {
			saveDialog = new Group();
			saveDialog.setBounds(0, 0, 480, 800);
			Image im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
			im.setBounds(0, 0, 480, 800);
			saveDialog.addActor(im);
			TextureAtlas atlas = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
			SpriteSheet sheet = new SpriteSheet(0.15f, atlas.findRegions("Save"),SpriteSheet.LOOP);
			sheet.setBounds(240 - sheet.getPrefWidth() / 2, 400 - sheet.getPrefHeight() / 2, sheet.getPrefWidth(), sheet.getPrefHeight());
			saveDialog.addActor(sheet);
		} 
		runningUI.setTouchable(Touchable.disabled);
		frontStage.addActor(saveDialog);
		System.out.println("showSave");
	}
	
	public void hideSaving() {
		
		if (saveDialog == null) {
			runningUI.setTouchable(Touchable.enabled);
			saveDialog.remove();
		}
		System.out.println("hideSave");
	}
	
	public boolean isSaving() {
		if (saveDialog != null)
			if (saveDialog.getParent() != null)	return true;
		return false;
	}
	
	public void setColor(float r, float g, float b, float a) {
		batch.setColor(r, g, b, a);
	}
	
	public abstract boolean isDangerous();
	
	/**********************************NetWork*********************************/
	
	
	public void superCreateDialog(String title, String context,
			ButtonDescription positive, ButtonDescription negative,
			ButtonDescription neutral, final InputProcessor toInputProcessor) {
		super.createDialog(title, context, positive, negative, neutral, toInputProcessor);
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
			gGame.iFunctions.createDialog(title, context, positive, negative,
				neutral, command);
		} else
		super.createDialog(title, context, positive, negative, neutral,
				toInputProcessor);
	}
	
	@Override
	public void createToast(String slogan, float duration) {
		// TODO Auto-generated method stub
		if (Gdx.app.getType() == ApplicationType.Android) {
			gGame.iFunctions.createToast(slogan, duration);
		} else
		super.createToast(slogan, duration);
	}
	
	
	
	public void createSubmitDialog(final Runnable complete) {
//		gGame.iFunctions.track("In "+getName()+" Over", (long)totalTime, ""+(long)levelScore, "level "+(gameLevel + 1));
		createDialog("", "Do you submit your score to Jewelry's Community??",
				new ButtonDescription("Ok", new Command() {

					@Override
					public void execute(Object data) {
						if (gGame.iFunctions.isInternet()) {
							
							gGame.iFunctions.showWaitingDialog();
							SubmitScoreAdapter submit = new SubmitScoreAdapter(new SubmitScoreEventListener() {
								
								@Override
								public void onSubmitScoreSucess(final String resultString) {
									gGame.iFunctions.hideWaitingDialog();
									if (gGame.getScreen() == me()) {
										System.out.println("show");
										Gdx.app.postRunnable(new Runnable() {
											
											@Override
											public void run() {
												new WorldList(gGame, new Runnable() {
													
													@Override
													public void run() {
														
														if (complete != null)
															complete.run();
													}
												}).updateDatas(PlayerInfo.parseModeData(getGameID(), resultString)).show(stage.getRoot());
											}
										});	
									}
								}
								
								@Override
								public void onSubmitScoreFailure(String resultString) {
									gGame.iFunctions.hideWaitingDialog();
									Gdx.app.postRunnable(new Runnable() {
										
										@Override
										public void run() {
											me().createToast("Network Error", 2f, new Runnable() {
												
												@Override
												public void run() {
													if (complete != null)
														complete.run();
												}
											});
										}
									});
									
									
								}
							});
							submit.submitScore(gGame.iFunctions.getDeviceId(), "1", ""+(getGameID() + 1), ""+levelScore, PlayerInfo.offlineName);
						} else {
							Gdx.app.postRunnable(new Runnable() {

								@Override
								public void run() {
									me().createToast("Please open your internet", 2f, new Runnable() {
										
										@Override
										public void run() {
											if (complete != null)
												complete.run();
										}
									});
								}
								
							});
						}
						
					}
				}), new ButtonDescription("Cancel", new Command() {

					@Override
					public void execute(Object data) {
						Gdx.app.postRunnable(new Runnable() {
							
							@Override
							public void run() {
								if (complete != null)
									complete.run();
							}
						});
						
					}
				}), null, null);
	}
	
	
	
	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return multiplexer;
	}
	
	public Skill[] getSkills() {
		return skillArrs;
	}
	
	protected GameScreen me() {
		return this;
	}
	
	public void manualSave(final Runnable complete) {
		showSaving();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				saveLevelOnly();
				Gdx.app.postRunnable(new Runnable() {
					
					@Override
					public void run() {
						hideSaving();
						if (complete != null) complete.run();
					}
				});
			}
		}).start();
	}
	
	public void autoSave(final Runnable complete) {
		showSaving();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				saveGame();
				Gdx.app.postRunnable(new Runnable() {
					
					@Override
					public void run() {
						hideSaving();
						if (complete != null) complete.run();
					}
				});
			}
		}).start();
	}
	
	protected void saveLevelOnly() {
		IFunctions iFunctions = gGame.iFunctions;
		iFunctions.putFastBool("data "+GAME_ID, false);
		// trang thai state
		iFunctions.putFastFloat("timeRemain "+GAME_ID, 90f);
		iFunctions.putFastFloat("totalTime "+GAME_ID, 0);
		iFunctions.putFastFloat("eclapsedTime "+GAME_ID, 0);
		iFunctions.putFastFloat("gameTime "+GAME_ID, 0);
		iFunctions.putFastFloat("levelScore "+GAME_ID, levelScore);
		iFunctions.putFastFloat("targetScore "+GAME_ID, targetScore);
		iFunctions.putFastInt("level "+GAME_ID, gameLevel);
		iFunctions.putFastInt("coins "+GAME_ID, addCoins);
		stateGame = DIAMOND_ANIMATION;
		iFunctions.putFastInt("state "+GAME_ID, stateGame);
		gamePhase[stateGame].save(iFunctions);
		
		String data = "";
		for (int i = 0; i < skillArrs.length; i++) {
			data = "";
			if (skillArrs[i].type != Skill.NONE)
				data += skillArrs[i].type+"|"+skillArrs[i].count;
			iFunctions.putFastString("skill "+i+" "+GAME_ID, data);
		}
		iFunctions.putFastBool("only Level "+GAME_ID, true);
		iFunctions.putFastBool("data "+GAME_ID, true);
		System.out.println("save level complete");
	}
	
	protected void saveGame() {
		System.out.println("beginSave complete");
		IFunctions iFunctions = gGame.iFunctions;
		iFunctions.putFastBool("data "+GAME_ID, false);
		
		iFunctions.putFastFloat("timeRemain "+GAME_ID, timeRemain);
		iFunctions.putFastFloat("totalTime "+GAME_ID, totalTime);
		iFunctions.putFastFloat("eclapsedTime "+GAME_ID, eclapsedTime);
		iFunctions.putFastFloat("gameTime "+GAME_ID, gameTime);
		iFunctions.putFastFloat("levelScore "+GAME_ID, levelScore);
		iFunctions.putFastFloat("targetScore "+GAME_ID, targetScore);
		iFunctions.putFastInt("level "+GAME_ID, gameLevel);
		iFunctions.putFastInt("coins "+GAME_ID, addCoins);
		
		
		// trang thai state
		iFunctions.putFastInt("state "+GAME_ID, stateGame);
		gamePhase[stateGame].save(iFunctions);
		// save trang thai ban
		String data = "";
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) 
				data += "|"+grid[i][j];
		data = data.substring(1);
		iFunctions.putFastString("grid "+GAME_ID, data);
		// save gia tri ban
		data = "";
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) 
				data += "|"+inGridFlag[i][j];
		data = data.substring(1);
		iFunctions.putFastString("flag "+GAME_ID, data);
		
		data = "";
		for (int i = 0; i < 8; i++)
			data += "|"+colHeight[i];
		data = data.substring(1);
		iFunctions.putFastString("heights "+GAME_ID, data);
		
		data = "";
		for (int i = 0; i < 8; i++)
			data += "|"+fallingNum[i];
		data = data.substring(1);
		iFunctions.putFastString("falling "+GAME_ID, data);
		// save roi
		fall.save(iFunctions);
		
		// save effect
		logic.save(iFunctions);
		// save logic
		// save diem + level
		
		// save items
		for (int i = 0; i < skillArrs.length; i++) {
			data = "";
			if (skillArrs[i].type != Skill.NONE)
				data += skillArrs[i].type+"|"+skillArrs[i].count;
			iFunctions.putFastString("skill "+i+" "+GAME_ID, data);
		}
		iFunctions.putFastBool("only Level "+GAME_ID, false);
		iFunctions.putFastBool("data "+GAME_ID, true);
		System.out.println("save complete");
	}
	
	public boolean parseGame() {
		IFunctions iFunctions = gGame.iFunctions;
		if (!iFunctions.getFastBool("data "+GAME_ID, false)) return false;
		
		timeRemain = iFunctions.getFastFloat("timeRemain "+ GAME_ID, 0);
		totalTime = iFunctions.getFastFloat("totalTime "+GAME_ID, totalTime);
		eclapsedTime = iFunctions.getFastFloat("eclapsedTime "+GAME_ID, eclapsedTime);
		gameTime = iFunctions.getFastFloat("gameTime "+GAME_ID, gameTime);
		
		levelScore = iFunctions.getFastFloat("levelScore " + GAME_ID, 0);
		runningScore = levelScore;
		targetScore = iFunctions.getFastFloat("targetScore " + GAME_ID, 0);
		gameLevel = iFunctions.getFastInt("level " + GAME_ID, 0);
		addCoins = iFunctions.getFastInt("coins "+ GAME_ID, 0);
		String data ="";
		// save items
		skillArrs = new Skill[4];

		for (int i = 0; i < skillArrs.length; i++) {
			data = iFunctions.getFastString("skill " + i + " " + GAME_ID, "");
			if (data != "") {
				String split[] = data.split("\\|");
				skillArrs[i] = new Skill(Integer.parseInt(split[0]), gGame,
						null);
				skillArrs[i].count = Integer.parseInt(split[1]);
				if (skillArrs[i].type == Skill.COMBO)
					combo.setDurationCombo(2 * 2 * CaculateCombo.DEFAULT_LIMIT);
			} else
				skillArrs[i] = new Skill(Skill.NONE, gGame, new Rectangle(-1,
						-1, -1, -1));

		}

		if (skillArrs[0] != null)
			skillArrs[0].setCollisionBound(new Rectangle(35, 7, 94, 94));
		if (skillArrs[1] != null)
			skillArrs[1].setCollisionBound(new Rectangle(144, 7, 94, 94));
		if (skillArrs[2] != null)
			skillArrs[2].setCollisionBound(new Rectangle(251, 7, 94, 94));
		if (skillArrs[3] != null)
			skillArrs[3].setCollisionBound(new Rectangle(358, 7, 94, 94));

		int total = 0;
		for (int i = 0; i < 4; i++) {
			if (skillArrs[i].type == Skill.NONE)
				total++;
		}
		if (total == 4) {
			canShowAd = 1;
		} else
			canShowAd = 0;
		
		
		if (iFunctions.getFastBool("only Level "+GAME_ID, false)) {
			iFunctions.putFastBool("only Level "+GAME_ID, false);
			iFunctions.putFastBool("data " + GAME_ID, false);
			stateGame = DIAMOND_ANIMATION;
			gamePhase[stateGame].setState(Phase.ON_BEGIN);
			System.out.println("state in only Level "+stateGame);
			return true;
		}
		
		System.out.println("stateGame "+stateGame+" "+gamePhase[stateGame]);
		// save trang thai ban
		data = iFunctions.getFastString("grid " + GAME_ID, "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					grid[i][j] = Integer.parseInt(split[i * 8 + j]);
					if (grid[i][j] != -1) {
						diamonds.get(i * 8 + j).setDiamondValue(grid[i][j]);
						diamonds.get(i * 8 + j).setAction(Diamond.REST);
					}
				}
		}

		data = iFunctions.getFastString("flag " + GAME_ID, "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++)
					inGridFlag[i][j] = Integer.parseInt(split[i * 8 + j]);
		}

		data = iFunctions.getFastString("heights " + GAME_ID, "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < 8; i++) {
				colHeight[i] = Integer.parseInt(split[i]);
				System.out.println("height "+i+" "+colHeight[i]);
			}
		}

		data = iFunctions.getFastString("falling " + GAME_ID, "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < 8; i++) {
				fallingNum[i] = Integer.parseInt(split[i]);
				System.out.println("fallingNum "+i+" "+fallingNum[i]);
			}
		}
		
		stateGame = iFunctions.getFastInt("state " + GAME_ID, 0);
		gamePhase[stateGame].parse(iFunctions);
		// save roi
		fall.parse(iFunctions);

		// save effect
		logic.parse(iFunctions);

		iFunctions.putFastBool("data " + GAME_ID, false);
		return true;
	}
	
	
	/*******************************MathFunctions********************************/
	
	protected float CordXOfCell(int cell, int width, int height) {
		return CordXOfCell(cell / 8, cell % 8, width, height);
	}
	
	protected float CordYOfCell(int cell, int width, int height) {
		return CordYOfCell(cell / 8, cell % 8, width, height);
	}
	
	protected float CordXOfCell(int row, int col, int width, int height) {
		float result = 0;
		result = gridPos.x + col * width + width / 2;
		return result;
	}
	
	protected float CordYOfCell(int row, int col, int width, int height) {
		float result = 0;
		result = gridPos.y + row * height + height / 2;
		
		return result;
	}
	
	public boolean canShowAd() {
		return canShowAd > 0;
	}
	
	public abstract String getName();
}
