package vn.sunnet.qplay.diamondlink.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class MusicAssets {
	public static final String LOGIN_MUSIC = "data/Musics/Login.mp3";
	public static final String MENU_MUSIC = "data/Musics/Menu.mp3";
	public static final String MENU1_MUSIC= "data/Musics/Menu1.mp3";
	public static final String MODE_MUSIC = "data/Musics/Mode.mp3";
	public static final String GET_READY_MUSIC = "data/Musics/GetReady.mp3";
	public static final String LEVEL_UP_MUSIC = "data/Musics/LevelUp.mp3";
	
//	public static final String RANK_UP_MUSIC = "data/Musics/RankUp.mp3";
	public static final String OVER_MUSIC = "data/Musics/Over.mp3";
	public static final String TIME_WARNING_MUSIC = "data/Musics/TimeWarning.mp3";
	
	public static final String CLASSIC_MUSIC = "data/Musics/Classic/Game.mp3";
	
	public static final String BUTTERFLY_MUSIC = "data/Musics/Butterfly/Game.mp3";
	
	public static final String MINER_MUSIC = "data/Musics/Miner/Game.mp3";
	
//	public static final String MISSION_MUSIC = "data/Musics/Mission/Game.mp3";
//	public static final String TASK_MUSIC = "data/Musics/Mission/Mission.mp3";
//	public static final String ROTATING_MUSIC = "data/Musics/Mission/Rotating.mp3";
//	public static final String GAME_START_MUSIC = "data/Musics/Mission/GameStart.mp3";
	
//	public static final String CHAIN_MUSIC = "data/Musics/Miner/Chain.mp3";
	
	private ObjectMap<String, Music> playingMusic = new ObjectMap<String, Music>();
	private AssetManager assetManager;
	
	public MusicAssets(AssetManager assetManager) {
		this.assetManager = assetManager;
		playingMusic.clear();
	}
	
	public void load() {
		assetManager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));
		assetManager.load(LOGIN_MUSIC, Music.class);
		assetManager.load(MENU_MUSIC, Music.class);
//		assetManager.load(MENU1_MUSIC, Music.class);
//		assetManager.load(MODE_MUSIC, Music.class);
		assetManager.load(GET_READY_MUSIC, Music.class);
		assetManager.load(LEVEL_UP_MUSIC, Music.class);
		assetManager.load(OVER_MUSIC, Music.class);
//		assetManager.load(RANK_UP_MUSIC, Music.class);
		assetManager.load(CLASSIC_MUSIC, Music.class);
		assetManager.load(BUTTERFLY_MUSIC, Music.class);
		assetManager.load(MINER_MUSIC, Music.class);
		assetManager.load(TIME_WARNING_MUSIC, Music.class);
//		assetManager.load(MISSION_MUSIC, Music.class);
//		assetManager.load(TASK_MUSIC, Music.class);
//		assetManager.load(ROTATING_MUSIC, Music.class);
//		assetManager.load(GAME_START_MUSIC, Music.class);
//		assetManager.load(CHAIN_MUSIC, Music.class);
	}
	
}
