package vn.sunnet.game.electro.libutils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;



public class EsUtil {

	// public static final Random RAND = new Random();

	private static boolean DEBUG_MODE = false;

	public static void setDebugMode(boolean b) {
		DEBUG_MODE = b;
	}

	public static final NumberFormat MONEY_FORMAT = new DecimalFormat(
			"$###,###,###");

	public static final NumberFormat NUMBER_FORMAT = new DecimalFormat(
			"###,###,###");

	public static final void log(String msg, int priority, Class clazz) {
		if (DEBUG_MODE) {
//			Log.println(priority, "es" + " - " + clazz, msg);
		}
	}

	public static final void log(String msg, Class clazz) {
		if (DEBUG_MODE) {
//			Log.i("caro" + " - " + clazz, msg);
		}
	}

	public static void print(String msg, Object o, Class clazz) {
//		Log.w("caro", "<----- Start print out an oject.");
//		Log.i("caro" + " - " + clazz, msg);
//		System.out.println(o);
//		Log.w("caro", "-----> End of an oject.");
	}

	/**
	 * Lấy và trả về Bitmap từ resource ID
	 * 
	 * @param id
	 * @param context
	 * @return
	 */
	

	/**
	 * Lấy và trả về Bitmap từ resource ID với chiều rộng và cao xác định
	 * 
	 * @param id
	 *            resource ID cần lấy
	 * @param context
	 * @param width
	 *            chiều rộng của ảnh trả về
	 * @param height
	 *            chiều cao của ảnh trả về
	 * @return
	 */
	

	/**
	 * Chờ đến khi một thread dừng hoàn toàn
	 * 
	 * @param thread
	 *            Thread cần dừng
	 */
	public static void stopThread(Thread thread) {
		boolean retry = true;

		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			try {
				digest.update(s.getBytes("UTF8"));
			} catch (UnsupportedEncodingException e) {
				return "";
			}
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(
						(0xFF & messageDigest[i]) | 0x100).substring(1, 3));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
