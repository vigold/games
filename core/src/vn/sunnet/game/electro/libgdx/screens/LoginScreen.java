package vn.sunnet.game.electro.libgdx.screens;

import java.net.HttpURLConnection;
import java.net.URL;




import vn.sunnet.game.electro.libgdx.scene2d.ui.SpriteSheet;
import vn.sunnet.game.electro.libutils.Codes;


import vn.sunnet.game.electro.rooms.ElectroRoomInfo;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;


public abstract class LoginScreen extends AbstractScreen {
	
	private static final int ACTION_CONNECT = 0;
	private static final int ACTION_LOGIN = 1;
	private static final int ACTION_ENTER_CITY = 2;
	private static final int REQUES_CODE = 0;

	private static final int SHOW_CONNECTING_DIALOG = 0;
	private static final int DISMISS_CONNECTING_DIALOG = 1;
	private static final int SHOW_CHECK_LOGIN_DIALOG = 2;
	private static final int DISMISS_CHECK_LOGIN_DIALOG = 3;
	private static final int SHOW_ENTER_CITY_DIALOG = 9;
	private static final int DISMISS_ENTER_CITY_DIALOG = 10;
	private static final int SHOW_WRONG_INFO_TOAST = 4;
	private static final int SHOW_DISCONNECT_TOAST = 5;
	private static final int SHOW_CONNECT_ERROR_DIALOG = 6;
	private static final int SHOW_NO_NETWORK_ERROR = 7;
	private static final int SHOW_DUPLICATE_USER_ERROR = 8;
	private static final int INFO_GAME = 14;
	private static final int SHARE_GAME = 15;
	private static final int DOWN_GAME = 16;
	private static final int SOCIAL_GAME = 17;
	private static final int EXIT_GAME = 18;
	
	private Label usernameLabel;
	private Label passwordLabel;
	private TextField usernameTextField;
	private TextField passwordTextField;
	private CheckBox checkBox;
	
	TextFieldFilter ui = null;
	
	private String rootRoomName = "";
	private String rootZoneName = "";
	
	private int myAction;
	
	Preferences preferences = Gdx.app.getPreferences("test");
	
	public LoginScreen(int width, int height, Game game) {
		// TODO Auto-generated constructor stub
		super(width, height, game);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		uiStage.act(delta);
		uiStage.draw();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		Gdx.app.log("test", "what the fuck");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}
	
	/**
	 * check accounts and login
	 */
	public void login() {
		String username = usernameTextField.getText().toString().trim();
		String password = passwordTextField.getText().toString().trim();
		if (!username.equals("") && !password.equals("")) {
			if ((username.length() < 6) || (username.length() > 45)) {
				createDialog("Lỗi tài khoản",
						"Tên tài khoản phải từ 6 đến 45 ký tự", new ButtonDescription("Đồng ý", null), null, null, null);
				
				return;
			}

			if ((password.length() < 0) || (password.length() > 45)) {
				createDialog("Lỗi mật khẩu",
						"Mật khẩu phải từ 6 đến 45 ký tự", new ButtonDescription("Đồng ý", null), null, null, null);
				return;
			}

			if (!haveInternet()) {
				showNoNetworkErrorDialog();

				return;
			}	
			
			
			
			myAction = ACTION_CONNECT;
			showLoadingDialog("Đang kết nối máy chủ", new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dismissLoadingDialog();
					createToast("Chưa thể kết nối đến máy chủ", 2f);
				}
			}, LoadingDialog.NEED_TIME_OUT, 15);

			
		}
	}
	
	/**
	 * to RegisterScreen
	 * 
	 */
	public abstract void toRegister();
	
	public void setMapRoot(String roomName, String zoneName) {
		this.rootRoomName = roomName;
		this.rootZoneName = zoneName;
	}
	
	private void showConnectionErrorDialog() {
		// prepare the alert box
		createDialog(
				"Lỗi kết nối",
				"Đã có lỗi xảy ra trong quá trình kết nối. Xin vui lòng kiểm tra lại kết nối và thử lại sau!",
				new ButtonDescription("", null), null, null, null);

	}
	
	private void showNoNetworkErrorDialog() {
		// prepare the alert box
		createDialog(
				"Lỗi kết nối",
				"Chưa có kết nối mạng! Bạn hãy bật kết nối mạng để sử dụng phần chơi online.",
				new ButtonDescription("", null), null, null, null);
		
	}
	
	private void showWrongInfoToast(int code) {
		switch (code) {
		case Codes.CODE_LOGIN_WRONG_INFO:
			createToast("Sai tên tài khoản hoặc mật khẩu", 2);
			break;
		case Codes.CODE_LOGIN_CONFLICT:
			createToast("Tài khoản đang được sử dụng", 2);
			break;
		case Codes.CODE_LOGIN_ERROR:
			createToast("Lỗi khi kiểm tra tài khoản, vui lòng thử lại sau", 2);
			break;
		default:
			break;
		}
	}
	
	private boolean haveInternet() {
		try {
			URL url = new URL("http://www.google.com");
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// trying to retrieve data from the source. If offline, this line will fail:
			Object objData = urlConnect.getContent();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	} 
	
	public void initUIStage() {
		super.initUIStage();
		usernameLabel = new Label("Tên tài khoản", loadingSkin);
		usernameTextField = new TextField("", loadingSkin);
		usernameTextField.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n') textField.getOnscreenKeyboard().show(false);
			}
		});
		passwordLabel = new Label("Mật khẩu", loadingSkin);
		passwordTextField = new TextField("", loadingSkin);
		passwordTextField.setPasswordCharacter('*');
		passwordTextField.setPasswordMode(true);
		ui = new TextFieldFilter() {
			
			@Override
			public boolean acceptChar(TextField arg0, char arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		passwordTextField.setTextFieldFilter(ui);
		
		checkBox = new CheckBox(" Lưu mật khẩu", loadingSkin);
		
		usernameTextField.setTextFieldFilter(new TextFieldFilter() {

			@Override
			public boolean acceptChar(TextField textField, char key) {
				// TODO Auto-generated method stub
				return Character.isLetterOrDigit(key) && (key != ')')
						&& (key != '(')
						&& (key != ':')
						&& (key != '=')
						&& (key != '?')
						&& (key != '!')
						&& (key != '>')
						&& (key != '<')
						&& (key != '.')
						&& (key != '-')
						&& (key != '_')
						&& (key != '*')
						&& (textField.getText().length() < 45);
			}});
		passwordTextField.setTextFieldFilter(new TextFieldFilter() {

			@Override
			public boolean acceptChar(TextField textField, char key) {
				// TODO Auto-generated method stub
				return Character.isLetterOrDigit(key) && (key != ')')
						&& (key != '(')
						&& (key != ':')
						&& (key != '=')
						&& (key != '?')
						&& (key != '!')
						&& (key != '>')
						&& (key != '<')
						&& (key != '.')
						&& (key != '-')
						&& (key != '_')
						&& (key != '*')
						&& (textField.getText().length() < 45);
			}});
	}

	
}
