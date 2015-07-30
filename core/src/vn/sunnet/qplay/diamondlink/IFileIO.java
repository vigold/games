package vn.sunnet.qplay.diamondlink;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;

public interface IFileIO {
	/*FileIO*/
	void putBool(String field, boolean value);
	boolean getBool(String field, boolean defaultValue);
	void putString(String field, String value);
	String getString(String field, String defaultString);
	void putInt(String field, int value);
	int getInt(String field, int defaultValue);
	void putFloat(String field, float value);
	float getFloat(String field, float defaultValue);
	
	void putFastBool(String field, boolean value);
	boolean getFastBool(String field, boolean defaultValue);
	void putFastString(String field, String value);
	String getFastString(String field, String defaultString);
	void putFastInt(String field, int value);
	int getFastInt(String field, int defaultValue);
	void putFastFloat(String field, float value);
	float getFastFloat(String field, float defaultValue);
	
	FileHandleResolver getResolver();
}
