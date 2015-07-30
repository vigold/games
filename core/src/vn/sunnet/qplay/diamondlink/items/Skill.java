package vn.sunnet.qplay.diamondlink.items;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Skill implements Cloneable{
	public final static int BOMB = 2;
//	public final static int HUMMER = 0;
	public final static int FIVE_COLOR = 1;
	public final static int AUGER = 0;
	public final static int CLOCK = 3;
	
	public final static int GOLD = 4;
	public final static int EXPLODE = 5;
	public final static int SPECIAL = 6;
	public final static int COMBO = 7;
	public final static int LACKCOLOR = 8;
	
	public final static int NONE = 9;
	public int type = BOMB;
	public int count = 0;
	public int maxCount = 0;
	
	TextureRegion display;
	private TextureRegion label;
	private BitmapFont font;
	public Vector2 position = new Vector2();
	Rectangle bounds;
	

	
	public Skill(int type, DiamondLink game, Rectangle bounds) {
		// TODO Auto-generated constructor stub
		this.type = type;
		TextureAtlas atlas = game.getAssetManager().get(UIAssets.ITEMS, TextureAtlas.class);
		
		this.bounds = bounds;
		position = new Vector2();
		if (bounds != null) {
			position = new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
		}
		display = atlas.findRegion(""+(type + 1));
		atlas = game.getAssetManager().get(UIAssets.BUY_ITEMS, TextureAtlas.class);
		label = atlas.findRegion("NumTab");
		font = game.getAssetManager().get(UIAssets.COIN_FONT, BitmapFont.class);
		switch (type) {
		case BOMB:
			maxCount = 3;
			break;
		case FIVE_COLOR:
			maxCount = 3;
			break;
		case AUGER:
			maxCount = 3;
			break;
		case CLOCK:
			maxCount = 1;
			break;
		case GOLD :
			maxCount = 1;
			break;
		case EXPLODE :
			
			maxCount = 1;
			break;
		case SPECIAL :
			
			maxCount = 1;
			break;
		case COMBO :
			
			break;
		case LACKCOLOR:
			
			maxCount = 1;
			break;
		}
	}
	
	public int get() {
		return count;
	}
	
	public void inc() {
		count++;
	}
	
	public void dec() {
		count--;
	}
	
	public boolean isMax() {
		return count >= maxCount;
	}
	
	public boolean isExist() {
		return count > 0;
	}
	
	public boolean collision(float x, float y) {
		return bounds.contains(x, y);
	}
	
	public void setCollisionBound(Rectangle bounds) {
		this.bounds = bounds;
		position = new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
	}
	
	public Rectangle getBound() {
		return bounds;
	}
	
	public void set(Vector3 position) {
		// TODO Auto-generated method stub
		this.position.set(position.x, position.y);
	}
	
	public void setPosition(Vector2 position) {
		this.position.set(position.x, position.y);
	}
	
	public void generateAt(int row, int col, GameScreen screen) {
		switch (type) {
		case BOMB:
			screen.logic.newEffect(row, col, Effect.RCTHUNDER_ITEM, null);
			break;
		case AUGER:
			screen.logic.newEffect(row, col, Effect.ROW_THUNDER_ITEM, null);
			break;
		case FIVE_COLOR:
			screen.logic.newEffect(row, col, Effect.CHAIN_THUNDER_ITEM, null);
			break;
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		if (type != NONE) {

			Color preColor = batch.getColor();
			if (count == 0)
				batch.setColor(0.4f, 0.4f, 0.4f, 1f);
			batch.draw(display, 
					position.x - 
					72 / 2, 
					position.y
					- 72 / 2, 
					72, 
					72);
			batch.setColor(preColor);
			
		}
	}

	public String getName() {
		switch (type) {
		case BOMB:
			return "Bomb";
		case FIVE_COLOR:
			return "ChainedThunder";
		case AUGER:
			return "RowThunder";
		case CLOCK:
			return "Clock";
		case GOLD :
			return "10%Gold";
		case EXPLODE :
			return "ProbalityFire";
		case SPECIAL :
			return "5SpecialGems";
		case COMBO :
			return "x2ComboTime";
		case LACKCOLOR:
			return "RemoveWhite";
		}
		return "";
	}
	
	public int getCost() {
		final int[] costs = { 600, 1000, 1100, 2000, 2000, 3000, 3000, 5000, 5000};
		return costs[type];
	}
	
	@Override
	public Skill clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Skill) super.clone();
	}
}
