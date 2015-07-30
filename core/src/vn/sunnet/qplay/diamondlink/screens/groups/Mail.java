package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.utils.Fields;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class Mail extends Group {
	
	private DiamondLink game;
	
	private AssetManager manager;
	private Assets assets;
	private TweenManager moveSystem;
	
	private NodeScreen screen;

	private Label fromUser;
	private Label title;
	private Label context;
	private ImageButton receive;
	private ImageButton delete;
	private ImageButton close;
	private Image gem;
	private Image coin;
	private Label gemValue;
	private Label coinValue;
	private long coins = 0;
	private long gems = 0;
	
	private Sound press;
	
	
	
	public Mail(DiamondLink game, NodeScreen screen) {
		this.game = game;
		this.screen = screen;
		
		this.assets = game.getAssets();
		this.manager = game.getAssetManager();
		this.moveSystem = game.getMoveSystem();
		
		initPrivateAssets();
		initContent();
	}
	
	private void initPrivateAssets() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	private void initContent() {
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		
		TextureAtlas atlas = manager.get(UIAssets.MAIL_FG, TextureAtlas.class);
		
		Image image = new Image(atlas.findRegion("MailFrame"));
		image.setBounds(15, 168, 454, 426);
		addActor(image);
		
		image = new Image(atlas.findRegion("FromUserFrame"));
		image.setBounds(48, 505, 170, 46);
		addActor(image);
		
		image = new Image(atlas.findRegion("TitleFrame"));
		image.setBounds(48, 445, 385, 46);
		addActor(image);
		
		image = new Image(atlas.findRegion("ContextFrame"));
		image.setBounds(48, 272, 386, 167);
		addActor(image);
		BitmapFont nameFont = assets.getBitmapFont(UIAssets.MENU_NAME_FONT);
//		Skin nameSkin = manager.get(UIAssets.NAME_SKIN, Skin.class);
		
		fromUser = new Label("", new LabelStyle(nameFont, nameFont.getColor()));
		fromUser.setBounds(65, 509, 142, 30);
		fromUser.setAlignment(Align.left|Align.center);
		fromUser.setWrap(true);
		addActor(fromUser);
		
		title = new Label("", new LabelStyle(nameFont, nameFont.getColor()));
		title.setBounds(65, 454, 355, 30);
		title.setAlignment(Align.left|Align.center);
		title.setWrap(true);
		addActor(title);
		
		context = new Label("", new LabelStyle(nameFont, nameFont.getColor()));
		context.setBounds(65, 291, 355, 134);
		context.setAlignment(Align.left|Align.top);
		context.setWrap(true);
		addActor(context);
		
		receive = new ImageButton(new TextureRegionDrawable(atlas.findRegion(
				"Receive", 0)), new TextureRegionDrawable(atlas.findRegion(
				"Receive", 1)));
		receive.setBounds(335, 496, 100, 46);
		addActor(receive);
		
		delete = new ImageButton(new TextureRegionDrawable(atlas.findRegion(
				"Delete", 0)), new TextureRegionDrawable(atlas.findRegion(
				"Delete", 1)));
		delete.setBounds(78, 200, 141, 53);;
		addActor(delete);
		
		close = new ImageButton(new TextureRegionDrawable(atlas.findRegion(
				"Close", 0)), new TextureRegionDrawable(atlas.findRegion(
				"Close", 1)));
		close.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Assets.playSound(press);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				hide();
			}
		});
		close.setBounds(250, 200, 141, 53);;
		addActor(close);
		
		atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		gem = new Image(atlas.findRegion("Gem"));
		gem.setBounds(0, 0, 25, 25);
		addActor(gem);
		gem.setVisible(false);
		
		coin = new Image(atlas.findRegion("Coin"));
		coin.setBounds(0, 0, 25, 25);
		addActor(coin);
		coin.setVisible(false);
		
		BitmapFont font = manager.get(UIAssets.COIN_FONT, BitmapFont.class);
		
		gemValue = new Label("", new LabelStyle(font, Assets.GEM_BLUE));
		gemValue.setBounds(0, 0, 75, 30);
		gemValue.setAlignment(Align.left|Align.bottom);
		gemValue.setWrap(true);
		addActor(gemValue);
		gemValue.setVisible(false);
		coinValue = new Label("", new LabelStyle(font, Assets.GOLDEN));
	
		coinValue.setBounds(0, 0, 75, 30);
		coinValue.setAlignment(Align.left|Align.bottom);
		coinValue.setWrap(true);
		addActor(coinValue);
		coinValue.setVisible(false);
		
		
	}
	
//	public void updateContent(EsObject mail, EventListener receiveListener, EventListener deleteListener) {
//		if (mail != null) {
//			String fromUser = mail.getString(Fields.NICKNAME);
//			String title = mail.getString(Fields.TITLE);
//			String context = mail.getString(Fields.CONTEXT);
//			long coin = mail.getLong(Fields.COIN);
//			long gem = mail.getLong(Fields.GEM);
//			coins = 0; gems = 0;
//			if (gem > 0) {
//				this.gem.setVisible(true);
//				this.gem.setBounds(260 - 25, 534, 25, 25);
//				this.gemValue.setX(260);
//				this.gemValue.setY(534);
//				this.gemValue.setText("" + gem);
//				this.gemValue.setVisible(true);
//				gems = gem;
//			} else this.gemValue.setVisible(false);
//			
//			if (coin > 0) {
//				System.out.println("coin la "+coin);
//				this.coin.setVisible(true);
//				this.coin.setBounds(260 - 25, 505, 25, 25);
//				this.coinValue.setX(260);
//				this.coinValue.setY(505);
//				this.coinValue.setText("" + coin);
//				this.coinValue.setVisible(true);
//				coins = coin;
//			} else this.coinValue.setVisible(false);
//			
//			if (coin > 0 || gem > 0) {
//				receive.clearListeners();
//				receive.addListener(receiveListener);
//				receive.setVisible(true);
//			} else receive.setVisible(false);
//			
//			delete.clearListeners();
//			delete.addListener(deleteListener);
//			delete.setVisible(true);
//			
//			this.fromUser.setText(fromUser);
//			this.title.setText(title);
//			this.context.setText(context);
//		}
//	}
	
	public void show(Group father) {
		father.addActor(this);
	}
	
	public void hide() {
		remove();
	}
	
}
