package vn.sunnet.qplay.diamondlink.screens;



import com.badlogic.gdx.Screen;

public interface MyScreen extends Screen {
	public int getScreenType();
	public void setBackScreen(MyScreen pScreen);
	public MyScreen getBackScreen();
	public void setToScreen(MyScreen pScreen);
	public MyScreen getToScreen();
	public void setMusicVolume(float volume);
}
