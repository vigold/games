package vn.sunnet.game.electro.libgdx.screens;



import vn.sunnet.game.electro.libgdx.scene2d.ui.SpriteSheet;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;


import vn.sunnet.game.electro.libutils.Codes;
import vn.sunnet.game.electro.libutils.EsUtil;
import vn.sunnet.game.electro.libutils.constant.EsCommands;
import vn.sunnet.game.electro.libutils.constant.EsFields;



import vn.sunnet.game.electro.rooms.ElectroRoomInfo;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;


public abstract class NodeScreen extends AbstractScreen  {
	
	public static final String SMS_EXTRA_CODE = "sunnet_electro";
	
	private static final int SHOW_LOADING_DIALOG = 1;
	private static final int DISMISS_LOADING_DIALOG = 2;
	private static final int SHOW_PAYMENT_SUCCESS_TOAST = 3;
	private static final int CANCEL_LOADING_CONFIRM = 4;
	private static final int EXIT_DIALOG = 5;
	
	private static final int ACTION_LEAVE_AND_ENTER_ROOM = 1001;
	private static final int ACTION_ENTER_ROOM = 1002;
	private static final int ACTION_LOAD_PAYMENT_INFO = 1003;

	private static final int HANDLE_MSG_LOAD_PAYMENT_SUCCESS = 1;
	private static final int HANDLE_MSG_PURCHASE_SUCCESS = 2;
	
	private static final int ACTION_CONNECT = 0;
	private static final int ACTION_LOGIN = 1;
	private static final int ACTION_ENTER_CITY = 2;
	
	private static final int MAX_RELOGIN = 3;
	private int reloginTime = 3; 
	
//	public NodeAdapter nodeAdapter;
	public String messageUniqueId;
//	protected ElectroRoomInfo roomInfo;
	private String paymentPreDesciption;
	
	private Runnable handleWhenDisconnect;
	private Runnable handleWhenReconneted;

	private boolean visible;
	
	private int nodeAction;
	
	private String joiningZoneName;
	private String joiningRoomName;
	private boolean joinFatherRoom;
	
	private int myAction;
	private String clientName = "";
	
	public NodeScreen(int width, int height, Game game) {
		// TODO Auto-generated constructor stub
		super(width, height, game);
	}
}
