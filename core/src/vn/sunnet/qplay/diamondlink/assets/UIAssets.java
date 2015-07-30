package vn.sunnet.qplay.diamondlink.assets;

import vn.sunnet.qplay.diamondlink.loaders.FreeTypeFontGeneratorLoader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UIAssets {

	public static final String BLACK_GLASS = "data/Game/BG/BlackGlass.png";
	public static final String DANGER = "data/Game/BG/Danger.png";
	public static final String MINER_BG = "data/Game/BG/MinerGame.jpg";
	public static final String GAME_BG = "data/Game/BG/Game.jpg";
	public static final String GAME_FG = "data/Game/FG/GameFG.pack";
	public static final String MENU_BG = "data/Login/BG/Menu.jpg";
	public static final String MENU_FG = "data/Menu/FG/FG.pack";
	public static final String LOGIN_FG_1 = "data/Login/FG/FG.pack";
	public static final String SUMMARY_FG = "data/Summary/FG/Summary.pack";
	public static final String WATER_BG = "data/Login/BG/WaterBG.png";
	public static final String MAIL_FG = "data/Mails/FG.pack";
	
	public static final String LIFE = "data/Menu/BG/Life.png";

	public static final String AVATARS = "data/Avatars/Avatars.pack";
	public static final String BUY_ITEMS = "data/BuyItem/FG/StoreFG.pack";
	public static final String ITEMS = "data/BuyItem/FG/Items.pack";

	
	public static final String MODE_FG = "data/Mode/FG/FG.pack";

	public static final String GOOD = "data/ComboEffects/FG/Good.png";
	public static final String EXCELLENT = "data/ComboEffects/FG/Excellent.png";
	public static final String COOL = "data/ComboEffects/FG/Cool.png";
	public static final String GREAT = "data/ComboEffects/FG/Great.png";
	public static final String PERFECT = "data/ComboEffects/FG/Perfect.png";


	
	public static final String MENU_NAME_FONT = "data/Menu/Fonts/Name.fnt";
	public static final String MENU_TIME_FONT = "data/Menu/Fonts/Time.fnt";
	public static final String CHAT_FONT = "data/Menu/Fonts/Chat.fnt";

	public static final String MENU_SCORE_FONT = "data/Menu/Fonts/ScoreFont.fnt";
	public static final String GAME_NAME_FONT = "data/Game/Fonts/NameFont.fnt";
//	public static final String GAME_SCORE_FONT = "data/Game/Fonts/ScoreFont.fnt";
	public static final String GAME_TIME_FONT = "data/Game/Fonts/TimeFont.fnt";
	public static final String COIN_FONT = "data/Menu/Fonts/CoinFont.fnt";
	public static final String EXP_FONT = "data/Menu/Fonts/ExpFont.fnt";
	public static final String COMBO_FONT = "data/ComboEffects/Fonts/ComboFont.fnt";
	public static final String USE_FONT = "data/Menu/Fonts/UseFont.fnt";
//	public static final String SUMMARY_SCORE_FONT = "data/Summary/Fonts/ScoreFont2.fnt";
	public static final String WORLD_RANK_FONT = "data/Summary/Fonts/WorldRankFont.fnt";
	public static final String FRIEND_RANK_FONT = "data/Summary/Fonts/FriendRankFont.fnt";
	public static final String ITEM_FONT = "data/BuyItem/Fonts/ItemFont.fnt";
	public static final String DIAMOND_FONT = "data/Game/Fonts/DiamondFont.fnt";
	
	public static final String SUMMARY_SCORE_FONT = "data/Summary/Fonts/SummaryScoreFont.fnt";
	public static final String SUMMARY_GOLD_FONT = "data/Summary/Fonts/SummaryGoldFont.fnt";
	public static final String SMALL_SCORE_FONT = "data/Game/Fonts/SmallScoreFont.fnt";
	public static final String LARGE_SCORE_FONT = "data/Game/Fonts/LargeScoreFont.fnt";
	
//	public static final String EVENT_TAB = "data/Tapjoy/Event.png";

	public static final String ARIAL_GENERATOR = "electro/boldfont.otf";
	public static final String DUC_GENERATOR = "electro/UTM Bienvenue.ttf";
	
	//Login
	public static final String LOGIN_LAYER_0 = "data/Login/BG/Layer0.jpg" ;
	public static final String LOGIN_LAYER_1 = "data/Login/BG/Layer1.png" ;
	public static final String LOGIN_LAYER_2 = "data/Login/BG/Layer2.png" ;
	public static final String LOGIN_FG = "data/Login/FG/LoginFG.pack";
	public static final String SETTING_FONT = "data/Login/FG/SettingFont.fnt";
	public static final String RANK_NAME_FONT = "data/Login/FG/RankNameFont.fnt";
	public static final String SHOP_GOLD_FONT = "data/Login/FG/ShopGoldFont.fnt";
	
	private AssetManager assetManager;
	
	public UIAssets(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public void load() {
		loadTextures();
		loadTextureAtlas();
		loadBitmaFonts();
	}
	
	private void loadTextures() {
		TextureParameter parameter = new TextureParameter();
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		

		assetManager.load(BLACK_GLASS, Texture.class, parameter);
		assetManager.load(DANGER, Texture.class, parameter);
		
		assetManager.load(GOOD, Texture.class, parameter);
		assetManager.load(EXCELLENT, Texture.class, parameter);
		assetManager.load(COOL, Texture.class, parameter);
		assetManager.load(PERFECT, Texture.class, parameter);
		assetManager.load(GREAT, Texture.class, parameter);
		
		
		
		assetManager.load(LOGIN_LAYER_0, Texture.class, parameter);
		assetManager.load(LOGIN_LAYER_1, Texture.class, parameter);
		assetManager.load(LOGIN_LAYER_2, Texture.class, parameter);
		
	}
	
	private void loadTextureAtlas() {
		//assetManager.load(MASCOTS, TextureAtlas.class);
		assetManager.load(ITEMS, TextureAtlas.class);
		assetManager.load(BUY_ITEMS, TextureAtlas.class);
		assetManager.load(GAME_FG, TextureAtlas.class);
		//assetManager.load(AVATARS, TextureAtlas.class);
//		assetManager.load(MENU_FG, TextureAtlas.class);
		assetManager.load(LOGIN_FG, TextureAtlas.class);
//		assetManager.load(LOGIN_FG_1, TextureAtlas.class);
		//assetManager.load(MISSION_FG, TextureAtlas.class);
//		assetManager.load(MODE_FG, TextureAtlas.class);
		assetManager.load(SUMMARY_FG, TextureAtlas.class);
		//assetManager.load(MAIL_FG, TextureAtlas.class);
	}
	
	private void loadBitmaFonts() {
		BitmapFontParameter parameter = new BitmapFontParameter();
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
//		assetManager.load(GAME_NAME_FONT, BitmapFont.class, parameter);
//		assetManager.load(GAME_SCORE_FONT, BitmapFont.class, parameter);
		assetManager.load(GAME_TIME_FONT, BitmapFont.class, parameter);
		assetManager.load(COIN_FONT, BitmapFont.class, parameter);
//		assetManager.load(EXP_FONT, BitmapFont.class, parameter);
//		assetManager.load(MENU_SCORE_FONT, BitmapFont.class, parameter);
		assetManager.load(COMBO_FONT, BitmapFont.class, parameter);
		assetManager.load(SUMMARY_SCORE_FONT, BitmapFont.class, parameter);
		assetManager.load(SUMMARY_GOLD_FONT, BitmapFont.class, parameter);
		
		assetManager.load(SMALL_SCORE_FONT, BitmapFont.class, parameter);
		assetManager.load(LARGE_SCORE_FONT, BitmapFont.class, parameter);

		assetManager.load(DIAMOND_FONT, BitmapFont.class, parameter);
		
//		assetManager.load(MENU_NAME_FONT, BitmapFont.class, parameter);
//		assetManager.load(MENU_TIME_FONT, BitmapFont.class, parameter);
//		assetManager.load(CHAT_FONT, BitmapFont.class, parameter);
		assetManager.load(SETTING_FONT, BitmapFont.class, parameter);
		assetManager.load(RANK_NAME_FONT, BitmapFont.class, parameter);
		assetManager.load(SHOP_GOLD_FONT, BitmapFont.class, parameter);
		
		assetManager.setLoader(
				FreeTypeFontGenerator.class,
				new FreeTypeFontGeneratorLoader(
						new InternalFileHandleResolver()));
		assetManager.load(ARIAL_GENERATOR, FreeTypeFontGenerator.class);
		assetManager.load(DUC_GENERATOR, FreeTypeFontGenerator.class);
	}
	
	public BitmapFont getBitmapFont(String name) {
		BitmapFont font = assetManager.get(name, BitmapFont.class);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return font;
	}
	
}
