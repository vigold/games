package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.ActorAccessor;
import vn.sunnet.qplay.diamondlink.ui.SpriteSheet;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class RanksSummary extends Group {
	
	GameScreen screen;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	EventListener okListener;
	EventListener shareListener;
	private Label rankInWorld;
	private Label rankInFriends;
	private Label nickName1;
	private Label nickName2;
	private Label scoreLabel1;
	private Label scoreLabel2;
	private SpriteSheet upWorldRank;
	private SpriteSheet upFriendsRank;
	
	public RanksSummary(DiamondLink game, GameScreen screen, EventListener shareListener,
			EventListener okListener) {
		this.screen = screen;
		
		this.manager = game.getAssetManager();
		this.assets = game.getAssets();
		this.moveSystem = game.getMoveSystem();

		this.okListener = okListener;
		this.shareListener = shareListener;

		initPrivateAssets();
		initContent();
	}

	private void initContent() {
		// TODO Auto-generated method stub
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		
		Image im = new Image(manager.get(UIAssets.GAME_BG, Texture.class));
		if (screen.GAME_ID == GameScreen.MINE_DIAMOND) im = new Image(manager.get(UIAssets.MINER_BG, Texture.class));
		addActor(im);
		
		TextureAtlas atlas = manager.get(UIAssets.SUMMARY_FG, TextureAtlas.class);
		im = new Image(atlas.findRegion("LevelupFrame"));
		im.setBounds(32, 143, 430, 451);
		addActor(im);
		
		im = new Image(atlas.findRegion("Light"));
		im.setBounds(DiamondLink.WIDTH / 2 - 627 / 2, DiamondLink.HEIGHT / 2 - 629 / 2, 627, 629);
		im.setOrigin(im.getWidth() / 2, im.getHeight() / 2);
		addActor(im);
		
		im.addAction(Actions.repeat(-1, Actions.rotateBy(360, 4f)));
//		Tween.to(im, ActorAccessor.ROTATION, 4).targetRelative(360).ease(Linear.INOUT)
//		.repeat(Tween.INFINITY, 0).start(moveSystem);
		
		im = new Image(atlas.findRegion("NewRecordText"));
		im.setBounds(51, 592, 377, 93);
		addActor(im);
		
		im = new Image(atlas.findRegion("UpWorldFrame"));
		im.setBounds(47, 318, 387, 138);
		addActor(im);
		
		im = new Image(atlas.findRegion("UpFriendFrame"));
		im.setBounds(47, 159, 387, 138);
		addActor(im);
		
		atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		im = new Image(atlas.findRegion("1stFrame"));
		im.setBounds(110, 173, 313, 65);
		addActor(im);
		im = new Image(atlas.findRegion("1stFrame"));
		im.setBounds(110, 333, 313, 65);
		addActor(im);
		
		im = new Image(atlas.findRegion("Star"));
		im.setX(185); im.setY(347);
		addActor(im);
		im = new Image(atlas.findRegion("Star"));
		im.setX(185); im.setY(185);
		addActor(im);
		
		BitmapFont font = manager.get(UIAssets.WORLD_RANK_FONT, BitmapFont.class);
		LabelStyle style = new LabelStyle(font, font.getColor());
		rankInWorld = new Label("Xếp thứ 1", style);
		rankInWorld.setWrap(true);
		rankInWorld.setAlignment(Align.center|Align.center);
		rankInWorld.setBounds(167, 412 + 40, 266, 48);
		addActor(rankInWorld);
		
		font = manager.get(UIAssets.FRIEND_RANK_FONT, BitmapFont.class);
		style = new LabelStyle(font, font.getColor());
		rankInFriends = new Label("Xếp thứ 1", style);
		rankInFriends.setWrap(true);
		rankInFriends.setAlignment(Align.center|Align.center);
		rankInFriends.setBounds(167, 252 + 40, 266, 48);
		addActor(rankInFriends);
		
		BitmapFont nameFont = assets.getBitmapFont(UIAssets.MENU_NAME_FONT);
		
		
		nickName1 = new Label("vutunglinh1", new LabelStyle(nameFont, nameFont.getColor()));
		nickName1.setWrap(true);
		nickName1.setAlignment(Align.left|Align.top);
		nickName1.setBounds(239, 371, 194, 30);
		addActor(nickName1);
		
		
		nickName2 = new Label("vutunglinh1", new LabelStyle(nameFont, nameFont.getColor()));
		nickName2.setWrap(true);
		nickName2.setAlignment(Align.left|Align.top);
		nickName2.setBounds(239, 212, 194, 30);
		addActor(nickName2);
		
	
		
		scoreLabel1 = new Label("0", new LabelStyle(nameFont, nameFont.getColor()));
		scoreLabel1.setWrap(true);
		scoreLabel1.setAlignment(Align.left|Align.top);
		scoreLabel1.setBounds(239, 333, 194, 40);
		addActor(scoreLabel1);
		
		
		scoreLabel2 = new Label("0", new LabelStyle(nameFont, nameFont.getColor()));
		scoreLabel2.setWrap(true);
		scoreLabel2.setAlignment(Align.left|Align.top);
		scoreLabel2.setBounds(239, 173, 194, 40);
		addActor(scoreLabel2);
		
		atlas = manager.get(UIAssets.SUMMARY_FG, TextureAtlas.class);
		Button button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Share", 0)), new TextureRegionDrawable(
				atlas.findRegion("Share", 1)));
		button.addListener(shareListener);	
		button.setBounds(240, 75, 201, 58);
		addActor(button);
		
		
		button = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Ok", 0)), new TextureRegionDrawable(
				atlas.findRegion("Ok", 1)));
		button.addListener(okListener);
		button.setBounds(42, 75, 170, 58);
		addActor(button);
		
		upWorldRank = new SpriteSheet(0.1f, atlas.findRegions("Up"), SpriteSheet.LOOP);
		upWorldRank.setBounds(63, 335, 46, 66);
		addActor(upWorldRank);
		
		upFriendsRank = new SpriteSheet(0.1f, atlas.findRegions("Up"), SpriteSheet.LOOP);
		upFriendsRank.setBounds(63, 174, 46, 66);
		addActor(upFriendsRank);
		upFriendsRank.setVisible(false);
	}

	private void initPrivateAssets() {
		// TODO Auto-generated method stub
		
	}
	
	public void updateContent(float newScore, String nickname, long rankInWorld, long rankInFriends) {
		scoreLabel1.setText(""+(long) newScore);
		scoreLabel2.setText(""+(long) newScore);
		
		nickName1.setText(""+nickname);
		nickName2.setText(""+nickname);
		
		this.rankInWorld.setText("Xếp thứ "+rankInWorld);
		this.rankInFriends.setText("Xếp thứ "+rankInFriends);
//		if (rankInFriends < PlayerInfo.rankInFriends) {
//			upFriendsRank.setVisible(false);
//		} else upFriendsRank.setVisible(true);
	}
	
	public void show(Group father) {
		father.addActor(this);
	}
	
	public void hide() {
		remove();
	}
	
	
}
