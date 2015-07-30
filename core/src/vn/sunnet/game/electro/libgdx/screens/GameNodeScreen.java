package vn.sunnet.game.electro.libgdx.screens;


import vn.sunnet.game.electro.libutils.EsUtil;
import vn.sunnet.game.electro.libutils.constant.EsCommands;
import vn.sunnet.game.electro.libutils.constant.EsFields;
import vn.sunnet.game.electro.rooms.ElectroRoomInfo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;


public abstract class GameNodeScreen extends NodeScreen {
	
	private static final int HANDLER_MSG_KICK_BY_MASTER = 0;
	private static final int HANDLER_MSG_KICK_TIME_OUT = 1;

	public GameNodeScreen(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
	}

}
