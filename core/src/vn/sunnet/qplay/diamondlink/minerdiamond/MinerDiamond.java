package vn.sunnet.qplay.diamondlink.minerdiamond;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenPath;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.LoadingDialog;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;


import vn.sunnet.game.electro.rooms.ElectroRoomInfo;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.butterflydiamond.ButterflyFall;
import vn.sunnet.qplay.diamondlink.butterflydiamond.ButterflyLogic;
import vn.sunnet.qplay.diamondlink.butterflydiamond.Spider;
import vn.sunnet.qplay.diamondlink.classicdiamond.ClassicGeneration;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.graphiceffects.Glaze;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
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

import vn.sunnet.qplay.diamondlink.screens.groups.FrontStage;
import vn.sunnet.qplay.diamondlink.screens.groups.LevelSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.MinerRunning;
import vn.sunnet.qplay.diamondlink.screens.groups.MinerSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.Pause;
import vn.sunnet.qplay.diamondlink.screens.groups.RanksSummary;
import vn.sunnet.qplay.diamondlink.screens.groups.Running;
import vn.sunnet.qplay.diamondlink.screens.groups.ScoreSummary;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.tweens.GameObjectAccessor;
import vn.sunnet.qplay.diamondlink.ui.ParticleEffectActor;
import vn.sunnet.qplay.diamondlink.ui.StonePiece;
import vn.sunnet.qplay.diamondlink.utils.Actions;
import vn.sunnet.qplay.diamondlink.utils.Fields;



public class MinerDiamond extends GameScreen implements InputProcessor{
	
    public ArrayList<IDiamond> diamondButtom = new ArrayList<IDiamond>();  
    int depth = 0;
    float runningDepth = 0;
    
    public AtlasRegion soilUp;
	public AtlasRegion soilLeft;
	public AtlasRegion soilRight;
	
	Image chains[] = new Image[2];
    
	private Sound press;
	private Sound levelUp;
	private Music gameMusic;
	private Music getReady;
	private Music curMusic;
	
	private BitmapFont nameFont;
	private BitmapFont scoreFont;
	private BitmapFont timeFont;
	private BitmapFont coinFont;
	
//	private Running runningUI;
	private Group overGroup;
	private Group summaryGroup;
	private Group rankingGroup;
	private Music levelUpMusic;
	private Music overMusic;
	private Music rankUpMusic;
	private Sound bravo;
	private Music timeWarning;
	private RanksSummary ranksSummary;
	private LevelSummary levelSummary;
	private MinerSummary scoreSummary;
//	private Pause pause;
	protected float plusExp;
	private int plusPercentExp;
	private int plusPercentCoin;
	private int plusCoin;
	private AtlasRegion glass;
	private Rectangle scissors;
	private Rectangle clipBounds;
	
	public boolean canOver = false;

	public int gemNum = 0;
	private AtlasRegion label;
	

    public MinerDiamond(int width, int height,  DiamondLink pGame) {
		super(width, height, pGame);
		GAME_ID = GameScreen.MINE_DIAMOND;
		
		multiplexer.addProcessor(this);
		
		
		
	}
    
    /*******************************InitGame************************************/
    
    @Override
    protected void saveGame() {
    	gGame.iFunctions.putFastInt("meters "+GAME_ID, depth);
    	super.saveGame();
    }
    
    @Override
    public boolean parseGame() {
    	if (gGame.iFunctions.getFastBool("data "+GAME_ID, false))
    		depth = gGame.iFunctions.getFastInt("meters "+GAME_ID, 0); 
    	return super.parseGame();
    }

	public void initParamsGame() {
		gameLevel = 0;
		levelScore = 0;
		totalScore = 0;
		TYPE_NUM = 22;
		COLOR_NUM = 7;
		
		DIAMOND_WIDTH = Diamond.DIAMOND_WIDTH;
		DIAMOND_HEIGHT = Diamond.DIAMOND_HEIGHT;
		gridPos = new Vector3(240 - 4 * DIAMOND_WIDTH, 195, 0);
		
//		gridPos = new Vector3(0, 225, 0);
		fallPos = new Vector3(240 - 4 * DIAMOND_WIDTH, DiamondLink.HEIGHT, 0);
		gridTable = new Rectangle(gridPos.x, gridPos.y, 8 * DIAMOND_WIDTH, 8 * DIAMOND_HEIGHT);
		scissors = new Rectangle();
		clipBounds = new Rectangle(gridTable.x,gridTable.y,gridTable.width,gridTable.height);
	}
	
	public void initPhases() {
		MAX_PHASE = 5;
		gamePhase = new Phase[MAX_PHASE];
		gamePhase[0] = new FirstMiner(this);
		gamePhase[1] = new SecondMiner(this);
		gamePhase[2] = new ThirdMiner(this);
		gamePhase[3] = new DiamondChange(this);
		gamePhase[4] = new FourthMiner(this);
	}
	
	public void initModules() {
		logic = new MineLogic(this);
		fall = new FallModule(this);
		generate = new MinerGeneration(this);
		ai = new DiamondAI(this);
		solution = new FindSolution(this);
		combo = new CaculateCombo(this);
		xScore = new GenerateXScore(this);
	}
	
	@Override
	public void initTextures() {
		// TODO Auto-generated method stub
		TextureAtlas lAtlas = gGame.getAssetManager().get(UIAssets.GAME_FG, TextureAtlas.class);
		comboFont = manager.get(UIAssets.COMBO_FONT, BitmapFont.class);
		diamondFont = manager.get(UIAssets.DIAMOND_FONT, BitmapFont.class);
		gGridTable = lAtlas.findRegion("Grid");
		gSelection = lAtlas.findRegion("Selection");
		
//		chains[0] = new Image(lAtlas.findRegion("Chain" , 1));
//		chains[1] = new Image(lAtlas.findRegion("Chain", 1));
		
//		chains[0].setBounds(0, gridPos.y + 122,
//				chains[0].getPrefWidth(), chains[0].getPrefHeight());
//		chains[1].setBounds(480, gridPos.y + 122,
//				chains[1].getPrefWidth(), chains[1].getPrefHeight());
		
		
		glass = lAtlas.findRegion("Glass");
		
		
//		nameFont = gGame.getAssetManager().get(UIAssets.GAME_NAME_FONT, BitmapFont.class);
//		scoreFont = gGame.getAssetManager().get(UIAssets.GAME_SCORE_FONT, BitmapFont.class);
//		timeFont = gGame.getAssetManager().get(UIAssets.GAME_TIME_FONT, BitmapFont.class);
		coinFont = gGame.getAssetManager().get(UIAssets.COIN_FONT, BitmapFont.class);
		
		glaze = new Glaze(this, manager.get(Assets.GLAZE_SHADER,
				ShaderProgram.class));
		glaze.setParams((float)Math.sqrt(2 * DIAMOND_WIDTH * DIAMOND_WIDTH) * 8, 45,
				gridPos.x, gridPos.y + 8 * DIAMOND_HEIGHT);
		
		lAtlas = gGame.getAssetManager().get(AnimationAssets.CHARACTERS_2, TextureAtlas.class);
		soilUp = lAtlas.findRegion("SoilDiamondUp");
		soilLeft = lAtlas.findRegion("SoilDiamondLeft");
		soilRight = lAtlas.findRegion("SoilDiamondRight");
		lAtlas = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		label = lAtlas.findRegion("NumTab");
	}

	@Override
	public void initAudio() {
		// TODO Auto-generated method stub
		getReady = gGame.getAssetManager().get(MusicAssets.GET_READY_MUSIC, Music.class);
		press = gGame.getAssetManager().get(SoundAssets.PRESS_SOUND, Sound.class);
		levelUp = gGame.getAssetManager().get(SoundAssets.LEVELUP_SOUND, Sound.class);
		gameMusic = gGame.getAssetManager().get(MusicAssets.MINER_MUSIC, Music.class);
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
	}
	
	public void initLevel() {
		questSaveScore = false;
		if (preStep == GAME_OVER) {
			gameLevel = 0;
			levelScore = 0;
			totalScore = 0;
			addCoins = 0;
			eatClock = 0;
		}
		totalTime = 0;
		multiplexer.clear();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(frontStage);
		shareComplete = false;
		levelScore = 0;
		timeLevel = 90;
		depth = 0;
		runningDepth = 0;
		gemNum = 0;
		targetScore = (gameLevel + 1) * 10000;
		timeRemain = timeLevel;
		float x = 0 , y = 0; 
		MyAnimation animation = null;
		DiamondOfMiner diamond = null;
		MinerGeneration mGeneration = (MinerGeneration) generate;
		gridBuf = mGeneration.generateAll();
		for (int i = 0 ; i < 8 ; i++) {
			colHeight[i] = 0;
			fallingNum[i] = 0;
		}
		canOver = false;
		for (int i = 0; i < MAX_PHASE; i++)
			gamePhase[i].onReset();
		
		if (avatars != null)
			for (int i = 0; i < avatars.length; i++) {
				if (avatars[i] != null)
					if (avatars[i].name.equals("Krilin")) {
						timeLevel += (Integer) avatars[i].effectParams[0] * 3;
					} else if (avatars[i].name.equals("Vegeta")) {
						timeLevel += (Integer) avatars[i].effectParams[0] * 3;
					} else if (avatars[i].name.equals("Songoku")) {
						timeLevel += (Integer) avatars[i].effectParams[0] * 3;
					}
			}
		
		checkCellEffect.clear();
		diamonds.clear();
		fall.onCreated();
 		logic.onCreated();
 		ai.onCreated();
 		solution.onCreated();
		xScore.onCreated();
		MineLogic mLogic = (MineLogic) logic;
//		lastFriendsRank = PlayerInfo.rankInFriends;
		for (int i = 0; i < 8 ; i++)
			for (int j = 0; j < 8 ; j++) {
				x = gridPos.x; y = gridPos.y + 540;
				diamond = mLogic.allocateDiamond(gridPos.x + j * 60 + 30, gridPos.y + i * 60 + 30, 60, 60, this);
//				diamond.setDestination(gridPos.x + j * 60 + 30, gridPos.y + i * 60 + 30);
				
			
				diamond.setDiamondValue(0);
				diamond.setAction(Diamond.FALL);
				
				diamonds.add(diamond);
				inGridFlag[i][j] = 0;
				grid[i][j] = -1;
			}	
		
		for (int i = 0 ; i < 2 ; i++)
			for (int j = 0 ; j < 8 ; j++) {
				x = gridPos.x; y = gridPos.y + 540;
				diamond = mLogic.allocateDiamond(x + j * 60 + 30, y + (i-2) * 60 + 30, 60, 60, this);
				diamond.setDestination(gridPos.x + j * 60 + 30, gridPos.y + (i-2) * 60 + 30);
				diamond.setDiamondValue(MinerGeneration.DownGridBuff[i][j]);
				diamond.setAction(Diamond.FLY);
				diamondButtom.add(diamond);
			}	

		
		
		for (int i = 0 ; i < 4 ; i++)
			for (int j = 0 ; j < 8 ; j++) {	
				if (!(i == 3 && (j == 0 || j == 7))){
					grid[i][j] = gridBuf[i][j];
//					if (i == 2 && j == 0) grid[i][j] = Diamond.BLUE_GEM * COLOR_NUM + 3;
					//gridFur[i][j] = gridCur[i][j];
					x = 0; y = gridPos.y + 540;
					diamond = (DiamondOfMiner) diamonds.get(i * 8 + j);
//					diamond.setSource(diamond.getDesX(), diamond.getDesY());
					diamond.setDiamondValue(grid[i][j]);
					
					diamond.setAction(Diamond.REST);		
					inGridFlag[i][j] = Operator.onBit(Effect.FIXED_POS, inGridFlag[i][j]);
					colHeight[j] = 4;
				}
			}	
		
		colHeight[0] = 3;
		colHeight[7] = 3;
//		grid[2][0] = Diamond.PINK_GEM * COLOR_NUM + 0;
		
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
		
		initGame = false;
		runningUI.updateContent(runningScore);
//		runningUI.updateTargetScore(targetScore);
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
	
	public void initInterface() {
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
		runningUI.updateContent(avatars);
		runningUI.updateContent(0);
		runningUI.updateContent(0);
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
//			private float plusExp;
//
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
//		// TODO Auto-generated method stub
		scoreSummary = new MinerSummary(gGame, this, new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				//postMessageToWall();
				hideScoreSummary();
				gGame.iFunctions.showInterstitial();
				gGame.setScreen(gGame.getLogin());
				PlayerInfo.coin += addCoins;
				if (generate.isPercentGold()) PlayerInfo.coin += Math.max(1, addCoins / 10);
				gGame.iFunctions.putInt("coins", PlayerInfo.coin);
				
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
				gGame.setScreen(gGame.getLogin());
				PlayerInfo.coin += addCoins;
				if (generate.isPercentGold()) PlayerInfo.coin += Math.max(1, addCoins / 10);
				gGame.iFunctions.putInt("coins", PlayerInfo.coin);
			}
		});
	}
	
	
	private void showScoreSummary() {
		scoreSummary.show(stage.getRoot());
	}
	
	private void hideScoreSummary() {
		scoreSummary.hide();
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
		runningUI = new MinerRunning(gGame, this, new ClickListener() {
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
//						if (curStep == GAME_RUNNING) {
//							
//						} else 
//						if (curStep == GAME_PAUSED) {
//							setStepGame(GameScreen.GAME_RUNNING);
////							multiplexer.addProcessor(stage);
//						}
						if (curStep != GAME_PAUSED && curStep != GAME_READY) {
//							setStepGame(GameScreen.GAME_PAUSED);
							multiplexer.removeProcessor(stage);
//							pause.show(frontStage.getRoot());
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
	
	
	/******************************UpdateGame***********************************/
	
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
	
	private void updateRanking(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	private void updateSummary(float deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateOverUI() {
		scoreSummary.updateContent(levelScore / 100, plusPercentExp, addCoins,
				plusPercentCoin, levelScore, PlayerInfo.getBestScore(gGame.iFunctions, 2), depth, combo.getTotalCombo(), combo.getComboMax());
		PlayerInfo.updateBestScore(gGame.iFunctions, 2, levelScore);
	}
	
	private void updateRankingUI() {
//		ranksSummary.updateContent(levelScore, PlayerInfo.nickName, PlayerInfo.rankInWorld, lastFriendsRank);
	}
	
	private void updateRunningUI() {
		runningUI.updateContent(runningScore);
		runningUI.updateContent(timeRemain, (addCoins), timeRemain / timeLevel);
		runningUI.updateContent(Math.round(runningDepth));
	}
	
	private void updateSummaryUI() {
//		levelSummary.updateContent((float) PlayerInfo.exp, assets.getExpOfScore(levelScore));
	}
	
	private void updateUI() {
//		scoreLabel.setText(""+(int)Math.round(runningScore));
//		timeLabel.setText(""+Math.round(timeRemain));
//		coinLabel.setText(""+coins);
//		unitsDigit.setText(""+(depth % 10));
//		tensDigit.setText(""+((depth / 10) % 10));
//		hundredsDight.setText(""+((depth / 100) % 10));
//		tensDigit.setText(""+((depth / 1000) % 10));
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
		timeSwap += deltaTime;
		updateRunningUI();
		totalTime += deltaTime;
		
		switch (stateGame) {
		case DIAMOND_REST:
			if (logic.SpecialEffect  == 0)
				timeRemain = Math.max(timeRemain - deltaTime, 0);
			gamePhase[0].update(deltaTime);
			break;
		case DIAMOND_MOVE:
			if (logic.SpecialEffect  == 0)
				timeRemain = Math.max(timeRemain - deltaTime, 0);
			gamePhase[1].update(deltaTime);
			break;
		case DIAMOND_ANIMATION:
			if (logic.SpecialEffect  == 0)
				timeRemain = Math.max(timeRemain - deltaTime, 0);
			gamePhase[2].update(deltaTime);
			break;
		case DIAMOND_CHANGE:
			if (logic.SpecialEffect  == 0)
				timeRemain = Math.max(timeRemain - deltaTime, 0);
			gamePhase[3].update(deltaTime);
			break;
		case DIAMOND_UP:
			gamePhase[4].update(deltaTime);
		}
//		System.out.println("phase hien tai "+stateGame);
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
		
		runningScore = Interpolation.linear.apply(runningScore, levelScore, 0.1f);
		runningDepth = Interpolation.linear.apply(runningDepth, depth, 0.1f);
		
//		if (((MinerRunning) runningUI).getWheel() == MinerRunning.WHEEL_ON) {
//			chains[0].setX(chains[0].getX() + deltaTime * -50);
//			chains[1].setX(chains[1].getX() + deltaTime * -50);
//			for (int i = 0; i < chains.length; i++) {
//				if (chains[i].getX() < - chains[i].getWidth()) {
//					chains[i].setX(DiamondLink.WIDTH);
//				}
//			}
//		}
		
		if (isOverTime() && addOverCondition()) {
			System.out.println("theo doi "+eclapsedTime+" "+gTouchMode+" "+stateGame+" "+questSaveScore);
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
				}
		}

	}
	
	/************************************DrawGame*******************************/
	
	public void presentRunning(float deltaTime) {
		drawDiamondTable(deltaTime);
	}
	
	private void drawDiamondTable(float delta) {
		batch.begin();
		batch.setColor(1f, 1f, 1f, 1f);
//		System.out.println("++++");
		if (canShowAd == 0)
		if (skillArrs != null)
		for (Skill skill : skillArrs) {
			skill.draw(batch, delta);
			if (skill.type != Skill.NONE)
//				if (skill.isExist()) 
				{
//				System.out.println("++++"+(skill.getBound().y + 50 + 10)+" "+skill.get()+" "+(skill.position.x - label.getRegionWidth()));
					
					batch.draw(label, skill.position.x - label.getRegionWidth()
							/ 2, skill.position.y - 72 / 2);
					coinFont.setColor(1f, 1f, 1f, 1f);
					coinFont.drawWrapped(batch, "" + skill.get(),
							skill.getBound().x, skill.getBound().y + 50 + 10

							, skill.getBound().width, HAlignment.CENTER);
					
				}
		}
		batch.setColor(1f, 1f, 1f, 0f);
		batch.draw(label, 0
				/ 2, 0);
		coinFont.setColor(1f, 1f, 1f, 1f);
		coinFont.drawWrapped(batch, "s",
				0f

				, 0f, delta, HAlignment.CENTER);
		
		batch.setColor(1f, 1f, 1f, 1f);
		ScissorStack.calculateScissors(gCamera, batch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);
		if (curStep != GAME_LEVEL_END && curStep != GAME_READY) {
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
				break;
			case DIAMOND_UP:
				gamePhase[4].draw(delta);
			}
			solution.draw(delta);
		}
		batch.flush();
		ScissorStack.popScissors();
		
//		for (int i = 0 ; i < 8 ; i++) {
//			String str = ""+fallingNum[i];
//			coinFont.draw(batch, str, gridPos.x + i * DIAMOND_WIDTH + 10, gridPos.y + gridTable.height + DIAMOND_HEIGHT);
//			str = ""+ colHeight[i];
//			coinFont.draw(batch, str, gridPos.x + i * DIAMOND_WIDTH + 10, gridPos.y - DIAMOND_HEIGHT);
//		}
		
		if (curSkill != null) {
			curSkill.draw(batch, delta);
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
		coinFont.draw(batch, Gdx.graphics.getFramesPerSecond()+" f/s", 0, DiamondLink.HEIGHT - 50);
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

	/*********************Libgdx Lifecycle Methods******************************/
	
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
//		showSummaryGroup();
		curMusic = getReady;
		runningUI.setTouchable(Touchable.enabled);
		super.show();
	}
	
	private void showDiscountCard() {
//		discountCard = null;
//		System.out.println("cai gi the");
//		if (vip == null) {
//			if (MathUtils.random(0, 3) == 0) {
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
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		stage.dispose();
	}
	
	/****************************GameLogic Methods******************************/
	
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
	
	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
	} 
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}
	
	public int touchCell(Vector2 Point) {
		int j = (int) (Point.x - gridPos.x) / 60;
		int i = (int) (Point.y - gridPos.y) / 60;
		
		return i * 8 + j;
	}
	
//	public int diamondType(int i) {
//		return i / COLOR_NUM;
//	}
//	
//	public int diamondColor(int i) {
//		return i % COLOR_NUM;
//	}
	
	private boolean upLevel(float exp, float addExp) {
		int lastLevel = assets.getLevelOfExp(exp);
		int curLevel = assets.getLevelOfExp(exp + addExp);
		return curLevel > lastLevel;
	}

	

	@Override
	public boolean isOverTime() {
		// TODO Auto-generated method stub
		return timeRemain == 0;
	}
	
	/*****************************Network Methods*******************************/

	
//	private void receiveSummary(final EsObject esObject) {
//		Gdx.app.postRunnable(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				gGame.iFunctions.putInt("sendScoreSucces", 1);
//				EsObject myRankInWorld = esObject.getEsObject(Fields.MYRANKINFO);
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
//		final ElectroServerAdapter esAdapter = ElectroServerAdapter
//				.getInstance();
////		createToast("Kết nối "+esAdapter.isConnected(), 2f);
//		
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
//		msg.setInteger(Fields.MODE_OF_SCORE, MINE_DIAMOND);
//		// thuong neu co
//		int lastLevel = assets.getLevelOfExp((float) PlayerInfo.exp);
//		int curLevel = assets.getLevelOfExp((float) PlayerInfo.exp
//				+ assets.getExpOfScore(levelScore) + plusExp);
//		int addGem = 0;
//		if (curLevel > lastLevel) {
//			Gdx.app.log("test", "len cap" + curLevel);
//			addGem = curLevel - lastLevel;
//		}
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
//		gGame.iFunctions.putInt(Fields.MODE_OF_SCORE, MINE_DIAMOND);
//		System.out.println("sendScoreSucess "
//				+ gGame.iFunctions.getInt("sendScoreSucces", 1));
//
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
	
	public void setWheel(int status) {
		((MinerRunning)runningUI).setWheel(status);
	}
	
	public void showSmoke(float x, float y) {
		final ParticleEffectActor target = new ParticleEffectActor(
				ParticleAssets.SOIL_SPRAY, this);
		target.setX(x); target.setY(y);
		frontStage.addActor(target);
		Timeline.createSequence()
				.push(Timeline.createSequence().delay(1f))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						target.getParticleEffect().getEmitters().get(0)
								.setContinuous(false);
					}
				})).push(Timeline.createSequence().delay(3f))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						target.remove();
						moveSystem.killTarget(target);
					}
				})).start(moveSystem);
	}
	
	public void showRockFall(float x, float y) {
		int num = MathUtils.random(16, 32);
		TextureAtlas atlas = manager.get(AnimationAssets.CHARACTERS_2,
				TextureAtlas.class);
		Array<AtlasRegion> regions = atlas.findRegions("StonePiece");
		for (int i = 0; i < num; i++) {
			frontStage.addActor(new StonePiece(regions.get(MathUtils.random(0,
					regions.size - 1)), new Vector2(MathUtils.random(x - 30,
					x + 30), MathUtils.random(y - 30, y + 30)), new Vector2(x,
					y), 30));
		}
		
	}
	
	public void showGoldFly(int color, float fromX, float fromY, float desX,
			float desY, final Command onComplete) {
		TextureAtlas atlas = manager.get(AnimationAssets.CHARACTERS_2,
				TextureAtlas.class);
		Array<AtlasRegion> golds = atlas.findRegions("GoldPiece");
		Image gold = null;
		Image goldFollow = new Image(atlas.findRegion("GoldFollow"));
		switch (color) {
		case 6:
			gold = new Image(golds.get(golds.size - 1));
			break;
		case 5:
			gold = new Image(golds.get(golds.size - 2));
			break;
		case 4:
			gold = new Image(golds.get(golds.size - 3));
			break;
		default:
			return;
		}
		final Image target1 = gold;
		final Image target2 = goldFollow;
		gold.setBounds(fromX - gold.getPrefWidth() / 2,
				fromY - gold.getPrefHeight() / 2, gold.getPrefWidth(),
				gold.getPrefHeight());
		goldFollow.setBounds(fromX - goldFollow.getPrefWidth() / 2, fromY
				- goldFollow.getPrefHeight() / 2, goldFollow.getPrefWidth(),
				goldFollow.getPrefHeight());
		frontStage.addActor(target2);
		frontStage.addActor(target1);

		Timeline.createSequence()
				.push(Timeline
						.createParallel()
						.push(Tween.to(target1, ActorAccessor.CPOS_XY, 1f)
								.target(desX, desY).waypoint(desX, fromY + 120)
								.path(TweenPaths.catmullRom).ease(Linear.INOUT))
						.push(Tween.to(target2, ActorAccessor.CPOS_XY, 1f)
								.target(desX, desY).waypoint(desX, fromY + 120)
								.path(TweenPaths.catmullRom).ease(Linear.INOUT)))
				.push(Timeline
						.createParallel()
						.push(Tween.to(target1, ActorAccessor.OPACITY, 1f)
								.target(0).ease(Linear.INOUT))
						.push(Tween.to(target2, ActorAccessor.OPACITY, 1f)
								.target(0).ease(Linear.INOUT)))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						target1.remove();
						target2.remove();
						moveSystem.killTarget(target1);
						moveSystem.killTarget(target2);
						if (onComplete != null)
							onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void showGemFly(int value, float fromX, float fromY, float desX,
			float desY, final Command onComplete) {
		
		final Image im = new Image(assets.getDiamondRegions(value).get(0));//To-Do arrayIndex
		TextureAtlas atlas = manager.get(AnimationAssets.CHARACTERS_2, TextureAtlas.class);
		Image im2 = null;
		switch (value / COLOR_NUM) {
		case IDiamond.BLUE_GEM:
			im2 = new Image(atlas.findRegion("BlueFollow"));
			break;
		case IDiamond.DEEP_BLUE_GEM:
			im2 = new Image(atlas.findRegion("DeepBlueFollow"));
			break;
		case IDiamond.PINK_GEM:
			im2 = new Image(atlas.findRegion("PinkFollow"));
			break;
		case IDiamond.RED_GEM:
			im2 = new Image(atlas.findRegion("RedFollow"));
			break;
		}
		im.setOrigin(im.getPrefWidth() / 2, im.getPrefHeight() / 2);
		im.setBounds(fromX - im.getPrefWidth() / 2, fromY - im.getPrefHeight()
				/ 2, im.getPrefWidth(), im.getPrefHeight());
		final Image target = im2;
		if (target != null) {
			target.setOrigin(target.getPrefWidth() / 2,
					target.getPrefHeight() / 2);
			target.setBounds(fromX - target.getPrefWidth() / 2,
					fromY - target.getPrefHeight() / 2, target.getPrefWidth(),
					target.getPrefHeight());
			frontStage.addActor(target);
			Timeline.createSequence()
			.push(Timeline
					.createParallel()
					.push(Tween.to(target, ActorAccessor.CPOS_XY, 1f)
							.target(desX, desY).waypoint(desX, fromY + 120)
							.path(TweenPaths.catmullRom).ease(Linear.INOUT))
					.push(Tween.to(target, ActorAccessor.SCALE_XY, 1f)
							.target(1f, 1f).ease(Linear.INOUT)))
			.push(Timeline
					.createParallel()
					.push(Tween.to(target, ActorAccessor.OPACITY, 1f)
							.target(0).ease(Linear.INOUT)))
			.start(moveSystem);
		}
		
		frontStage.addActor(im);
		Timeline.createSequence()
				.push(Timeline
						.createParallel()
						.push(Tween.to(im, ActorAccessor.CPOS_XY, 1f)
								.target(desX, desY).waypoint(desX, fromY + 120)
								.path(TweenPaths.catmullRom).ease(Linear.INOUT))
						.push(Tween.to(im, ActorAccessor.SCALE_XY, 1f)
								.target(1f, 1f).ease(Linear.INOUT)))
				.push(Timeline
						.createParallel()
						.push(Tween.to(im, ActorAccessor.OPACITY, 1f)
								.target(0).ease(Linear.INOUT)))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						im.remove();
					
						moveSystem.killTarget(im);
						if (onComplete != null)
							onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void showMarkFly(int mark, float x, float y) {
		showComboScore(1, mark, 0, x, y);
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
	
	public boolean addOverCondition() {
		return canOver;
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
//						gGame.setScreen(gGame.getLogin());
//						gGame.iFunctions.showInterstitial();
					}
				});
			}
		});
	}




	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MinerDiamond";
	}

	@Override
	public boolean isDangerous() {
		return timeRemain < 10;
	}
	
}
