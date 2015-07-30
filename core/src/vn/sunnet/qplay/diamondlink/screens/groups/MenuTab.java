package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.DiamondLink;

import com.badlogic.gdx.scenes.scene2d.Group;

public class MenuTab extends AbstractGroup {

	public MenuTab(DiamondLink game, NodeScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void show(Group father) {
		father.addActor(this);
	}

	@Override
	public void hide() {
		remove();
	}

	@Override
	protected void initContent() {
		
	}

	@Override
	protected void initPrivateAssets() {
		// TODO Auto-generated method stub

	}

}
