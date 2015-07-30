package vn.sunnet.qplay.diamondlink.screens.groups;

import java.util.Random;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;
import vn.sunnet.qplay.diamondlink.items.Avatar;
import vn.sunnet.qplay.diamondlink.items.Item;
import vn.sunnet.qplay.diamondlink.loaders.ShopDescription;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.ui.MyImage;
import vn.sunnet.qplay.diamondlink.utils.Fields;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;


public class ScoreList extends AbstractGroup {

	

	float plusY = 464 - 428;
	
	AssetManager manager;
	Assets assets;
	TweenManager moveSystem;
	
	ScrollPane worldScroll;
	Table worldContent;
	
	Array<TextureRegion> avatars;
	
	Image myAvatar = null;
	Image myAvatarInRanks = null;
	Label leftDays = null;
	int me;
	
	BitmapFont provinceFont;
	
	Random random = new Random();
	
	private AtlasRegion noAvatar;
	
	public ScoreList(DiamondLink game, NodeScreen screen) {
		super(game, screen);
		manager = game.getAssetManager();
		assets = game.getAssets();
		moveSystem = game.getMoveSystem();
		initPrivateAssets();
		initContent();
	}
	
	public void initPrivateAssets() {
		TextureAtlas atlas = manager.get(UIAssets.AVATARS, TextureAtlas.class);
		ShopDescription shop = manager.get(Assets.AVATAR_SHOP, ShopDescription.class);
		
		avatars = new Array<TextureRegion>();
		avatars.clear();
		for (int i = 0; i < shop.size(); i++) {
			Item item = shop.get(i);
			avatars.add(item.display);
		}
		
		atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		noAvatar = atlas.findRegion("NoAvatar");
		FreeTypeFontGenerator generator = manager.get(UIAssets.ARIAL_GENERATOR, FreeTypeFontGenerator.class);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 14;
		provinceFont = generator.generateFont(parameter);
		provinceFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void initContent() {
		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
		Image im = new MyImage(atlas.findRegion("WorldRankFrame"));
		im.setBounds(0, 0, 400, 464);
		addActor(im);
		setBounds(0, 0, 400, 464);
		
		worldContent = new Table();
		worldScroll = new ScrollPane(worldContent);
		worldScroll.setBounds(11, 13 + plusY, 377, 356);
		worldScroll.setScrollingDisabled(true, false);
		addActor(worldScroll);
		
		BitmapFont nameFont = assets.getBitmapFont(UIAssets.MENU_NAME_FONT);
		leftDays = new Label("", new LabelStyle(nameFont, nameFont.getColor()));
		leftDays.setWrap(true);
		leftDays.setBounds(400 / 2 - 207 / 2, 5, 207, plusY);
		leftDays.setAlignment(Align.center|Align.center);
		addActor(leftDays);
	}
	
	public void updateContent(String name, final float myRank, final float[] ranks) {// moi dong them 40 pixel
		final float addPixel = 40;
		final float lineWidth = 374;
		final float lineHeight = 85;
		
		
		worldContent.clearChildren();
		TextureAtlas atlas = manager.get(UIAssets.MENU_FG, TextureAtlas.class);
//		Skin nameSkin = manager.get(UIAssets.NAME_SKIN, Skin.class);
		BitmapFont nameFont = assets.getBitmapFont(UIAssets.MENU_NAME_FONT);
		BitmapFont scoreFont = assets.getBitmapFont(UIAssets.MENU_SCORE_FONT);
		BitmapFont expFont = assets.getBitmapFont(UIAssets.EXP_FONT);
		// my rank
		Group group = new Group();
		Image bg = new MyImage(atlas.findRegion("3rdFrame"));
		bg.setBounds(0, 0, lineWidth, lineHeight);
		group.addActor(bg);
		
		Image rank = new Image(atlas.findRegion("My"));
		rank.setBounds(0, lineHeight / 2 - 60 / 2, 60, 60);
		group.addActor(rank);
		
		TextureRegion avatarRegion = atlas.findRegion("NoAvatar");
			
		
		Image icon = new Image(avatarRegion);
		icon.setBounds(60, 4, lineHeight - 4, lineHeight - 8);
		group.addActor(icon);
		myAvatar = icon;
		
		Image im = new Image(atlas.findRegion("AvatarWrap"));
		im.setBounds(60, 4, lineHeight - 4, lineHeight - 8);
		group.addActor(im);
	
		Image star = new Image(atlas.findRegion("Star"));
		star.setBounds(60 + lineHeight - 4, lineHeight - 50, 60, 50);
		group.addActor(star);
		
//		double exp = myRank.getDouble(Fields.EXP);
//		int level = assets.getLevelOfExp((float) exp);
//		LabelStyle style = new LabelStyle(expFont, expFont.getColor());
//		Label label = new Label(""+level, style);
//		label.setWrap(true);
//		label.setAlignment(Align.center|Align.center);
//		label.setBounds(60 + lineHeight - 4, lineHeight - 50 + 10, 60, 50);
//		group.addActor(label);
		
		Image modeIcon = new Image();
		modeIcon.setBounds(60 + lineHeight - 4 + 15, lineHeight - 50 - 30, 30, 30);
		group.addActor(modeIcon);
//		if (getModeOfScore(myRank.getInteger(Fields.MODE_OF_SCORE)) != null)
		modeIcon.setDrawable(new TextureRegionDrawable(getModeOfScore(0))); 
		
//		label = new Label(myRank.getString(Fields.NICKNAME), new LabelStyle(nameFont, nameFont.getColor()));
//		label.setWrap(true);
//		label.setAlignment(Align.left|Align.top);
//		label.setBounds(60 + lineHeight - 4 + 60, 49, 125, 34);
//		group.addActor(label);
		Label label = new Label(""
				+ Operator.convertNumberToString((int) myRank), new LabelStyle(scoreFont,
				scoreFont.getColor()));
		label.setWrap(true);
		label.setAlignment(Align.left|Align.bottom);
		label.setBounds(60 + lineHeight - 4 + 60, 19, 240, 34);
		group.addActor(label);
//		int position = (int) (myRank.getLong(Fields.MYRANK));
//		me = position;
//		label = new Label(""+position + suffixRank(position), new LabelStyle(nameFont, nameFont.getColor()));
//		label.setWrap(true);
//		label.setAlignment(Align.right|Align.bottom);
//		label.setBounds(lineWidth - 120, 57, 120, 69);
//		group.addActor(label);
		
//		label = new Label(myRank.getString(Fields.LOCATION), new LabelStyle(
//				provinceFont, Color.BLACK));
//		label.setWrap(true);
//		label.setAlignment(Align.left|Align.bottom);
//		label.setBounds(60 + lineHeight - 4 + 60, 4, 240, 34);
//		group.addActor(label);
		
		worldContent.add(group).pad(1, 1, 1, 1).minWidth(lineWidth).minHeight(lineHeight).fill();
		worldContent.row();
		if (ranks == null) return;
		// listrank
		for (int i = 0; i < ranks.length; i++) {
			group = new Group();
			switch (i) {
			case 0:
				bg = new MyImage(atlas.findRegion("1stFrame"));
				bg.setBounds(0, 0, lineWidth, lineHeight);
				group.addActor(bg);
				rank = new Image(atlas.findRegion("rank1"));
				rank.setBounds(0, lineHeight / 2 - 60 / 2, 60, 60);
				group.addActor(rank);
				break;
			case 1:
				bg = new MyImage(atlas.findRegion("2ndFrame"));
				bg.setBounds(0, 0, lineWidth, lineHeight);
				group.addActor(bg);
				rank = new Image(atlas.findRegion("rank2"));
				rank.setBounds(0, lineHeight / 2 - 60 / 2, 60, 60);
				group.addActor(rank);
				break;
			case 2:
				bg = new MyImage(atlas.findRegion("3rdFrame"));
				bg.setBounds(0, 0, lineWidth, lineHeight);
				group.addActor(bg);
				rank = new Image(atlas.findRegion("rank3"));
				rank.setBounds(0, lineHeight / 2 - 60 / 2, 60, 60);
				group.addActor(rank);
				break;
			default:
				bg = new MyImage(atlas.findRegion("3rdFrame"));
				bg.setBounds(0, 0, lineWidth, lineHeight);
				group.addActor(bg);
				int position = (i + 1);
				
				label = new Label(""+ position, new LabelStyle(expFont, expFont.getColor()));
				label.setWrap(true);
				label.setAlignment(Align.center|Align.center);
				label.setBounds(0, lineHeight / 2 - 60 / 2, 60, 60);
				group.addActor(label);
				break;
				
			}
			
			avatarRegion = atlas.findRegion("NoAvatar");
				
			
			icon = new Image(avatarRegion);
			icon.setBounds(60, 4, lineHeight - 4, lineHeight - 8);
			group.addActor(icon);
			if (me == i + 1) myAvatarInRanks = icon;
			
			im = new Image(atlas.findRegion("AvatarWrap"));
			im.setBounds(60, 4, lineHeight - 4, lineHeight - 8);
			group.addActor(im);
			
			star = new Image(atlas.findRegion("Star"));
			star.setBounds(60 + lineHeight - 4, lineHeight - 50, 60, 50);
			group.addActor(star);
			
//			exp = ranks[i].getDouble(Fields.EXP);
//			level = assets.getLevelOfExp((float) exp);
//			label = new Label(""+level, style);
//			label.setWrap(true);
//			label.setAlignment(Align.center|Align.center);
//			label.setBounds(61 + lineHeight - 4, lineHeight - 50 + 10, 60, 50);
//			group.addActor(label);
			
			modeIcon = new Image();
			modeIcon.setBounds(60 + lineHeight - 4 + 15, lineHeight - 50 - 30, 30, 30);
			group.addActor(modeIcon);
//			if (getModeOfScore(ranks[i].getInteger(Fields.MODE_OF_SCORE)) != null)
			modeIcon.setDrawable(new TextureRegionDrawable(getModeOfScore(0)));
			
//			label = new Label(ranks[i].getString(Fields.NICKNAME), new LabelStyle(nameFont, nameFont.getColor()));
//			label.setWrap(true);
//			label.setAlignment(Align.left|Align.top);
//			label.setBounds(60 + lineHeight - 4 + 60, 49, 125, 34);
//			group.addActor(label);
			
			label = new Label(""
					+ Operator.convertNumberToString((int) ranks[i]), new LabelStyle(
					scoreFont, scoreFont.getColor()));
			label.setWrap(true);
			label.setAlignment(Align.left|Align.bottom);
			label.setBounds(60 + lineHeight - 4 + 60, 19, 240, 34);
			group.addActor(label);
			
//			label = new Label(ranks[i].getString(Fields.LOCATION), new LabelStyle(
//					provinceFont, Color.BLACK));
//			label.setWrap(true);
//			label.setAlignment(Align.left|Align.bottom);
//			label.setBounds(60 + lineHeight - 4 + 60, 4, 240, 34);
//			group.addActor(label);
			
			worldContent.add(group).pad(1, 1, 1, 1).minWidth(lineWidth).minHeight(lineHeight).fill();
			worldContent.row();
		}
		
//		leftDays.setText("Còn "+PlayerInfo.leftDays+" ngày");
		
		System.out.println("WorldList update");
		
	}
	
	public void updateAvatars(Avatar[] avatars, int maxAvatar) {
//		TextureRegion display = null;
//		if (avatars != null) {
//			for (int i = 0 ; i < avatars.length; i++) {
//				if (avatars[i] != null) {
//					display = avatars[i].display;
//				}
//			}
//		}
//		if (display != null) {
//			if (myAvatar != null)
//			myAvatar.setDrawable(new TextureRegionDrawable(display));
//			if (myAvatarInRanks != null)
//			myAvatarInRanks.setDrawable(new TextureRegionDrawable(display));
//		} else {
//			if (myAvatar != null)
//			myAvatar.setDrawable(new TextureRegionDrawable(noAvatar));
//			if (myAvatarInRanks != null)
//			myAvatarInRanks.setDrawable(new TextureRegionDrawable(noAvatar));
//		}
	}
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		
		
	}
	
	public void show(Group father) {
		father.addActor(this);
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

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
