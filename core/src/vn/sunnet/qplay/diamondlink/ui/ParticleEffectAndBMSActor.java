package vn.sunnet.qplay.diamondlink.ui;

import vn.sunnet.lib.bmspriter.BMSAnimationBlueprint;
import vn.sunnet.lib.bmspriter.BMSRenderObjectProducer;
import vn.sunnet.lib.bmspriter.BMSSCMLFile;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxAnimationInstance;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxROP_TextureAtlas;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxRenderer;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

//public class ParticleEffectAndBMSActor extends Actor {
//	ParticleEffect effect;
//	BMSLibgdxAnimationInstance bms;
//	BMSLibgdxRenderer renderer;
//	
//	public ParticleEffectAndBMSActor(String effect, String bmsFile ,String atlasFile, String animationName, GameScreen screen) {
//		// TODO Auto-generated constructor stub
//		this.effect = screen.assets.getParticleEffect(effect);
//		
//		TextureAtlas atlas = screen.gGame.getAssetManager().get(atlasFile,
//				TextureAtlas.class);
//		BMSSCMLFile file = screen.gGame.getAssetManager().get(bmsFile, BMSSCMLFile.class);
//		BMSRenderObjectProducer producer = new BMSLibgdxROP_TextureAtlas(atlas);
//		BMSAnimationBlueprint print = file.getEntity(0).getAnimation(animationName);
//		this.bms = new BMSLibgdxAnimationInstance(print, producer);
//		this.bms.setLooping(true);
//		
//	}
//	
//	@Override
//	public void draw(SpriteBatch batch, float parentAlpha) {
//		// TODO Auto-generated method stub
//		if (renderer == null) renderer = new BMSLibgdxRenderer(batch);
//		effect.setPosition(getX(), getY());
//		effect.draw(batch, Gdx.graphics.getDeltaTime());
//		bms.animate((int)(Gdx.graphics.getDeltaTime() * 1000 *0.4f));
//		renderer.render(bms, getX(), getY() + 20);
//	}
//	
//	public ParticleEffect getParticleEffect() {
//		return effect;
//	}
//	
//	public BMSLibgdxAnimationInstance getBMS() {
//		return bms;
//	}
//}
