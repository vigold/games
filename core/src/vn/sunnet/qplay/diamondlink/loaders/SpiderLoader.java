package vn.sunnet.qplay.diamondlink.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.tests.LibGdxAtlasLoader;

public class SpiderLoader extends SynchronousAssetLoader<LibGdxAtlasLoader, SpiderLoader.SpiterLoaderParameter> {
	
	private FileHandleResolver resolver;

	public SpiderLoader(FileHandleResolver resolver) {
		super(resolver);
		this.resolver = resolver;
	}
	
	static public class SpiterLoaderParameter extends AssetLoaderParameters<LibGdxAtlasLoader> {
		public String atlas_path = "";
	}

	@Override
	public LibGdxAtlasLoader load(AssetManager assetManager, String fileName,
			FileHandle file, SpiterLoaderParameter parameter) {
		SCMLReader reader = new SCMLReader(file.read());
		String path = file.parent().toString();
		System.out.println("path "+path);
		LibGdxAtlasLoader loader = new LibGdxAtlasLoader(reader.getData(), assetManager.get(parameter.atlas_path, TextureAtlas.class));
		loader.load(path);
		return loader;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, SpiterLoaderParameter parameter) {
		Array<AssetDescriptor> dependencies = null;
		if (!parameter.atlas_path.equals("")) {
			dependencies = new Array<AssetDescriptor>();
			dependencies.add(new AssetDescriptor(parameter.atlas_path, TextureAtlas.class));
		}
		return dependencies;
	}

}
