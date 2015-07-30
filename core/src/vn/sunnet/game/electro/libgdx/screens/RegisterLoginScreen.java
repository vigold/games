package vn.sunnet.game.electro.libgdx.screens;

import java.net.HttpURLConnection;
import java.net.URL;


import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.game.electro.libutils.Codes;

import vn.sunnet.game.electro.rooms.ElectroRoomInfo;


import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;


public abstract class RegisterLoginScreen extends AbstractScreen  {
	
	public static final int MAX_LOGIN = 10;
	
	
	public static String faceUserName = "";
	public static String facePassWord = "";
	public static int faceGender = 0;
	public static String facePhone = "";
	public static String faceCompany = "1";
	
	private static final int ACTION_CONNECT = 0;
	private static final int ACTION_LOGIN = 1;
	private static final int ACTION_ENTER_CITY = 2;
	

	private String rootRoomName = "";
	private String rootZoneName = "";
	private String clientName = "";
	private int myAction;
	
	public int people = 0;

	public RegisterLoginScreen(int width, int height, Game game) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		uiStage.act(delta);
		uiStage.draw();
	}
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		
	}
	
	public void setMapRoot(String roomName, String zoneName, String clientName) {
		this.rootRoomName = roomName;
		this.rootZoneName = zoneName;
		this.clientName = clientName;
	}

	
	
	@SuppressWarnings("unused")
	public boolean haveInternet() {
		try {
			URL url = new URL("http://www.google.com");
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// trying to retrieve data from the source. If offline, this line will fail:
			Object objData = urlConnect.getContent();
		} catch (Exception e) {
			// TODO: handle exception
			dismissLoadingDialog();
			return false;
		}
		return true;
	} 
	
	/**
	 * check accounts and login
	 */
	private void login() {
		System.out.println("login in REgister");
		
		showLoadingDialog("Đang đăng nhập hệ thống", new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dismissLoadingDialog();
				createToast("Chưa đăng nhập được", 2f);
			}
		},  LoadingDialog.NEED_TIME_OUT, 15);
		String username = faceUserName;
		String password = facePassWord;
		if (!username.equals("") && !password.equals("")) {
			if (!haveInternet()) {
				showNoNetworkErrorDialog();
				return;
			}	
			
			
			
			myAction = ACTION_CONNECT;
			

			
		}
	}
	
	
	protected boolean isFirstLogin() {
		return true;
	}
	
	public void register_login() {
		if (Gdx.app.getType() == ApplicationType.Android) {
			locate();
		} else {
			locate();
		}
	}
	
	public void locate() {
		register();
		
	}
	
	public void register() {
		
		
	}
	
	protected void showConnectionErrorDialog() {
		// prepare the alert box
		createDialog("Lỗi kết nối",
				"Đã có lỗi trong quá trình kết nối. Xin kiểm tra kết nối và thử lại!", new ButtonDescription("Tiếp tục", new Command() {
					
					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						locate();
					}
				}), null, null, null);
		
	}
	
	protected void showNotLocatePositionDialog() {
		createDialog("Lỗi kết nối", "Xin kết nối lại với sever",
				new ButtonDescription("Tiếp tục", new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						locate();
					}
				}), null, null, null);
		
	}
	
	protected void showNoNetworkErrorDialog() {
		// prepare the alert box
		System.out.println("show NonetworkDialog");
		createDialog("Lỗi kết nối",
				"Chưa kết nối mạng! Bạn hãy bật kết nối mạng để chơi.",
				new ButtonDescription("Tiếp tục", new Command() {

					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						locate();
					}
				}), null, null, null);
	}
	
	protected void showWrongInfoToast(int code) {
		switch (code) {
		case Codes.CODE_LOGIN_WRONG_INFO:
			System.out.println("Lỗi khi đăng nhập");
			createToast("Lỗi khi đăng nhập. Vui lòng thử lại!", 2);
			break;
		case Codes.CODE_LOGIN_CONFLICT:
			createToast("Tài khoản đang được sử dụng", 2);
			break;
		case Codes.CODE_LOGIN_ERROR:
			createToast("Lỗi khi kiểm tra tài khoản, vui lòng thử lại sau", 2);
			break;
		case Codes.CODE_DISCONNECT_SERVER:
			createToast("Lỗi kết nối máy chủ, xin vui lòng thử lại sau", 2);
		default:
			break;
		}
	}
	

	
}
