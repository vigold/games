package vn.sunnet.qplay.diamondlink.items;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.regexp.internal.recompile;

public class Item {
	public static int BASED_ON_TIMES = 1;
	public static int BASED_ON_HHMMSS = 0;
	
	/*For Default Template In Shop*/
	public TextureRegion display;
	
	public String name ="";
	public String description ="";
	public String type;
	public int id;
	public Object effect;
	public Object[] effectParams;
	public Object cost;
	public Object[] costParams;
	
	public float hh = 0;
	public float mm = 0;
	public float ss = 0;
	public int times = 0;
	/*For Current Item in Bag*/
	public double timeTotal = 0;
	public int expiredType = BASED_ON_HHMMSS;
	public int posInBag = -1;
	
	
	public String getCostType() {
		return (String)cost;
	}
	
	public int getCostValue() {
		return (Integer)costParams[0];
	}
	
	
	
	public String getType() {
		return type;
	}
	
	public float getDefaultExpiredTime() {
		return (times!= 0? times : hh * 3600 + mm * 60 + ss);
	}
	
	public boolean isExpired() {
		if (timeTotal == 0) return true;
		return false;
	}
	
	public String getExpiredTimeString() {
		int time = (int) Math.round(timeTotal);
		int hh =  time / 3600;
		int mm = (time % 3600) / 60;
		int ss = ((time % 3600) % 60);
		String result ="";
		
		if (this.expiredType == BASED_ON_TIMES) {// loai tính theo lần
			if (times == Integer.MIN_VALUE) result = "";
			else result = "Còn" + (int) timeTotal +" lần";		
		} else {
			String h = ((hh < 10) ? "0" : "") + hh;
			String m = ((mm < 10) ? "0" : "") + mm;
			String s = ((ss < 10) ? "0" : "") + ss;
			result = "Còn "+h+":"+m+":"+s;
		}
		return result;
	}
	
	public int getDefaultExpiredType() {
		return (times != 0 ? BASED_ON_TIMES : BASED_ON_HHMMSS);
	}
	
	public int getExpiredType() {
		return expiredType;
	}
	
	public boolean isTemplate() {
		return posInBag == -1;
	}
}
