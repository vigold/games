package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.ui.MyImage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ClassicRunning extends Running {

	public ClassicRunning(DiamondLink game, GameScreen screen,
			EventListener pauseEventListener) {
		super(game, screen, pauseEventListener);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void initContent() {
		init480x800();
	}
	
	private void init480x800() {
		TextureAtlas atlas = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		
		setBounds(0, 0, DiamondLink.getFixedWith(), DiamondLink.getFixedHeight());
	
		Image im = new Image(manager.get(UIAssets.LOGIN_LAYER_0, Texture.class));
		im.setBounds(0, 0, DiamondLink.getFixedWith(), DiamondLink.getFixedHeight());
		addActor(im);
		
		Texture texture = manager.get(UIAssets.LOGIN_LAYER_1, Texture.class);
		final Image target = new Image(texture);
		target.addAction(Actions.repeat(-1, Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				target.setBounds(DiamondLink.getFixedWith() / 2 - target.getPrefWidth() / 2, -target.getPrefHeight(), target.getPrefWidth(), target.getPrefHeight());
				return true;
			}
		}, Actions.moveTo(0, DiamondLink.getFixedHeight(), 15, Interpolation.linear))));
		addActor(target);
		
		
		alarm = new Image(manager.get(UIAssets.DANGER, Texture.class));
		alarm.setBounds(0, 0, DiamondLink.getFixedWith(), DiamondLink.getFixedHeight());
		alarm.setVisible(false);
		addActor(alarm);
		
		im = new Image(atlas.findRegion("GridBackGround"));
		im.setBounds(screen.gridPos.x + DiamondLink.getFixedWith() / 2 - im.getPrefWidth() / 2,
				180,
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		

		
		for (int i = 0; i < 8 ; i++)
			for (int j = 0; j < 4; j++) {
				im = new Image(atlas.findRegion("InnerBound"));
				im.setBounds(screen.gridPos.x + i * 60, screen.gridPos.y + 60 * ((i % 2 == 0? 1: 0) + j * 2), 60, 60);
				addActor(im);
			}
		
		im = new Image(atlas.findRegion("GridBottom"));
		im.setBounds(DiamondLink.getFixedWith() / 2 - im.getPrefWidth() / 2, 0,
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		
		
		im = new Image(atlas.findRegion("GridTop"));
		im.setBounds(DiamondLink.getFixedWith() / 2 - im.getPrefWidth() / 2, DiamondLink.getFixedHeight() - im.getPrefHeight(),
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		im = new Image(atlas.findRegion("Split"));
		im.setBounds(198, DiamondLink.getFixedHeight() - im.getPrefHeight(),
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		im = new Image(atlas.findRegion("Target"));
		im.setBounds(45, DiamondLink.getFixedHeight() - im.getPrefHeight(),
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		im = new Image(atlas.findRegion("Score"));
		im.setBounds(228, DiamondLink.getFixedHeight() - im.getPrefHeight(),
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		
		
		im = new Image(atlas.findRegion("EmptyTime"));
		im.setBounds(80, 155, 325 , 31);
		addActor(im);
		

		
		timeCounter = new MyImage(atlas.findRegion("FullTime"));
		timeCounter.setBounds(81, 155, 325, 31);
		addActor(timeCounter);
		

		
		BitmapFont font = manager.get(UIAssets.GAME_TIME_FONT, BitmapFont.class);
		LabelStyle labelStyle = new LabelStyle(font, font.getColor());
		timeLabel = new Label("01:30", labelStyle);
		timeLabel.setWrap(true);
		timeLabel.setAlignment(Align.center|Align.center);
		timeLabel.setBounds(0, 0, 457, font.getLineHeight());
		timeWraper = new Table();
		timeWraper.setOrigin(457 / 2, font.getLineHeight() / 2);
		timeWraper.setTransform(true);
		timeWraper.addActor(timeLabel);
		timeWraper.setBounds(14, 167 - 7, 457, font.getLineHeight());
		addActor(timeWraper);
		
		 
		Group group = new Group();
		group.setBounds(0, 16, DiamondLink.getFixedWith(), 129);
		addActor(group);
//		group.addActor(new Image(atlas.findRegion("BottomGrid")));
		
		
		
		
		
		BitmapFont coinFont = manager.get(UIAssets.COIN_FONT, BitmapFont.class);
		
		labelStyle = new LabelStyle(coinFont, Color.WHITE);
		coinLabel = new Label("0", labelStyle);
		coinLabel.setWrap(true);
		coinLabel.setAlignment(Align.center|Align.center);
		coinLabel.setBounds(0, 0, 86, font.getLineHeight());
		coinWraper = new Table();
		coinWraper.setOrigin(80 / 2, font.getLineHeight() / 2);
		coinWraper.setBounds(58, 115 + 15, 86, font.getLineHeight());
		coinWraper.setTransform(true);
		coinWraper.addActor(coinLabel);
		group.addActor(coinWraper);

		
		
		atlas = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		
		itemsBound[0] = new Image(atlas.findRegion("ItemBound"));
		itemsBound[0].setBounds(35, 7, itemsBound[0].getPrefWidth(), itemsBound[0].getPrefHeight());
		addActor(itemsBound[0]);
		
		itemsBound[1] = new Image(atlas.findRegion("ItemBound"));
		itemsBound[1].setBounds(144, 7, itemsBound[1].getPrefWidth(), itemsBound[1].getPrefHeight());
		addActor(itemsBound[1]);
		
		itemsBound[2] = new Image(atlas.findRegion("ItemBound"));
		itemsBound[2].setBounds(251, 7, itemsBound[2].getPrefWidth(), itemsBound[2].getPrefHeight());
		addActor(itemsBound[2]);
		
		itemsBound[3] = new Image(atlas.findRegion("ItemBound"));
		itemsBound[3].setBounds(358, 7, itemsBound[3].getPrefWidth(), itemsBound[3].getPrefHeight());
		addActor(itemsBound[3]);
		
		
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Pause", 0)), new TextureRegionDrawable(
				atlas.findRegion("Pause", 1)));
		button.addListener(pauseEventListener);
		
		button.setBounds(381, 711, 74, 76);
		addActor(button);
		
		BitmapFont scoreFont = manager.get(UIAssets.SMALL_SCORE_FONT, BitmapFont.class);
		labelStyle = new LabelStyle(scoreFont, Color.WHITE);
		scoreLabel = new Label("0", labelStyle);
		scoreLabel.setWrap(true);
		scoreLabel.setAlignment(Align.center|Align.top);
		scoreLabel.setBounds(0, 0, 191, 44);
		scoreBound = new Table();
		scoreBound.setBounds(207, 715 , 191, 44);
		
		scoreBound.addActor(scoreLabel);
		addActor(scoreBound);
		
		targetScore = new Label("0", labelStyle);
		targetScore.setWrap(true);
		targetScore.setAlignment(Align.center|Align.top);
		targetScore.setBounds(0, 0, 170, 44);
		Table table = new Table();
		table.setBounds(12, 715 , 191, 44);
		
		table.addActor(targetScore);
		addActor(table);
		
		avatars[0] = new Image();
		avatars[0].setBounds(60 - 75 / 2, 760 - 75 / 2, 75, 75);
		avatars[1] = new Image();
		avatars[1].setBounds(60 - 75 / 2, 760 - 75 / 2, 25, 25);
	}
	
	private void init640x1136() {
		
	}
	
	private void init750x1334() {
		
	}
	
	private void init1125x2001() {
		
	}
	
	private void init1242x2208() {
		
	}
	
}
