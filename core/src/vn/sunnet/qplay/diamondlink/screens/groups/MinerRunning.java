package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.MyImage;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.audio.Music;
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

public class MinerRunning extends Running {
	
	private Label unitsDigit;
	private Label tensDigit;
	private Label hundredsDight;
	private Label thousandsDigit;
	private Image wheels[];
	
	private Music chain;
	
	public static  final int WHEEL_ON = 1;
	public static final int WHEEL_OFF = 0;
	private int wheelStatus = 0;

	public MinerRunning(DiamondLink game, GameScreen screen,
			EventListener pauseEventListener) {
		super(game, screen, pauseEventListener);
	}
	
	@Override
	protected void initPrivateAssets() {
		// TODO Auto-generated method stub
		super.initPrivateAssets();
//		chain = manager.get(MusicAssets.CHAIN_MUSIC, Music.class);
//		chain.setLooping(true);
	}
	
	@Override
	protected void initContent() {
		// TODO Auto-generated method stub
	
		wheels = new Image[2];
		TextureAtlas atlas = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
	
		Image im = new Image(manager.get(UIAssets.LOGIN_LAYER_0, Texture.class));
		addActor(im);
		
		Texture texture = manager.get(UIAssets.LOGIN_LAYER_1, Texture.class);
		final Image target = new Image(texture);
		target.addAction(Actions.repeat(-1, Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				target.setBounds(240 - target.getPrefWidth() / 2, -target.getPrefHeight(), target.getPrefWidth(), target.getPrefHeight());
				return true;
			}
		}, Actions.moveTo(0, 800, 15, Interpolation.linear))));
		addActor(target);
		
		alarm = new Image(manager.get(UIAssets.DANGER, Texture.class));
		alarm.setVisible(false);
		addActor(alarm);
		
		im = new Image(atlas.findRegion("GridBackGround"));
		im.setBounds(screen.gridPos.x + 240 - im.getPrefWidth() / 2,
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
		im.setBounds(480 / 2 - im.getPrefWidth() / 2, 0,
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		im = new Image(atlas.findRegion("GridTop"));
		im.setBounds(480 / 2 - im.getPrefWidth() / 2, 800 - im.getPrefHeight(),
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		im = new Image(atlas.findRegion("Score"));
		im.setBounds(30, 800 - im.getPrefHeight(),
				im.getPrefWidth(), im.getPrefHeight());
		addActor(im);
		
		
		im = new Image(atlas.findRegion("EmptyTime"));
		im.setBounds(152, 155, 323, im.getPrefHeight());
		addActor(im);
		
		timeCounter = new MyImage(atlas.findRegion("FullTime"));
		timeCounter.setBounds(153, 155, 320, 31);
		addActor(timeCounter);
		
		im = new Image(atlas.findRegion("MeterTable"));
		im.setBounds(9, 150, im.getPrefWidth() + 3, im.getPrefHeight());
		addActor(im);
		
		for (int i = 0; i < 4; i++) {
			im = new Image(atlas.findRegion("MeterCell"));
			im.setBounds(9 + 3 * (i + 1) + i * 30, 153, im.getPrefWidth(), im.getPrefHeight());
			addActor(im);
		}
		
		BitmapFont font = manager.get(UIAssets.COIN_FONT, BitmapFont.class);
		
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		unitsDigit = new Label("0", labelStyle);
		unitsDigit.setWrap(true);
		unitsDigit.setAlignment(Align.center|Align.center);
		unitsDigit.setBounds(9 + 3 * 4 + 30 * 3, 153 + 35, 30, 26);
		addActor(unitsDigit);
		
		tensDigit = new Label("0", labelStyle);
		tensDigit.setWrap(true);
		tensDigit.setAlignment(Align.center|Align.center);
		tensDigit.setBounds(9 + 3 * 3 + 30 * 2, 153 + 35 , 30, 26);
		addActor(tensDigit);
		
		hundredsDight = new Label("0", labelStyle);
		hundredsDight.setWrap(true);
		hundredsDight.setAlignment(Align.center|Align.center);
		hundredsDight.setBounds(9 + 3 * 2 + 30, 153 + 35, 30, 26);
		addActor(hundredsDight);
		
		thousandsDigit = new Label("0", labelStyle);
		thousandsDigit.setWrap(true);
		thousandsDigit.setAlignment(Align.center|Align.center);
		thousandsDigit.setBounds(9 + 3, 153 + 35, 30, 26);
		addActor(thousandsDigit);
		
		font = manager.get(UIAssets.GAME_TIME_FONT, BitmapFont.class);
		labelStyle = new LabelStyle(font, Color.WHITE);
		timeLabel = new Label("01:30", labelStyle);
		timeLabel.setWrap(true);
		timeLabel.setAlignment(Align.center|Align.center);
		timeLabel.setBounds(0, 0, 276, 30);
		timeWraper = new Table();
		timeWraper.setOrigin(276 / 2, 30 / 2);
		timeWraper.setTransform(true);
		timeWraper.addActor(timeLabel);
		timeWraper.setBounds(192, 155 + 5, 276, 30);
		addActor(timeWraper);
		
		Group group = new Group();
		group.setBounds(0, 16, 480, 129);
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
		coinWraper.setBounds(58, 115 + 15 , 86, font.getLineHeight());
		coinWraper.setTransform(true);
		coinWraper.addActor(coinLabel);
		group.addActor(coinWraper);
		// buttons
		
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
		
//		im = new Image(atlas.findRegion("ScoreFrame2"));
//		im.setBounds(22, 696, im.getPrefWidth(), im.getPrefHeight());
//		addActor(im);
		
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Pause", 0)), new TextureRegionDrawable(
				atlas.findRegion("Pause", 1)));
		button.addListener(pauseEventListener);
		
		button.setBounds(381, 711, 74, 76);
		addActor(button);
		
		BitmapFont scoreFont = manager.get(UIAssets.LARGE_SCORE_FONT, BitmapFont.class);
		labelStyle = new LabelStyle(scoreFont, Color.WHITE);
		scoreLabel = new Label("0", labelStyle);
		scoreLabel.setWrap(true);
		scoreLabel.setAlignment(Align.center|Align.center);
		scoreLabel.setBounds(0, 0, 370, 82);
		scoreBound = new Table();
		scoreBound.setBounds(12, 710, 370, 82);
		scoreBound.addActor(scoreLabel);
		addActor(scoreBound);
		
	
//		avatars[0] = new Image();
//		avatars[0].setBounds(60 - 75 / 2, 760 - 75 / 2, 75, 75);
//		avatars[1] = new Image();
//		avatars[1].setBounds(60 - 75 / 2, 760 - 75 / 2, 25, 25);
//		addActor(avatars[0]);
//		addActor(avatars[1]);
	}
	
	public void setWheel(int status) {
		wheelStatus = status;
//		if (status == WHEEL_ON) {
//			
//			wheels[0].addAction(Actions.repeat(-1, Actions.rotateBy(360, 4f)));
//			wheels[1].addAction(Actions.repeat(-1, Actions.rotateBy(360, 4f)));
//			System.out.println("wheel ON");
//			Assets.playMusic(chain);
//		} else {
//			wheels[0].clearActions();
//			wheels[1].clearActions();
//			Assets.pauseMusic(chain);
//		}
	}
	
	public int getWheel() {
		return wheelStatus;
	}

	public void updateContent(int depth) {
		unitsDigit.setText(""+(depth % 10));
		tensDigit.setText(""+((depth % 100) / 10));
		hundredsDight.setText(""+((depth % 1000) / 100));
		thousandsDigit.setText(""+(depth / 1000));
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
	}
}
