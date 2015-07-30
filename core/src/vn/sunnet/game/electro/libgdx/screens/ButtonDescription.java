package vn.sunnet.game.electro.libgdx.screens;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;

import com.badlogic.gdx.scenes.scene2d.EventListener;

public class ButtonDescription {
	
	private String label = "";
	private Command callback;
	
	public ButtonDescription(String label, Command callback) {
		// TODO Auto-generated constructor stub
		this.label = label;
		this.callback = callback;
	}
	
	public void excute() {
		if (callback != null)
			callback.execute(null);
	}
	
	public String name() {
		return label;
	}
}
