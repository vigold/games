package vn.sunnet.qplay.diamondlink.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

public class FreeTypeFontGeneratorLoader
		extends
		SynchronousAssetLoader<FreeTypeFontGenerator, FreeTypeFontGeneratorLoader.Parameters> {

	public FreeTypeFontGeneratorLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	static public class Parameters extends
			AssetLoaderParameters<FreeTypeFontGenerator> {
	}

	@Override
	public FreeTypeFontGenerator load(AssetManager manager, String name,
			FileHandle file, Parameters parameters) {
		// TODO Auto-generated method stub
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		return generator;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String arg0, FileHandle arg1,
			Parameters arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
