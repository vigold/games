package vn.sunnet.qplay.diamondlink.screens.groups;

import java.util.Random;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.ui.MyImage;
import vn.sunnet.qplay.diamondlink.utils.ModeLeaderBoard;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;


public class WorldList extends Group {
	float plusY = 464 - 428;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	DiamondLink game;
	
	ScrollPane worldScroll;
	Table worldContent;
	Image tabs[] = new Image[3];
	Image logos[] = new Image[3];
	int selectTab = 0;
	private String names[] = {"Tien du", "Xuc Xich", "Cong Cong","Tung hoi","Temp"};
	private int scores[] = {50000, 10000, 2000, 1000, 500, 0};
	
	Array<TextureRegion> avatars;
	
	Image myAvatar = null;
	Image myAvatarInRanks = null;
	Label leftDays = null;
	int me;
	
	BitmapFont provinceFont;
	
	Random random = new Random();
	
	private AtlasRegion noAvatar;
	private Runnable closeComplete;
	private ModeLeaderBoard leaderDatas[] = new ModeLeaderBoard[3];
	private boolean post = false;

	private ImageButton share;

	private Sound press;
	
	public WorldList(DiamondLink game, Runnable closeComplete) {
		this.game = game;
		manager = game.getAssetManager();
		assets = game.getAssets();
		moveSystem = game.getMoveSystem();
		this.closeComplete = closeComplete;
		initPrivateAssets();
		initContent();
	}
	
	private void initPrivateAssets() {
//		TextureAtlas atlas = manager.get(UIAssets.AVATARS, TextureAtlas.class);
//		ShopDescription shop = manager.get(Assets.AVATAR_SHOP, ShopDescription.class);
//		
//		avatars = new Array<TextureRegion>();
//		avatars.clear();
//		for (int i = 0; i < shop.size(); i++) {
//			Item item = shop.get(i);
//			avatars.add(item.display);
//		}
//		
//		atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
//		noAvatar = atlas.findRegion("NoAvatar");
		
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
		FreeTypeFontGenerator generator = manager.get(UIAssets.DUC_GENERATOR, FreeTypeFontGenerator.class);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		provinceFont = generator.generateFont(parameter);
//		provinceFont = generator.generateFont(20, AbstractScreen.FONT_CHARACTERS, false);
		provinceFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	private void initContent() {
		
		setBounds(0, 0, 480, 800);
		
		Image im = new Image(manager.get(UIAssets.BLACK_GLASS, Texture.class));
		im.setBounds(0, 0, 480, 800);
		addActor(im);
		im.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				hide();
				closeComplete.run();
				return true;
			}
		});
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		
		share = new ImageButton(new TextureRegionDrawable(atlas.findRegion("Share", 0)), new TextureRegionDrawable(atlas.findRegion("Share", 1)));
		share.setBounds(480 / 2 - share.getPrefWidth() / 2, 25, share.getPrefWidth(), share.getPrefHeight());
		share.setOrigin(share.getWidth() / 2, share.getHeight() / 2);
		share.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.iFunctions.postScreenshotsToWall();
			}
		});
		share.setTransform(true);
		share.addAction(Actions.repeat(-1, Actions.sequence(
				Actions.rotateTo(5f, 0.05f, Interpolation.linear),
				Actions.rotateTo(-5f, 0.1f, Interpolation.linear),
				Actions.rotateTo(0,  0.05f, Interpolation.linear), Actions.delay(0.5f))));
		
//		Timeline.createSequence()
//		.push(Timeline
//				.createSequence()
//				.push(Tween.to(share, ActorAccessor.ROTATION, 0.05f)
//						.target(-5).ease(Quad.INOUT))
//				.push(Tween.to(share, ActorAccessor.ROTATION, 0.1f)
//						.target(5).ease(Quad.INOUT))
//				.push(Tween.to(share, ActorAccessor.ROTATION, 0.05f)
//						.target(0).ease(Quad.INOUT)).repeat(5, 0))
//		.repeat(Tween.INFINITY, 2).start(moveSystem);

		
		addActor(share);
		share.setVisible(false);
		
		Group total = new Group();
		
		total.setBounds(13, 109, 454, 623);
		
		addActor(total);
		
		
		im = new MyImage(atlas.findRegion("LeaderBoardFrame"));
		im.setBounds(0, 0, im.getPrefWidth(), im.getPrefHeight());
		total.addActor(im);
		
		
		
		
		worldContent = new Table();
		worldScroll = new ScrollPane(worldContent);
		worldScroll.setBounds(26, 29, 408, 449);
		worldScroll.setScrollingDisabled(true, false);
		total.addActor(worldScroll);
		
		tabs[0] = new Image(selectTab == 0 ? atlas.findRegion("Tab", 1)
				: atlas.findRegion("Tab", 0));
		tabs[0].setBounds(169, 548 - tabs[0].getPrefHeight(),
				tabs[0].getPrefWidth(), tabs[0].getPrefHeight());
	

		tabs[1] = new Image(selectTab == 0 ? atlas.findRegion("Tab", 1)
				: atlas.findRegion("Tab", 0));
		tabs[1].setBounds(256, 548 - tabs[1].getPrefHeight(),
				tabs[1].getPrefWidth(), tabs[1].getPrefHeight());

		tabs[2] = new Image(selectTab == 0 ? atlas.findRegion("Tab", 1)
				: atlas.findRegion("Tab", 0));
		tabs[2].setBounds(349, 548 - tabs[2].getPrefHeight(),
				tabs[2].getPrefWidth(), tabs[2].getPrefHeight());
//		updateTab();
		
		for (int i = 0; i < 3; i++) {
			total.addActor(tabs[i]);
			final int index = i;
			tabs[index].addListener(new ClickListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					
					return true;
				}
				
				@Override
				public void touchUp(InputEvent arg0, float arg1, float arg2,
						int arg3, int arg4) {
					if (selectTab != index) {
						selectTab = index;
						updateTab();
					}
					
				}
			});
		}
		
		im = new Image(atlas.findRegion("Classic"));
		im.setBounds(208 - im.getPrefWidth() / 2, 517 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		total.addActor(im);
		im.setTouchable(Touchable.disabled);
		logos[0] = im;
		
		im = new Image(atlas.findRegion("Butterfly"));
		im.setBounds(298 - im.getPrefWidth() / 2, 517 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		total.addActor(im);
		im.setTouchable(Touchable.disabled);
		logos[1] = im;
		
		im = new Image(atlas.findRegion("Miner"));
		im.setBounds(389 - im.getPrefWidth() / 2, 517 - im.getPrefHeight() / 2, im.getPrefWidth(), im.getPrefHeight());
		total.addActor(im);
		im.setTouchable(Touchable.disabled);
		logos[2] = im;
		
		atlas = manager.get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Back", 0)), new TextureRegionDrawable(
				atlas.findRegion("Back", 1)));
		button.setBounds(47, 34, button.getPrefWidth(), button.getPrefHeight());
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
				closeComplete.run();
			}
		});
		addActor(button);
		
	}
	
	public WorldList updateDatas(ModeLeaderBoard...boards){
		for (int i = 0; i < boards.length; i++) {
			leaderDatas[boards[i].getModeId()] = boards[i];
		}
		for (int i = 0; i < 3; i++) {
			if (leaderDatas[i] == null) {
				tabs[i].setVisible(false);
				logos[i].setVisible(false);
			} else {
				if (leaderDatas[selectTab] == null) selectTab = i;
			}
		}
		
		updateTab();
		return this;
	}
	
	private void updateTab() {
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
//		updateContent(PlayerInfo.getNames(PlayerInfo.getBestScore(
//				game.iFunctions, selectTab)), PlayerInfo.getScores(PlayerInfo
//				.getBestScore(game.iFunctions, selectTab)), "You",
//				(int)PlayerInfo.getBestScore(game.iFunctions, selectTab),
//				PlayerInfo.getPosition(PlayerInfo.getBestScore(game.iFunctions,
//						selectTab)));
		updateContent(leaderDatas[selectTab].getNames(), leaderDatas[selectTab].getScores(), leaderDatas[selectTab].getName(), leaderDatas[selectTab].getScore(), leaderDatas[selectTab].getRank());
		for (int i = 0; i < 3; i++) {
			tabs[i].setDrawable(new TextureRegionDrawable(
					selectTab == i ? atlas.findRegion("Tab", 1) : atlas
							.findRegion("Tab", 0)));
		}
	}
	
	public void updateContent(String names[], float scores[], String me, float myScore, int myRank) {
		worldContent.clearChildren();
		
		if (names == null) return;
		
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		Group group = new Group();
		Image bg = new MyImage(atlas.findRegion("MyOrder"));
		bg.setBounds(0, 0, 408, 63);
		group.addActor(bg);
		
		bg = new Image(atlas.findRegion("Me"));
		bg.setBounds(0, 0, 63, 63);
		group.addActor(bg);
		
		BitmapFont font = manager.get(UIAssets.RANK_NAME_FONT, BitmapFont.class);
		
		Label label = new Label(me, new LabelStyle(font, font.getColor()));
		label.setWrap(true);
		label.setAlignment(Align.left|Align.top);
		label.setBounds(89, 0, 183, 63);
		group.addActor(label);
		System.out.println("me"+me);
		
		label = new Label(""+(int)myScore, new LabelStyle(font, font.getColor()));
		label.setWrap(true);
		label.setAlignment(Align.left|Align.center);
		label.setBounds(272, 20, 240, 34);
		group.addActor(label);
		
		label = new Label(""+myRank+" "+suffixRank(myRank), new LabelStyle(provinceFont, Color.BLACK));
		label.setWrap(true);
		label.setAlignment(Align.center|Align.top);
		label.setBounds(380 - label.getPrefWidth(), 63 - 20, label.getPrefWidth(), 20);
		group.addActor(label);

		
		
		if (myRank > -1) {
			worldContent.add(group).pad(1, 1, 5, 1).minWidth(408).minHeight(63).fill();
			worldContent.row();
			share.setVisible(true);
		} else share.setVisible(false);
		share.setVisible(true);
		int len = Math.min(10, scores.length);
		
		
		
		for (int i = 0; i < len; i++) {
			group = new Group();
			switch (i) {
			case 0:
				bg = new MyImage(atlas.findRegion("1st"));
				bg.setBounds(0, 0, 408, 63);
				group.addActor(bg);
				
				break;
			case 1:
				bg = new MyImage(atlas.findRegion("2st"));
				bg.setBounds(0, 0, 408, 63);
				group.addActor(bg);
				
				break;
			case 2:
				bg = new MyImage(atlas.findRegion("3st"));
				bg.setBounds(0, 0, 408, 63);
				group.addActor(bg);
				
				break;
			default:
				bg = new MyImage(atlas.findRegion("OtherOrder"));
				bg.setBounds(0, 0, 408, 63);
				group.addActor(bg);
				int position = (i + 1);
				
				label = new Label(""+ position, new LabelStyle(font, font.getColor()));
				label.setWrap(true);
				label.setAlignment(Align.center|Align.top);
				label.setBounds(0, 0, 72, 63);
				group.addActor(label);
				break;
				
			}
			
			label = new Label(names[i], new LabelStyle(font, font.getColor()));
			label.setWrap(true);
			label.setAlignment(Align.left|Align.top);
			label.setBounds(0, 0, 183, 63);
			Table table = new Table();
			table.setBounds(89, 0, 183, 63);
			table.addActor(label);
			table.setClip(true);
			group.addActor(table);
			
			label = new Label(""+(int)scores[i], new LabelStyle(font, font.getColor()));
			label.setWrap(true);
			label.setAlignment(Align.left|Align.center);
			label.setBounds(0, 5, 240, 34);
			table = new Table();
			table.setBounds(272, 20, 240, 34);
			table.setClip(true);
			table.addActor(label);
			group.addActor(table);
			
			worldContent.add(group).pad(1, 1, 5, 1).minWidth(408).minHeight(63).fill();
			worldContent.row();
		}
		
		for (int i = 0; i < 10 - len; i++) {
			group = new Group();
			worldContent.add(group).pad(1, 1, 5, 1).minWidth(408).minHeight(63).fill();
			worldContent.row();
		}
	}
	
	public void updateAvatars(Avatar[] avatars, int maxAvatar) {
		TextureRegion display = null;
		if (avatars != null) {
			for (int i = 0 ; i < avatars.length; i++) {
				if (avatars[i] != null) {
					display = avatars[i].display;
				}
			}
		}
		if (display != null) {
			if (myAvatar != null)
			myAvatar.setDrawable(new TextureRegionDrawable(display));
			if (myAvatarInRanks != null)
			myAvatarInRanks.setDrawable(new TextureRegionDrawable(display));
		} else {
			if (myAvatar != null)
			myAvatar.setDrawable(new TextureRegionDrawable(noAvatar));
			if (myAvatarInRanks != null)
			myAvatarInRanks.setDrawable(new TextureRegionDrawable(noAvatar));
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		moveSystem.update(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
//		if (post) {
//			game.iFunctions.postScreenshotsToWall();
//			post = false;
//		}
	}
	
	public void show(Group father) {
		father.addActor(this);
//		if (game.getScreen() instanceof GameScreen) //post = true;
//			((GameScreen) game.getScreen()).superCreateDialog("",
//					"Do you want to share your score to your FB friends?",
//					new ButtonDescription("Ok", new Command() {
//
//						@Override
//						public void execute(Object data) {
//							Gdx.app.postRunnable(new Runnable() {
//								
//								@Override
//								public void run() {
//									game.iFunctions.postScreenshotsToWall();
//								}
//							});
//							
//						}
//					}), new ButtonDescription("Cancel", new Command() {
//
//						@Override
//						public void execute(Object data) {
//
//						}
//					}), null, null);
	}
	
	public void hide() {
		remove();
	}
	
	public TextureRegion getDisplay(String avatar) {
		if (assets.totalShop.get(avatar) == null) return null;
		return assets.totalShop.get(avatar).display;
	}
	
	public TextureRegion getModeOfScore(int mode) {
		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		switch (mode) {
		case GameScreen.CLASSIC_DIAMOND:
			return atlas.findRegion("ClassicIcon");
		case GameScreen.BUTTERFLY_DIAMOND:
			return atlas.findRegion("ButterflyIcon");
		case GameScreen.MINE_DIAMOND:
			return atlas.findRegion("MinerIcon");
		}
		return null;
	}
	
	private String suffixRank(int position) {
		// TODO Auto-generated method stub
		int i = position % 10;
		switch (i) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			break;
		}
		return "th";
	}
	
}
