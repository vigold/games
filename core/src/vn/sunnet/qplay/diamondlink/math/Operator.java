package vn.sunnet.qplay.diamondlink.math;



public class Operator {
	
	public static int mask = 0x00000001; 
	public static int ONE_BIT = mask;
	public static int ZERO_BIT = 0;
	
	public static int onBit(int bit, int value) {// bit tinh tu 0
		return (value | (mask << bit));
	}
	
	public static int offBit(int bit, int value) {// bit tinh tu 0
		return (value & ~(mask << bit));
	}
	
	public static int getBit(int bit, int value) {// bit tinh tu 0
		return ((value >> bit) & mask);
	}
	
	public static int setBit(int bit, int value, int bitValue) {// bit tinh tu 0
		assert(bitValue > ONE_BIT);
		if (bitValue == ONE_BIT) return onBit(bit, value);
		return offBit(bit, value);
	}
	
	public static boolean hasOnly(int bit , int value) {
		return (offBit(bit, value) == 0 && value > 0);
	}
	
	public static String convertSecondsToMM_SS(float time) {
		int count = (int) time;
		int mm = count / 60;
		int ss = count % 60;
		String m = (mm < 10? "0"+mm: ""+mm);
		String s = (ss < 10? "0"+ss: ""+ss);
		return m+":"+s;
	}
	
	public static String convertNumberToString(int num) {
		String result = "";
		while (num / 1000 > 0) {
			String str = ""+(num % 1000);
			int len = str.length();
			for (int i = 0; i < 3 - len; i++) {
				str = "0" + str;
			}
			
			result = "," + str + result;
			num /= 1000;
		}
		result = (num % 1000) + result;
		return result;
	}
	
	public static String convertNumber10KToString(int num) {
		
		String result = "";
		if (num / 10000 > 0) {
			result = "K";
			num = num / 1000;
		}
		while (num / 1000 > 0) {
			String str = ""+(num % 1000);
			int len = str.length();
			for (int i = 0; i < 3 - len; i++) {
				str = "0" + str;
			}
			
			result = "," + str + result;
			num /= 1000;
		}
		result = (num % 1000) + result;
		return result;
	}
	
	public static String convertNumberMToString(int num) {
		
		String result = "";
		if (num / 100000 > 0) {
			result = "K";
			num = num / 1000;
		}
		while (num / 1000 > 0) {
			String str = ""+(num % 1000);
			int len = str.length();
			for (int i = 0; i < 3 - len; i++) {
				str = "0" + str;
			}
			
			result = "," + str + result;
			num /= 1000;
		}
		result = (num % 1000) + result;
		return result;
	}
}
