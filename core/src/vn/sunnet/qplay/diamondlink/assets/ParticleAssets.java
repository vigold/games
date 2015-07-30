package vn.sunnet.qplay.diamondlink.assets;

import vn.sunnet.qplay.diamondlink.loaders.ParticleEffectLoader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.ObjectMap;

public class ParticleAssets {
	
	public static final String FIRE = "data/Particles/fire2.txt";
	public static final String SMOKE = "data/Particles/smoke2.txt";
	public static final String SPRAY = "data/Particles/star.pts";
	public static final String TIME_SPRAY = "data/Particles/time_spray.p";
	public static final String COIN_SPRAY = "data/Particles/coin_spray.p";
	public static final String FIREWORK = "data/Particles/firework.pts";
	public static final String EXPLODE_SPRAY = "data/Particles/explode_spray.p";
	public static final String SOIL_SPRAY = "data/Particles/soil_spray.p";
	public static final String PASS_STAR = "data/Particles/pass_star.pts";
	public static final String SEVEN_COLORS = "data/Particles/7Colors.p";
	
	public static final String BLUE_BUTTERFLY = "data/Particles/BlueButterfly.p";
	public static final String GREEN_BUTTERFLY = "data/Particles/GreenButterfly.p";
	public static final String YEALLOW_BUTTERFLY = "data/Particles/YeallowButterfly.p";
	public static final String ORANGE_BUTTERFLY = "data/Particles/OrangeButterfly.p";
	public static final String PINK_BUTTERFLY = "data/Particles/PinkButterfly.p";
	public static final String WHITE_BUTTERFLY = "data/Particles/WhiteButterfly.p";
	public static final String RED_BUTTERFLY = "data/Particles/RedButterfly.p";
	
	private final int MIN = 1;
	private final int MAX = 2;
	AssetManager assetManager;
	ObjectMap<String, ParticleEffectPool> effectPools = new ObjectMap<String, ParticleEffectPool>();
	
	public ParticleAssets(AssetManager assetManager) {
		this.assetManager = assetManager;
		effectPools.clear();
	}
	
	public void  load() {
		assetManager.setLoader(ParticleEffect.class, new ParticleEffectLoader(new InternalFileHandleResolver()));
		assetManager.load(FIRE, ParticleEffect.class);
		assetManager.load(SMOKE, ParticleEffect.class);
		assetManager.load(SPRAY, ParticleEffect.class);
		assetManager.load(FIREWORK, ParticleEffect.class);
		assetManager.load(TIME_SPRAY, ParticleEffect.class);
		assetManager.load(COIN_SPRAY, ParticleEffect.class);
		assetManager.load(EXPLODE_SPRAY, ParticleEffect.class);
		assetManager.load(SOIL_SPRAY, ParticleEffect.class);
		assetManager.load(PASS_STAR, ParticleEffect.class);
		assetManager.load(SEVEN_COLORS, ParticleEffect.class);
		
		assetManager.load(RED_BUTTERFLY, ParticleEffect.class);
		assetManager.load(GREEN_BUTTERFLY, ParticleEffect.class);
		assetManager.load(BLUE_BUTTERFLY, ParticleEffect.class);
		assetManager.load(YEALLOW_BUTTERFLY, ParticleEffect.class);
		assetManager.load(ORANGE_BUTTERFLY, ParticleEffect.class);
		assetManager.load(WHITE_BUTTERFLY, ParticleEffect.class);
		assetManager.load(PINK_BUTTERFLY, ParticleEffect.class);
	}
	
	public PooledEffect get(String name) {
		ParticleEffectPool pool = effectPools.get(name);
		if (pool == null) {
			ParticleEffect effect = assetManager.get(name, ParticleEffect.class);
			if (effect == null) return null;
			pool = new ParticleEffectPool(effect, MIN, MAX);
			effectPools.put(name, pool);
		}
		return pool.obtain();
	}
	
	public void free(String name, ParticleEffect effect) {
		ParticleEffectPool pool = effectPools.get(name);
		if (pool != null) {
			pool.free((PooledEffect) effect);
		}
	}
}
