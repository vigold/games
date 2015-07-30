package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ScoreSummary extends AbstractGroup {
	
	protected GameScreen screen;
	
	protected Image light;
//	protected Image newRecord;
	protected Label curScore;
	protected Group curScoreWrap;
	protected Label addCoin;
	protected Label percentCoin;
//	protected Label exp;
//	protected Label percentExp;
	protected Label bestScore;
//	protected SpriteSheet upScore;
	protected Group panel;
	
	protected EventListener okListener;
	protected EventListener shareListener;
	protected Sound press;
	protected Sound bravo;
	protected Button ok;
	protected Button cancel;
	
	private ParticleEffect firework;
	
	
	public ScoreSummary(DiamondLink game,  GameScreen screen, EventListener shareListener,
			EventListener okListener) {
		super(game, screen);
		this.screen = screen;
		this.okListener = okListener;
		this.shareListener = shareListener;
		initPrivateAssets();
		initContent();
	}

	protected void initContent() {
		setBounds(0, 0, DiamondLink.getFixedWith(), DiamondLink.getFixedHeight());
		Image im = new Image(manager.get(UIAssets.LOGIN_LAYER_0, Texture.class));
//		if (screen.GAME_ID == GameScreen.MINE_DIAMOND) im = new Image(manager.get(UIAssets.MINER_BG, Texture.class));
		addActor(im);
		
		Texture texture = manager.get(UIAssets.LOGIN_LAYER_1, Texture.class);
		final Image im2 = new Image(texture);
		im2.addAction(Actions.repeat(-1, Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				im2.setBounds(DiamondLink.getFixedWith() / 2 - im2.getPrefWidth() / 2, -im2.getPrefHeight(), im2.getPrefWidth(), im2.getPrefHeight());
				return true;
			}
		}, Actions.moveTo(0, DiamondLink.getFixedHeight(), 15, Interpolation.linear))));
		addActor(im2);
		
		im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
		im.setBounds(0, 0, DiamondLink.getFixedWith(), DiamondLink.getFixedHeight());
		
		TextureAtlas atlas = manager.get(UIAssets.SUMMARY_FG, TextureAtlas.class);
		

		light = new Image(atlas.findRegion("Light"));
		light.setBounds(DiamondLink.WIDTH / 2 - 629 / 2, DiamondLink.HEIGHT / 2, 627, 629);
		light.setOrigin(light.getWidth() / 2, light.getHeight() / 2);
		addActor(light);
		light.addAction(Actions.repeat(-1, Actions.rotateBy(360, 4f)));
		
		panel = new Group();
		panel.setBounds(0, 141, DiamondLink.getFixedWith(), 616);
		panel.setOrigin(panel.getWidth() / 2, panel.getHeight() / 2);
		addActor(panel);
		
		
		
		im = new Image(atlas.findRegion("ScoreFrame"));
		im.setBounds(30, 100, 423, 474);
		panel.addActor(im);
		
		
		BitmapFont font = manager.get(UIAssets.SUMMARY_SCORE_FONT, BitmapFont.class);
		LabelStyle style = new LabelStyle(font, Color.ORANGE);
		curScore = new Label(""+0, style);
		curScore.setWrap(true);
		curScore.setAlignment(Align.center|Align.center);
		curScore.setBounds(125, 0, 226, 59);
		
		curScoreWrap = new Group();
		curScoreWrap.setBounds(0, 452, DiamondLink.getFixedWith(), 59);
		curScoreWrap.addActor(curScore);
		curScoreWrap.setTransform(true);
		curScoreWrap.setOrigin(DiamondLink.getFixedWith() / 2, 59 / 2);
		panel.addActor(curScoreWrap);
		
		im = new Image(atlas.findRegion("Cup"));
		im.setBounds(64, 511, im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		panel.addActor(im);
		
		im = new Image(atlas.findRegion("Line"));
		im.setBounds(64, 118 + 16, im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		panel.addActor(im);
		
		im = new Image(atlas.findRegion("Line"));
		im.setBounds(64, 192 + 83 * 2 + 83 / 2 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		panel.addActor(im);
		
		im = new Image(atlas.findRegion("Line"));
		im.setBounds(64, 192 + 83 * 1 + 83 / 2 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		panel.addActor(im);
		
		im = new Image(atlas.findRegion("Line"));
		im.setBounds(64, 192 + 83 * 0 + 83 / 2 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		panel.addActor(im);
		
		
		
		im = new Image(atlas.findRegion("Best"));
		im.setBounds(81, 137, im.getPrefWidth(), im.getPrefHeight());
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		panel.addActor(im);
		
		font = manager.get(UIAssets.SUMMARY_SCORE_FONT, BitmapFont.class);
		style = new LabelStyle(font, Color.ORANGE);
		bestScore = new Label(""+0, style);
		bestScore.setWrap(true);
		bestScore.setAlignment(Align.center|Align.top);
		bestScore.setBounds(0, 0, 209, 59);
		
		Table wrapper = new Table();
		wrapper.setBounds(213, 118 + 20, 209, 59);
		wrapper.add(bestScore);
		wrapper.setTransform(true);
		wrapper.setOrigin(wrapper.getWidth() / 2, wrapper.getHeight() / 2);
		panel.addActor(wrapper);
		
		atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		
		cancel = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Quit", 0)), new TextureRegionDrawable(
				atlas.findRegion("Quit", 1)));
		cancel.addListener(shareListener);	
		cancel.setBounds(151 - 10, 150, cancel.getPrefWidth(), cancel.getPrefHeight());
		cancel.setTransform(true);
		cancel.setOrigin(cancel.getWidth()/2, cancel.getHeight() / 2);
		addActor(cancel);
		
		
		ok = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Next", 0)), new TextureRegionDrawable(
				atlas.findRegion("Next", 1)));
		ok.addListener(okListener);
		ok.setBounds(245 + 10, 150, ok.getPrefWidth(), ok.getPrefHeight());
		ok.setTransform(true);
		ok.setOrigin(ok.getWidth()/2, ok.getHeight() / 2);
		addActor(ok);
	}

	protected void initPrivateAssets() {
		// TODO Auto-generated method stub
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
		bravo = manager.get(SoundAssets.BRAVO_SOUND, Sound.class);
		firework = assets.getParticleEffect(ParticleAssets.FIREWORK);
		firework.setPosition(MathUtils.random(100, DiamondLink.getFixedWith() - 100), MathUtils.random(200, 600));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		firework.draw(batch, Gdx.graphics.getDeltaTime());
		if (firework.isComplete()) {
			firework.reset();
			firework.setPosition(MathUtils.random(100, DiamondLink.getFixedWith() - 100), MathUtils.random(200, 600));
		}
	}
	
	public void updateContent(float addExp, int plusExpPercent , float addCoins, int plusCoinPercent, float newScore, float bestScore) {
		System.out.println("exp o score"+addExp);
//		exp.setText(""+(int)addExp);
		addCoin.setText(""+(int)(addCoins)+(screen.generate.isPercentGold()? " +10%":""));
		this.bestScore.setText(""+Operator.convertNumberToString((int)bestScore));
		curScore.setText(""+Operator.convertNumberToString((int)newScore));
		if (bestScore < newScore) {
//			newRecord.setVisible(true);
			light.setVisible(true);
//			upScore.setVisible(true);

			Assets.playSound(bravo);
		} else {
//			newRecord.setVisible(false);
			light.setVisible(false);
//			upScore.setVisible(true);
		}
//		percentExp.setText("+ " + plusExpPercent + "%");
//		percentCoin.setText("+ " + plusCoinPercent + "%");
	}
	
	public void show(Group father) {
		screen.gGame.iFunctions.showAdView(false);
		screen.gGame.iFunctions.showInterstitial();
		father.addActor(this);
		
		panel.addAction(Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				panel.setScale(0.2f, 0.2f);
				ok.setVisible(false);
				cancel.setVisible(false);
				return true;
			}
		}, Actions.scaleTo(1f, 1f, 2f, Interpolation.bounceOut), new Action() {
			
			@Override
			public boolean act(float arg0) {
				moveSystem.killTarget(curScoreWrap);
				curScoreWrap.setScale(2f);
				curScoreWrap.setOrigin(curScoreWrap.getWidth() / 2, curScoreWrap.getHeight() / 2);
				curScoreWrap.addAction(Actions.sequence(Actions.scaleTo(1f, 1f, 1f, Interpolation.bounce), new Action() {
					
					@Override
					public boolean act(float delta) {
						if (!(screen.getGameID() == GameScreen.CLASSIC_DIAMOND && (screen.levelScore >= screen.targetScore)))
						screen.createSubmitDialog(null);
						ok.setVisible(true);
						cancel.setVisible(true);
						return true;
					}
				}));
				
				return true;
			}
		}));
		
		
	}
	
	public void hide() {
		screen.gGame.iFunctions.hideAdView();
		remove();
	}

	@Override
	public void reset() {
		curScore.setText("0");
		addCoin.setText("0");
//		percentCoin.setText("0");
//		exp.setText("0");
//		percentExp.setText("0");
		bestScore.setText("0");
	}
	
}
