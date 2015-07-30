package vn.sunnet.qplay.diamondlink.loaders;

import java.util.ArrayList;

import com.badlogic.gdx.utils.ObjectMap;

public class MailsDescription {
	
	ArrayList<String> list = new ArrayList<String>();
	ObjectMap<String, Slogan> hashmap = new ObjectMap<String, Slogan>();
	
	public MailsDescription() {
		list.clear();
	}
	
	public void addItem(Slogan slogan) {
		list.add(slogan.type + "");
		hashmap.put(slogan.type + "", slogan);
	}
	
	public void addItem(Slogan slogan, int index) {
		list.add(index, slogan.type + "");
		hashmap.put(slogan.type + "", slogan);
	}
	
	public void remove(int index) {
		String name = list.remove(index);
		hashmap.remove(name);
	}
	
	public void remove(Slogan slogan) {
		hashmap.remove(slogan.type +"");
		list.remove(slogan.type +"");
	}
	
	public void clear() {
		hashmap.clear();
		list.clear();
	}
	
	/**
	 * 
	 * @param index position of list 
	 * @return Slogan
	 */
	
	public Slogan get(int index) {
		String name = list.get(index);
		return hashmap.get(name);
	}
	
	/**
	 * 
	 * @param name name of slogan
	 * @param index type of slogan
	 * @return Slogan
	 */
	
	public Slogan get(String type) {
		return hashmap.get(type);
	}
	
	public int size() {
		return list.size();
	}
}
