package vn.sunnet.qplay.diamondlink.gameobjects;

import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;





public interface IDiamond extends IDiamondList{
	
	public static final int DIAMOND_WIDTH = 60;
	public static final int DIAMOND_HEIGHT = 60;
	
	// actions of diamonds
	public static final int ENHANCE_FALL = 512;
	public static final int ENHANCE_UP_AND_FALL = 1024;
	public static final int UP_AND_FALL = 256;
	public static final int FLY = 128;
	public static final int UP_TO_GRID = 64;
	public static final int ONE_ASPECT_SWAP = 32;
	public static final int FALL = 16; // Fall
	public static final int COMBINE_MOVE = 8;
	public static final int TWO_ASPECT_SWAP = 4; // Swap
	public static final int GRADUALLY_DISAPPEAR = 2; 
	public static final int FRAME_CHANGE = 1; // 
	public static final int REST = 0; //
	// types of diamonds
	
	public static final int NORMAL_DIAMOND = 0;
	public static final int BUTTERFLY_DIAMOND = 1;
	public static final int FIRE_DIAMOND = 2;
	public static final int FIVE_COLOR_DIAMOND = 3;
	public static final int BLINK_DIAMOND = 4;
	public static final int RT_DIAMOND = 5;
	public static final int CT_DIAMOND = 6;
	public static final int SOIL_DIAMOND = 7;
	public static final int ROCK_DIAMOND = 8;
	public static final int BOX_DIAMOND = 9;
	public static final int PEARL_DIAMOND = 10;
	public static final int LAVA = 11;
	public static final int MARK_DIAMOND = 12;
	public static final int CLOCK_DIAMOND = 13;
	public static final int COIN_DIAMOND = 14;
	public static final int HYPER_CUBE = 15;
	public static final int LASER_DIAMOND = 16;
	public static final int X_SCORE_GEM = 17;
	public static final int BLUE_GEM = 18;
	public static final int DEEP_BLUE_GEM = 19;
	public static final int PINK_GEM = 20;
	public static final int RED_GEM = 21;
	
	
	public static final int BLINK_FIRE_DIAMOND = 11;
	public static final int BOMB_DIAMOND = 12;
	
	// color of diamonds
	
	public static final int SWORD_DIAMOND = 2;
	public static final int SHIELD_DIAMOND = 3;
	public static final int HEALTH_DIAMOND = 4;
	public static final int MANA_DIAMOND = 0;
	public static final int GOLD_DIAMOND = 1;
	
	public static final int BLUE = 0;
	public static final int GREEN = 1;
	public static final int ORANGE = 2;
	public static final int PINK = 3;
	public static final int RED = 4;
	public static final int WHITE = 5;
	public static final int YELLOW = 6;
	
	// the game diamond belonged
	public void setScreen(GameScreen screen);
	
	// Visual Characters of diamond
	public void setDiamondValue(int value);
	
	public int getDiamondValue();
	
	public int getDiamondType(); 
	
	public int getDiamondColor();
	
//	public void setSprite(MyAnimation animation);
	
	public MyAnimation getSprite();
	
//	public void setRearSprite(MyAnimation animation);
	
	public MyAnimation getRearSprite();
	
//	public void setFrontSprite(MyAnimation animation);
	
	public MyAnimation getFrontSprite();
	
	public PooledEffect getRearEffect();
	
	public PooledEffect getFrontEffect();
	
	// Action Charaters of diamond
	public void setSource(float xx, float yy);
	
	public void setDestination(float xx, float yy);
	
	public void setCenterPosition(float xx, float yy);
	
	public Vector2 getSource();
	
	public Vector2 getDestination();
	
	public Vector2 getCenterPosition();
	
	public Rectangle getBound();
	
	public void setActivieTime(float time);
	
	public float getActiveTime();
	
	public float getSourX();
	
	public float getSourY();
	
	public float getDesX();
	
	public float getDesY();
	
	public float getPosX();
	
	public float getPosY();
	
	public void setAction(int Action); 
	
	public int getAction();
	
	public boolean containsAction(int Action);
	
	public boolean isDestination();
	
	public boolean isFinished(int Action);
	
	public boolean isFinish();
	
	public void setFinish(int Action);
	
	public void setFinish(boolean flag);
	
	public float getTime();
	
	public void update(float deltaTime);
	
	public void draw(float deltaTime, SpriteBatch pBatch);
	
}
