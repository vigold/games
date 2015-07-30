package vn.sunnet.qplay.diamondlink.classicdiamond;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;



import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.LoadingDialog;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libgdx.screens.Toast;


import vn.sunnet.game.electro.rooms.ElectroRoomInfo;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.MapAssets;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.graphiceffects.Glaze;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.items.Item;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.loaders.ShopDescription;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.modules.CaculateCombo;
import vn.sunnet.qplay.diamondlink.modules.DiamondAI;
import vn.sunnet.qplay.diamondlink.modules.FallModule;
import vn.sunnet.qplay.diamondlink.modules.FindSolution;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.modules.GenerateXScore;
import vn.sunnet.qplay.diamondlink.phases.DiamondChange;
import vn.sunnet.qplay.diamondlink.phases.GemAI;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.screens.groups.ClassicSummary;

import vn.sunnet.qplay.diamondlink.screens.groups.ClassicRunning;
import vn.sunnet.qplay.diamondlink.screens.groups.FrontStage;
import vn.sunnet.qplay.diamondlink.screens.groups.LevelSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.Pause;
import vn.sunnet.qplay.diamondlink.screens.groups.RanksSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.Running;
import vn.sunnet.qplay.diamondlink.screens.groups.ScoreSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.WorldList;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.tweens.GameObjectAccessor;
import vn.sunnet.qplay.diamondlink.ui.MyImage;
import vn.sunnet.qplay.diamondlink.ui.ParticleEffectActor;
import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;
import vn.sunnet.qplay.diamondlink.utils.Actions;
import vn.sunnet.qplay.diamondlink.utils.CheckExistAdapter;
import vn.sunnet.qplay.diamondlink.utils.CheckExistEventListener;
import vn.sunnet.qplay.diamondlink.utils.Fields;
import vn.sunnet.qplay.diamondlink.utils.SubmitScoreAdapter;
import vn.sunnet.qplay.diamondlink.utils.SubmitScoreEventListener;






import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenPath;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;


public class ClassicDiamond extends GameScreen implements InputProcessor{
	
	private Sound bravo;
	private Sound press;
	private Sound levelUp;
	private Music gameMusic;
	private Music getReady;
	private Music timeWarning;
	private Music curMusic;
	
	private Music levelUpMusic;
	private Music rankUpMusic;
	private Music overMusic;
	
	private BitmapFont coinFont;

	
	private ClassicSummary scoreSummaryUI;
	private LevelSummary levelSummaryUI;
	private RanksSummary ranksSummaryUI;
//	private Pause pauseUI;
	
	int plusPercentExp = 0;
	int plusPercentCoin = 0;
	float plusExp = 0;
	float plusCoin = 0;
//	private BitmapFont scoreFont;
	private TextureRegion label;
	
	
	public ClassicDiamond(int width, int height, DiamondLink pGame) {
		super(width, height, pGame);
		GAME_ID = GameScreen.CLASSIC_DIAMOND;
		multiplexer.addProcessor(this);
	}
	
	/******************************InitGame***********************************/

	public void initParamsGame() {
		gameLevel = 0;
		levelScore = 0;
		totalScore = 0;
		TYPE_NUM = 18;
		COLOR_NUM = 7;
		
		DIAMOND_WIDTH = Diamond.DIAMOND_WIDTH;
		DIAMOND_HEIGHT = Diamond.DIAMOND_HEIGHT;
		gridPos = new Vector3(0, 195, 0);
		
		
		fallPos = new Vector3(240 - 4 * DIAMOND_WIDTH, DiamondLink.HEIGHT, 0);
		gridTable = new Rectangle(gridPos.x, gridPos.y, 8 * DIAMOND_WIDTH, 8 * DIAMOND_HEIGHT);
		
	}
	
	public void initPhases() {
		MAX_PHASE = 4;
		if (gamePhase != null) gamePhase = null;
		gamePhase = new Phase[MAX_PHASE];
		gamePhase[0] = new FirstClassic(this);
		gamePhase[1] = new SecondClassic(this);
		gamePhase[2] = new ThirdClassic(this);
		gamePhase[3] = new DiamondChange(this);
	}
	
	public void initModules() {
		logic = new GameLogic(this);
		fall = new FallModule(this);
		generate = new ClassicGeneration(this);
		ai = new DiamondAI(this);
		solution = new FindSolution(this);
		combo = new CaculateCombo(this);
		xScore = new GenerateXScore(this);
	}
	
	@Override
	public boolean isOverTime() {
		// TODO Auto-generated method stub
		return timeRemain == 0;
	};
	
	public void initLevel() {
		questSaveScore = false;
		multiplexer.clear();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(frontStage);
		
		if (preStep == GAME_OVER) {
			gameLevel = 0;
			levelScore = 0;
			totalScore = 0;
			totalTime = 0;
			targetScore = Math.round(MapAssets.CLASSIC_TARGETS[gameLevel + 1] / 10);
		}
		runningScore = levelScore;
		addCoins = 0;
		eatClock = 0;
//		shareComplete = false;
		gameTime = 0;
		for (int i = 0; i < MAX_PHASE; i++)
			gamePhase[i].onReset();
		timeLevel = 90;
		eclapsedTime = 0;
		timeRemain = timeLevel;
		
		float x = 0 , y = 0; 
		MyAnimation animation = null;
		IDiamond diamond = null;
		for (int i = 0 ; i < 8 ; i++) {
			colHeight[i] = 0;
			fallingNum[i] = 0;
			fixedNum[i] = 0;
		}
//		gridBuf = generate.generateAll();
		checkCellEffect.clear();
		diamonds.clear();
		fall.onCreated();
 		logic.onCreated();
 		ai.onCreated();
 		solution.onCreated();
 		xScore.onCreated();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				diamond = logic.allocateDiamond(
						CordXOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT),
						CordYOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT),
						DIAMOND_WIDTH, DIAMOND_HEIGHT, this);
				diamond.setDestination(
						CordXOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT),
						CordYOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT));
				diamond.setDiamondValue(0);
				diamond.setAction(Diamond.FALL);
				diamonds.add(diamond);
				inGridFlag[i][j] = 0;
				colHeight[j] = 0;
				fixedNum[j] = colHeight[j];
				grid[i][j] = -1;
			}
 		
// 		gridBuf[0][0] = Diamond.HYPER_CUBE * 7 + 0;
// 		gridBuf[0][1] = Diamond.LASER_DIAMOND * 7 + 0;
//		
// 		gridBuf[3][3] = Diamond.LASER_DIAMOND * 7 + 1;
// 		gridBuf[3][4] = 0;
//		
// 		gridBuf[6][6] = Diamond.LASER_DIAMOND * 7 + 1;
// 		gridBuf[6][7] = 0;
//		
//		for (int i = 0; i < 8; i++)
//			for (int j = 0; j < 8; j++) {
//				diamond = logic.allocateDiamond(
//						CordXOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT),
//						CordYOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT),
//						DIAMOND_WIDTH, DIAMOND_HEIGHT, this);
//				diamond.setDestination(
//						CordXOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT),
//						CordYOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT));
//				diamond.setDiamondValue(gridBuf[i][j]);
//				diamond.setAction(Diamond.REST);
//				diamonds.add(diamond);
//				inGridFlag[i][j] = Operator.onBit(Effect.FIXED_POS, inGridFlag[i][j]);
//				colHeight[j] = 8;
//				fixedNum[j] = colHeight[j];
//				grid[i][j] = gridBuf[i][j];
//			}
		
		
		initGame = false;
		if (preStep == GAME_OVER) {
		if (!parseGame()) 
			{
				stateGame = DIAMOND_ANIMATION;
				gamePhase[stateGame].setState(Phase.ON_BEGIN);
			}
		} else 
		{
			stateGame = DIAMOND_ANIMATION;
			gamePhase[stateGame].setState(Phase.ON_BEGIN);
		}
		runningUI.updateContent(runningScore);
		runningUI.updateTargetScore(targetScore);
	}
	
	@Override
	public void sendSkills(Skill[] skills) {
		// TODO Auto-generated method stub
		super.sendSkills(skills);
		if (skillArrs[0] != null)
			skillArrs[0].setCollisionBound(new Rectangle(35, 7, 94, 94));
		if (skillArrs[1] != null)
			skillArrs[1].setCollisionBound(new Rectangle(144, 7, 94, 94));
		if (skillArrs[2] != null)
			skillArrs[2].setCollisionBound(new Rectangle(251, 7, 94, 94));
		if (skillArrs[3] != null) 
			skillArrs[3].setCollisionBound(new Rectangle(358, 7, 94, 94));
		for (int i = 0; i < skillArrs.length; i++) {
			if (skillArrs[i] == null) {
//				System.out.println("bang null++++++++++++++++++++++++++++++++++++++++++++"+ i);
				skillArrs[i] = new Skill(Skill.NONE, gGame, new Rectangle(-1, -1,
						-1, -1));
			} else {
				if (skillArrs[i].type == Skill.COMBO) combo.setDurationCombo(2 * 2 * CaculateCombo.DEFAULT_LIMIT);
			}
		}
		
		int total = 0;
		for (int i = 0; i < 4; i++) {
			if (skillArrs[i].type == Skill.NONE) total++;
		}
		if (total == 4) {
			canShowAd = 1;
		} else canShowAd = 0;
	}
	
	public void initInterface() {
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		itemPoint = new Vector3();
		curPoint = new Vector3();
		lastPoint = new Vector3();
		
		initRunningInterface();
		initPauseInterface();
		initScoreSummary();
		
		initFrontStage();
		runningUI.updateContent(avatars);
		
	}
	
	private void initFrontStage() {
		
		frontStage = new FrontStage(gGame, this, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				// TODO Auto-generated method stub
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
//						setStepGame(GameScreen.GAME_PAUSED);
//						pauseUI.show(frontStage.getRoot());
						setGamePaused();
					}
				});
			}
		});
	}
	
	private void resetInterfaces() {
		runningUI.reset();
		((FrontStage)frontStage).reset();
		runningUI.updateContent(avatars);
		runningUI.updateContent(0);
		runningScore  = 0;
		
	}
	
	private void resetFrontStage() {
		((FrontStage)frontStage).reset();
	}
	
	
	
	private void showRanksSummary() {
		ranksSummaryUI.show(stage.getRoot());
	}
	
	private void hideRanksSummary() {
		ranksSummaryUI.hide();
	}
	
	private void showLevelSummary() {
		levelSummaryUI.show(stage.getRoot());
	}
	
	private void hideLevelSummary() {
		levelSummaryUI.hide();
	}
	
	

	private void initScoreSummary() {
		scoreSummaryUI = new ClassicSummary(gGame, this, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
//				postMessageToWall();
				hideScoreSummary();
				
				if (levelScore < targetScore) {
					PlayerInfo.coin += addCoins;
					if (generate.isPercentGold()) PlayerInfo.coin += Math.max(1, addCoins / 10);
					
					gGame.iFunctions.putInt("coins", PlayerInfo.coin);
					gGame.setScreen(gGame.getLogin());
					gGame.iFunctions.showInterstitial();
					return;
				}
				
				if (generate.isPercentGold()) addCoins += Math.max(1, addCoins / 10);
				
				for (int i = Math.min(MapAssets.CLASSIC_TARGETS.length - 1, gameLevel); i < MapAssets.CLASSIC_TARGETS.length; i++) {
					if (levelScore < Math.round(MapAssets.CLASSIC_TARGETS[i] / 10) || i == MapAssets.CLASSIC_TARGETS.length - 1) {
						targetScore = Math.round(MapAssets.CLASSIC_TARGETS[i] / 10);
						gameLevel = i - 1;
						break;
					}
				}
				
				
				
				manualSave(new Runnable() {
					
					@Override
					public void run() {
						gGame.iFunctions.showInterstitial();
						gGame.setScreen(gGame.getLogin());
					}
				});
				
			}
		}, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				hideScoreSummary();
				PlayerInfo.coin += addCoins;
				if (generate.isPercentGold()) PlayerInfo.coin += Math.max(1, addCoins / 10);
				
				gGame.iFunctions.putInt("coins", PlayerInfo.coin);
				if (levelScore < targetScore) {
					gGame.setScreen(gGame.getLogin());
					gGame.iFunctions.showInterstitial();
				} else {
					for (int i = Math.min(MapAssets.CLASSIC_TARGETS.length - 1, gameLevel); i < MapAssets.CLASSIC_TARGETS.length; i++) {
						if (levelScore < Math.round(MapAssets.CLASSIC_TARGETS[i] / 10) || i == MapAssets.CLASSIC_TARGETS.length - 1) {
							targetScore = Math.round(MapAssets.CLASSIC_TARGETS[i] / 10);
							gameLevel = i - 1;
							break;
						}
					}
					initLevel();
					initGame = false;
					eclapsedTime = 0;
					setStepGame(GAME_READY);
				}
				
			}
		});
//		showScoreSummary();
	}
	
	
	private void showScoreSummary() {
		scoreSummaryUI.show(stage.getRoot());
	}
	
	private void hideScoreSummary() {
		scoreSummaryUI.hide();
	}

	private void initPauseInterface() {
		// TODO Auto-generated method stub
		pause = new Pause(gGame, this, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				// TODO Auto-generated method stub
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pause.hide();
						multiplexer.addProcessor(stage);
						setStepGame(preStep);
						
						if (curMusic == getReady)
							Assets.playMusic(curMusic, curMusic.getVolume());
						else
							Assets.playMusic(curMusic);
					}
				});
			}
		}, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				// TODO Auto-generated method stub
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pause.hide();
						//hideLevelSummary();
						//hideRanksSummary();
						//hideScoreSummary();
						
//						saveGame();
//						gGame.setScreen(gGame.getLogin());
//						gGame.iFunctions.showInterstitial();
						autoSave(new Runnable() {
							
							@Override
							public void run() {
								gGame.setScreen(gGame.getLogin());
								gGame.iFunctions.showInterstitial();
								System.out.println("save end");
							}
						});
					}
				});
			}
		}, null, null, null, null);
	}

	private void initRunningInterface() {
		// TODO Auto-generated method stub
		runningUI = new ClassicRunning(gGame, this, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				// TODO Auto-generated method stub
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (curStep != GAME_PAUSED && curStep != GAME_READY) {
//							setStepGame(GameScreen.GAME_PAUSED);
							multiplexer.removeProcessor(stage);
//							pauseUI.show(frontStage.getRoot());
							setGamePaused();
							Assets.pauseMusic(curMusic);
							Assets.pauseMusic(timeWarning);
							gGame.iFunctions.hideAdView();
						}
					}
				});
			}
		});
		runningUI.show(stage.getRoot());
	}
	
	

	@Override
	public void initTextures() {
		// TODO Auto-generated method stub
		TextureAtlas lAtlas = gGame.getAssetManager().get(UIAssets.GAME_FG, TextureAtlas.class);
		
		comboFont = manager.get(UIAssets.COMBO_FONT, BitmapFont.class);
		diamondFont = manager.get(UIAssets.DIAMOND_FONT, BitmapFont.class);
		hintSymbol = new MyAnimation(0.2f, lAtlas.findRegions("Hint"), Animation.PlayMode.LOOP);
		gSelection = lAtlas.findRegion("Selection");
		
		coinFont = gGame.getAssetManager().get(UIAssets.COIN_FONT, BitmapFont.class);
		
		
		
		glaze = new Glaze(this, manager.get(Assets.GLAZE_SHADER,
				ShaderProgram.class));
		glaze.setParams((float)Math.sqrt(2 * DIAMOND_WIDTH * DIAMOND_WIDTH) * 8, 45,
				gridPos.x, gridPos.y + 8 * DIAMOND_HEIGHT);
		lAtlas = gGame.getAssetManager().get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		label = lAtlas.findRegion("NumTab");

	}

	@Override
	public void initAudio() {
		// TODO Auto-generated method stub
		getReady = gGame.getAssetManager().get(MusicAssets.GET_READY_MUSIC, Music.class);
		press = gGame.getAssetManager().get(SoundAssets.PRESS_SOUND, Sound.class);
		levelUp = gGame.getAssetManager().get(SoundAssets.LEVELUP_SOUND, Sound.class);
		gameMusic = gGame.getAssetManager().get(MusicAssets.CLASSIC_MUSIC, Music.class);
		gameMusic.setLooping(true);
		gameMusic.setVolume(DiamondLink.musicVolume);
		levelUpMusic = manager.get(MusicAssets.LEVEL_UP_MUSIC, Music.class);
		levelUpMusic.setLooping(true);
		overMusic = manager.get(MusicAssets.OVER_MUSIC, Music.class);
		overMusic.setLooping(true);
		bravo = manager.get(SoundAssets.BRAVO_SOUND, Sound.class);
		timeWarning = manager.get(MusicAssets.TIME_WARNING_MUSIC, Music.class);
		timeWarning.setLooping(true);
	}
	
	/******************************Ligdx Lifecycle Methods*********************/
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(multiplexer);
		setProcessorAfterDismissingLoading(multiplexer);
		if (firstCreated) {
			initAudio();
			initTextures();
			initInterface();
			firstCreated = false;

		} else {
			resetInterfaces();
		} 
		
		
		
		curMusic = getReady;
		System.out.println("fuck"+runningUI.getTouchable());
		runningUI.setTouchable(Touchable.enabled);
		super.show();
	}
	
	private void showDiscountCard() {
//		discountCard = null;
//		System.out.println("cai gi the");
//		if (vip == null) {
//			if (MathUtils.random(0, 3) == 0) 
//			{
//				System.out.println("the cao");
//				discountCard = new DiscountCard(gGame, this, new ClickListener() {
//					@Override
//					public boolean touchDown(InputEvent event, float x, float y,
//							int pointer, int button) {
//						Assets.playSound(press);
//						return true;
//					}
//	
//					@Override
//					public void touchUp(InputEvent arg0, float arg1, float arg2,
//							int arg3, int arg4) {
//						discount();
//						
//					}
//				}, new ClickListener() {
//					@Override
//					public boolean touchDown(InputEvent event, float x, float y,
//							int pointer, int button) {
//						Assets.playSound(press);
//						return true;
//					}
//	
//					@Override
//					public void touchUp(InputEvent arg0, float arg1, float arg2,
//							int arg3, int arg4) {
//						discountCard.hide();
//						Gdx.app.postRunnable(new Runnable() {
//							
//							@Override
//							public void run() {
//								discountCard = null;
//								eclapsedTime = 0;
//								setStepGame(GAME_READY);
//							}
//						});
//						
//					}
//				});
//				discountCard.show(frontStage.getRoot());
//			} else {
//				System.out.println(" ko the cao 1");
//				eclapsedTime = 0;
//				setStepGame(GAME_READY);
//			}
//		} else {
//			System.out.println("ko the cao 2");
//			eclapsedTime = 0;
//			setStepGame(GAME_READY);
//		}
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Assets.pauseMusic(curMusic);
		Assets.pauseMusic(timeWarning);

	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Assets.pauseMusic(curMusic);
		Assets.pauseMusic(timeWarning);
		if (curStep != GAME_PAUSED && curStep != GAME_OVER && curStep != GAME_SUMMARY)
		setGamePaused();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		if (curMusic == getReady)
			Assets.playMusic(curMusic, curMusic.getVolume());
		else
			Assets.playMusic(curMusic);
	}
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		gCamera.update();
		batch.setProjectionMatrix(gCamera.combined);
//		System.out.println("curStep"+curStep);
		switch (curStep) {
			case GAME_READY: updateReady(deltaTime); break;
			case GAME_RUNNING: updateRunning(deltaTime); break;
			case GAME_PAUSED: updatePaused(deltaTime); break;
			case GAME_LEVEL_END: updateLevelEnd(deltaTime); break;
			case GAME_OVER: updateOver(deltaTime); break;
			case GAME_SUMMARY: updateSummary(deltaTime); break;
			case GAME_RANKING: updateRanking(deltaTime); break;
		}
//		System.out.println("step = "+curStep+" preStep = "+preStep);
		if (curStep != GAME_PAUSED)
			moveSystem.update(deltaTime);
		stage.act(deltaTime);
		frontStage.act(deltaTime);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	/******************************UpdateGame**********************************/
	
	private void updateOverUI() {
		scoreSummaryUI.updateContent(levelScore / 100, plusPercentExp, addCoins,
				plusPercentCoin, levelScore, PlayerInfo.getBestScore(gGame.iFunctions, 0), (int) gameTime, combo.getTotalCombo(), combo.getComboMax());
		PlayerInfo.updateBestScore(gGame.iFunctions, 0, levelScore);
	}
	
	private void updateRankingUI() {
//		ranksSummaryUI.updateContent(levelScore, PlayerInfo.nickName, PlayerInfo.rankInWorld, lastFriendsRank);
	}
	
	private void updateRunningUI() {
		runningUI.updateContent(runningScore);
		runningUI.updateContent(timeRemain, (addCoins), timeRemain / timeLevel);
		runningUI.updateTargetScore(targetScore);
	}
	
	private void updateSummaryUI() {
//		levelSummaryUI.updateContent((float) PlayerInfo.exp, assets.getExpOfScore(levelScore));
	}
	
	private void updateRanking(float deltaTime) {
		// TODO Auto-generated method stub
	}

	private void updateSummary(float deltaTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateOver(float deltaTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updatePaused(float deltaTime) {
	}
	
	private void beginAction() {
		System.out.println("beginAction");
		((FrontStage)frontStage).beginAction(new Command() {
			
			@Override
			public void execute(Object data) {
				// TODO Auto-generated method stub
				eclapsedTime = 0;
				gTouchMode = TOUCH_GAME;
				setStepGame(GAME_RUNNING);
				curMusic = gameMusic;
				Assets.playMusic(curMusic);
//				showNoMoreMove(null);
			}
		});
	}
	
	private void completeAction() {
		((FrontStage)frontStage).completeAction();
	}
	
	private void endAction() {
		((FrontStage)frontStage).endAction();
	}
	
	private void lastFeverAction() {
		((FrontStage)frontStage).lastFeverAction();
	}
	
	public void updateReady(float deltaTime) {
		if (eclapsedTime == 0) {
			System.out.println("BeginReady");
			curMusic = getReady;
			if (DiamondLink.MUSIC == 0) 
				Assets.playMusic(curMusic, 0);
			else Assets.playMusic(curMusic);
			beginAction();
		}
		eclapsedTime += deltaTime;
	}
	
	public void updateLevelEnd(float deltaTime) {
		eclapsedTime += deltaTime;
		if (preStep == GAME_OVER) {// lan dau tien khi vao choi
			eclapsedTime = 0;
			if (initGame) {
				initLevel();
				initGame = false;
				eclapsedTime = 0;
				setStepGame(GAME_READY);
//				showDiscountCard();
			}
			
		} 
	}
	
	public void updateRunning(float deltaTime) {
		updateRunningUI();
		if (logic.SpecialEffect  == 0)
			timeRemain = Math.max(timeRemain - deltaTime, 0);
		totalTime += deltaTime;
		gameTime += deltaTime;
		switch (stateGame) {
		case DIAMOND_REST:
			gamePhase[0].update(deltaTime);
			break;
		case DIAMOND_MOVE:
			gamePhase[1].update(deltaTime);
			break;
		case DIAMOND_ANIMATION:
			gamePhase[2].update(deltaTime);
			break;
		case DIAMOND_CHANGE:
			gamePhase[3].update(deltaTime);
			break;
		}
//		if (vip != null) {
//			vip.update(deltaTime);
//			if (vip.id == VipCard.BLACK_TORTOISE && !isOverTime()) {
//				boolean isEffect = false;
//				int countEffect = 0;
//				int lastRow = 0;
//				int lastCol = 0;
//				for (int i = 0; i < 64; i++) {
//					Diamond diamond = (Diamond) diamonds.get(i);
//					if (diamond.isCanActive()) {
//						if (diamond.getActiveTime() == 0) {
//							int row = i / 8;
//							int col = i % 8;
//							if (certainCell(inGridFlag[row][col])) {
//								generateEffect(row, col, true);
//								isEffect = true;
//								countEffect++;
//								lastRow = row;
//								lastCol = col;
//							}
//						}
//					}
//				}
//				if (isEffect) {
//					if (stateGame == DIAMOND_REST) {
//						gamePhase[stateGame]
//								.setBranch(GameScreen.DIAMOND_ANIMATION);
//						gamePhase[stateGame].setState(Phase.ON_END);
//						logic.state = GameLogic.ON_RUNNING;
//						logic.update(deltaTime);
//					} else if (stateGame == DIAMOND_ANIMATION) {
//						gamePhase[stateGame].setState(Phase.ON_RUNNING);
//						logic.state = GameLogic.ON_RUNNING;
//						logic.update(deltaTime);
//					}
//				}
//			}
//		}
		xScore.update(deltaTime);
		combo.update(deltaTime);
		
		boolean isChange = false;
//		System.out.println(runningScore+" "+levelScore);
		if (runningScore < levelScore) isChange = true;
		else isChange = false;
		
		runningScore = Interpolation.linear.apply(runningScore, levelScore, 0.1f);
		
		
		
		if (isOverTime() && eatClock == 0) {
			gTouchMode = TOUCH_UI;
			if (eclapsedTime == 0) {
				if (levelScore < targetScore)
					endAction();
				else completeAction();
			}
			eclapsedTime += deltaTime;
			if (eclapsedTime > 3f)
				if (!gamePhase[stateGame].isActive()) {
					int cell = getSpecialPosition();
					if (cell == -1) {
						// GameOver
						if (!questSaveScore) {
							runningUI.finish();
							((FrontStage) frontStage).finish();
							questSaveScore = true;
							setStepGame(GAME_SUMMARY);
							updateOverUI();
							showScoreSummary();
//							gGame.setScreen(gGame.getLogin());
						}
					} else {
						generateEffect(cell, false);
						if (stateGame == DIAMOND_REST) {
							gamePhase[stateGame]
									.setBranch(GameScreen.DIAMOND_ANIMATION);
							gamePhase[stateGame].setState(Phase.ON_END);
						} else if (stateGame == DIAMOND_ANIMATION) {
							gamePhase[stateGame].setState(Phase.ON_RUNNING);
							logic.state = GameLogic.ON_RUNNING;
						}
					}
				}
		}
	}
	
	public void presentRunning(float deltaTime) {
		drawDiamondTable(deltaTime);
	}
	
	private void drawDiamondTable(float delta) {
		batch.begin();
		batch.setColor(1f, 1f, 1f, 1f);
//		System.out.println("showAd"+canShowAd);
		if (canShowAd == 0)
		if (skillArrs != null)
		for (Skill skill : skillArrs) {
			skill.draw(batch, delta);
			if (skill.type != Skill.NONE)
//				if (skill.isExist()) 
				{
					coinFont.setColor(1f, 1f, 1f, 1f);
					batch.draw(label, skill.position.x - label.getRegionWidth()
							/ 2, skill.position.y - 72 / 2);

					coinFont.drawWrapped(batch, "" + skill.get(),
							skill.getBound().x, skill.getBound().y + 50 + 10

							, skill.getBound().width, HAlignment.CENTER);
				}
		}
		coinFont.setColor(1f, 1f, 1f, 1f);
		
		if (curStep != GAME_LEVEL_END && curStep != GAME_READY)
		{
			switch (stateGame) {
			case DIAMOND_REST:
				gamePhase[0].draw(delta);
				break;
			case DIAMOND_MOVE:
				gamePhase[1].draw(delta);
				break;
			case DIAMOND_ANIMATION:
				gamePhase[2].draw(delta);
				break;
			case DIAMOND_CHANGE:
				gamePhase[3].draw(delta);
			}
			
	//		for (int i = 0 ; i < 8 ; i++) {
	//			String str = ""+fallingNum[i];
	//			coinFont.draw(batch, str, gridPos.x + i * DIAMOND_WIDTH + 10, gridPos.y + gridTable.height + DIAMOND_HEIGHT);
	//			str = ""+ colHeight[i];
	//			coinFont.draw(batch, str, gridPos.x + i * DIAMOND_WIDTH + 10, gridPos.y - DIAMOND_HEIGHT);
	//		}
	
			solution.draw(delta);
			
			if (curSkill != null) {
				curSkill.draw(batch, delta);
			}
		}
        batch.end();
	}
	
	/**********************************DrawGame********************************/
	
	public void presentLevelEnd(float deltaTime) {
		presentRunning(deltaTime);
	}
	
	
	@Override
	public void presentOver(float deltaTime) {
//		batch.begin();
//		firework.draw(batch, deltaTime);
//		batch.end();
	}

	@Override
	public void presentPaused(float deltaTime) {
		presentRunning(deltaTime);
	}
	
	public void presentReady(float deltaTime) {
		presentRunning(deltaTime);
	}
	
	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setProjectionMatrix(gCamera.combined);
		batch.setProjectionMatrix(gCamera.combined);

		stage.draw();
		switch (curStep) {
			case GAME_READY: presentReady(deltaTime); break;
			case GAME_RUNNING: presentRunning(deltaTime); break;
			case GAME_PAUSED: presentPaused(deltaTime); break;
			case GAME_LEVEL_END: presentLevelEnd(deltaTime); break;
			case GAME_OVER: presentOver(deltaTime); break;	
			case GAME_SUMMARY: presentSummary(deltaTime); break;
			case GAME_RANKING: presentRanking(deltaTime); break;
		}
		
		
		
		batch.begin();
//		effect.draw(batch, deltaTime);
////		if (effect.isComplete()) effect.reset();
//		effect.setPosition(240, 400);
		coinFont.draw(batch, Gdx.graphics.getFramesPerSecond()+" f/s", 0, DiamondLink.HEIGHT);
		batch.end();
		
		frontStage.draw();
		if (canShowAd == 1) {
			gGame.iFunctions.hideAdView();
			gGame.iFunctions.showAdView(false);
			canShowAd++;
		}
	}

	private void presentRanking(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	private void presentSummary(float deltaTime) {
		// TODO Auto-generated method stub
	}

//	/*******************************MathFunctions********************************/
//	
//	private float CordXOfCell(int cell, int width, int height) {
//		return CordXOfCell(cell / 8, cell % 8, width, height);
//	}
//	
//	private float CordYOfCell(int cell, int width, int height) {
//		return CordYOfCell(cell / 8, cell % 8, width, height);
//	}
//	
//	protected float CordXOfCell(int row, int col, int width, int height) {
//		float result = 0;
//		result = gridPos.x + col * width + width / 2;
//		return result;
//	}
//	
//	protected float CordYOfCell(int row, int col, int width, int height) {
//		float result = 0;
//		result = gridPos.y + row * height + height / 2;
//		
//		return result;
//	}

	/********************************Network Methods***************************/


	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Keys.BACK || keyCode == Keys.ESCAPE) {
			Gdx.app.postRunnable(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (curStep != GAME_PAUSED && curStep != GAME_OVER && curStep != GAME_SUMMARY)
						setGamePaused();
					
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
	
	/******************************GameLogic Methods***************************/
	
	private int getSpecialPosition() {
		for (int i = 0; i < 64; i++) {
			if (canGenerateEffect(i)) return i;
		}
		return -1;
	}
	
	private boolean canGenerateEffect(int cell) {
		return canGenerateEffect(cell / 8, cell % 8);
	}
	
	private boolean canGenerateEffect(int row, int col) {
		int value = grid[row][col];
		int dType = logic.diamondType(value);
		int dColor = logic.diamondColor(value);

		return (dType == IDiamond.FIVE_COLOR_DIAMOND)
				|| (dType == IDiamond.BLINK_DIAMOND)
				|| (dType == IDiamond.RT_DIAMOND)
				|| (dType == IDiamond.CT_DIAMOND)
				|| (dType == IDiamond.HYPER_CUBE)
				|| (dType == IDiamond.LASER_DIAMOND);
	}
	
	private void generateEffect(int cell, boolean isVipActive) {
		generateEffect(cell / 8, cell % 8, isVipActive);
	}
	
	private void generateEffect(int row, int col, boolean isVipActive) {
		int value = grid[row][col];
		int dType = logic.diamondType(value);
		int dColor = logic.diamondColor(value);
		switch (dType) {
		case IDiamond.FIVE_COLOR_DIAMOND:
			logic.newEffect(row, col, Effect.CHAIN_THUNDER, null, isVipActive);
			break;
		case IDiamond.BLINK_DIAMOND:
			logic.newEffect(row, col, Effect.ROW_COL_THUNDER, null, isVipActive);
			break;
		case IDiamond.RT_DIAMOND:
			logic.newEffect(row, col, Effect.ROW_THUNDER, null, isVipActive);
			break;
		case IDiamond.CT_DIAMOND:
			logic.newEffect(row, col, Effect.COL_THUNDER, null, isVipActive);
			break;
		case IDiamond.FIRE_DIAMOND:
			logic.newEffect(row, col, Effect.EXPLODE, null, isVipActive);
			break;
		case IDiamond.HYPER_CUBE:
			logic.newEffect(row, col, Effect.EXTRA_CHAIN_THUNDER, null, isVipActive);
			break;
		case IDiamond.LASER_DIAMOND:
			logic.newEffect(row, col, Effect.CROSS_LASER, null, isVipActive);
			break;
		}
	}
	
	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public void setMusicVolume(float volume) {
		DiamondLink.musicVolume = volume;
		if (gameMusic != null) gameMusic.setVolume(DiamondLink.musicVolume);
	}
	
	private boolean upLevel(float exp, float addExp) {
		int lastLevel = assets.getLevelOfExp(exp);
		int curLevel = assets.getLevelOfExp(exp + addExp);
		return curLevel > lastLevel;
	}
	
	
	
	/**********************************NetWork*********************************/
	
	@Override
	public void sendVips(VipCard vip) {
		// TODO Auto-generated method stub
		super.sendVips(vip);
		if (vip != null)
		switch (vip.id) {
		case VipCard.RED_PHOENIX:
			combo.setDurationCombo(2 * CaculateCombo.DEFAULT_LIMIT);
			break;
		default:
			combo.setDurationCombo(CaculateCombo.DEFAULT_LIMIT);
			break;
		}
	}
	
	public void showItem(int add, final String about, float fromX, float fromY, float desX, float desY) {
		float timeMove = 1f;
		if (isOverTime()) return;
		TextureRegion region = null;
		ParticleEffectActor particle = null;
		Random random = new Random();
		if (about.equals("CoinDiamond")) {
			particle = new ParticleEffectActor(ParticleAssets.COIN_SPRAY, this);
		} else if (about.equals("ClockDiamond")) {
			eatClock++;
			particle = new ParticleEffectActor(ParticleAssets.TIME_SPRAY, this);
		} else if (about.equals("ClockItem")) {
			eatClock++;
			particle = new ParticleEffectActor(ParticleAssets.TIME_SPRAY, this);
		} 
		particle.setX(fromX); particle.setY(fromY);
		
		frontStage.addActor(particle);
		final ParticleEffectActor target = particle;
		
		((FrontStage) frontStage).showItem(new Vector2(fromX, fromY),
				new Vector2(desX, desY), particle, new Command() {

			@Override
			public void execute(Object data) {
				// TODO Auto-generated method stub
				if (about.equals("CoinDiamond")) {
					addCoins += 15;
					System.out.println(addCoins);
					runningUI.eatCoinAction();
				} else if (about.equals("ClockDiamond")) {
					timeRemain = Math.min(timeRemain + 5,  timeLevel);
					System.out.println(timeRemain);
					eatClock--;
					runningUI.eatTimeAction();
				} else if (about.equals("ClockItem")) {
					timeRemain = Math.min(timeRemain + 10,  timeLevel);
					System.out.println(timeRemain);
					eatClock--;
					runningUI.eatTimeAction();
				}
			}
		});
	}

	@Override
	public void playMusic() {
		if (curMusic == getReady)
			Assets.playMusic(curMusic, curMusic.getVolume());
		else
			Assets.playMusic(curMusic);
	}

	@Override
	public void pauseMusic() {
		Assets.pauseMusic(curMusic);
		Assets.pauseMusic(timeWarning);
	}

	@Override
	public void setGamePaused() {
		if (isSaving()) return;
		gGame.iFunctions.hideAdView();
		setStepGame(GameScreen.GAME_PAUSED);
		
		if (Gdx.app.getVersion() == 0)
			pause.show(frontStage.getRoot());
		else gGame.iFunctions.showPauseDialog(new Runnable() {
			
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pause.hide();
						multiplexer.addProcessor(stage);
						setStepGame(preStep);
						if (canShowAd > 0) canShowAd = 1;
						if (curMusic == getReady)
							Assets.playMusic(curMusic, curMusic.getVolume());
						else
							Assets.playMusic(curMusic);
					}
				});
			}
		}, new Runnable() {
			
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pause.hide();
						//hideLevelSummary();
						//hideRanksSummary();
						//hideScoreSummary();
//						saveGame();
						autoSave(new Runnable() {
							
							@Override
							public void run() {
								gGame.setScreen(gGame.getLogin());
								gGame.iFunctions.showInterstitial();
								System.out.println("save end");
							}
						});
						
						
					}
				});
			}
		});
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ClassicDiamond";
	}

	@Override
	public boolean isDangerous() {
		
		return timeRemain < 10;
	}
	
}
