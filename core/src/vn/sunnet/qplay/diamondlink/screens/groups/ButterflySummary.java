package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButterflySummary extends ScoreSummary {
	
	Label butterlfyLabel;
	private Label comboLabel;
	private Label comboMax;

	public ButterflySummary(DiamondLink game, GameScreen screen,
			EventListener shareListener, EventListener okListener) {
		super(game, screen, shareListener, okListener);
	}

	@Override
	protected void initContent() {
		super.initContent();
		
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		Image im = new Image(atlas.findRegion("1"));
		im.setBounds(134 - im.getPrefWidth() / 2, 192 + 83 * 2 + 83 / 2 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		panel.addActor(im);
		
		im = new Image(atlas.findRegion("Butterfly"));
		im.setBounds(134 - im.getPrefWidth() / 2, 192 + 83 * 1 + 83 / 2 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		panel.addActor(im);
		
		atlas = manager.get(UIAssets.SUMMARY_FG, TextureAtlas.class);
		
		im = new Image(atlas.findRegion("Combo"));
		im.setBounds(134 - im.getPrefWidth() / 2, 192 + 83 * 0 + 83 / 2 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		panel.addActor(im);
		
		BitmapFont font = manager.get(UIAssets.SUMMARY_GOLD_FONT, BitmapFont.class);
		
		addCoin = new Label("0", new Label.LabelStyle(font, font.getColor()));
		addCoin.setBounds(213, 192 + 83 * 2 + 83 / 2 - addCoin.getPrefHeight() / 2, 209, 51);
		addCoin.setWrap(true);
		addCoin.setAlignment(Align.center, Align.center);
		panel.addActor(addCoin);
		
		butterlfyLabel = new Label("0", new Label.LabelStyle(font, font.getColor()));
		butterlfyLabel.setBounds(213, 192 + 83 * 1 + 83 / 2 - butterlfyLabel.getPrefHeight() / 2, 209, 51);
		butterlfyLabel.setWrap(true);
		butterlfyLabel.setAlignment(Align.center, Align.center);
		panel.addActor(butterlfyLabel);
		
		comboLabel = new Label("0", new Label.LabelStyle(font, font.getColor()));
		comboLabel.setBounds(213, 192 + 83 * 0 + 83 / 2 - comboLabel.getPrefHeight() / 2, 209, 51);
		comboLabel.setWrap(true);
		comboLabel.setAlignment(Align.center, Align.center);
		panel.addActor(comboLabel);
		
	}
	

	public void updateContent(float addExp, int plusExpPercent, float addCoins,
			int plusCoinPercent, float newScore, float bestScore, int butterflies, int combo, int max) {
		// TODO Auto-generated method stub
		super.updateContent(addExp, plusExpPercent, addCoins, plusCoinPercent,
				newScore, bestScore);
		butterlfyLabel.setText(""+butterflies);
		comboLabel.setText(""+combo);
//		comboMax.setText(""+max);
	}
	
}
