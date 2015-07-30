package vn.sunnet.qplay.diamondlink.screens.groups;

import java.util.Random;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.ParticleEffectActor;
import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sun.glass.ui.Size;

public class FrontStage extends Stage {
	
	DiamondLink game;
	GameScreen screen;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	protected Sound press;
	
	protected Image begin;
	protected Image end;
	protected Image timeover;
	protected Image noMoreMove;
	protected Label scoreLabel;
	protected Image card;
	protected Image icon_card;
	protected Image icon_avatar;
	protected Image levelComplete;
	
	protected Image avatars[] = new Image[2];
	
	
	private Shop shop;
	
	private EventListener pauseEventListener;
	private ImageButton goShop;
	
	public FrontStage(DiamondLink game, GameScreen screen, EventListener pauseListener) {
		// TODO Auto-generated constructor stub
		super(new StretchViewport(DiamondLink.getFixedWith(), DiamondLink.getFixedHeight()));
		
		this.game = game;
		this.screen = screen;

		manager = game.getAssetManager();
		assets = game.getAssets();
		moveSystem = game.getMoveSystem();
		
		this.pauseEventListener = pauseListener;
		
		initPrivateAssets();
		initContent();
	}
	
	

	public void initContent() {
		System.out.println("chay cha khong chay con");
		TextureAtlas atlas = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		begin = new Image(atlas.findRegion("Begin"));
		end = new Image(atlas.findRegion("End"));
		timeover = new Image(atlas.findRegion("TimeOver"));
		levelComplete = new Image(atlas.findRegion("LevelComplete"));
		noMoreMove = new Image(atlas.findRegion("NoMoreMove"));
		begin.setBounds(DiamondLink.getFixedWith() / 2 - begin.getPrefWidth() / 2,
				DiamondLink.getFixedHeight()/ 2 - begin.getPrefHeight() / 2,
				begin.getPrefWidth(), begin.getPrefHeight());
		end.setBounds(DiamondLink.getFixedWith() / 2 - end.getPrefWidth() / 2,
				DiamondLink.getFixedHeight()/ 2 - end.getPrefHeight() / 2,
				end.getPrefWidth(), end.getPrefHeight());
		timeover.setBounds(DiamondLink.getFixedWith() / 2 - timeover.getPrefWidth() / 2,
				DiamondLink.getFixedHeight()/ 2 - timeover.getPrefHeight() / 2,
				timeover.getPrefWidth(), timeover.getPrefHeight());
		levelComplete.setBounds(DiamondLink.getFixedWith() / 2 - levelComplete.getPrefWidth() / 2,
				DiamondLink.getFixedHeight()/ 2 - levelComplete.getPrefHeight() / 2,
				levelComplete.getPrefWidth(), levelComplete.getPrefHeight());
		noMoreMove.setBounds(DiamondLink.getFixedWith() / 2 - noMoreMove.getPrefWidth() / 2,
				DiamondLink.getFixedHeight()/ 2 - noMoreMove.getPrefHeight() / 2,
				noMoreMove.getPrefWidth(), noMoreMove.getPrefHeight());
		noMoreMove.setOrigin(noMoreMove.getPrefWidth() / 2, noMoreMove.getPrefHeight() / 2);
		begin.setOrigin(begin.getPrefWidth() / 2, begin.getPrefHeight() / 2);
		end.setOrigin(end.getPrefWidth() / 2, end.getPrefHeight() / 2);
		timeover.setOrigin(timeover.getPrefWidth() / 2, timeover.getPrefHeight() / 2);
		levelComplete.setOrigin(levelComplete.getPrefWidth() / 2, levelComplete.getPrefHeight() / 2);
		addActor(begin);
		addActor(end);
		addActor(timeover);
		addActor(noMoreMove);
		addActor(levelComplete);
		begin.setVisible(false);
		end.setVisible(false);
		timeover.setVisible(false);
		noMoreMove.setVisible(false);
		levelComplete.setVisible(false);
		
//		if (!PlayerInfo.isOnline) return;
		goShop = new ImageButton(new TextureRegionDrawable(atlas.findRegion("GoShop", 0)), new TextureRegionDrawable(atlas.findRegion("GoShop", 1)));
		goShop.setBounds(DiamondLink.getFixedWith() - goShop.getPrefWidth()- 40, 151 - goShop.getPrefHeight() - 7, goShop.getPrefWidth(), goShop.getPrefHeight());
		addActor(goShop);
		goShop.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (DiamondLink.SOUND == 1)
					press.play();
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				showShop();
			}
		});
		goShop.setOrigin(goShop.getWidth() / 2, goShop.getHeight() / 2);
		
		
	}
	
	protected void showShop() {
		game.iFunctions.hideAdView();
		screen.setStepGame(GameScreen.GAME_PAUSED);
		final BuyItemTable dialog = new BuyItemTable(game, screen, null).sendSkills(screen.getSkills());
		dialog.setCloseListener(new Runnable() {
			
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					
					@Override
					public void run() {
						PlayerInfo.coin -= dialog.getPayment();
						game.iFunctions.putInt("coins", (int)PlayerInfo.coin);
						dialog.remove();
						screen.sendSkills(dialog.getSelectedSkills());
						screen.setStepGame(screen.preStep);
					}
				});
			}
		}).show(this.getRoot());
	}
	
	public void reset() {
		clear();
		initContent();
	}

	private void initPrivateAssets() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		if (screen.curStep != GameScreen.GAME_SUMMARY) {
			goShop.setVisible(true);
			if (screen.isDangerous()) {
				if (goShop.getActions().size == 0) {
					goShop.setTransform(true);
					goShop.addAction(Actions.repeat(-1, Actions.sequence(
							Actions.rotateTo(5f, 0.05f, Interpolation.linear),
							Actions.rotateTo(-5f, 0.1f, Interpolation.linear),
							Actions.rotateTo(0,  0.05f, Interpolation.linear), Actions.delay(0.5f))));
				}
			} else {
				goShop.clearActions();
				goShop.setRotation(0);
			}
		} else goShop.setVisible(false);
	}
	
	public boolean isEmptyAvatar(Avatar avatars[]) {
		if (avatars == null) return true;
		for (Avatar avatar: avatars) 
			if (avatar != null) return false;
		
		return true;
	}
	
	public void finish() {
		end.setVisible(false);
		card.remove();
		icon_card.remove();
		icon_avatar.remove();
	}
	
	public void noMoreMoveAction(final Runnable complete) {
		moveSystem.killTarget(noMoreMove);
		
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						noMoreMove.setScale(1f);
						noMoreMove.getColor().a = 1;
						noMoreMove.setVisible(true);

//						Sound sound = manager.get(SoundAssets., Sound.class);
//						Assets.playSound(sound);
					}
				}))
				.beginParallel()
				.push(Tween.from(noMoreMove, ActorAccessor.SCALE_XY, 1f)
						.target(10f, 10f).ease(Bounce.INOUT))
				.push(Tween.from(noMoreMove, ActorAccessor.OPACITY, 1f)
						.target(0).ease(Bounce.INOUT)).end()
				.push(Timeline.createSequence().delay(1f))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						noMoreMove.setVisible(false);
						if (complete != null)
							complete.run();
					}
				})).start(moveSystem);
	}
	
	public void beginAction(final Command onComplete) {
		moveSystem.killTarget(begin);
		card = new Image();
		icon_card = new Image();
		icon_avatar = new Image();
//		addActor(icon_card);
//		addActor(icon_avatar);
//		if (screen.vip != null) {
//			TextureRegion region = null;
//			if (screen.vip.name.equals("GreenDragon")) {
//				region = new TextureRegion(manager.get(UIAssets.GREEN_DRAGON, Texture.class));
//			} else if (screen.vip.name.equals("WhiteTiger")) {
//				region = new TextureRegion(manager.get(UIAssets.WHITE_TIGER, Texture.class));
//			} else if (screen.vip.name.equals("RedPhoenix")) {
//				region = new TextureRegion(manager.get(UIAssets.RED_PHOENIX, Texture.class));
//			} else if (screen.vip.name.equals("BlackTortoise")) {
//				region = new TextureRegion(manager.get(UIAssets.BLACK_TORTOISE, Texture.class));
//			}
//			if (region == null) {
//				return;	
//			}
//			card.setDrawable(new TextureRegionDrawable(region));
//			card.setOrigin(card.getPrefWidth() / 2, card.getPrefHeight() / 2);
//			System.out.println("showCard");
//		} else {
//			System.out.println("showIconCard");
//			showIconCard();
//		}
//		if (isEmptyAvatar(PlayerInfo.myAvatars)) {
//			System.out.println("showIconAvatar");
//			showIconAvatar();
//		}
		
		
		final Image target = card;
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						begin.setScale(1f);
						begin.getColor().a = 1;
						begin.setVisible(true);
						
						addActor(target);
						target.setX(DiamondLink.getFixedWith() / 2 - target.getPrefWidth() / 2); 
						target.setY(DiamondLink.getFixedHeight()/ 2 - target.getPrefHeight() / 2);
						target.setSize(target.getPrefWidth(), target.getPrefHeight());
					}
				}))
				
				.push(Timeline
						.createParallel()

						.push(Tween.from(begin, ActorAccessor.SCALE_XY, 0.5f)
								.target(10f, 10f).ease(Bounce.INOUT))
						.push(Tween.from(begin, ActorAccessor.OPACITY, 0.5f)
								.target(0).ease(Bounce.INOUT))
						.push(Tween.from(target, ActorAccessor.SCALE_XY, 1f)
								.target(0.1f, 0.1f).ease(Quad.INOUT)))

				.push(Timeline.createSequence().delay(2f))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						begin.setVisible(false);
						moveSystem.killTarget(target);
						if (screen.vip != null) {
							target.setDrawable(new TextureRegionDrawable(screen.vip.display));
							target.setBounds(368 + 87 / 2 - 80 / 2, 32 + 87 / 2 - 80 / 2, 80, 80);
						}
//						target.remove();
						onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void completeAction() {
		moveSystem.killTarget(levelComplete);
		
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						levelComplete.setScale(1f);
						levelComplete.getColor().a = 1;
						levelComplete.setVisible(true);
//						alarm.setVisible(false);
						Sound sound = manager.get(SoundAssets.KENG_KENG_SOUND, Sound.class);
						Assets.playSound(sound);
					}
				}))
				.beginParallel()
				.push(Tween.from(levelComplete, ActorAccessor.SCALE_XY, 1f)
						.target(10f, 10f).ease(Bounce.INOUT))
				.push(Tween.from(levelComplete, ActorAccessor.OPACITY, 1f)
						.target(0).ease(Bounce.INOUT)).end()
				.push(Timeline.createSequence().delay(1f))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						levelComplete.setVisible(false);
						lastFeverAction();
					}
				})).start(moveSystem);
	}
	
	public void endAction() {
		moveSystem.killTarget(timeover);
		
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						timeover.setScale(1f);
						timeover.getColor().a = 1;
						timeover.setVisible(true);
//						alarm.setVisible(false);
						Sound sound = manager.get(SoundAssets.KENG_KENG_SOUND, Sound.class);
						Assets.playSound(sound);
					}
				}))
				.beginParallel()
				.push(Tween.from(timeover, ActorAccessor.SCALE_XY, 1f)
						.target(10f, 10f).ease(Bounce.INOUT))
				.push(Tween.from(timeover, ActorAccessor.OPACITY, 1f)
						.target(0).ease(Bounce.INOUT)).end()
				.push(Timeline.createSequence().delay(1f))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						timeover.setVisible(false);
						lastFeverAction();
					}
				})).start(moveSystem);
	}
	
	public void lastFeverAction() {
		moveSystem.killTarget(end);
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						end.setScale(1f);
						end.getColor().a = 1;
						end.setVisible(true);
					}
				}))
				.beginParallel()
				.push(Tween.from(end, ActorAccessor.SCALE_XY, 0.5f)
						.target(10f, 10f).ease(Bounce.INOUT))
				.push(Tween.from(end, ActorAccessor.OPACITY, 0.5f)
						.target(0).ease(Bounce.INOUT)).end()
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						
					}
				})).start(moveSystem);
	}
	
	public void showItem(Vector2 from, Vector2 to, ParticleEffectActor particle, final Command onComplete) {
		particle.setX(from.x); particle.setY(from.y);	
		addActor(particle);
		final ParticleEffectActor target = particle;

		Timeline.createSequence()
				.push(Tween.to(target, ActorAccessor.POS_XY, 2f)
						.target(to.x, to.y).waypoint(to.x, from.y + 120)
						.path(TweenPaths.catmullRom).ease(Linear.INOUT))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						moveSystem.killTarget(target);
						target.remove();
						if (onComplete != null) onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void showComboScore(Label label, final Command onComplete) {

		final Label target = label;
		addActor(target);

		Timeline.createSequence()
				.beginParallel()
				.push(Tween.to(target, ActorAccessor.POS_XY, 1f)
						.targetRelative(0, Diamond.DIAMOND_WIDTH)
						.ease(Linear.INOUT))
				.push(Tween.to(target, ActorAccessor.OPACITY, 1f).target(0)
						.ease(Linear.INOUT)).end()
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						target.remove();
						moveSystem.killTarget(target);
						if (onComplete != null) onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void showSense(Image im, final Command onComplete) {
		addActor(im);
		final Image target = im;
		Timeline.createSequence()
				.beginParallel()
				.push(Tween.to(target, ActorAccessor.SCALE_XY, 1f)
						.target(1.5f, 1.5f).ease(Linear.INOUT))
				.push(Tween.to(target, ActorAccessor.OPACITY, 1f).target(0)
						.ease(Linear.INOUT)).end()
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						target.remove();
						moveSystem.killTarget(target);
						if (onComplete != null) onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void showSlogan(Table label, final Command onComplete) {
		final Table target = label;
		addActor(target);
		
		Timeline.createSequence()

				.push(Timeline
						.createParallel()
						.push(Tween.from(target, ActorAccessor.SCALE_XY, 1f)
								.target(10f, 10f).ease(Bounce.INOUT))
						.push(Tween.from(target, ActorAccessor.OPACITY, 1f)
								.target(0).ease(Bounce.INOUT)))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						target.remove();
						moveSystem.killTarget(target);
						if (onComplete != null) onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	public void showMascot(Sound mascotScream, SpriteSheet mascot, final Command onComplete) {
		final Sound sound1 = mascotScream;
		addActor(mascot);
		final SpriteSheet target = mascot;
		mascot.setScale(1f, 1f);
		Timeline.createSequence()
				.push(Timeline.createSequence().delay(
						0.1f * target.getMaxFrame()))
				.push(Tween.call(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						Assets.playSound(sound1);
					}
				}))
				.push(Timeline
						.createParallel()
						.push(Tween.to(target, ActorAccessor.SCALE_XY, 1f)
								.target(3f, 3f).ease(Bounce.INOUT))
						.push(Tween.to(target, ActorAccessor.OPACITY, 1f)
								.target(0).ease(Bounce.INOUT)))
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						moveSystem.killTarget(target);
						target.remove();
						if (onComplete != null)
							onComplete.execute(null);
					}
				})).start(moveSystem);
	}
	
	private FrontStage me() {
		return this;
	}
	
	
	
}
