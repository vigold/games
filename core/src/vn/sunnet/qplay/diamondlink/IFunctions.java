package vn.sunnet.qplay.diamondlink;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;

public interface IFunctions extends IFaceAPI, IPayment, IFileIO{
	/*NetWork*/
	boolean isInternet();
	
	String getUserName();
	
	String getDeviceId(); 
	
	/*AdView*/
	
	void showAdView(boolean up);
	void hideAdView();
	void showInterstitial();
	
	void showPauseDialog(Runnable resume, Runnable quit);
	
	/*UserInterface*/
	void showWebDialog(String url);
	void showDebugDialog(Runnable yesCallback);
	void showChatDialog(Runnable yesCallback, Runnable noCallback);
	void showWaitingDialog();
	void hideWaitingDialog();
	String getChatText();
	
	void showCHPlay(String url);
	
	void onBuyCoin(String coinName,float cost, Runnable onStart, Runnable onError, Runnable onSuccess);
	
	void createDialog(final String title, final String context,
			final ButtonDescription positive, final ButtonDescription negative,
			final ButtonDescription neutral, final Command changeInputProcessor);
	
	void createToast(final String context, float time);
	void postScreenshotsToWall();
	
	void track(String category, String action, String label, long value);
	void track(String category, String action, String label, long value, String productName, float productPrice, String transactionID);
	void track(String screenName, String productName, float productPrice, String transactionID);
	void track(String category, long value, String variable ,String label);
}
