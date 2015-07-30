package vn.sunnet.qplay.diamondlink.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;

public class SoundAssets {
	public static final String BACK_SOUND = "data/Sounds/Press.mp3";
	public static final String PRESS_SOUND = "data/Sounds/Back.mp3";
	public static final String LEVELUP_SOUND = "data/Sounds/Levelup.mp3";
	public static final String KENG_KENG_SOUND ="data/Sounds/KengKeng.mp3";
	
	public static final String THREE_COMBINE1_SOUND = "data/Sounds/3Combine1.mp3";
	public static final String THREE_COMBINE2_SOUND = "data/Sounds/3Combine2.mp3";
	public static final String THREE_COMBINE3_SOUND = "data/Sounds/3Combine3.mp3";
	public static final String THREE_COMBINE4_SOUND = "data/Sounds/3Combine4.mp3";
	public static final String THREE_COMBINE5_SOUND = "data/Sounds/3Combine5.mp3";
	public static final String FOUR_COMBINE_SOUND = "data/Sounds/4Combine.mp3";
	public static final String FIVE_COMBINE_SOUND = "data/Sounds/5Combine.mp3";
	public static final String T_COMBINE_SOUND = "data/Sounds/TCombine.mp3";
	public static final String CHAINED_THUNDER_SOUND = "data/Sounds/ChainedThunder.mp3";
	public static final String FALL_DOWN_SOUND = "data/Sounds/FallDown.mp3";
	public static final String MOVE_SOUND = "data/Sounds/Move.mp3";
	public static final String MOVE_FAIL_SOUND = "data/Sounds/MoveFail.mp3";
	public static final String SELECT_SOUND = "data/Sounds/Select.mp3";
	public static final String THUNDER_SOUND = "data/Sounds/Thunder.mp3";
	public static final String T_THUNDER_SOUND = "data/Sounds/TThunder.mp3";	
	public static final String EXPLODE_SOUND = "data/Sounds/Explode.mp3";
	public static final String EAT_TIME_SOUND = "data/Sounds/EatTime.mp3";
	public static final String EAT_COIN_SOUND = "data/Sounds/EatCoin.mp3";
	public static final String BRAVO_SOUND = "data/Musics/Bravo.mp3";

	
	public static final String BUTTERFLY_APPEAR = "data/Sounds/Butterfly/ButterflyAppear.mp3";
	public static final String BUTTERFLY_CATCHED = "data/Sounds/Butterfly/ButterflyCatched.mp3";
	public static final String BUTTERFLY_CURED = "data/Sounds/Butterfly/ButterflyCured.mp3";
	
	public static final String COMPLETE_SOUND = "data/Sounds/Mission/Complete.mp3";
	public static final String RESULT_SOUND = "data/Sounds/Mission/Result.mp3";
//	public static final String TIGER_SOUND = "data/Sounds/WhiteTiger.mp3";
//	public static final String DRAGON_SOUND = "data/Sounds/GreenDragon.mp3";
//	public static final String PHOENIX_SOUND = "data/Sounds/RedPhoenix.mp3";
//	public static final String TORTOISE_SOUND = "data/Sounds/BlackTortoise.mp3";
	
	public static final String LASER_SOUND = "data/Sounds/Laser.mp3";
	
	private AssetManager assetManager;
	
	public SoundAssets(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public void load() {
		assetManager.setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));
		assetManager.load(PRESS_SOUND, Sound.class);
		assetManager.load(BACK_SOUND, Sound.class);
		assetManager.load(FALL_DOWN_SOUND, Sound.class);
		
		assetManager.load(THREE_COMBINE1_SOUND, Sound.class);
		assetManager.load(THREE_COMBINE2_SOUND, Sound.class);
		assetManager.load(THREE_COMBINE3_SOUND, Sound.class);
		assetManager.load(THREE_COMBINE4_SOUND, Sound.class);
		assetManager.load(THREE_COMBINE5_SOUND, Sound.class);
		assetManager.load(FOUR_COMBINE_SOUND, Sound.class);
		assetManager.load(FIVE_COMBINE_SOUND, Sound.class);
		assetManager.load(T_COMBINE_SOUND, Sound.class);
		assetManager.load(CHAINED_THUNDER_SOUND, Sound.class);
		assetManager.load(FALL_DOWN_SOUND, Sound.class);
		assetManager.load(MOVE_SOUND, Sound.class);
		assetManager.load(MOVE_FAIL_SOUND, Sound.class);
		assetManager.load(SELECT_SOUND, Sound.class);
		assetManager.load(THUNDER_SOUND, Sound.class);
		assetManager.load(T_THUNDER_SOUND, Sound.class);
		assetManager.load(BUTTERFLY_APPEAR, Sound.class);
		assetManager.load(BUTTERFLY_CATCHED, Sound.class);
		assetManager.load(BUTTERFLY_CURED, Sound.class);
		assetManager.load(COMPLETE_SOUND, Sound.class);
		assetManager.load(RESULT_SOUND, Sound.class);
		assetManager.load(LEVELUP_SOUND, Sound.class);
		assetManager.load(BRAVO_SOUND, Sound.class);
		assetManager.load(EXPLODE_SOUND, Sound.class);
		assetManager.load(EAT_TIME_SOUND, Sound.class);
		assetManager.load(EAT_COIN_SOUND, Sound.class);
		assetManager.load(KENG_KENG_SOUND, Sound.class);
		
//		assetManager.load(TIGER_SOUND, Sound.class);
//		assetManager.load(DRAGON_SOUND, Sound.class);
//		assetManager.load(PHOENIX_SOUND, Sound.class);
//		assetManager.load(TORTOISE_SOUND, Sound.class);
		assetManager.load(LASER_SOUND, Sound.class);
	}
}
