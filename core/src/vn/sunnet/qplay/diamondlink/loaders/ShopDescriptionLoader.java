package vn.sunnet.qplay.diamondlink.loaders;

import java.io.IOException;

import vn.sunnet.qplay.diamondlink.items.Item;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class ShopDescriptionLoader extends AsynchronousAssetLoader<ShopDescription, ShopDescriptionLoader.Parameters> {
	
	protected XmlReader xml = new XmlReader();
	protected Element root;
	protected ShopDescription description;
	protected TextureAtlas atlas;
	

	public ShopDescriptionLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadAsync(AssetManager manager, String filename, FileHandle file,
			Parameters arg3) {
		// TODO Auto-generated method stub
		String source = root.getAttribute("source");
		String path = file.parent().toString() + "/" + source;
//		String path = source;

		atlas = manager.get(path, TextureAtlas.class);

		description = loadShopDescription(manager, root, file);
	}

	@Override
	public ShopDescription loadSync(AssetManager manager, String filename,
			FileHandle file, Parameters parameters) {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String filename, FileHandle file,
			Parameters parameters) {
		// TODO Auto-generated method stub
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		try {
			xml = new XmlReader();
			root = xml.parse(file.read());
//			root = xml.parse(Gdx.files.classpath(filename));
			String source = root.getAttribute("source");
			String path = file.parent().toString() + "/" + source;
		
//			String path = source;
			System.out.println("loadShopParamters "+path);
			dependencies.add(new AssetDescriptor(path, TextureAtlas.class));
			return dependencies;
		} catch (IOException e) {
			throw new GdxRuntimeException("Couldn't load ShopDescrition '" + filename + "'", e);
		}
	}
	
	private ShopDescription loadShopDescription(AssetManager manager, Element root, FileHandle descriptionFile) {
		ShopDescription description = new ShopDescription();
		Array<Element> items = root.getChildrenByName("item");
		for (Element element: items) {
			loadItem(manager, description, element, descriptionFile);
		}
		return description;
	}
	
	private void loadItem(AssetManager manager, ShopDescription description, Element element, FileHandle descriptionFile) {
		Item item = new Item();
		item.name = element.getAttribute("name");
		item.id = element.getIntAttribute("id");
		item.type = element.getAttribute("type");
		item.description = element.getAttribute("description");
		System.out.println(""+item.description);
		
		item.effect = element.getChildByName("effect").getAttribute("type");
		String paramsType = element.getChildByName("effect").getAttribute("paramtype");
		int params = element.getChildByName("effect").getIntAttribute("params");
		if (paramsType.equals("Integer")) {
			if (params > 0) {
				item.effectParams = new Integer[params];
				for (int i = 0; i < params; i++) {
					item.effectParams[i] = element.getChildByName("effect").getIntAttribute("params"+i);
				}
			}
		} else if (paramsType.equals("String")) {
			if (params > 0) {
				item.effectParams = new String[params];
				for (int i = 0; i < params; i++) {
					item.effectParams[i] = element.getChildByName("effect").getAttribute("params"+i);
				}
			}
		}
		
		item.cost = element.getChildByName("cost").getAttribute("type");
		paramsType = element.getChildByName("cost").getAttribute("paramtype");
		params = element.getChildByName("cost").getIntAttribute("params");
	
		if (paramsType.equals("Integer")) {
			if (params > 0) {
				item.costParams = new Integer[params];
				for (int i = 0; i < params; i++) {
					item.costParams[i] = element.getChildByName("cost").getIntAttribute("params"+i);
				}
			}
		} else if (paramsType.equals("String")) {
			if (params > 0) {
				item.costParams = new String[params];
				for (int i = 0; i < params; i++) {
					item.costParams[i] = element.getChildByName("cost").getAttribute("params"+i);
				}
			}
		}
		
		
		item.hh = element.getChildByName("time").getFloatAttribute("hh");
		item.mm = element.getChildByName("time").getFloatAttribute("mm");
		item.ss = element.getChildByName("time").getFloatAttribute("ss");
		item.times = element.getChildByName("time").getIntAttribute("times");
		
//		System.out.println(item.name+" "+item.hh+" "+item.mm+" "+ item.ss+" "+item.times);
		
		item.display = atlas.findRegion(item.name);
		
		description.addItem(item);
	}
	
	static public class Parameters extends AssetLoaderParameters<ShopDescription> {
		
	}
}
