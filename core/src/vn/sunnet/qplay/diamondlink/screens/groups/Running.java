package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.MusicAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.MyImage;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class Running extends AbstractGroup {
	
	protected GameScreen screen;
	protected Image alarm;
	protected MyImage timeCounter;
	protected Label timeLabel;
	protected Group timeWraper;
	protected Label coinLabel;
	protected Group coinWraper;
	private Music timeWarning;
	protected Label scoreLabel;
	protected Image[] avatars = new Image[2];
	protected float runningCoins = 0;
	protected float targetCoins = 0;
	protected Label targetScore;
	protected Table scoreBound; 
	private boolean isChange = false;
	protected Image[] itemsBound = new Image[4];
	protected Button goShop;
	
	protected EventListener pauseEventListener;
	private Sound press;
	
	
	public Running(DiamondLink game, GameScreen screen, EventListener pauseEventListener) {
		super(game, screen);
		this.screen = screen;
		this.pauseEventListener = pauseEventListener;
		initPrivateAssets();
		initContent();
	}
	
	protected abstract void initContent();

//	protected void initContent() {
//		// TODO Auto-generated method stub
//		switch (screen.GAME_ID) {
//			case GameScreen.CLASSIC_DIAMOND:
//				initContentOfClassicDiamond();
//				break;
//			case GameScreen.BUTTERFLY_DIAMOND:
//				initContentOfButterflyDiamond();
//				break;
//			
//		}
//	}

	protected void initPrivateAssets() {
		// TODO Auto-generated method stub
		timeWarning = manager.get(MusicAssets.TIME_WARNING_MUSIC, Music.class);
		timeWarning.setLooping(true);
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	public void updateContent(Avatar avatars[]) {
		
	}
	
	public void updateContent(float score) {
		scoreLabel.setText(""+Operator.convertNumberToString((int)Math.round(score)));
	}
	
	public void updateContent(int depth) {
		
	}
	
	public void updateTargetScore(float score) {
		targetScore.setText(""+Operator.convertNumberToString((int)Math.round(score)));
	}
	
	public void updateContent(float timeRemain, long coins, float percentTime) {
		targetCoins = coins;
		if (percentTime < 0) percentTime = 0;
		switch (screen.GAME_ID) {
		case GameScreen.CLASSIC_DIAMOND:
			timeCounter.setWidth(325f * percentTime);
			timeLabel.setText(""+ Operator.convertSecondsToMM_SS(Math.round(timeRemain)));
			if (timeRemain < 10 && !screen.isOverTime()) {
				if (!timeWarning.isPlaying()) Assets.playMusic(timeWarning);
				int time = (int) (timeRemain / 0.2f);
				if (( time % 2 == 0 ? 1 : 0) == 1) {
					alarm.setVisible(true);
				} else alarm.setVisible(false);
			} else {
				alarm.setVisible(false);
				Assets.pauseMusic(timeWarning);
			}
			break;
		case GameScreen.MINE_DIAMOND:
			timeCounter.setY(155);
			timeCounter.setWidth(320 * percentTime);
			timeLabel.setText(""+ Operator.convertSecondsToMM_SS(Math.round(timeRemain)));
			if (timeRemain < 10 && !screen.isOverTime()) {
				if (!timeWarning.isPlaying()) Assets.playMusic(timeWarning);
				int time = (int) (timeRemain / 0.2f);
				if (( time % 2 == 0 ? 1 : 0) == 1) {
					alarm.setVisible(true);
				} else alarm.setVisible(false);
			} else {
				alarm.setVisible(false);
				Assets.pauseMusic(timeWarning);
			}
			break;
		}
	}
	
	
	
	public void reset() {
		runningCoins = 0;
		targetCoins = 0;
		scoreLabel.setText("0");
		timeLabel.setText("01:30");
		coinLabel.setText("0");
		if (screen.GAME_ID == GameScreen.CLASSIC_DIAMOND)
		timeCounter.setWidth(325f);
		else if (screen.GAME_ID  == GameScreen.MINE_DIAMOND) timeCounter.setWidth(320);
		alarm.setVisible(false);
	}
	
	public void eatCoinAction() {
//		if (!moveSystem.containsTarget(coinWraper))
//			Timeline.createSequence()
//					.push(Tween.call(new TweenCallback() {
//
//						@Override
//						public void onEvent(int type,
//								BaseTween<?> source) {
//							// TODO Auto-generated method
//							// stub
//							coinWraper.setScale(1f);
//						}
//					}))
//					.push(Tween
//							.to(coinWraper,
//									ActorAccessor.SCALE_XY,
//									0.5f)
//							.target(1.5f, 1.5f)
//							.ease(Elastic.INOUT))
//					.push(Tween
//							.to(coinWraper,
//									ActorAccessor.SCALE_XY,
//									0.5f).target(1f, 1f)
//							.ease(Elastic.INOUT))
//
//					.start(moveSystem);
	}
	
	public void eatTimeAction() {
		if (!moveSystem.containsTarget(timeWraper))
			Timeline.createSequence()
			.push(Tween.call(new TweenCallback() {
				
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					// TODO Auto-generated method stub
					System.out.println("goi eate Time++++++++++++++++++++++++++++++++");
					timeWraper.setScale(1f);
				}
			}))
			.push(Tween
					.to(timeWraper,
							ActorAccessor.SCALE_XY,
							0.5f)
					.target(1.2f, 1.2f)
					.ease(Elastic.INOUT))
			.push(Tween
					.to(timeWraper,
							ActorAccessor.SCALE_XY,
							0.5f).target(1f, 1f)
					.ease(Elastic.INOUT))

			.start(moveSystem);
	}
	
	public void show(Group father) {
		father.addActor(this);
	}
	
	public void hide() {
		remove();
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		runningCoins = Interpolation.linear.apply(runningCoins, targetCoins, 0.1f);
		
		coinLabel.setText(""+ Operator.convertNumberToString(Math.round(runningCoins)));
		if (screen.canShowAd()) {
			for (int i = 0; i < itemsBound.length; i++)
				itemsBound[i].setVisible(false);
		} else {
			for (int i = 0; i < itemsBound.length; i++)
				itemsBound[i].setVisible(true);
		}
	}
	
	public void changeScore() {
		scoreBound.setScale(1f);
		scoreBound
				.addAction(Actions.sequence(Actions.scaleBy(1.5f, 1.5f, 1f,
						Interpolation.bounceIn), Actions.delay(0.5f),
						Actions.scaleBy(1 / 1.5f, 1 / 1.5f, 1f,
								Interpolation.bounceIn)));
	}
	
	public void finish() {
		alarm.setVisible(true);
	}
	
	
}
