package vn.sunnet.qplay.diamondlink.loaders;

import java.io.IOException;

import vn.sunnet.qplay.diamondlink.loaders.ShopDescriptionLoader.Parameters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class MailsDescriptionLoader
		extends
		AsynchronousAssetLoader<MailsDescription, MailsDescriptionLoader.Parameters> {
	
	protected XmlReader xml = new XmlReader();
	protected Element root;
	protected MailsDescription description;
	protected Texture texture;

	public MailsDescriptionLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
		
	}

	static public class Parameters extends
			AssetLoaderParameters<MailsDescription> {
	}

	@Override
	public void loadAsync(AssetManager manager, String filename, FileHandle file,
			Parameters arg3) {
		// TODO Auto-generated method stub
		String source = root.getAttribute("source");
		String path = file.parent().toString() + "/" + source;

		texture = manager.get(path, Texture.class);

		description = loadMailsDescription(manager, root, file);
	}
	
	private MailsDescription loadMailsDescription(AssetManager manager, Element root, FileHandle descriptionFile) {
		MailsDescription description = new MailsDescription();
		Array<Element> items = root.getChildrenByName("slogan");
		for (Element element: items) {
			loadItem(manager, description, element, descriptionFile);
		}
		return description;
	}
	
	private void loadItem(AssetManager manager, MailsDescription description, Element element, FileHandle descriptionFile) {
		Slogan slogan = new Slogan();
		slogan.name = element.getAttribute("name");

		slogan.description = element.getAttribute("description");
		
		slogan.type = element.getAttribute("type");
		
		slogan.effect = element.getChildByName("effect").getAttribute("type");
		String paramsType = element.getChildByName("effect").getAttribute("paramtype");
		int params = element.getChildByName("effect").getIntAttribute("params");
		if (paramsType.equals("Integer")) {
			if (params > 0) {
				slogan.effectParams = new Integer[params];
				for (int i = 0; i < params; i++) {
					slogan.effectParams[i] = element.getChildByName("effect").getIntAttribute("params"+i);
				}
			}
		} else if (paramsType.equals("String")) {
			if (params > 0) {
				slogan.effectParams = new String[params];
				for (int i = 0; i < params; i++) {
					slogan.effectParams[i] = element.getChildByName("effect").getAttribute("params"+i);
				}
			}
		}
		
		slogan.display = new TextureRegion(texture);
		Gdx.app.log("test", slogan.name+" "+slogan.description+" "+slogan.type);
		description.addItem(slogan);
	}
	
	@Override
	public MailsDescription loadSync(AssetManager arg0, String arg1,
			FileHandle arg2, Parameters arg3) {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String filename, FileHandle file,
			Parameters parameters) {
		// TODO Auto-generated method stub
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		try {
			root = xml.parse(file);
			String source = root.getAttribute("source");
			String path = file.parent().toString() + "/" + source;
			System.out.println("loadMailsParamters "+path);
			dependencies.add(new AssetDescriptor(path, Texture.class));
			return dependencies;
		} catch (IOException e) {
			throw new GdxRuntimeException("Couldn't load ShopDescrition '" + filename + "'", e);
		}
	}
}
