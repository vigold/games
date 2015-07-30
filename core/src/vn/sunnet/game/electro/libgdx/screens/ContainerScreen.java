package vn.sunnet.game.electro.libgdx.screens;


import vn.sunnet.game.electro.libutils.EsUtil;
import vn.sunnet.game.electro.libutils.constant.EsCommands;
import vn.sunnet.game.electro.libutils.constant.EsFields;
import vn.sunnet.game.electro.rooms.ElectroRoomInfo;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;


public abstract class ContainerScreen extends NodeScreen {
	
	private static final int ACTION_REFRESH_CHILD_LIST = 1000;
	protected int containerAction;
	
	
	public ContainerScreen(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
	}
	
}
