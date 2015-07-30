package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.ui.MyImage;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class InfoTable extends AbstractGroup {
	
	private MyImage percentIm;
	private Label level;
	private Label percentLabel;
	private Label coins;
	private Label gems;
	
	private EventListener plusCoins;
	private EventListener plusGems;
	
	public InfoTable(DiamondLink game, NodeScreen screen, EventListener plusCoins, EventListener plusGems) {
		super(game, screen);
		this.plusCoins = plusCoins;
		this.plusGems = plusGems;
		initPrivateAssets();
		initContent();
		
	}
	
	protected void initPrivateAssets() {
		
	}
	
	protected void initContent() {// 90 - 8
		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		BitmapFont font = manager.get(UIAssets.EXP_FONT, BitmapFont.class);
		Image im = new Image(atlas.findRegion("ExpFrame"));
		im.setBounds(30, 6, 126, 43);
//		addActor(im);
		
		percentIm = new MyImage(atlas.findRegion("FullExp"));
		percentIm.setBounds(30 + 126 / 2 - 118 / 2, 13.5f, 118 * 0.5f, 28);
//		addActor(percentIm);
		
		im = new Image(atlas.findRegion("Star"));
		im.setBounds(0, -1, 60, 60);
//		addActor(im);
		
		LabelStyle style = new LabelStyle(font, font.getColor());
		level  = new Label("1000", style);
		level.setAlignment(Align.center|Align.center);
		level.setWrap(true);
		level.setBounds(0, 3, 60, 60);
//		addActor(level);
		
		percentLabel = new Label("50%", style);
		percentLabel.setAlignment(Align.center|Align.center);
		percentLabel.setWrap(true);
		percentLabel.setBounds(60, 12, 96, 43);
//		addActor(percentLabel);
		
		im = new Image(atlas.findRegion("CoinFrame")); //90 - 8
		im.setBounds(168, 0, 296, 60);
		addActor(im);
		
		im = new Image(atlas.findRegion("CoinBound"));
		im.setBounds(183, 10, 120, 41);
		addActor(im);
		
		im = new Image(atlas.findRegion("CoinBound"));
		im.setBounds(339, 10, 100, 41);
//		addActor(im);
		
		im = new Image(atlas.findRegion("Coin"));
		im.setBounds(185, 16, 24, 25);
		addActor(im);
		
		im = new Image(atlas.findRegion("Gem"));
		im.setBounds(341, 16, 24, 25);
//		addActor(im);
		
		font = manager.get(UIAssets.COIN_FONT, BitmapFont.class);
		style = new LabelStyle(font, Assets.GOLDEN);
		
		coins = new Label("1", style);
		coins.setAlignment(Align.center|Align.top);
		coins.setWrap(true);
		coins.setBounds(185 + 24, 10 + 57, 100 - 24, 41);
		addActor(coins);
		
		style = new LabelStyle(font, Assets.GEM_BLUE);
		gems = new Label("1", style);
		gems.setAlignment(Align.center|Align.top);
		gems.setWrap(true);
		gems.setBounds(341 + 24, 10 + 57, 80 - 24, 41);
//		addActor(gems);
		
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("BuyGem")), new TextureRegionDrawable(
				atlas.findRegion("BuyGem")));
		button.addListener(plusGems);
		button.setBounds(422, 10, 41, 41);
//		addActor(button);
		
		button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("BuyCoin")), new TextureRegionDrawable(
				atlas.findRegion("BuyCoin")));
		button.addListener(plusCoins);
		button.setBounds(284, 10, 41, 41);
		addActor(button);
		
		setBounds(0, 0, DiamondLink.WIDTH, 60);
	}
	
	public void updateContent(float exp, long coins, long gems) {
		int level = assets.getLevelOfExp(exp);
		float percent = assets.getPercentOfLevel(exp, level);
		percentLabel.setText(""+(int)(percent * 100)+"%");
		percentIm.setWidth(118f * percent);
		this.level.setText(""+level);
		this.coins.setText(""+Operator.convertNumberMToString((int) coins));
		this.gems.setText(""+Operator.convertNumberMToString((int)gems));
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show(Group father) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
