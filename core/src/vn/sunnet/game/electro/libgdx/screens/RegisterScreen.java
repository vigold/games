package vn.sunnet.game.electro.libgdx.screens;

import vn.sunnet.game.electro.libgdx.scene2d.ui.SpriteSheet;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;


public abstract class RegisterScreen extends AbstractScreen {
	
	public static final int MAX_REGISTER_TIME = 3;

	// private static final int SHOW_CONNECTING_DIALOG = 0;
	// private static final int DISMISS_CONNECTING_DIALOG = 1;
//	private static final int SHOW_REGISTER_DIALOG = 2;
//	private static final int DISMISS_REGISTER_DIALOG = 3;
//	private static final int SHOW_USER_EXIST_TOAST = 4;
//	private static final int SHOW_LENGTH_ERROR_TOAST = 5;
//	private static final int SHOW_ERROR_TOAST = 6;
//	private static final int SHOW_REGISTER_SUCCESS_TOAST = 7;
//	private static final int SHOW_DISCONNECT_TOAST = 8;
//	private static final int SHOW_CONNECT_ERROR_DIALOG = 9;
//	private static final int SHOW_NO_NETWORK_ERROR = 10;

//	private int registerTimes;


	private String username;
	private String password;
	
	
	protected Label usernameLabel;
	protected Label password1Label;
	protected Label password2Label;
	protected TextField usernameTextField;
	protected TextField password1TextField;
	protected TextField password2TextField;
	protected CheckBox male;
	protected CheckBox female;

	@SuppressWarnings({ "unused" })
	private Stage uiStage;
	@SuppressWarnings("unused")
	private SpriteSheet loadingSymbol;
	@SuppressWarnings("unused")
	private Label loadingSlogan;
	private Skin loadingSkin;
	@SuppressWarnings("unused")
	private TextureAtlas loadingAtlas;
	
	public RegisterScreen(int width, int height, Game game) {
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
		super.render(delta);
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
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
		password1Label = new Label("Mật khẩu", loadingSkin);
		password1TextField = new TextField("", loadingSkin);
		password1TextField.setPasswordCharacter('*');
		password1TextField.setPasswordMode(true);
		password2Label = new Label("Mật khẩu", loadingSkin);
		password2TextField = new TextField("", loadingSkin);
		password2TextField.setPasswordCharacter('*');
		password2TextField.setPasswordMode(true);
		male = new CheckBox("Nam", loadingSkin);
		male.setChecked(true);
		female = new CheckBox("Nữ", loadingSkin);
		female.setChecked(false);
		
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
		password1TextField.setTextFieldFilter(new TextFieldFilter() {

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
						&& (textField.getText().length() < 32);
			}});
		password2TextField.setTextFieldFilter(new TextFieldFilter() {

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
						&& (textField.getText().length() < 32);
			}});
	}

	public void register() {
		username = usernameTextField.getText().toString();
		password = password1TextField.getText().toString();
		String password2 = password2TextField.getText().toString();

		if ((username.length() < 6) || (username.length() > 45))
			createToast("Tên tài khoản phải dài từ 6 đến 45 kí tự", 2);
		else if ((password.length() < 6) || (password.length() > 32))
			createToast("Mật khẩu phải dài từ 6 đến 32 kí tự", 2);
			
		else if (!password.equals(password2))
			createToast("Hai mật khẩu không trùng khớp", 2);
		else {
			showLoadingDialog("Đang lấy dữ liệu", new Runnable() {
				
				@Override
				public void run() {
			
					dismissLoadingDialog();
				}
			},  LoadingDialog.NEED_TIME_OUT, 15);
//			if (restClient == null)
//				restClient = new EsRegisterAdapter(this);
//			int gender = 0;
//			if (!male.isChecked())
//				gender = 1;
//			restClient.register(username, password, gender, "", "1");
			return;
		}
	}
	
	public void reset() {
		usernameTextField.setText("");
		password1TextField.setText("");
		password2TextField.setText("");
		male.setChecked(true);
		female.setChecked(false);
	}
	
	@SuppressWarnings("unused")
	private void showConnectionErrorDialog() {
		// prepare the alert box
//		createDialog(
//				"Lỗi kết nối",
//				"Đã có lỗi xảy ra trong quá trình kết nối. Xin vui lòng kiểm tra lại kết nối và thử lại sau!",
//				null, "Tiếp tục", null, null, null);
	}
	
	@SuppressWarnings("unused")
	private void showNoNetworkErrorDialog() {
		// prepare the alert box
//		createDialog(
//				"Lỗi kết nối",
//				"Chưa có kết nối mạng! Bạn hãy bật kết nối mạng để sử dụng phần chơi online.",
//				null, "Tiếp tục", null, null, null);
	}
	
	public abstract void toScreenAfterRegisterSuccess();
	
}
