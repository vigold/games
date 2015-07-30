package vn.sunnet.qplay.diamondlink.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import vn.sunnet.qplay.diamondlink.loaders.MailsDescriptionLoader.Parameters;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class ShaderProgramLoader extends SynchronousAssetLoader<ShaderProgram, ShaderProgramLoader.Parameters> {

	public ShaderProgramLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	static public class Parameters extends
			AssetLoaderParameters<ShaderProgram> {

	}

	@Override
	public ShaderProgram load(AssetManager manager, String name, FileHandle file,
			Parameters arg3) {
		// TODO Auto-generated method stub
		ShaderProgram shaderProgram = null;
		InputStream in = file.read();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String vertName = null;
		String fragName = null;
		try {
			vertName = reader.readLine();
			fragName = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileHandle vert = resolve(file.parent().toString() +"/"+ vertName);
		FileHandle frag = resolve(file.parent().toString() +"/"+ fragName);
		System.out.println(vert.readString());
		System.out.println(frag.readString());
		shaderProgram = new ShaderProgram(vert, frag);
		if (!shaderProgram.isCompiled())
			throw new GdxRuntimeException(""+shaderProgram.getLog());
		return shaderProgram;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String arg0, FileHandle arg1,
			Parameters arg2) {
		// TODO Auto-generated method stub
		return null;
	}
}
