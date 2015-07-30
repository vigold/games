package vn.sunnet.qplay.diamondlink.ui;

import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {
	
	ParticleEffect effect;
	
	public ParticleEffectActor(String name, GameScreen screen) {
		// TODO Auto-generated constructor stub
		effect = screen.assets.getParticleEffect(name);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		effect.setPosition(getX(), getY());
		effect.draw(batch, Gdx.graphics.getDeltaTime());
	}
	
	public ParticleEffect getParticleEffect() {
		return effect;
	}
}
