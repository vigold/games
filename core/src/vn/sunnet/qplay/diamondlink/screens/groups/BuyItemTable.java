package vn.sunnet.qplay.diamondlink.screens.groups;

import java.text.BreakIterator;
import java.util.Random;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.items.Item;
import vn.sunnet.qplay.diamondlink.items.Skill;
import vn.sunnet.qplay.diamondlink.loaders.ShopDescription;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.screens.Login;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.MyImage;
import vn.sunnet.qplay.diamondlink.utils.Actions;
import vn.sunnet.qplay.diamondlink.utils.Fields;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;


public class BuyItemTable extends AbstractGroup {
	
	Sound press;
	
	Label numItem[] = new Label[9];
	Image selectItems[] = new Image[9];
	Image items[] = new Image[4];
	
	Skill skills[] = new Skill[4];

	Label totalCoins;
	
	Label skillInfo;
	
	Random random = new Random();
	
	BitmapFont infoFont;
	float eclapsedTime = 0;
	float runningCoins = 0;
	float curCoins = 0;
	float gameCoins = 0;
	float returnCoins = 0;
	
	InfoTable relatedTalbe;
	
	private final String[] effects = { "Eat diamonds in a row",
			"Eat same color diamonds",
			"Eat diamonds in a row and a col",
			"Extra 10 second to playing time", " Earn more 10% gold for one match",
			"Double the probability appearing fire diamonds", "Start with 5 special diamonds",
			"Double the time of combo", "Remove all of white diamonds from match" };
	private final int[] costs = { 600, 1000, 1100, 2000, 2000, 3000, 3000, 5000, 5000};
	
	private BitmapFont coinLargeFont;
	private BitmapFont coinSmallFont;
	private Runnable closeListener;
	private Button back;
	private Group total;

	public BuyItemTable(DiamondLink game, NodeScreen screen, Runnable closeListener) {
		super(game, screen);
		this.closeListener = closeListener;
		initPrivateAssets();
		initContent();
		
	}
	
	protected void initPrivateAssets() {
		for (int i = 0; i < skills.length; i++)
			skills[i] =  null;
		FreeTypeFontGenerator generator = manager.get(UIAssets.DUC_GENERATOR, FreeTypeFontGenerator.class);
		
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		coinSmallFont = generator.generateFont(parameter);
//		coinSmallFont = generator.generateFont(20, AbstractScreen.FONT_CHARACTERS, false);
		coinSmallFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		coinLargeFont = generator.generateFont(parameter);
//		coinLargeFont = generator.generateFont(30, AbstractScreen.FONT_CHARACTERS, false);
		coinLargeFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	protected void initContent() {
		
		setBounds(0, 0, 480, 800);
		
		Image im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
		addActor(im);
		
		total = new Group();
		total.setBounds(14, 127, 455, 639);
		addActor(total);
		
		TextureAtlas atlas = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		TextureAtlas atlas2 = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		TextureAtlas atlas3 = manager.get(UIAssets.ITEMS, TextureAtlas.class);
		
		

		if (screen instanceof GameScreen) im.setVisible(true);
		else im.setVisible(false);

		im = new Image(atlas.findRegion("StoreFrame"));
		total.addActor(im);
		
		Table table = new Table();
		for (int i = 0; i < 9; i++) {
			final int index = i;
			Group group = new Group();
			im = new Image(atlas.findRegion("Line"));
			im.setBounds(0, 0, 404, 110);
			group.addActor(im);
			
			im = new Image(atlas.findRegion("ItemBound"));
			im.setBounds(17, 12, im.getPrefWidth(), im.getPrefHeight());
			group.addActor(im);
			
			im = new Image(atlas3.findRegion(""+(i + 1)));
			im.setBounds(17 + 89 /2 - 70 / 2, 12 + 89 / 2 - 70 / 2, 70, 70);
			group.addActor(im);
			
			selectItems[i] = new Image(atlas.findRegion("NumTab"));
			selectItems[i].setBounds(36, 12, selectItems[i].getPrefWidth(), selectItems[i].getPrefHeight());
			group.addActor(selectItems[i]);
			selectItems[i].setVisible(false);
			
			numItem[i]  = new Label("x0", new LabelStyle(coinSmallFont, Color.WHITE));
			numItem[i].setBounds(36, 12, 53, 25);
			numItem[i].setWrap(true);
			numItem[i].setAlignment(Align.center, Align.center);
			group.addActor(numItem[i]);
			numItem[i].setVisible(false);
			numItem[i].addListener(new ClickListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					removeSkill(index);
					return true;
				}
			});
			
			final Button button = new ImageButton(new TextureRegionDrawable(
					atlas2.findRegion("Buy", 0)), new TextureRegionDrawable(
					atlas2.findRegion("Buy", 1)));
			group.addActor(button);
			button.setBounds(273, 20, button.getPrefWidth(), button.getPrefHeight());
			
			button.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					if (DiamondLink.SOUND == 1)
						Assets.playSound(press);
					return true;
				}
				
				@Override
				public void touchUp(InputEvent arg0, float arg1, float arg2,
						int arg3, int arg4) {
					if (button.isChecked()) {
						addSkill(index);
						button.setChecked(false);
					}
				}
			});
			
			Label label = new Label(""+costs[i], new LabelStyle(coinSmallFont, Color.WHITE));
			label.setBounds(273, 25, 118, 31);
			label.setWrap(true);
			label.setAlignment(Align.center, Align.center);
			group.addActor(label);
			label.setTouchable(Touchable.disabled);
			
			label = new Label(""+effects[i], new LabelStyle(coinSmallFont, Color.LIGHT_GRAY));
			label.setBounds(106, 0, 167, 115);
			label.setWrap(true);
			label.setAlignment(Align.center, Align.center);
			group.addActor(label);
			
			table.add(group).pad(0, 0, 2, 0).minWidth(404).minHeight(110).fill();
			table.row();
			
			im = new Image(atlas2.findRegion("1"));
			im.setBounds(404 - im.getPrefWidth(), 20, im.getPrefWidth(), im.getPrefHeight());
			group.addActor(im);
		}
		
		ScrollPane pane = new ScrollPane(table);
		pane.setBounds(28, 32, 404, 459);
		pane.setScrollingDisabled(true, false);
		total.addActor(pane);
		
		im = new Image(atlas2.findRegion("1"));
		im.setBounds(390, 507, im.getPrefWidth(), im.getPrefHeight());
		total.addActor(im);
		
		BitmapFont font = manager.get(UIAssets.SHOP_GOLD_FONT, BitmapFont.class);
		
		totalCoins = new Label("0", new LabelStyle(font, Color.WHITE));
		totalCoins.setBounds(201, 507, 185, 47);
		totalCoins.setWrap(true);
		totalCoins.setAlignment(Align.right, Align.center);
		total.addActor(totalCoins);
		
		back = new ImageButton(new TextureRegionDrawable(atlas.findRegion(
				"Back", 0)), new TextureRegionDrawable(atlas.findRegion("Back",
				1)));

		back.setBounds(47, 129 - 75, back.getPrefWidth(), back.getPrefHeight());
		back.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					Assets.playSound(press);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (closeListener != null)
					closeListener.run();
			}
		});
		
		addActor(back);
		
	}
	
	private int findItem(int type) {
		for (int i = 0; i < 4; i++) {
			if (skills[i] != null)
				if (skills[i].type == type)
				return i;
		}
		for (int i = 0; i < 4; i++) {
			if (skills[i] == null) return i;
		}
		return -1;
	}
	
	private void addSkill(final int type) {
		int pos = findItem(type);
		if (pos == -1) {
			screen.createToast("You can't buy any items", 2f);
			return;
		}
		
		if (skills[pos] != null) {
			if (skills[pos].isMax()) {
				screen.createToast("You can't buy this item anymore", 2f);
				return;
			}
		} else {
			skills[pos] = new Skill(type, game, null);
		}
		
		curCoins += costs[type];
		if (curCoins > PlayerInfo.coin + gameCoins + returnCoins) {
			Gdx.app.log("test", curCoins+" coin "+PlayerInfo.coin);
			curCoins -= costs[type];
			if (skills[pos].get() == 0)
				skills[pos] = null;
			showShop();
			screen.createToast("Lack of golds", 2f);
			return;
		}
		
		selectItems[type].setVisible(true);
		skills[pos].inc();
		numItem[type].setText(""+skills[pos].get());
		numItem[type].setVisible(true);
	}
	
	private void removeSkill(final int type) {
		int pos = findItem(type); 
		if (pos == -1) return;
		curCoins -= skills[pos].get() * costs[type];
		selectItems[type].setVisible(false);
		numItem[type].setVisible(false);
		skills[pos].count = 0;
		skills[pos] = null;
	}
	
	public void setInfoTable(InfoTable infoTable) {
		relatedTalbe = infoTable;
	} 
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		if (runningCoins < getUsedCoins()) {
			runningCoins = Math.min(runningCoins + 10000 * delta, getUsedCoins());
			
		} else {
			runningCoins = Math.max(runningCoins - 10000 * delta, getUsedCoins());
		}
		totalCoins.setText(""+(int)(PlayerInfo.coin - runningCoins));
	}
	
	public void show(Group father) {
		father.addActor(this);
	}
	
	public void reset() {
		
		for (int i = 0; i < skills.length; i++) {
			skills[i] = null;
		}
		for (int i = 0 ; i < numItem.length; i++) {
			selectItems[i].setVisible(false);
			numItem[i].setVisible(false);
		}
		
		runningCoins = 0;
		curCoins = 0;
		gameCoins = 0;
		returnCoins = 0;
	} 
	
	public void hide() {
		coinLargeFont.dispose();
		coinSmallFont.dispose();
		remove();
	}
	
	public long getPayment() {
		return (long) (curCoins - gameCoins - returnCoins);
	}
	
	public float getUsedCoins() {
		return (curCoins - gameCoins - returnCoins);
	}
	
	public Skill[] getSelectedSkills() {
		return skills;
	}
	
	public BuyItemTable sendSkills(Skill[] skills) {
		reset();
//		this.gameCoins = gameCoins;
		for (int i = 0; i < skills.length; i++) {
			if (skills[i].type != Skill.NONE)
				try {
					this.skills[i] = skills[i].clone();
					selectItems[this.skills[i].type].setVisible(true);
					numItem[this.skills[i].type].setText(""+this.skills[i].get());
					numItem[this.skills[i].type].setVisible(true);
					curCoins += this.skills[i].get() * costs[this.skills[i].type];
					returnCoins = curCoins;
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					curCoins = 0;
					returnCoins = 0;
				}
		}
		return this;
	}
	
	public BuyItemTable setCloseListener(Runnable closeListener) {
		this.closeListener = closeListener;
		return this;
	}
	
	private void showShop() {
		total.setVisible(false);
		back.setVisible(false);
		new Shop(game, screen, null, new Runnable() {
			
			@Override
			public void run() {
				total.setVisible(true);
				back.setVisible(true);
			}
		}).show(this);
	}
	
}
