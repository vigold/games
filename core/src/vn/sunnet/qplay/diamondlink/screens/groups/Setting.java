package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.LoadingDialog;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.utils.Actions;
import vn.sunnet.qplay.diamondlink.utils.ChangeNameAdapter;
import vn.sunnet.qplay.diamondlink.utils.ChangeNameEventListenter;
import vn.sunnet.qplay.diamondlink.utils.Fields;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class Setting extends Group {
	
	DiamondLink game;
	
	AbstractScreen screen;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	Sound press;
	
	private Image musicBut;
	private Image soundBut;
	private Image vibartorBut;
	private Image notificationBut;
	private ImageButton login;
	private ImageButton logout;
	private ImageButton invite;
	private Runnable closeListener;
	
	String linkgame = "https://play.google.com/store/apps/details?id=vn.sgen.ketgioi";

	private BitmapFont provinceFont;

	private TextField nickName;

	public Setting(DiamondLink game, AbstractScreen screen, Runnable closeCompleted) {
		// TODO Auto-generated constructor stub
		this.game = game;
		
		this.screen = screen;
		
		manager = game.getAssetManager();
		assets = game.getAssets();
		moveSystem = game.getMoveSystem();
		
		this.closeListener = closeCompleted;
		
		initPrivateAssets();
		initContent();
	}
	
	private void initContent() {
		// TODO Auto-generated method stub
		final TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		
		Image im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
		im.setBounds(0, 0, 480, 800);
		addActor(im);
		im.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				hide();
				closeListener.run();
				return true;
			}
		});
		
		im = new Image(atlas.findRegion("SettingFrame"));
		im.setBounds(14, 292, im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		
		BitmapFont font = manager.get(UIAssets.SETTING_FONT, BitmapFont.class);
		Label label = new Label("Music", new Label.LabelStyle(font, font.getColor()));
		label.setBounds(76, 412, label.getPrefWidth(), label.getPrefHeight());
		addActor(label);
		
		label = new Label("Sound", new Label.LabelStyle(font, font.getColor()));
		label.setBounds(260, 412, label.getPrefWidth(), label.getPrefHeight());
		addActor(label);
		
		label = new Label("Notify", new Label.LabelStyle(font, font.getColor()));
		label.setBounds(76, 361, label.getPrefWidth(), label.getPrefHeight());
//		addActor(label);
		
		label = new Label("Ads", new Label.LabelStyle(font, font.getColor()));
		label.setBounds(260, 361, label.getPrefWidth(), label.getPrefHeight());
//		addActor(label);
		

		musicBut = new Image(atlas.findRegion("Tick", (1 - DiamondLink.MUSIC)));
		musicBut.setBounds(170, 404 + 5, musicBut.getPrefWidth(), musicBut.getPrefHeight());
		musicBut.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				DiamondLink.MUSIC = 1 - DiamondLink.MUSIC;
				musicBut.setDrawable(new TextureRegionDrawable(atlas
						.findRegion("Tick", (DiamondLink.MUSIC))));
				
				if (DiamondLink.MUSIC == 0) {
					screen.pauseMusic();
					Gdx.app.log("test", "nhac tat");
				} else {
					screen.playMusic();
					Gdx.app.log("test", "nhac bat");
//					music.play();
				}
			}
		});
		
		addActor(musicBut);
		
		soundBut = new Image(atlas.findRegion("Tick", (1 - DiamondLink.SOUND)));
		soundBut.setBounds(360, 404 + 5, soundBut.getPrefWidth(), soundBut.getPrefHeight());
		addActor(soundBut);
		soundBut.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				DiamondLink.SOUND = 1 - DiamondLink.SOUND;
				soundBut.setDrawable(new TextureRegionDrawable(atlas
						.findRegion("Tick", (DiamondLink.SOUND))));
			}
		});
		
		
		vibartorBut = new Image(atlas.findRegion("Tick", (1 - DiamondLink.ADS)));
		vibartorBut.setBounds(360, 355 + 5, vibartorBut.getPrefWidth(), vibartorBut.getPrefHeight());
//		addActor(vibartorBut);
		vibartorBut.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				DiamondLink.ADS = 1 - DiamondLink.ADS;
				vibartorBut.setDrawable(new TextureRegionDrawable(atlas
						.findRegion("Tick", (DiamondLink.ADS))));
				if (DiamondLink.ADS == 0)
					game.iFunctions.hideAdView();
				else game.iFunctions.showAdView(true);
				
			}
		});
		
		
		
		
		notificationBut = new Image(atlas.findRegion("Tick", (1 - DiamondLink.NOTIFICATION)));
		notificationBut.setBounds(170, 355 + 5, notificationBut.getPrefWidth(), notificationBut.getPrefHeight());
//		addActor(notificationBut);
		notificationBut.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				DiamondLink.NOTIFICATION = 1 - DiamondLink.NOTIFICATION;
				notificationBut.setDrawable(new TextureRegionDrawable(atlas
						.findRegion("Tick", (DiamondLink.NOTIFICATION))));
			}
		});
				
		TextureAtlas atlas2 = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		Button ok = new ImageButton(new TextureRegionDrawable(
				atlas2.findRegion("Back", 0)), new TextureRegionDrawable(
				atlas2.findRegion("Back", 1)));
		ok.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				hide();
				closeListener.run();
			}
		});
		ok.setBounds(47, 292 - 75, ok.getPrefWidth(), ok.getPrefHeight());
		addActor(ok);
		atlas2 = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		ok = new ImageButton(new TextureRegionDrawable(
				atlas2.findRegion("ChangeName", 0)), new TextureRegionDrawable(
				atlas2.findRegion("ChangeName", 1)));
		ok.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
//				hide();
//				closeListener.run();
				String str = new String(nickName.getText());
				if (nickName.getText().length() == 0 || str.trim().length() == 0) {
					screen.createToast("Your name's at least one letter", 2f);
					return;
				}
				changeName(game.iFunctions.getDeviceId(), nickName.getText());
			}
		});
		ok.setBounds(335, 318 + 10, ok.getPrefWidth(), ok.getPrefHeight());
		addActor(ok);
		
		
		atlas2 = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		TextureRegionDrawable cursor = new TextureRegionDrawable(atlas2.findRegion("Split"));
		TextureRegionDrawable selection = new TextureRegionDrawable(atlas2.findRegion("Split"));
		TextFieldStyle textFieldStyle = new TextFieldStyle(
				provinceFont,
				provinceFont.getColor(),
				cursor,
				cursor, null);
		
		nickName = new TextField("", textFieldStyle);
		nickName.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n') textField.getOnscreenKeyboard().show(false);
			}
		});
		
//		nickName.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {
//	        @Override
//	        public void show(boolean visible) {
//	            //Gdx.input.setOnscreenKeyboardVisible(true);
//	        	Gdx.input.getTextInput(new TextInputListener() {
//					
//					@Override
//					public void input(String text) {
//						nickName.setText(text);
//					}
//					
//					@Override
//					public void canceled() {
//						 System.out.println("Cancelled.");
//					}
//				}, "", "");
//	        }
//	    });
		
		
		
		nickName.setTextFieldFilter(new TextFieldFilter() {

			@Override
			public boolean acceptChar(TextField textField, char key) {
				// TODO Auto-generated method stub
				return (Character.isLetterOrDigit(key) || key == ' ') && (key != ')')
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
						&& (textField.getText().length() < 10);
			}});
		
		nickName.setBounds(64, 336, 229, 33);
		addActor(nickName);
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		updateContent();
	}
	
	public void updateContent() {
		if (game.iFunctions.isLoggedin()) {
			if (logout == null) return;
			if (logout.getParent() == null) {
				addActor(logout);
				if (login != null)
				login.remove();
			}
		} else {
			if (login == null) return;
			if (login.getParent() == null) {
				addActor(login);
				if (logout != null)
				logout.remove();
			}
		}
	}
	
	public void updateSettings() {
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		musicBut.setDrawable(new TextureRegionDrawable(atlas.findRegion(
				"Tick", (DiamondLink.MUSIC))));
		soundBut.setDrawable(new TextureRegionDrawable(atlas.findRegion(
				"Tick", (DiamondLink.SOUND))));
		vibartorBut.setDrawable(new TextureRegionDrawable(atlas.findRegion(
				"Tick", (DiamondLink.ADS))));
		notificationBut.setDrawable(new TextureRegionDrawable(
				(atlas.findRegion("Tick",
						(DiamondLink.NOTIFICATION)))));
	}

	private void initPrivateAssets() {
		// TODO Auto-generated method stub
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
		FreeTypeFontGenerator generator = manager.get(UIAssets.DUC_GENERATOR, FreeTypeFontGenerator.class);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		provinceFont = generator.generateFont(parameter);
//		provinceFont = generator.generateFont(30, AbstractScreen.FONT_CHARACTERS, false);
		provinceFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public void show(Group father) {
		father.addActor(this);
		updateSettings();
	}
	
	public void hide() {
		remove();
		provinceFont.dispose();
	}
	
	private void rankFaceFriends() {
//		screen.showLoadingDialog("Xếp hạng bạn bè", new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				screen.dismissLoadingDialog();
//				screen.createDialog("Lỗi", "Mất kết nối máy chủ",
//						new ButtonDescription("Tiếp tục", new Command() {
//
//							@Override
//							public void execute(Object data) {
//								// TODO Auto-generated method stub
//								game.setScreen(game.getLogin());
//							}
//						}), null, null, null);
//			}
//		},  LoadingDialog.NEED_TIME_OUT, 30);
//		EsObject esObject = new EsObject();
//		esObject.setString(Fields.ACTION, Actions.GET_RANK_IN_FRIENDS);
//		esObject.setStringArray(Fields.FACEFRIENDS, game.iFunctions.getFriendsID());
//		esObject.setString(Fields.FACENAME, game.iFunctions.getUserID());
//		screen.nodeAdapter.requestNodePlugin(esObject, "LoginPlugin");	
	}
	
	private void changeName(String deviceid, final String name) {
		game.iFunctions.showWaitingDialog();
		ChangeNameAdapter adapter = new ChangeNameAdapter(new ChangeNameEventListenter() {
			
			@Override
			public void onChangeNameEventSuccess(String resultString) {
				game.iFunctions.hideWaitingDialog();
				screen.createToast(""+resultString, 2f);
				PlayerInfo.offlineName = name;
				game.iFunctions.putString("name", PlayerInfo.offlineName);
			}
			
			@Override
			public void onChangeNameEventFailure(String resultString) {
				game.iFunctions.hideWaitingDialog();
				screen.createToast(""+resultString, 2f);
			}
		});
		adapter.changeName(deviceid, name);
	}
}
