package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class AbstractGroup extends Group {
	
	protected DiamondLink game;
	protected AbstractScreen screen;
	protected AssetManager manager;
	protected Assets assets;
	protected TweenManager moveSystem;
	
	public AbstractGroup(DiamondLink game, AbstractScreen screen) {
		this.game = game;
		this.screen = screen;
		this.manager = game.getAssetManager();
		this.assets = game.getAssets();
		this.moveSystem = game.getMoveSystem();
	}
	public abstract void reset();
	public abstract void show(Group father);
	public abstract void hide();
	protected abstract void initContent();
	protected abstract void initPrivateAssets();
	
	public AbstractScreen getScreen() {
		return screen;
	}
}
