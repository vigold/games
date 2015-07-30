package vn.sunnet.qplay.diamondlink.butterflydiamond;

import java.util.ArrayList;
import java.util.Random;




import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.LoadingDialog;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;


import vn.sunnet.game.electro.rooms.ElectroRoomInfo;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.classicdiamond.ClassicGeneration;
import vn.sunnet.qplay.diamondlink.classicdiamond.FirstClassic;
import vn.sunnet.qplay.diamondlink.classicdiamond.SecondClassic;
import vn.sunnet.qplay.diamondlink.classicdiamond.ThirdClassic;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.graphiceffects.Glaze;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.logiceffects.TempEffect;
import vn.sunnet.qplay.diamondlink.math.BezierConfig;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.CaculateCombo;
import vn.sunnet.qplay.diamondlink.modules.DiamondAI;
import vn.sunnet.qplay.diamondlink.modules.FallModule;
import vn.sunnet.qplay.diamondlink.modules.FindSolution;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.modules.GenerateXScore;
import vn.sunnet.qplay.diamondlink.phases.DiamondChange;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.screens.groups.ButterflySummary;

import vn.sunnet.qplay.diamondlink.screens.groups.ButterflyRunning;
import vn.sunnet.qplay.diamondlink.screens.groups.FrontStage;
import vn.sunnet.qplay.diamondlink.screens.groups.LevelSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.Pause;
import vn.sunnet.qplay.diamondlink.screens.groups.RanksSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.Running;
import vn.sunnet.qplay.diamondlink.screens.groups.ScoreSummary;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.tweens.GameObjectAccessor;
import vn.sunnet.qplay.diamondlink.ui.ParticleEffectActor;

import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;
import vn.sunnet.qplay.diamondlink.utils.Actions;
import vn.sunnet.qplay.diamondlink.utils.Fields;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenPath;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;



public class ButterflyDiamond extends GameScreen implements InputProcessor{
	
	public Sound butterflyAppear;
	public Sound butterflyCatched;
	public Sound butterflyCured;
	private Sound press;
	private Sound levelUp;
	private Music gameMusic;
	private Music getReady;
	private Music curMusic;
	
	private Music levelUpMusic;
	private Music overMusic;
	private Music rankUpMusic;
	private Sound bravo;
	private Music timeWarning;
	private int plusPercentExp;
	private int plusPercentCoin;
	private int plusExp;
	private int plusCoin;
	private PooledEffect firework;
	
	private BitmapFont nameFont;
	private BitmapFont butterflyFont;
//	private BitmapFont timeFont;
	private BitmapFont coinFont;
	private Texture background;
	
	private AtlasRegion gridTop;
	private AtlasRegion gridBottom;
	private AtlasRegion avatarFrame;
	private AtlasRegion topFrame;
	private AtlasRegion bottomFrame;
	private AtlasRegion timeFrame;
	private AtlasRegion missionFrame;
	private AtlasRegion[] gDiamonds;
	private AtlasRegion gRCThunder;
	private AtlasRegion gExplode;
	private AtlasRegion gChainThunder;
	private AtlasRegion gCoin;
	private ObjectMap<String, TextureRegion> gRegions;



	
//	private Running runningUI;
	private ButterflySummary scoreSummary;
	private LevelSummary levelSummary;
	private RanksSummary ranksSummary;
//	private Pause pause;
	
	private final String gItemNames[] = {
			"Bomb","Hummer","FiveColor","Auger",
	};

	private int coins;
	private float remainTime;
	private float runningScore = 0;
	private float eclapsedTime = 0;

	
	public TextureRegion spiderRegion;
	public ArrayList<IDiamond> butterflyOut = new ArrayList<IDiamond>();
	public boolean beginLevel = false;
	public Spider spider;
	public Vector2 park = new Vector2(55 , 740 );
	//26 + 80 * 3, 12 + 20, 72, 72
	public int butterflyNum;
	public int butterflyStep;

	private AtlasRegion butterflySymbol;
	private TextureRegion label;
	
	public ButterflyDiamond (int width, int height, DiamondLink pGame) {
		super(width, height, pGame);
		// TODO Auto-generated constructor stub
		GAME_ID = GameScreen.BUTTERFLY_DIAMOND;
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
//		gridPos = new Vector3(240 - 4 * DIAMOND_WIDTH, 204, 0);
		
		gridPos = new Vector3(0, 153 + 15, 0);
		fallPos = new Vector3(240 - 4 * DIAMOND_WIDTH, DiamondLink.HEIGHT, 0);
		gridTable = new Rectangle(gridPos.x, gridPos.y, 8 * DIAMOND_WIDTH, 8 * DIAMOND_HEIGHT);
	}
	
	public void initPhases() {
		MAX_PHASE = 4;
		if (gamePhase != null) gamePhase = null;
		gamePhase = new Phase[MAX_PHASE];
		gamePhase[0] = new FirstButterfly(this);
		gamePhase[1] = new SecondButterfly(this);
		gamePhase[2] = new ThirdButterfly(this);
		gamePhase[3] = new DiamondChange(this);
	}
	
	public void initModules() {
		if (logic != null) logic = null;
		logic = new ButterflyLogic(this);
		if (fall != null) fall = null;
		fall = new ButterflyFall(this);
		if (generate != null) generate = null;
		generate = new ClassicGeneration(this);
		if (ai != null) ai = null;
		ai = new DiamondAI(this);
		solution = new FindSolution(this);
		combo = new CaculateCombo(this);
		xScore = new GenerateXScore(this);
	}
	
	@Override
	public boolean isOverTime() {
		// TODO Auto-generated method stub
		return spider.state == Spider.SPIDER_CATCH;
	};
	
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
		if (total == 4) canShowAd = 1;
		else canShowAd = 0;
	}
	
	@Override
	protected void saveGame() {
		// TODO Auto-generated method stub
		super.saveGame();
		gGame.iFunctions.putFastBool("beginLevel "+GAME_ID, beginLevel);
		gGame.iFunctions.putFastInt("butterflyNum "+GAME_ID, butterflyNum);
		gGame.iFunctions.putFastInt("butterflyStep "+GAME_ID, butterflyStep);
		// save Position
		String data1 = "";
		String data2 = "";
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) 
			{
				int value = inGridFlag[i][j];
				DiamondOfButterfly diamond = (DiamondOfButterfly) diamonds.get(i * 8 + j);
				if (Operator.getBit(Effect.ONE_ASPECT_SWAP, value) > 0) {
					data1 += "|" + (i * 8 + j) + "|"
							+ diamond.getDiamondValue() + "|"
							+ diamond.getSourX() + "|" + diamond.getSourY()
							+ "|" + diamond.getPosX() + "|" + diamond.getPosY()
							+ "|" + diamond.getDesX() + "|" + diamond.getDesY()+"|"+diamond.getUpStep();

				}
				
				if (Operator.getBit(Effect.UP_TO_GRID, value) > 0) {
					data2 += "|" + (i * 8 + j) + "|"
							+ diamond.getDiamondValue() + "|"
							+ diamond.getSourX() + "|" + diamond.getSourY()
							+ "|" + diamond.getPosX() + "|" + diamond.getPosY()
							+ "|" + diamond.getDesX() + "|" + diamond.getDesY()+"|"+diamond.getUpStep();
				}
			}
		
		if (data1 != "") data1 = data1.substring(1);
		if (data2 != "") data2 = data2.substring(1);
		
		gGame.iFunctions.putFastString("one_aspect_swaps "+GAME_ID, data1);
		gGame.iFunctions.putFastString("up_to_grids "+GAME_ID, data2);
		spider.save(gGame.iFunctions);
		// save 
	}
	
	@Override
	public boolean parseGame() {
		boolean result = super.parseGame();
		if (result) {
//    		depth = gGame.iFunctions.getInt("meters "+GAME_ID, 0);
			beginLevel = gGame.iFunctions.getFastBool("beginLevel "+GAME_ID, false);
			butterflyNum = gGame.iFunctions.getFastInt("butterflyNum "+GAME_ID, 0);
			butterflyStep = gGame.iFunctions.getFastInt("butterflyStep "+GAME_ID, 0);
			// save Position
			String data = gGame.iFunctions.getFastString("one_aspect_swaps "+GAME_ID, "");
			if (data != "") {
				String split[] = data.split("\\|");
				for (int i = 0; i < split.length / 9; i++) {
					int cell = Integer.parseInt(split[i * 9]);
					int dValue = Integer.parseInt(split[i * 9 + 1]);
					float sourX = Float.parseFloat(split[i * 9 + 2]);
					float sourY = Float.parseFloat(split[ i * 9 + 3]);
					float posX = Float.parseFloat(split[i * 9 + 4]);
					float posY = Float.parseFloat(split[ i * 9 + 5]);
					float desX = Float.parseFloat(split[i  * 9 + 6]);
					float desY = Float.parseFloat(split[i * 9 + 7]);
					int step = Integer.parseInt(split[i * 9 + 8]);
					DiamondOfButterfly diamond = (DiamondOfButterfly) diamonds.get(cell);
					diamond.setDiamondValue(dValue);
					diamond.setSource(sourX, sourY);
					diamond.setCenterPosition(posX, posY);
					diamond.setDestination(desX, desY);
					diamond.setUpStep(step);
					diamond.setAction(Diamond.ONE_ASPECT_SWAP);
				}
			}
			
			data = gGame.iFunctions.getFastString("up_to_grids "+GAME_ID, "");
			if (data != "") {
				String split[] = data.split("\\|");
				for (int i = 0; i < split.length / 9; i++) {
					int cell = Integer.parseInt(split[i * 9]);
					int dValue = Integer.parseInt(split[i * 9 + 1]);
					float sourX = Float.parseFloat(split[i * 9 + 2]);
					float sourY = Float.parseFloat(split[ i * 9 + 3]);
					float posX = Float.parseFloat(split[i * 9 + 4]);
					float posY = Float.parseFloat(split[ i * 9 + 5]);
					float desX = Float.parseFloat(split[i  * 9 + 6]);
					float desY = Float.parseFloat(split[i * 9 + 7]);
					int step = Integer.parseInt(split[i * 9 + 8]);
					DiamondOfButterfly diamond = (DiamondOfButterfly) diamonds.get(cell);
					diamond.setDiamondValue(dValue);
					diamond.setSource(sourX, sourY);
					diamond.setCenterPosition(posX, posY);
					diamond.setDestination(desX, desY);
					diamond.setUpStep(step);
					diamond.setAction(Diamond.UP_TO_GRID);
				}
			}
			spider.parse(gGame.iFunctions);
		}
		return result;
	}
	
	public void initLevel() {
		
		if (preStep == GAME_OVER) {
			gameLevel = 0;
			levelScore = 0;
			totalScore = 0;
			runningScore = 0;
			addCoins = 0;
		}
		moveSystem.killAll();
		multiplexer.clear();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(frontStage);
		butterflyOut.clear();
		for (int i = 0; i < MAX_PHASE; i++)
			gamePhase[i].onReset();
		selection = -1;
		questSaveScore = false;
		totalTime = 0;
		levelScore = 0;
		shareComplete = false;
		timeLevel = 90;
		beginLevel = true;
		spider = new Spider(0, 800 + 80, 80, 80, this);
//		lastFriendsRank = PlayerInfo.rankInFriends;
		eclapsedTime = 0;
		targetScore = (gameLevel + 1) * 1000 + 500 * gameLevel;
		remainTime = timeLevel;
		float x = 0 , y = 0; 
		MyAnimation animation = null;
		IDiamond diamond = null;
		for (int i = 0 ; i < 8 ; i++) {
			colHeight[i] = 0;
			fallingNum[i] = 0;
			fixedNum[i] = 0;
		}
		
		butterflyNum = 0;
		butterflyStep = 1;
		
//		gridBuf = generate.generateAll();
		checkCellEffect.clear();
		diamonds.clear();
		fall.onCreated();
 		logic.onCreated();
 		ai.onCreated();
 		solution.onCreated();
 		xScore.onCreated();
 		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++) {
				
//				animation = gGame.getAssets().getDiamondAnimation(gridBuf[i][j], getGameID(), Assets.frameDuration);
				diamond = logic.allocateDiamond(CordXOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT), CordYOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT), DIAMOND_WIDTH, DIAMOND_HEIGHT, this);
				
				diamond.setDestination(CordXOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT), CordYOfCell(i, j, DIAMOND_WIDTH, DIAMOND_HEIGHT));
				
				diamond.setDiamondValue(0);
//				diamond.setSprite(animation);
				diamond.setAction(Diamond.FALL);
				diamonds.add(diamond);
				inGridFlag[i][j] = 0;
				colHeight[j] = 0;
				fixedNum[j] = colHeight[j];
				grid[i][j] = -1;
			}	
 		
// 		for (int i = 0 ; i < 8 ; i++)
//			for (int j = 0 ; j < 8 ; j++) {
// 			diamonds.get(i * 8 + j).setAction(Diamond.REST);
// 			grid[i][j] = diamonds.get(i * 8 + j).getDiamondValue();
// 			inGridFlag[i][j] = Operator.onBit(Effect.FIXED_POS, inGridFlag[i][j]);
// 			colHeight[j] = 8;
// 		}
		
//		stateGame = DIAMOND_ANIMATION;
// 		spider.addButterfly(3);
//		stateGame = DIAMOND_REST;
//		gamePhase[stateGame].setState(Phase.ON_BEGIN);
		initGame = false;
		
		if (preStep == GAME_OVER) {
			if (!parseGame()) {
				stateGame = DIAMOND_ANIMATION;
				gamePhase[stateGame].setState(Phase.ON_BEGIN);
			}
		} else {
			stateGame = DIAMOND_ANIMATION;
			gamePhase[stateGame].setState(Phase.ON_BEGIN);
		}
		runningUI.updateContent(runningScore);
//		runningUI.updateTargetScore(targetScore);
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
						setStepGame(GameScreen.GAME_PAUSED);
						pause.show(frontStage.getRoot());
					}
				});
			}
		});
	}
	
	private void resetInterfaces() {
		runningUI.reset();
		((FrontStage)frontStage).reset();
		runningUI.updateContent(0);
		runningUI.updateContent(avatars);
		runningScore  = 0;
	}
	
	private void resetFrontStage() {
		((FrontStage)frontStage).reset();
	}
	
	private void initRanksSummary() {
		// TODO Auto-generated method stub
//		ranksSummary = new RanksSummary(gGame, this, new ClickListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				Assets.playSound(press);
//				return true;
//			}
//
//			@Override
//			public void touchUp(InputEvent arg0, float arg1, float arg2,
//					int arg3, int arg4) {
//				postMessageToWall();
//			}
//		}, new ClickListener() {
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
//				hideRanksSummary();
//				float exp = (float) PlayerInfo.exp;
//				float addExp = assets.getExpOfScore(levelScore);
//				if (upLevel(exp, addExp + plusExp)) {
//					Assets.pauseMusic(curMusic);
//					curMusic = levelUpMusic;
//					Assets.playMusic(curMusic);
//
//					Assets.playSound(bravo);
//
//					setStepGame(GameScreen.GAME_SUMMARY);
//					showLevelSummary();
//				} else
//					gGame.setScreen(gGame.getMenu());
//			}
//
//		});
	}
	
	private void showRanksSummary() {
		ranksSummary.show(stage.getRoot());
	}
	
	private void hideRanksSummary() {
		ranksSummary.hide();
	}

	private void initLevelSummary() {
//		levelSummary = new LevelSummary(gGame, this, new ClickListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y,
//					int pointer, int button) {
//				Assets.playSound(press);
//				return true;
//			}
//
//			@Override
//			public void touchUp(InputEvent arg0, float arg1, float arg2,
//					int arg3, int arg4) {
//				postMessageToWall();
//
//			}
//		}, new InputListener() {
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
//				hideLevelSummary();
//				gGame.setScreen(gGame.getMenu());
//			}
//		});
	}
	
	private void showLevelSummary() {
		levelSummary.show(stage.getRoot());
	}
	
	private void hideLevelSummary() {
		levelSummary.hide();
	}

	private void initScoreSummary() {
		// TODO Auto-generated method stub
		scoreSummary = new ButterflySummary(gGame, this, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				hideScoreSummary();
				
				gGame.iFunctions.showInterstitial();
				PlayerInfo.coin += addCoins;
				if (generate.isPercentGold()) PlayerInfo.coin += Math.max(1, addCoins / 10);
				gGame.iFunctions.putInt("coins", PlayerInfo.coin);
				gGame.setScreen(gGame.getLogin());

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
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				hideScoreSummary();
				gGame.iFunctions.showInterstitial();
				PlayerInfo.coin += addCoins;
				if (generate.isPercentGold()) PlayerInfo.coin += Math.max(1, addCoins / 10);
				gGame.iFunctions.putInt("coins", PlayerInfo.coin);
				gGame.setScreen(gGame.getLogin());
				
			}
		});
	}
	
	
	private void showScoreSummary() {
		scoreSummary.show(stage.getRoot());
	}
	
	private void hideScoreSummary() {
		scoreSummary.hide();
	}

	private void initLevelEndInterface() {
		// TODO Auto-generated method stub
		
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
						setStepGame(preStep);
						multiplexer.addProcessor(stage);
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
//						hideLevelSummary();
//						hideRanksSummary();
//						hideScoreSummary();
//						saveGame();
						autoSave(new Runnable() {
							
							@Override
							public void run() {
								gGame.setScreen(gGame.getLogin());
								gGame.iFunctions.showInterstitial();
							}
						});
						
					}
				});
			}
		}, null, null, null, null);
	}

	private void initRunningInterface() {
		// TODO Auto-generated method stub
		runningUI = new ButterflyRunning(gGame, this, new ClickListener() {
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
//							pause.show(frontStage.getRoot());
							setGamePaused();
							Assets.pauseMusic(curMusic);
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
		TextureAtlas lAtlas = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		comboFont = manager.get(UIAssets.COMBO_FONT, BitmapFont.class);
		diamondFont = manager.get(UIAssets.DIAMOND_FONT, BitmapFont.class);
		spiderRegion = lAtlas.findRegion("Spider");
		butterflySymbol = lAtlas.findRegion("ButterflySymbol");
		hintSymbol = new MyAnimation(0.2f, lAtlas.findRegions("Hint"), Animation.PlayMode.LOOP);
		gSelection = lAtlas.findRegion("Selection");
		
//		nameFont = manager.get(UIAssets.GAME_NAME_FONT, BitmapFont.class);
//		scoreFont = manager.get(UIAssets.GAME_SCORE_FONT, BitmapFont.class);
//		timeFont = manager.get(UIAssets.GAME_TIME_FONT, BitmapFont.class);
		coinFont = manager.get(UIAssets.COIN_FONT, BitmapFont.class);
		
		gRegions = new ObjectMap<String, TextureRegion>();
		for (int i = 0 ; i < gItemNames.length; i++) {
			TextureRegion region = lAtlas.findRegion(gItemNames[i]);
			gRegions.put(gItemNames[i], region);
		}
		

		
		
		gCoin = lAtlas.findRegion("Coin");
		firework = assets.getParticleEffect(ParticleAssets.FIREWORK);
		glaze = new Glaze(this, manager.get(Assets.GLAZE_SHADER,
				ShaderProgram.class));
		glaze.setParams((float)Math.sqrt(2 * DIAMOND_WIDTH * DIAMOND_WIDTH) * 8, 45,
				gridPos.x, gridPos.y + 8 * DIAMOND_HEIGHT);
		lAtlas = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		label = lAtlas.findRegion("NumTab");
		
	}

	@Override
	public void initAudio() {
		// TODO Auto-generated method stub
		getReady = gGame.getAssetManager().get(MusicAssets.GET_READY_MUSIC, Music.class);
		press = gGame.getAssetManager().get(SoundAssets.PRESS_SOUND, Sound.class);
		levelUp = gGame.getAssetManager().get(SoundAssets.LEVELUP_SOUND, Sound.class);
		gameMusic = gGame.getAssetManager().get(MusicAssets.BUTTERFLY_MUSIC, Music.class);
		gameMusic.setLooping(true);
		gameMusic.setVolume(DiamondLink.musicVolume);
		levelUpMusic = manager.get(MusicAssets.LEVEL_UP_MUSIC, Music.class);
		levelUpMusic.setLooping(true);
		overMusic = manager.get(MusicAssets.OVER_MUSIC, Music.class);
		overMusic.setLooping(true);
//		rankUpMusic = manager.get(MusicAssets.RANK_UP_MUSIC, Music.class);
//		rankUpMusic.setLooping(true);
		bravo = manager.get(SoundAssets.BRAVO_SOUND, Sound.class);
		timeWarning = manager.get(MusicAssets.TIME_WARNING_MUSIC, Music.class);
		timeWarning.setLooping(true);
		
		butterflyAppear = manager.get(SoundAssets.BUTTERFLY_APPEAR, Sound.class);
		butterflyCatched = manager.get(SoundAssets.BUTTERFLY_CATCHED, Sound.class);
		butterflyCured = manager.get(SoundAssets.BUTTERFLY_CURED, Sound.class);
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
		
		FreeTypeFontGenerator generator = manager.get(UIAssets.ARIAL_GENERATOR, FreeTypeFontGenerator.class);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		butterflyFont = generator.generateFont(parameter);
//		butterflyFont = generator.generateFont(30, AbstractScreen.FONT_CHARACTERS, false);
		butterflyFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		curMusic = getReady;
		
		runningUI.setTouchable(Touchable.enabled);
		super.show();
	}
	
	
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Assets.pauseMusic(curMusic);
		butterflyFont.dispose();
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Assets.pauseMusic(curMusic);
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
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	/******************************UpdateGame**********************************/
	
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		gCamera.update();
		batch.setProjectionMatrix(gCamera.combined);
		switch (curStep) {
			case GAME_READY: updateReady(deltaTime); break;
			case GAME_RUNNING: updateRunning(deltaTime); break;
			case GAME_PAUSED: updatePaused(deltaTime); break;
			case GAME_LEVEL_END: updateLevelEnd(deltaTime); break;
			case GAME_OVER: updateOver(deltaTime); break;
			case GAME_SUMMARY: updateSummary(deltaTime); break;
			case GAME_RANKING: updateRanking(deltaTime); break;
		}
		if (curStep != GAME_PAUSED)
			moveSystem.update(deltaTime);
		stage.act(deltaTime);
		frontStage.act(deltaTime);
	}
	
	private void updateOverUI() {
		scoreSummary.updateContent(levelScore / 100, plusPercentExp, addCoins,
				plusPercentCoin, levelScore, PlayerInfo.getBestScore(gGame.iFunctions, 1), butterflyNum, combo.getTotalCombo(), combo.getComboMax());
		PlayerInfo.updateBestScore(gGame.iFunctions, 1, levelScore);
	}
	
	private void updateRankingUI() {
//		ranksSummary.updateContent(levelScore, PlayerInfo.nickName, PlayerInfo.rankInWorld, lastFriendsRank);
	}
	
	private void updateRunningUI() {
		runningUI.updateContent(runningScore);
		runningUI.updateContent(timeRemain, (addCoins), timeRemain / timeLevel);
	}
	
	private void updateSummaryUI() {
//		levelSummary.updateContent((float) PlayerInfo.exp, assets.getExpOfScore(levelScore));
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
		((FrontStage)frontStage).beginAction(new Command() {
			
			@Override
			public void execute(Object data) {
				// TODO Auto-generated method stub
				eclapsedTime = 0;
				gTouchMode = TOUCH_GAME;
				setStepGame(GAME_RUNNING);
				curMusic = gameMusic;
				Assets.playMusic(curMusic);
			}
		});
	}
	
	private void endAction() {
		((FrontStage)frontStage).endAction();
	}
	
	private void lastFeverAction() {
		((FrontStage)frontStage).lastFeverAction();
	}
	
	public void updateReady(float deltaTime) {
		if (eclapsedTime == 0) {
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
//			setStepGame(GAME_READY);
		} 
	}
	
	public void updateRunning(float deltaTime) {
		updateRunningUI();
		if (logic.SpecialEffect  == 0)
		remainTime = Math.max(remainTime - deltaTime, 0);
		
		totalTime += deltaTime;
		
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
		
		if (vip != null) {
			vip.update(deltaTime);
			if (vip.id == VipCard.BLACK_TORTOISE && !isOverTime()) {
				boolean isEffect = false;
				int countEffect = 0;
				int lastRow = 0;
				int lastCol = 0;
				for (int i = 0; i < 64; i++) {
					Diamond diamond = (Diamond) diamonds.get(i);
					if (diamond.isCanActive()) {
						if (diamond.getActiveTime() == 0) {
							int row = i / 8;
							int col = i % 8;
							if (certainCell(inGridFlag[row][col])) {
								generateEffect(row, col, true);
								isEffect = true;
								countEffect++;
								lastRow = row;
								lastCol = col;
							}
						}
					}
				}
				if (isEffect) {
					if (stateGame == DIAMOND_REST) {
						gamePhase[stateGame]
								.setBranch(GameScreen.DIAMOND_ANIMATION);
						gamePhase[stateGame].setState(Phase.ON_END);
						logic.state = GameLogic.ON_RUNNING;
						logic.update(deltaTime);
					} else if (stateGame == DIAMOND_ANIMATION) {
						gamePhase[stateGame].setState(Phase.ON_RUNNING);
						logic.state = GameLogic.ON_RUNNING;
						logic.update(deltaTime);
					}
				}
			}
		}
//		xScore.update(deltaTime);
		combo.update(deltaTime);
		
		spider.update(deltaTime);
		
		runningScore = Interpolation.linear.apply(runningScore, levelScore, 0.1f);
		
		if (isOverTime()) {
			gTouchMode = TOUCH_UI;
			if (eclapsedTime == 0) {
				endAction();
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
							showScoreSummary();
							updateOverUI();
							setStepGame(GAME_SUMMARY);
							//gGame.setScreen(gGame.getLogin());
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
				} else {
//					System.out.println("++++++++++++++++++++++++++"+logic.state+" "+logic.saveFirst+ " "+logic.first+" "+checkCellEffect.size());
//					for (IEffect effect: checkCellEffect) {
//						System.out.println("Con effect tai "+effect.getSource(0)+" loai "+effect.getType()+" "+effect.getStepEffect()+" "+effect.isFinished());
//					}
				}
		}
	}
	
	
	
	/**********************************DrawGame********************************/
	
	
	public void presentRunning(float deltaTime) {
		drawDiamondTable(deltaTime);
	}
	
	private void drawDiamondTable(float delta) {
		batch.begin();
		batch.setColor(1f, 1f, 1f, 1f);
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
		
		if (curStep != GAME_LEVEL_END && curStep != GAME_READY) {
			switch (stateGame) {
			case DIAMOND_REST:
				gamePhase[0].draw(delta);
	//			coinFont.draw(batch, "Rest "+(butterflyOut.size() > 0), 480 / 2, 800 / 2);
				break;
			case DIAMOND_MOVE:
				gamePhase[1].draw(delta);
	//			coinFont.draw(batch, "Move "+(butterflyOut.size() > 0), 480 / 2, 800 / 2);
				break;
			case DIAMOND_ANIMATION:
				gamePhase[2].draw(delta);
	//			coinFont.draw(batch, "Animation "+(butterflyOut.size() > 0) + gamePhase[2].getState(), 480 / 2, 800 / 2);
				break;
			case DIAMOND_CHANGE:
				gamePhase[3].draw(delta);
	//			coinFont.draw(batch, "Change "+(butterflyOut.size() > 0), 480 / 2, 800 / 2);
			}
	
			solution.draw(delta);
			
			TextBounds bounds = butterflyFont.getBounds(""+butterflyNum);
			butterflyFont.draw(batch, ""+butterflyNum, park.x - bounds.width / 2, park.y + 1.5f * bounds.height);
			
			
			batch.setColor(1f, 1f, 1f, 1f);
			spider.draw(delta);
			
	//		for (int i = 0 ; i < 8 ; i++) {
	//			String str = ""+fallingNum[i];
	//			coinFont.draw(batch, str, gridPos.x + i * DIAMOND_WIDTH + 15, gridPos.y + gridTable.height + DIAMOND_HEIGHT);
	//			str = ""+ colHeight[i];
	//			coinFont.draw(batch, str, gridPos.x + i * DIAMOND_WIDTH + 15, gridPos.y - DIAMOND_HEIGHT / 2);
	//		}
			
			if (curSkill != null) {
				curSkill.draw(batch, delta);
			}
		}
		batch.end();
	}
	
	
	
	public void presentLevelEnd(float deltaTime) {
		presentRunning(deltaTime);
	}
	
	
	@Override
	public void presentOver(float deltaTime) {
		
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
//		batch.begin();
//		if (firework.isComplete()) {
//			firework.reset();
//			firework.setPosition(MathUtils.random(30, 426), MathUtils.random(
//					DiamondLink.HEIGHT / 2, DiamondLink.HEIGHT));
//			
//		}
//		firework.draw(batch, deltaTime);
//		batch.end();
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
//	private float CordXOfCell(int row, int col, int width, int height) {
//		float result = 0;
//		result = gridPos.x + col * width + width / 2;
//		
//		return result;
//	}
//	
//	private float CordYOfCell(int row, int col, int width, int height) {
//		float result = 0;
//		result = gridPos.y + row * height + height / 2;
//		
//		return result;
//	}

	/********************************Network Methods***************************/

	
//	private void receiveSummary(final EsObject esObject) {
//		Gdx.app.postRunnable(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				gGame.iFunctions.putInt("sendScoreSucces", 1);
//				EsObject myRankInWorld = esObject.getEsObject(Fields.MYRANKINFO);
//				
//				if (esObject.variableExists(Fields.RANKS_IN_FRIENDS)) {
//					lastFriendsRank = esObject.getLong(Fields.RANKS_IN_FRIENDS);
//				}
//				
//				System.out.println("Receive Sever->Client score = "+myRankInWorld.getLong(Fields.BESTSCORE));
//				
//				PlayerInfo.updateRankInWorld(myRankInWorld);
//				updateRankingUI();
//				updateOverUI();
//				updateSummaryUI();
//				showScoreSummary();
//				setStepGame(GAME_OVER);
//				
//				runningScore = 0;
//				eclapsedTime = 0;
//				
//				Assets.pauseMusic(curMusic);
//				curMusic = overMusic;
//				Assets.playMusic(curMusic);
//			}
//		});
//	}
	
	private void sendSummary() {
		
//		createToast("Kết nối "+esAdapter.isConnected(), 2f);
		
//		plusExp = 0;
//		plusPercentExp = 0;
//		plusPercentCoin = 0;
//		plusCoin = 0;
//		if (avatars != null) {
//			for (int i = 0; i < avatars.length; i++)
//				if (avatars[i] != null)
//					if (avatars[i].name.equals("Gaara")) {
//						plusExp += ((Integer) avatars[i].effectParams[0] / 100f)
//								* assets.getExpOfScore(levelScore);
//						plusPercentExp = (Integer) avatars[i].effectParams[0];
//					} else if (avatars[i].name.equals("Sasuke")) {
//						plusExp += ((Integer) avatars[i].effectParams[0] / 100f)
//								* assets.getExpOfScore(levelScore);
//						plusPercentExp = (Integer) avatars[i].effectParams[0];
//					} else if (avatars[i].name.equals("Naruto")) {
//						plusExp += ((Integer) avatars[i].effectParams[0] / 100f)
//								* assets.getExpOfScore(levelScore);
//						plusPercentExp = (Integer) avatars[i].effectParams[0];
//					} else if (avatars[i].name.equals("Naruto")) {
//						plusExp += ((Integer) avatars[i].effectParams[0] / 100f)
//								* assets.getExpOfScore(levelScore);
//						plusPercentExp = (Integer) avatars[i].effectParams[0];
//					}
//		}
//		
//		if (avatars != null) {
//			for (int i = 0; i < avatars.length; i++)
//				if (avatars[i] != null)
//					if (avatars[i].name.equals("Bakura")) {
//						plusCoin += ((Integer) avatars[i].effectParams[0] / 100f)
//								* addCoins;
//						plusPercentCoin = (Integer) avatars[i].effectParams[0];
//					} else if (avatars[i].name.equals("Kaiba")) {
//						plusCoin += ((Integer) avatars[i].effectParams[0] / 100f)
//								* addCoins;
//						plusPercentCoin = (Integer) avatars[i].effectParams[0];
//					} else if (avatars[i].name.equals("Yugi")) {
//						plusCoin += ((Integer) avatars[i].effectParams[0] / 100f)
//								* addCoins;
//						plusPercentCoin = (Integer) avatars[i].effectParams[0];
//					}
//		}
//
//		showLoadingDialog("Gửi điểm", new Runnable() {
//
//			@Override
//			public void run() {
//
//				reconnectToSever(new Runnable() {
//
//					@Override
//					public void run() {
//						Gdx.app.postRunnable(new Runnable() {
//
//							@Override
//							public void run() {
//								gGame.setScreen(gGame.getMenu());
//							}
//						});
//					}
//				}, new Runnable() {
//
//					@Override
//					public void run() {
//						createDialog("Lỗi", "Mất kết nối máy chủ",
//								new ButtonDescription("Tiếp tục",
//										new Command() {
//
//											@Override
//											public void execute(Object data) {
//												// TODO Auto-generated method
//												// stub
//												gGame.setScreen(gGame
//														.getLogin());
//											}
//										}), null, null, null);
//					}
//				});
//			}
//		}, LoadingDialog.NEED_TIME_OUT, 15);
//		EsObject msg = new EsObject();
//		msg.setString(Fields.ACTION, Actions.UPDATE_SUMMARY);
//		msg.setLong(Fields.BESTSCORE, (long) levelScore);
//		msg.setDouble(Fields.EXP, assets.getExpOfScore(levelScore) + plusExp);
//		if (gGame.iFunctions.getFriendsID() != null) {
//			msg.setStringArray(Fields.FACEFRIENDS, gGame.iFunctions.getFriendsID());
//		}
//		
//		if (levelScore > 400000)
//			msg.setInteger(Fields.MODES, MINE_DIAMOND + 1);
//		msg.setInteger(Fields.MODE_OF_SCORE, BUTTERFLY_DIAMOND);
//		// thuong neu co
//		int lastLevel = assets.getLevelOfExp((float) PlayerInfo.exp);
//		int curLevel = assets.getLevelOfExp((float) PlayerInfo.exp
//				+ assets.getExpOfScore(levelScore) + plusExp);
//		int addGem = 0;
//		if (curLevel > lastLevel) {
//			Gdx.app.log("test", "len cap" + curLevel);
//			addGem = curLevel - lastLevel;
//		}
////		addGem = 500;
//		Gdx.app.log("test", "Cong coin" + (addCoins + plusCoin));
//		msg.setLong(Fields.GEM, addGem);
//		msg.setLong(Fields.COIN, (long) (addCoins + plusCoin));
//
//		System.out.println("Send Client->Sever score = " + levelScore
//				+ " exp = " + (assets.getExpOfScore(levelScore) + plusExp)
//				+ " coin = " + (addCoins + plusCoin) + " gem =  " + addGem);
//
//		// luu lai neu chua gui duoc
//		gGame.iFunctions.putInt(Fields.BESTSCORE, (int) levelScore);
//		gGame.iFunctions.putInt(Fields.EXP,
//				(int) (assets.getExpOfScore(levelScore) + plusExp));
//		gGame.iFunctions.putInt(Fields.GEM, (int) addGem);
//		gGame.iFunctions.putInt(Fields.COIN, (int) (addCoins + plusCoin));
//		gGame.iFunctions.putInt("sendScoreSucces", 0);
//		if (levelScore > 400000)
//			gGame.iFunctions.putInt(Fields.MODES, MINE_DIAMOND + 1);
//		gGame.iFunctions.putInt(Fields.MODE_OF_SCORE, BUTTERFLY_DIAMOND);
//		System.out.println("sendScoreSucess "
//				+ gGame.iFunctions.getInt("sendScoreSucces", 1));
//		nodeAdapter.requestNodePlugin(msg, "LoginPlugin");
	}
	
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
	
	public void showButterflyOutOfGrid(int color, float x, float y) {
		String name = null;
		Array<AtlasRegion> regions = null;
		switch (color) {
		case Diamond.RED:
			name = (ParticleAssets.RED_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		case Diamond.GREEN:
			name = (ParticleAssets.GREEN_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		case Diamond.BLUE:
			name = (ParticleAssets.BLUE_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		case Diamond.YELLOW:
			name = (ParticleAssets.YEALLOW_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		case Diamond.ORANGE:
			name = (ParticleAssets.ORANGE_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		case Diamond.WHITE:
			name = (ParticleAssets.WHITE_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		case Diamond.PINK:
			name = (ParticleAssets.PINK_BUTTERFLY);
			regions = assets.getDiamondRegions(Diamond.BUTTERFLY_DIAMOND * COLOR_NUM + color);
			break;
		}
		Vector2 point1 = null;
		Vector2 point2 = null;
		if (x > 240) {
			point1 = new Vector2(120, 800 - 60);
			point2 = new Vector2(120, 800 - 60 - 360);
		} else {
			point1 = new Vector2(360, 800 - 60);
			point2 = new Vector2(360, 800 - 60 - 360);
		}
		
		final SpriteSheet sheet = new SpriteSheet(0.05f, regions, SpriteSheet.LOOP);
//		final ParticleEffectAndBMSActor effect = new ParticleEffectAndBMSActor(
//				name, Assets.BUT_BMS, Assets.ATLAS, "NewAnimation", this);
		sheet.setBounds(x - sheet.getPrefWidth() / 2, y - sheet.getPrefHeight()
				/ 2, sheet.getPrefWidth(), sheet.getPrefHeight());
//		effect.setPosition(x, y);
//		frontStage.addActor(effect);
		frontStage.addActor(sheet);
		Random random = new Random();
		float relativeX = 2 * DIAMOND_WIDTH * (1 - random.nextInt(2) * 2);
		float relativeY = 2 * DIAMOND_HEIGHT;
		relativeX = (x + relativeX > DiamondLink.WIDTH? -relativeX : relativeX);
		relativeX = (x + relativeX < 0? -relativeX : relativeX);
		relativeY = (y + relativeY > DiamondLink.HEIGHT? -relativeY : relativeY);
		relativeY = (y + relativeY < 0? -relativeY : relativeY);
		Timeline.createSequence()
				.push(Tween.to(sheet, ActorAccessor.CPOS_XY, 1f)
						.targetRelative(relativeX, relativeY)
						.ease(Quad.INOUT))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						sheet.setFrameDuration(0.025f);
					}
				}))
				.push(Tween.to(sheet, ActorAccessor.CPOS_XY, 2f)
						.target(park.x, park.y).ease(Linear.INOUT)
						.waypoint(point1.x, point1.y)
						.waypoint(point2.x, point2.y)
						.path(TweenPaths.catmullRom))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						sheet.setFrameDuration(0.05f);
					}
				}))
				.push(Tween.to(sheet, ActorAccessor.OPACITY, 1f).target(0)
						.ease(Linear.INOUT))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						sheet.remove();
						moveSystem.killTarget(sheet);
						butterflyNum++;
					}
				})).start(moveSystem);
//		Timeline.createSequence()
//				.push(Tween.to(effect, ActorAccessor.POS_XY, 1f)
//						.targetRelative(relativeX, relativeY)
//						.ease(Quad.INOUT))
//				.push(Tween.to(effect, ActorAccessor.POS_XY, 2f)
//						.target(park.x, park.y).ease(Linear.INOUT)
//						.waypoint(point1.x, point1.y)
//						.waypoint(point2.x, point2.y)
//						.path(TweenPaths.catmullRom))
//				.push(Tween.call(new TweenCallback() {
//
//					@Override
//					public void onEvent(int type, BaseTween<?> source) {
//						// TODO Auto-generated method stub
//						effect.remove();
//						moveSystem.killTarget(effect);
//					}
//				})).start(moveSystem);
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
	public boolean keyDown(int keyCode) {
		// TODO Auto-generated method stub
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
	
	private void generateEffect(int cell) {
		generateEffect(cell / 8, cell % 8);
	}
	
	private void generateEffect(int row, int col) {
		int value = grid[row][col];
		int dType = logic.diamondType(value);
		int dColor = logic.diamondColor(value);
		switch (dType) {
		case IDiamond.FIVE_COLOR_DIAMOND:
			logic.newEffect(row, col, Effect.CHAIN_THUNDER, null);
			break;
		case IDiamond.BLINK_DIAMOND:
			logic.newEffect(row, col, Effect.ROW_COL_THUNDER, null);
			break;
		case IDiamond.RT_DIAMOND:
			logic.newEffect(row, col, Effect.ROW_THUNDER, null);
			break;
		case IDiamond.CT_DIAMOND:
			logic.newEffect(row, col, Effect.COL_THUNDER, null);
			break;
		case IDiamond.FIRE_DIAMOND:
			logic.newEffect(row, col, Effect.EXPLODE, null);
			break;
		}
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

	@Override
	public void playMusic() {
		// TODO Auto-generated method stub
		if (curMusic == getReady)
			Assets.playMusic(curMusic, curMusic.getVolume());
		else
			Assets.playMusic(curMusic);
	}

	@Override
	public void pauseMusic() {
		// TODO Auto-generated method stub
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
						setStepGame(preStep);
						if (canShowAd > 0) canShowAd = 1;
						multiplexer.addProcessor(stage);
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
//						hideLevelSummary();
//						hideRanksSummary();
//						hideScoreSummary();
						autoSave(new Runnable() {
							
							@Override
							public void run() {
								gGame.setScreen(gGame.getLogin());
								gGame.iFunctions.showInterstitial();
							}
						});
//						saveGame();
						
					}
				});
			}
		});
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ButterflyDiamond";
	}

	@Override
	public boolean isDangerous() {
		for (int i = 63; i > 63 - 16; i--) {
			if (diamonds.get(i).getDiamondType() == Diamond.BUTTERFLY_DIAMOND) return true;
		}
		return false;
	}
	
}
