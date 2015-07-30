package vn.sunnet.qplay.diamondlink.items;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.loaders.ShopDescription;

public class VipCard extends Item{
	public static final int BLACK_TORTOISE = 15;
	public static final int GREEN_DRAGON = 12;
	public static final int WHITE_TIGER = 13;
	public static final int RED_PHOENIX = 14;
	
	private final int DELAY = 4;
	
	public int combo = 0;
	
	DiamondLink game;
	
	AssetManager manager;
	
	float delay = 0;
	
	boolean isShowed = false;
	
	public VipCard(int type, DiamondLink game) {
		// TODO Auto-generated constructor stub
		this.id = type;
		this.game = game;
		manager = game.getAssetManager();
		ShopDescription cards = manager.get(Assets.VIP_SHOP, ShopDescription.class);
		switch (type) {
		case GREEN_DRAGON:
			display = cards.get(""+type).display;
			this.name = "GreenDragon";
			break;
		case WHITE_TIGER:
			display = cards.get(""+type).display;
			this.name = "WhiteTiger";
			break;
		case RED_PHOENIX:
			display = cards.get(""+type).display;
			this.name = "RedPhoenix";
			break;
		case BLACK_TORTOISE:
			display = cards.get(""+type).display;
			this.name = "BlackTortoise";
			break;
		}
	}
	
	public VipCard() {
	}
	
	public void update(float delta) {
		delay = Math.max(delay - delta, 0);
	}
	
	public void resetDelay() {
		delay = DELAY;
	}
	
	public float getDeplay() {
		return delay;
	}
	
	public TextureRegion getDisplay() {
		return display;
	}
	
	public void setShow(boolean isShow) {
		this.isShowed = isShow;
	}
	
	public boolean isShowed() {
		return isShowed;
	}
}
