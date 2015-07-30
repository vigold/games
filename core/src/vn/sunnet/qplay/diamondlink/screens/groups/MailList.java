package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.LoadingDialog;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.loaders.MailsDescription;
import vn.sunnet.qplay.diamondlink.loaders.Slogan;
import vn.sunnet.qplay.diamondlink.ui.MyImage;
import vn.sunnet.qplay.diamondlink.utils.Actions;
import vn.sunnet.qplay.diamondlink.utils.Fields;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class MailList extends Group {
	
	private DiamondLink game;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	NodeScreen screen;
	
	Sound press;
	
	ScrollPane mailScroll;
	Table mailContent;
	
	Mail curMail;
	
	
	public MailList(DiamondLink game, NodeScreen screen) {
		// TODO Auto-generated constructor stub
		this.game = game;
		
		manager = game.getAssetManager();
		assets = game.getAssets();
		moveSystem = game.getMoveSystem();
		this.screen = screen;
		
		initPrivateAssets();
		initContent();
	}

	private void initContent() {
		curMail = new Mail(game, screen);
		
		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		Image im = new Image(atlas.findRegion("MailFrame"));
		im.setBounds(14, 141, 450, 526);
		addActor(im);
		
		im = new Image(atlas.findRegion("MailBG"));
		im.setBounds(45, 201, 392, 368);
		addActor(im);
		
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Exit", 0)), new TextureRegionDrawable(
				atlas.findRegion("Exit", 1)));
		button.addListener(new InputListener() {
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
			}
		});
		button.setBounds(405, 622, 68, 68);
		addActor(button);
		
		button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("ReceiveAll", 0)), new TextureRegionDrawable(
				atlas.findRegion("ReceiveAll", 1)));
		button.addListener(new InputListener() {
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
				receiveMail(-1);
				hide();
			}
		});
		button.setBounds(119, 119, 241, 54);
		addActor(button);
		
		mailContent = new Table();
		mailScroll = new ScrollPane(mailContent);
		mailScroll.setBounds(50, 207, 380, 355);
		mailScroll.setScrollingDisabled(true, false);
		addActor(mailScroll);
	}
	
//	public void updateContent(EsObject[] mails) {
//		mailContent.clear();
//		if (mails == null) {
//			System.out.println("Mail khong co");
//			return;
//		}
//		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
////		Skin nameSkin = manager.get(UIAssets.NAME_SKIN, Skin.class);
////		Skin chatSKin = manager.get(UIAssets.CHAT_SKIN, Skin.class);
//		BitmapFont nameFont = assets.getBitmapFont(UIAssets.MENU_NAME_FONT);
//		BitmapFont chatFont = assets.getBitmapFont(UIAssets.CHAT_FONT);
//		MailsDescription description = manager.get(Assets.MAILS, MailsDescription.class);
//		
//		for (int i = 0 ; i < mails.length; i++) {
//			String fromeUser = mails[i].getString(Fields.NICKNAME);
//			String title = mails[i].getString(Fields.TITLE);
//			
//			
//			Group group = new Group();
//			Image im = new MyImage(atlas.findRegion("1stFrame"));
//			im.setBounds(0, 0, 380, 70);
//			group.addActor(im);
//			
//			final EsObject mail = mails[i];
//			final int position = i;
//			
//			im.addListener(new ClickListener() {
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					// TODO Auto-generated method stub
//					Assets.playSound(press);
//					curMail.updateContent(mail, new ClickListener() {
//						@Override
//						public boolean touchDown(InputEvent event, float x,
//								float y, int pointer, int button) {
//							Assets.playSound(press);
//							return true;
//						}
//						
//						@Override
//						public void touchUp(InputEvent arg0, float arg1,
//								float arg2, int arg3, int arg4) {
//							receiveMail(position);
//							curMail.hide();
//						}
//					}, new ClickListener() {
//						@Override
//						public boolean touchDown(InputEvent event, float x,
//								float y, int pointer, int button) {
//							Assets.playSound(press);
//							return true;
//						}
//						
//						@Override
//						public void touchUp(InputEvent arg0, float arg1,
//								float arg2, int arg3, int arg4) {
//							deleteQuestion(position);
//							curMail.hide();
//						}
//					});
//					curMail.show(me());
//				}
//			});
//			
//			im = new Image(atlas.findRegion("ic_launcher"));
//			im.setBounds(0, 5, 60, 60);
//			group.addActor(im);
//			
//			Label label = new Label(fromeUser, new LabelStyle(nameFont, nameFont.getColor()));
//			label.setWrap(true);
//			label.setAlignment(Align.left|Align.center);
//			label.setBounds(70, 35, 380 - 70, 35);
//			group.addActor(label);
//			label.setTouchable(Touchable.disabled);
//			
//			LabelStyle style = new LabelStyle(chatFont, Color.GREEN);
//			label = new Label(title, style);
//			label.setWrap(true);
//			label.setAlignment(Align.left|Align.center);
//			label.setBounds(70, 0, 380 - 70, 35);
//			group.addActor(label);
//			label.setTouchable(Touchable.disabled);
//			
//			Button button = new ImageButton(new TextureRegionDrawable(
//					atlas.findRegion("Receive", 0)), new TextureRegionDrawable(
//					atlas.findRegion("Receive", 1)));
//			button.addListener(new ClickListener() {
//				@Override
//				public void clicked(InputEvent event, float x, float y) {
//					// TODO Auto-generated method stub
//					receiveMail(position);
//				}
//			});
//			button.setBounds(296, 70 / 2 - 55 / 2, 71, 55);
//			group.addActor(button);
//			
//			mailContent.add(group).pad(1, 0, 1, 0).minWidth(380).minHeight(70).fill();
//			mailContent.row();
//		}
//		repairUI(mails.length);
//	}
	
	private void repairUI(int mails ) {
	
		if (mails * 70 < 355) {
			int addGroups = (int) ((355 - mails * 70) / 70);
			for (int i = 0; i < addGroups; i++) {
				Group group = new Group();
				mailContent.add(group).pad(1, 0, 1, 0).minWidth(380).minHeight(70).fill();
				mailContent.row();
			}
		} 
	}

	private void initPrivateAssets() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	public void show(Group father) {
		father.addActor(this);
	}
	
	public void hide() {
		remove();
	}
	
	private void receiveMail(int position) {
		
	}
	
	public void deleteQuestion(final int position) {
		screen.createDialog("Xóa thư", "Bạn chắc chắn muốn xóa?",
				new ButtonDescription("Có", new Command() {

					@Override
					public void execute(Object data) {
						deleteMail(position);
					}
				}), new ButtonDescription("Không", null), null, null);
	}
	
	public void deleteMail(int position) {
		screen.showLoadingDialog("Đang xử lý", new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				screen.dismissLoadingDialog();
				screen.createDialog("Lỗi", "Mất kết nối máy chủ",
						new ButtonDescription("Tiếp tục", new Command() {

							@Override
							public void execute(Object data) {
								// TODO Auto-generated method stub
								game.setScreen(game.getLogin());
							}
						}), null, null, null);
			}
		},  LoadingDialog.NEED_TIME_OUT, 15);
//		EsObject message = new EsObject();
//		message.setString(Fields.ACTION, Actions.DELETE_MAILS);
//		message.setInteger(Fields.POSITION, position);
//		screen.nodeAdapter.requestNodePlugin(message, "LoginPlugin");
	}
	
	private MailList me() {
		return this;
	}
}
