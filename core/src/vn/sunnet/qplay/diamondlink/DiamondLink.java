package vn.sunnet.qplay.diamondlink;



import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.game.electro.libgdx.screens.RegisterLoginScreen;
import vn.sunnet.qplay.diamondlink.butterflydiamond.ButterflyDiamond;

import vn.sunnet.qplay.diamondlink.classicdiamond.ClassicDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.minerdiamond.MinerDiamond;

import vn.sunnet.qplay.diamondlink.screens.FrontGame;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.screens.Loading;
import vn.sunnet.qplay.diamondlink.screens.Login;
import vn.sunnet.qplay.diamondlink.screens.Mode;




import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.tweens.GameObjectAccessor;
import vn.sunnet.qplay.diamondlink.utils.CheckExistAdapter;
import vn.sunnet.qplay.diamondlink.utils.CheckExistEventListener;
import vn.sunnet.qplay.diamondlink.utils.GetLeaderBoardAdapter;
import vn.sunnet.qplay.diamondlink.utils.GetLeaderBoardEventListener;
import vn.sunnet.qplay.diamondlink.utils.SubmitScoreAdapter;
import vn.sunnet.qplay.diamondlink.utils.SubmitScoreEventListener;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;




import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.Input;


public class DiamondLink extends Game {
	
	public static final String iconLink = "http://a.imageshack.us/img826/5865/q24s.png";
	public static final String googleDownLink = "https://play.google.com/store/apps/details?id=com.yoentertainment.kimcuong2014";
	public static final String qplayDownLink = "";
	public static final String newsLink = "http://diamondsaga.com/news";
	public static final String eventLink = "http://diamondsaga.com/";
	
	public static final int GOOGLE_VER = 1;
	public static final int OTHER_VER = 0;
	
	public int paymentVer = GOOGLE_VER;
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	
	public static int MUSIC = 1;
	public static int SOUND = 1;
	public static int ADS = 1;
	public static int NOTIFICATION = 1;
	public static float musicVolume = 1;
	public static float soundVolume = 1;
	public static boolean hello = true;
	
	public IFunctions iFunctions;
	
	Assets assets;
	AssetManager assetManager;
	TweenManager moveSystem;

	FrontGame frontGame;

	Mode mode;
	Loading loading;
	Login login;

	GameScreen game;



	AbstractScreen screenBeforePause;
	GameScreen onlineGames[];
	GameScreen offlineGames[];
	
	float idleTime = 0;
	
	public DiamondLink(IFunctions iFunctions) {
		this.iFunctions = iFunctions;
	}
	
	@Override
	public void create() {	
		updateLogin();
		updateAudio();
		readOfflineScores();
		
	
		if (moveSystem == null) {
			Tween.setWaypointsLimit(1000);
			moveSystem = new TweenManager();
			Tween.registerAccessor(Actor.class, new ActorAccessor());
			Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		}
		
		if (assetManager == null)
			assetManager = new AssetManager(iFunctions.getResolver());
		
		if (assets == null)
			assets = new Assets(this);
		
		
		if (loading == null) 
			loading = new Loading(WIDTH, HEIGHT, this, new Runnable() {

				@Override
				public void run() {
					assets.createCharacterAnimations();
					assets.createTotalShop();
				}
			});
			
		System.out.println(getMoment());
		
		
		
		if (onlineGames == null) {
			onlineGames = new GameScreen[GameScreen.MAX_GAME];
//			offlineGames = new GameScreen[GameScreen.MAX_GAME];
		}
		
//		if (mode == null) {
//			mode = new Mode(WIDTH, HEIGHT, this);
//		}
		Texture.setAssetManager(assetManager);
		if (screenBeforePause == null)
			assets.load();
		setScreen(loading);
		Gdx.input.setCatchBackKey(true);
		idleTime = 2f * 60;
//		writeToFile();
		
//		Gdx.input.isPeripheralAvailable(Input.Peripheral.OnscreenKeyboard);
//		Gdx.input.setOnscreenKeyboardVisible(true);
	}
	
	private void writeToFile() {
		FileHandle file = Gdx.files.external("customers");
		String totalString = "";
		for (int i = 1; i < 101; i++) {
			String prefix = "('"+i+"', '"+i+"', '"+i+"',";
			String suffix = " 0, 0, 0, 5000000, '0000-00-00 00:00:00', 1, 1, 1, 1, 0, '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', 1, 'abc', '0000-00-00 00:00:00', 0, 0),\r\n";
			totalString += (prefix + suffix);
			
		}
		file.writeString(totalString, true);
		System.out.println("complete");
	}
	
	
	private void updateAudio() {
		MUSIC = iFunctions.getInt("MUSIC", 1);
		SOUND = iFunctions.getInt("Sound", 1);
		ADS = iFunctions.getInt("ADS", 1);
		NOTIFICATION = iFunctions.getInt("NOTIFICATION", 1);
	}
	
	private void readOfflineScores() {
		PlayerInfo.offlineName = iFunctions.getString("offlinename", "");
		System.out.println("ten offline "+PlayerInfo.offlineName);
		PlayerInfo.lastScore = iFunctions.getFloat("lastscore", 0);
		int scores = iFunctions.getInt("scores", 0);
		if (scores > 0) {
			PlayerInfo.highScores = new float[scores];
			for (int i = 0; i < scores; i++) {
				PlayerInfo.highScores[i] = iFunctions.getFloat("score"+i, 0);
			}
		}
	}
	
	public String getMoment() {
		Calendar nowCalendar = Calendar.getInstance();
		Date nowDate = nowCalendar.getTime();
		String split[] = nowDate.toString().split(" ");
		String result = "";
		for (int i = 0; i < split.length; i++) {
			if (i == 3) split[i] = split[i].substring(0, 2);
			System.out.println(i+" "+split[i]);
			result += " "+split[i];
		}
		return result.substring(1);
	}
	
	private void updateLogin() {
//		Calendar nowCalendar = Calendar.getInstance();
//		Date nowDate = nowCalendar.getTime();
////		iFunctions.putString("lastlogin", "");
//		String lastLogin = iFunctions.getString("lastlogin", "");
//		if (lastLogin.equals("")) {
//			iFunctions.putInt("days", 1);
//		} else {
//			System.out.println("lastLogin" + lastLogin);
//			Timestamp lastTimestamp = Timestamp.valueOf(lastLogin);
//			
//			if (nowDate.getTime() > lastTimestamp.getTime()) {
//				if (nowDate.getDate() != lastTimestamp.getDate()
//						|| nowDate.getMonth() != lastTimestamp.getMonth()
//						|| nowDate.getYear() != lastTimestamp.getYear()) {
//					int days = iFunctions.getInt("days", 0);
//					iFunctions.putInt("days", days + 1);
//				}
//			}
//		}
//		
//		iFunctions.putString("lastlogin", new Timestamp(nowCalendar.getTimeInMillis()).toString());
		
		PlayerInfo.coin = iFunctions.getInt("coins", 1000);
		
//		PlayerInfo.coin = 10000;
	}
	
	public boolean canShowPayment() {
		return iFunctions.canShowPayment();
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		super.render();
		//remainConnect();
	}
	
	private void remainConnect() {
	}
	

	@Override
	public void dispose() {
		iFunctions.putInt("MUSIC", MUSIC);
		iFunctions.putInt("Sound", SOUND);
		iFunctions.putInt("ADS", ADS);
		iFunctions.putInt("NOTIFICATION", NOTIFICATION);
		if (login != null)
			login.dispose();
		if (loading != null)
			loading.dispose();
		if (assetManager != null)
			assetManager.dispose();
		
		if (frontGame != null)
			frontGame.dispose();

//		mode.dispose();

		
	
		for (int i = 0; i < onlineGames.length; i++) {
			if (onlineGames[i] != null) onlineGames[i].dispose();
		}
		
		
	}
	
	public void updateUI() {
	}
	
	
	
	public Login getLogin() {
		if (login == null)
			login = new Login(WIDTH, HEIGHT, this);
		return login;
	}
	
	
	
	
	public GameScreen getGame() {
		return game;
	}
	
	
	
	public FrontGame getFrontGame() {
		if (frontGame == null)
			frontGame = new FrontGame(WIDTH, HEIGHT, this);
		return frontGame;
	}
	
	public GameScreen getGame(int type) {
		switch (type) {
		
		case GameScreen.CLASSIC_DIAMOND:
			if (onlineGames[type] == null) onlineGames[type] = new ClassicDiamond(WIDTH, HEIGHT, this);
			game = onlineGames[type];
			return game;
		case GameScreen.BUTTERFLY_DIAMOND:
			if (onlineGames[type] == null) onlineGames[type] = new ButterflyDiamond(WIDTH, HEIGHT, this);
			game = onlineGames[type];
			return game;
		case GameScreen.MINE_DIAMOND:
			if (onlineGames[type] == null) onlineGames[type] = new MinerDiamond(WIDTH, HEIGHT, this);
			game = onlineGames[type];
			return game;
		case GameScreen.MISSION_DIAMOND:
			
			return game;
		}
		
		return null;
	}
	
	public Mode getMode() {
		return mode;
	}
	

	
	public Assets getAssets() {
		return assets;
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	public TweenManager getMoveSystem() {
		return moveSystem;
	}
	
	public AbstractScreen getScreenBeforePaused() {
		return screenBeforePause;
	}
	
	public void setScreenBeforePaused(AbstractScreen screen) {
		screenBeforePause = screen;
	}
	
	public static int getFixedWith() {
		switch (Gdx.app.getType()) {
		case Android:
		
			return 480;
		case Desktop:
		case iOS:
			int resW = Gdx.graphics.getWidth();
			int resH = Gdx.graphics.getHeight();
			System.out.println(resW+"+++++++++++++++++++++++++++++++++"+resH);
			if (resW == 640 && resH == 960) {
				return resW;
			} else if (resW == 640 && resH == 1136) {
				return resW;
			} else if (resW ==  750 && resH == 1334) {
				return resW;
			} else if (resW == 1125 && resH == 2001) {
				return resW;
			} else if (resW == 1242 && resH == 2208) {
				return resW;
			} else {
				return 480;
			}
			
		default:
			return 480;
		}
	}
	
	public static int getFixedHeight() {
		switch (Gdx.app.getType()) {
		case Android:
		
			return 800;
		case Desktop:
		case iOS:
			int resW = Gdx.graphics.getWidth();
			int resH = Gdx.graphics.getHeight();
			if (resW == 640 && resH == 960) {
				return resH;
			} else if (resW == 640 && resH == 1136) {
				return resH;
			} else if (resW ==  750 && resH == 1334) {
				return resH;
			} else if (resW == 1125 && resH == 2001) {
				return resH;
			} else if (resW == 1242 && resH == 2208) {
				return resH;
			} else {
				return 800;
			}
			
		default:
			return 800;
		}
	}

}
