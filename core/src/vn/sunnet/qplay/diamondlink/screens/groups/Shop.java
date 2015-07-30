package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.game.electro.libgdx.screens.Toast;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Shop extends AbstractGroup {
	
	private Image avatarTab;
	private Image vipTab;
	private Image exchangeTab;
	
	
	
	private Image selectTab;
	
	private Sound press;
	
	private EventListener payCoinListener;
	private EventListener toBagListener;
	
	private final float cost[] = {0.99f, 4.99f, 9.99f, 19.99f};
	private final int values[] = {1000, 5500, 12000, 26000}; 
	private final String coinPackages[] = {"1000_coins.","5500_coins.","12000_coins.","26000_coins."};
	
	private Label totalCoins;
	private Runnable closeComplete;
	
	public Shop(DiamondLink game, AbstractScreen screen, EventListener payCoinListener, Runnable closeComplete) {
		super(game, screen);
		this.payCoinListener = payCoinListener;
		this.closeComplete = closeComplete;
		
		initPrivateAssets();
		initContent();
	}
	
	protected void initPrivateAssets() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	protected void initContent() {
		setBounds(0, 0, 480, 800);
		Image im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
		im.setBounds(0, 0, 480, 800);
		addActor(im);
		im.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				hide();
				if (closeComplete != null)
				closeComplete.run();
				return true;
			}
		});
		Group total = new Group();
		addActor(total);
		final TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		total.setBounds(240 - 453 / 2, 400 - 388 / 2, 453, 461);
		
		BitmapFont font = manager.get(UIAssets.SHOP_GOLD_FONT, BitmapFont.class);
		
		im = new Image(atlas.findRegion("ShopFrame"));
		total.addActor(im);
		
		Table table = new Table();
		for (int i = 0; i < cost.length; i++) {
			Group group = new Group();
			final int index = i;
			im = new Image(atlas.findRegion("Bounder"));
			group.addActor(im);
			im = new Image(atlas.findRegion(""+(i + 1)));
			im.setBounds(109 - im.getPrefWidth(), 0, im.getPrefWidth(), im.getPrefHeight());
			group.addActor(im);
			
			Label label = new Label("x"+values[i], new Label.LabelStyle(font, font.getColor()));
			label.setWrap(true);
			label.setAlignment(Align.left|Align.top);
			label.setBounds(109, 20, label.getPrefWidth(), label.getPrefHeight());
			group.addActor(label);
			
			final Button button = new ImageButton(new TextureRegionDrawable(
					atlas.findRegion("Buy", 0)), new TextureRegionDrawable(
					atlas.findRegion("Buy", 1)));
			button.setBounds(80 + 5 + 200, 3, button.getPrefWidth(), button.getPrefHeight());
			button.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					if (DiamondLink.SOUND == 1)
						press.play();
					return true;
				}
				
				@Override
				public void touchUp(InputEvent arg0, float arg1, float arg2,
						int arg3, int arg4) {
					if (button.isChecked()) {
						game.iFunctions.onBuyCoin(coinPackages[index], cost[index], null, null, null);
						button.setChecked(false);
					}
//					screen.createToast("Add "+values[index]+" golds", 2f);
//					int value = game.iFunctions.getInt("coins", 0);
//					value += values[index];
//					game.iFunctions.putInt("coins", value);
//					PlayerInfo.coin = value;
				}
			});
			group.addActor(button);
			
			
			
			label = new Label("" + cost[i]+"$", new Label.LabelStyle(font,
					font.getColor()));
			label.setWrap(true);
			label.setBounds(
					button.getX() + button.getWidth() / 2
							- label.getPrefWidth() / 2, 10 + 3,
					label.getPrefWidth(), label.getPrefHeight());
			label.setTouchable(Touchable.disabled);
			label.setAlignment(Align.center|Align.top);
			group.addActor(label);
			
			table.add(group).pad(5, 1, 5, 1).minWidth(420).minHeight(60).fill();
			table.row();
		}
		
		ScrollPane pane = new ScrollPane(table);
		pane.setScrollingDisabled(true, false);
		pane.setBounds(18, 35, 420, 266);
		total.addActor(pane);
		
		totalCoins = new Label("", new Label.LabelStyle(font, font.getColor()));
		totalCoins.setBounds(173, 324, 212, 49);
		totalCoins.setWrap(true);
		totalCoins.setAlignment(Align.right, Align.center);
		total.addActor(totalCoins);
		
		im = new Image(atlas.findRegion("1"));
		im.setBounds(389, 324, im.getPrefWidth(), im.getPrefHeight());
		total.addActor(im);
		
		TextureAtlas atlas2 = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas2.findRegion("Back", 0)), new TextureRegionDrawable(
				atlas2.findRegion("Back", 1)));
		button.setBounds(47, total.getY() - 75, button.getPrefWidth(), button.getPrefHeight());
		button.addListener(new InputListener(){
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
				if (closeComplete != null)
				closeComplete.run();
			}
		});
		addActor(button);
		
	}
	
	public void setFocusScreen(NodeScreen screen) {
		this.screen = screen;
		
	}
	
	public void show(Group father) {
		father.addActor(this);
	}
	
	public void show(Group father, int shopIndex) {
		father.addActor(this);
	}
	
	public void hide() {
		remove();
	}
	
	public Shop me() {
		return this;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void act(float arg0) {
		// TODO Auto-generated method stub
		super.act(arg0);
		totalCoins.setText(""+(int)PlayerInfo.coin);
	}
}
