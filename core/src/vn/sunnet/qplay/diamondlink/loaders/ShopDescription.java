package vn.sunnet.qplay.diamondlink.loaders;

import java.util.ArrayList;

import vn.sunnet.qplay.diamondlink.items.Item;
import vn.sunnet.qplay.diamondlink.math.Iterator;

import com.badlogic.gdx.utils.ObjectMap;

public class ShopDescription {
	ArrayList<String> list = new ArrayList<String>();
	ObjectMap<String, Item> hashmap = new ObjectMap<String, Item>();
	
	public ShopDescription() {
		// TODO Auto-generated constructor stub
		list.clear();
	}
	
	public void addItem(Item item) {
		list.add(item.id+"");
		hashmap.put(item.id+"", item);
	}
	
	public void addItem(Item item, int index) {
		list.add(index, item.id+"");
		hashmap.put(item.id+"", item);
	}
	
	public void remove(int index) {
		String name = list.remove(index);
		hashmap.remove(name);
	}
	
	public void remove(Item item) {
		hashmap.remove(item.id+"");
		list.remove(item.id+"");
	}
	
	public void clear() {
		hashmap.clear();
		list.clear();
	}
	
	public Item get(int index) {
		String name = list.get(index);
		return hashmap.get(name);
	}
	
	public Item get(String id) {
		return hashmap.get(id);
	}
	
	public int size() {
		return list.size();
	}
}
