package vn.sunnet.qplay.diamondlink.assets;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.sun.xml.internal.bind.v2.model.core.ID;

public class AnimationAssets {
	
	public static final String EFFECTS = "data/Effects/Effects.pack";
	public static final String CHARACTERS = "data/Characters/Characters.pack";
	public static final String CHARACTERS_2 = "data/Characters/Characters2.pack";
	public static final String DIAMONDS = "data/Characters/Diamonds.pack";
	
	public static final float frameDuration = 5f/60f;
	
	final String [] normalDiamondName = {
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// NORMAL_DIAMOND
			"BlueButterfly","GreenButterfly","OrangeButterfly","PinkButterfly","RedButterfly","WhiteButterly","YeallowButterfly", // BUTTERFLY_DIAMOND
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// FIRE_DIAMOND
			"FiveColorDiamond","FiveColorDiamond","FiveColorDiamond","FiveColorDiamond","FiveColorDiamond","FiveColorDiamond","FiveColorDiamond", // FIVE_COLOR_DIAMOND
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// BLINK_DIAMOND
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// RT_DIAMOND
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// CT_DIAMOND
			"Stone0","Stone1","Stone2","Stone3","Gold0","Gold1","Gold2", // SOIL_DIAMOND
			"Stone0","Stone1","Stone2","Stone3","Gold0","Gold1","Gold2", // ROCK_DIAMOND
			"Stone0","Stone1","Stone2","Stone3","Gold0","Gold1","Gold2",  // BOX_DIAMOND
			"MarkStone0","MarkStone1","MarkStone2","MarkStone3","MarkStone4","MarkStone5","MarkStone0",//PEARL_DIAMOND
			"Lava","Lava","Lava","Lava","Lava","Lava","Lava", // LAVA
			"MarkStone0","MarkStone1","MarkStone2","MarkStone3","MarkStone4","MarkStone5","MarkStone0",//MARK_DIAMOND
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// CLOCK_DIAMOND
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// COIN_DIAMOND
			"ThunderHyper","ThunderHyper","ThunderHyper","ThunderHyper","ThunderHyper","ThunderHyper","ThunderHyper",// HYPER_CUBE
			"LaserDiamond","LaserDiamond","LaserDiamond","LaserDiamond","LaserDiamond","LaserDiamond","LaserDiamond",// HYPER_CUBE
			"BlueDiamond","GreenDiamond","OrangeDiamond","PinkDiamond","RedDiamond","WhiteDiamond","YeallowDiamond",// xScoreGEM
			
			"BlueGem0","BlueGem1","BlueGem2","BlueGem0","BlueGem0","BlueGem0","BlueGem0", // BLUE_GEM
			"DeepBlueGem0","DeepBlueGem1","DeepBlueGem2","DeepBlueGem0","DeepBlueGem0","DeepBlueGem0","DeepBlueGem0", // DEEP_BLUE_GEM
			"PinkGem0","PinkGem1","PinkGem2","PinkGem0","PinkGem0","PinkGem0","PinkGem0", // PINK_GEM
			"RedGem0","RedGem1","RedGem2","RedGem0","RedGem0","RedGem0","RedGem0", // RED_GEM
			
			};
	
	final String[] effectName = { "RearExplode", "Explode", "SMCThunder",
			"MCThunder", "RCThunder", "RoundLight", "CThunder", "RThunder",
			"StoneExplode", "CellExplode", "RearBlink", "FrontBlink", "Fire",
			"Row", "Col", "Coin", "HourHand", "MinuteHand", "BlueThunder",
			"GreenThunder", "WhiteThunder", "RedThunder", "OrangeThunder",
			"PinkThunder", "YeallowThunder", "ChainExplode", "Laser",
			"RearBlueGem", "RearPinkGem", "RearDeepBlueGem", "RearRedGem",
			"Glass", "SoilDiamondLeft","SoilDiamondRight","SoilDiamondUp"};
	
	
	private AssetManager assetManager;
	
	private ObjectMap<String, Array<AtlasRegion>> animationRegions = new ObjectMap<String, Array<AtlasRegion>>();
	
	public AnimationAssets(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public void load() {
		assetManager.load(CHARACTERS_2, TextureAtlas.class);
		assetManager.load(CHARACTERS, TextureAtlas.class);
		assetManager.load(EFFECTS, TextureAtlas.class);
		assetManager.load(DIAMONDS, TextureAtlas.class);
	}
	
	public float getEffectTime(String name, float frameDuration) {
		Array<AtlasRegion> arr = animationRegions.get(name);
		if (arr == null) return 0;
		return frameDuration * arr.size;
	} 
	
	public TextureRegion getDiamondRegion(int value) {
		return animationRegions.get(normalDiamondName[value]).get(0);
	}
	
	public Array<AtlasRegion> getDiamondRegions(int value) {
		return animationRegions.get(normalDiamondName[value]);
	}
	
	public Array<AtlasRegion> getEffectRegions(String name) {
		return animationRegions.get(name);
	}
	
	public MyAnimation getDiamondAnimation(int value, float duration) {
		MyAnimation myAnimation = null;
		if (animationRegions.get(normalDiamondName[value]) == null)
			throw new GdxRuntimeException("Animation not exist: "
					+ normalDiamondName[value]);
		if (normalDiamondName[value].equals("BlueGem")) System.out.println("BlueGem so luon frame "+getDiamondRegions(value).size);
		return getAnimation(normalDiamondName[value], duration);
	}
	
	public MyAnimation getEffectAnimation(String name, float duration) {
		if (name == "FrontBlink") duration *= 2;
		return getAnimation(name, duration);
	}
	
	public MyAnimation getRearAnimation(int type, int color, float duration) {
		switch (type) {
		case IDiamond.BLINK_DIAMOND:
			return getEffectAnimation("RearBlink", duration);
		case IDiamond.FIRE_DIAMOND:
			return getEffectAnimation("Fire", duration);
		case IDiamond.CLOCK_DIAMOND:
			return getEffectAnimation("MinuteHand", duration);
		case IDiamond.BLUE_GEM:
			return getEffectAnimation("RearBlueGem", duration);
		case IDiamond.DEEP_BLUE_GEM:
			return getEffectAnimation("RearDeepBlueGem", duration);
		case IDiamond.PINK_GEM:
			return getEffectAnimation("RearPinkGem", duration);
		case IDiamond.RED_GEM:
			return getEffectAnimation("RearRedGem", duration);
		case IDiamond.MARK_DIAMOND:
		case IDiamond.SOIL_DIAMOND:
			return getDiamondAnimation(7 * 7, duration);
		}
		return null;
	}
	
	public MyAnimation getFrontAnimation(int type, int color, float duration) {
		
		switch (type) {	
			case IDiamond.BLINK_DIAMOND: return getEffectAnimation("FrontBlink", duration);
			case IDiamond.RT_DIAMOND: return getEffectAnimation("Row", duration);
			case IDiamond.CT_DIAMOND: return getEffectAnimation("Col", duration);
			case IDiamond.CLOCK_DIAMOND: return getEffectAnimation("HourHand", duration);
			case IDiamond.COIN_DIAMOND: return getEffectAnimation("Coin", duration);
			case IDiamond.MARK_DIAMOND: return getEffectAnimation("Glass", duration);
		}
		return null;
	}
	
	public MyAnimation getAnimation(String name, float duration) {
		if (animationRegions.get(name) == null) throw new GdxRuntimeException("Animation not exist: " + name);
		Array<AtlasRegion> regions = animationRegions.get(name);
		if (name.equals("Glass")) {
			int num = (int) (2f / duration);
			for (int i = 0; i < num; i++) {// them delay
				regions.add(new AtlasRegion(regions.get(0)));
			}
		}
		return new MyAnimation(duration, animationRegions.get(name));
	}
	
	/*******************************EffectAnimations*****************************/

	
	public void createAnimations() {
		createDiamondAnimations();
		createEffectAnimations();
	}

	private void createDiamondAnimations() {
		
		TextureAtlas lAtlas1 = assetManager.get(CHARACTERS, TextureAtlas.class);
		TextureAtlas lAtlas2 = assetManager.get(CHARACTERS_2, TextureAtlas.class);
		TextureAtlas lAtlas0 = assetManager.get(DIAMONDS, TextureAtlas.class);
		for (int i = 0 ; i < normalDiamondName.length ; i++) {
			if (normalDiamondName[i].contains("_")) {
				String [] name = normalDiamondName[i].split("_");
				Array<AtlasRegion> temp = new Array<AtlasRegion>();
				temp.add(lAtlas1.findRegion(name[0], Integer.parseInt(name[1])));
				animationRegions.put(normalDiamondName[i], temp);
			} else {
				Array<AtlasRegion> temp = lAtlas0.findRegions(normalDiamondName[i]);
				if (temp.size == 0) {
					temp = lAtlas1.findRegions(normalDiamondName[i]);
					if (temp.size == 0)
						temp = lAtlas2.findRegions(normalDiamondName[i]);
				}
				animationRegions.put(normalDiamondName[i], temp);
			}
		}
	}
	
	private void createEffectAnimations() {
		TextureAtlas lAtlas1 = assetManager.get(EFFECTS, TextureAtlas.class);
		TextureAtlas lAtlas2 = assetManager.get(CHARACTERS_2, TextureAtlas.class);
		for (int i = 0 ; i < effectName.length ; i++) {
			if (effectName[i].contains("_")) {
				String [] name = effectName[i].split("_");
				Array<AtlasRegion> temp = new Array<AtlasRegion>();
				temp.add(lAtlas1.findRegion(name[0], Integer.parseInt(name[1])));
				animationRegions.put(effectName[i], temp);
			} else {
				Array<AtlasRegion> temp = lAtlas1.findRegions(effectName[i]);
				if (temp.size == 0) temp = lAtlas2.findRegions(effectName[i]);
				animationRegions.put(effectName[i], temp);
			}
		}
	}
	
	
}
