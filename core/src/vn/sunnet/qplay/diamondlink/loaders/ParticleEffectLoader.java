package vn.sunnet.qplay.diamondlink.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectLoader extends SynchronousAssetLoader<ParticleEffect, ParticleEffectLoader.ParticleEffectParameter> {
	
	public ParticleEffectLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public ParticleEffect load(AssetManager manager, String fileName, FileHandle file,
			ParticleEffectParameter parameter) {
		// TODO Auto-generated method stub
		ParticleEffect effect = new ParticleEffect();
		effect.load(file, file.parent());
		return effect;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String arg0, FileHandle arg1,
			ParticleEffectParameter arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	
	static public class ParticleEffectParameter extends AssetLoaderParameters<ParticleEffect> {
		
	}

}
