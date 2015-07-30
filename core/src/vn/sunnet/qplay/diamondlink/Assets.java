package vn.sunnet.qplay.diamondlink;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import vn.sunnet.lib.bmspriter.BMSFile;
import vn.sunnet.lib.bmspriter.BMSSCMLFile;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxSCMLAssetLoader;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.Item;
import vn.sunnet.qplay.diamondlink.loaders.FreeTypeFontGeneratorLoader;
import vn.sunnet.qplay.diamondlink.loaders.MailsDescription;
import vn.sunnet.qplay.diamondlink.loaders.MailsDescriptionLoader;
import vn.sunnet.qplay.diamondlink.loaders.ParticleEffectLoader;
import vn.sunnet.qplay.diamondlink.loaders.ShaderProgramLoader;
import vn.sunnet.qplay.diamondlink.loaders.ShopDescription;
import vn.sunnet.qplay.diamondlink.loaders.ShopDescriptionLoader;
import vn.sunnet.qplay.diamondlink.loaders.SpiderLoader;
import vn.sunnet.qplay.diamondlink.loaders.SpiderLoader.SpiterLoaderParameter;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.brashmonkey.spriter.tests.LibGdxAtlasLoader;

public class Assets {
	
	public static Color GOLDEN = new Color(1, 164f/255f, 0, 1);
	public static Color GEM_BLUE = new Color(95f/255f, 240f/255f, 253f/255f, 1f);
	public static Color PERCENT_BROWN = new Color(207f/255f, 165f/255f, 196f/255f, 1f);
	
	public static final String AVATAR_SHOP = "data/Avatars/AvatarShop.txt";
	public static final String VIP_SHOP = "data/Cards/CardShop.txt";
	public static final String COIN_SHOP = "data/Coins/CoinShop.txt";
	public static final String SHOP_DESCIPTIONS = "data/Shop/ItemDescriptions.txt";
	
//	public static final String AVATAR_SHOP = "AvatarShop.txt";
//	public static final String VIP_SHOP = "CardShop.txt";
//	public static final String COIN_SHOP = "CoinShop.txt";
	
	public static final String MAILS = "data/Mails/Mails.txt";
	
//	public static final String SPIDER_BMS = "data/BMS/cd nhen.scml";
//	public static final String SPIDER_ATLAS = "data/BMS/SpiderAtlas.pack";
	public static final String SPIDER_BMS = "data/BMS/Spider.scml";
	public static final String STAR_BMS = "data/BMS/Star.scml";
	public static final String BUT_BMS = "data/BMS/Butterfly.scml";
	public static final String ATLAS = "data/BMS/BMS.pack";
	/*Level*/
	public static final int MAX_LEVEL = 100;
	DiamondLink game;
	
	/*Shaders*/
	public static final String GLAZE_SHADER = "data/Shaders/glaze.shader";
	
	public static ShopDescription totalShop;
	public static ObjectMap<Integer, String> shopDescriptions;
	
	Preferences preferences = Gdx.app.getPreferences("DiamondLink");
	
	ParticleAssets particleAssets;
	AnimationAssets animationAssets;
	MusicAssets musicAssets;
	SoundAssets soundAssets;
	UIAssets uiAssets;
	
	public Assets(DiamondLink game) {
		// TODO Auto-generated constructor stub
		this.game = game;
		particleAssets = new ParticleAssets(game.getAssetManager());
		animationAssets = new AnimationAssets(game.getAssetManager());
		musicAssets = new MusicAssets(game.getAssetManager());
		soundAssets = new SoundAssets(game.getAssetManager());
		uiAssets = new UIAssets(game.getAssetManager());
		caculateMaxExps();
	}
	
	private void loadFile() {
//		shopDescriptions = new ObjectMap<Integer, String>();
//		try {
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//				   Gdx.files.internal(SHOP_DESCIPTIONS).read(), "UTF-8"));
//			while (true) {
//				try {
//					String str = br.readLine();
//					if (str == null) break;
//					else {
//						
//						String split[] = str.split("\\|");
//						int id = Integer.parseInt(split[0]);
//						String des = split[1];
//						System.out.println(des);
//						shopDescriptions.put(id, des);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void loadShaders() {
		game.assetManager.setLoader(ShaderProgram.class,
				new ShaderProgramLoader(new InternalFileHandleResolver()));
		game.getAssetManager().load(GLAZE_SHADER, ShaderProgram.class);
	}
	
	private void loadOthers() {
//		game.assetManager.setLoader(MailsDescription.class,
//				new MailsDescriptionLoader(new InternalFileHandleResolver()));
//		
//		game.assetManager.load(MAILS, MailsDescription.class);
//		game.assetManager.setLoader(ShopDescription.class,
//				new ShopDescriptionLoader(new InternalFileHandleResolver()));
//		game.assetManager.load(AVATAR_SHOP, ShopDescription.class);
//		game.assetManager.load(VIP_SHOP, ShopDescription.class);
//		game.assetManager.load(COIN_SHOP, ShopDescription.class);
		loadFile();
	}
	
	private void loadBMSes() {
		game.assetManager.setLoader(BMSSCMLFile.class, new BMSLibgdxSCMLAssetLoader());
//		game.assetManager.load(SPIDER_BMS, BMSSCMLFile.class);
		
		game.assetManager.load(STAR_BMS, BMSSCMLFile.class);
		game.assetManager.load(BUT_BMS, BMSSCMLFile.class);
		game.assetManager.load(ATLAS, TextureAtlas.class);
		
		
		game.assetManager.setLoader(LibGdxAtlasLoader.class, new SpiderLoader(new InternalFileHandleResolver()));
		SpiderLoader.SpiterLoaderParameter parameter = new SpiterLoaderParameter();
		parameter.atlas_path = ATLAS;
		game.assetManager.load(SPIDER_BMS, LibGdxAtlasLoader.class, parameter);
	}
	
	public void load() {
		particleAssets.load();
		animationAssets.load();
		musicAssets.load();
		soundAssets.load();
		uiAssets.load();
		loadShaders();
		loadOthers();
		loadBMSes();
	}
	
	/**************************Particle Effects***************************/
	
	public PooledEffect getParticleEffect(String name) {
		return particleAssets.get(name);
	}
	
	public void collectParticleEffect(String name, ParticleEffect effect) {
		particleAssets.free(name, effect);
	}
	
	public void createTotalShop() {
//		if (totalShop != null) return;
//		totalShop = new ShopDescription();
//		ShopDescription avatars = game.getAssetManager().get(AVATAR_SHOP, ShopDescription.class);
//		ShopDescription cards = game.getAssetManager().get(VIP_SHOP, ShopDescription.class);
//		for (int i = 0; i < avatars.size(); i++) {
//			Item item = avatars.get(i);
//			totalShop.addItem(item);
//		}
//		
//		for (int i = 0; i < cards.size(); i++) {
//			Item item = cards.get(i);
//			totalShop.addItem(item);
//		}
	}
	
	public static Item getItemInShop(String id) {
		System.out.println("Lay id item "+id);
		return totalShop.get(id);
	}
	
	public String getItemDescription(int id) {
		if (shopDescriptions.get(id) == null) return "";
		return shopDescriptions.get(id);
	}
	
	/***********************************Sound-Music*****************************/
	
	public static void playSound(Sound sound) {
		if (DiamondLink.SOUND == 1) {
			sound.play(DiamondLink.soundVolume);
		}
	}
	
	public static void pauseSound(Sound sound) {
		sound.pause();
	}
	
	public static void playMusic(Music music) {
		if (DiamondLink.MUSIC == 1) {
			music.setVolume(DiamondLink.musicVolume);
			music.play();
		}
	}
	
	public static void playMusic(Music music, float volume) {
		music.setVolume(volume);
		music.play();
	}
	
	public static void pauseMusic(Music music) {
		music.pause();
	}
	
	/*********************************Files**************************************/
	
	public void setBoolean(String field, boolean value) {
		preferences.putBoolean(field, value);
		preferences.flush();
	}
	
	public boolean getBoolean(String field) {
		return preferences.getBoolean(field, false);
	}
	/*******************************Exp***************************************/
	float exp_min[] = new float[MAX_LEVEL]; //0->99
	
	public float getMinExpOfLevel(int level) {
		float ratio = level;
		if (level == 1) return 0;
		return exp_min[level - 1]; 
	}
	
	public int getLevelOfExp(float exp) {
		int level = 1;
		if (getMinExpOfLevel(2) > exp) return 1;//
		if (exp >= getMinExpOfLevel(MAX_LEVEL)) return MAX_LEVEL;
		int i = 2;
		int j = MAX_LEVEL;
		while (j - i > 1) {
			int r = (i + j) / 2;
			if (exp < getMinExpOfLevel(r)) j = r;
			else if (exp > getMinExpOfLevel(r)) i = r;
			else return (r);
		}
		if (exp >= getMinExpOfLevel(i)) return i;
		if (exp < getMinExpOfLevel(j)) return j - 1;
		return 0;
	}
	
	public float getPercentOfLevel(float exp, int level) {
		if (level == MAX_LEVEL) return 1;
		float ratio = (exp - getMinExpOfLevel(level))
				/ (getMinExpOfLevel(level + 1) - getMinExpOfLevel(level));
		if (ratio < 0 && level == MAX_LEVEL) return 1;
		return ratio;
	} 
	
	public float getExpOfScore(float score) {
		return score / 100;
	}
	
	private void caculateMaxExps() {
		// TODO Auto-generated method stub
		exp_min[0] = 0;
		for (int i = 1; i < MAX_LEVEL; i++) {
			float index = i + 1;
			exp_min[i] = exp_min[i - 1] + 400 * (index) * (index + 1);
		}
	}
	/************************************Animation*****************************************/
	
	
	public MyAnimation getDiamondAnimation(int value,
			float duration) {
		// TODO Auto-generated method stub
		return animationAssets.getDiamondAnimation(value, duration);
	}

	public MyAnimation getRearAnimation(int type, int color, float frameduration) {
		// TODO Auto-generated method stub
		return animationAssets.getRearAnimation(type, color, frameduration);
	}

	public MyAnimation getFrontAnimation(int type, int color, float frameduration) {
		// TODO Auto-generated method stub
		return animationAssets.getFrontAnimation(type, color, frameduration);
	}

	public float getEffectTime(String name, float frameDuration) {
		// TODO Auto-generated method stub
		return animationAssets.getEffectTime(name, frameDuration);
	}

	public MyAnimation getEffectAnimation(String string, float frameduration) {
		// TODO Auto-generated method stub
		return animationAssets.getEffectAnimation(string, frameduration);
	}

	public Array<AtlasRegion> getDiamondRegions(int i) {
		// TODO Auto-generated method stub
		return animationAssets.getDiamondRegions(i);
	}
	
	public void createCharacterAnimations() {
		animationAssets.createAnimations();
	}
	
	/**************************************UI******************************************/
	
	public BitmapFont getBitmapFont(String name) {
		return uiAssets.getBitmapFont(name);
	}

}
