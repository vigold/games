package vn.sunnet.game.electro.libgdx.screens;


import vn.sunnet.game.electro.libutils.EsUtil;
import vn.sunnet.game.electro.libutils.constant.EsCommands;
import vn.sunnet.game.electro.libutils.constant.EsFields;
import vn.sunnet.game.electro.rooms.ElectroRoomInfo;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;


public abstract class GameContainerScreen extends ContainerScreen {

	private static final int ACTION_CREATE_GAME = 1;
	private static final int ACTION_JOIN_GAME = 2;

	private static final int HANDLE_CREATE_GAME_SUCCESS = 0;
	private static final int HANDLE_CREATE_GAME_FAILURE = 1;
	private static final int HANDLE_JOIN_GAME_SUCCESS = 2;
	private static final int HANDLE_JOIN_GAME_FAILURE = 3;
	
	private int mAction;
	private int joiningGameId;

	
	public GameContainerScreen(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
		
	}

}
