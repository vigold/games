package vn.sunnet.qplay.diamondlink.butterflydiamond;


import java.util.Iterator;

import org.apache.http.impl.auth.GGSSchemeBase;

import vn.sunnet.lib.bmspriter.BMSAnimationBlueprint;
import vn.sunnet.lib.bmspriter.BMSRenderObjectProducer;
import vn.sunnet.lib.bmspriter.BMSSCMLFile;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxAnimationInstance;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxROP_TextureAtlas;
import vn.sunnet.lib.bmspriter.libgdxrenderer.BMSLibgdxRenderer;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.DynamicGameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.tweens.GameObjectAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.tests.LibGdxAtlasLoader;
import com.brashmonkey.spriter.tests.LibGdxDrawer;



public class Spider extends DynamicGameObject{
	
	public static final int SPIDER_INIT = -1;
	
	public static final int SPIDER_REST = 2;
	
	public static final int SPIDER_MOVE = 3;
	
	public static final int SPIDER_CATCH = 4;
	
	public static final int SPIDER_FALL = 1;
	
	public static final int SPIDER_BEGIN = 0;
	
	public int state = SPIDER_INIT;
	
//	protected Vector2 source = null; // dat o tam cua vat the
//	protected Vector2 destination = null;
	
	ObjectMap<Integer, IDiamond> butterflies = new ObjectMap<Integer, IDiamond>();
	
//	public int spriteFrame = 0;
	
	
	
	private final int STEP_TIME = 4;
	private final int LOOP_TIMES = 1;
	
	public float elapsedTime = 0;
	
//	private int times = 0;
	
	private Animation animation = null;
	
	
	
	private ButterflyDiamond screen = null;
	
	public boolean huntFlag = false;
	
	public boolean butterflyMove = false;
	
//	public float butterflyX = 0;
//	
//	public float butterflyY = 0;
	
	public DiamondOfButterfly catchedButterfly = null;
	
	public BMSLibgdxRenderer renderer;
	private BMSLibgdxAnimationInstance idle;
	private BMSLibgdxAnimationInstance move;
	private BMSLibgdxAnimationInstance catched;
	
	private Player spider;
	private LibGdxDrawer drawer;
	private LibGdxAtlasLoader asset;
	
	private Animation rowAn = null;
	private Animation colAn = null;
	private Animation mainCrossAn = null;
	private Animation minorCrossAn = null;
	
	private boolean remove[] = new boolean[64];
	
	public Spider(float x, float y, float width, float height, ButterflyDiamond screen) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
		this.screen = screen;
		
		this.animation = new MyAnimation(0.05f, screen.spiderRegion);
		
		this.renderer = new BMSLibgdxRenderer(screen.batch);
		
		asset = screen.manager.get(Assets.SPIDER_BMS, LibGdxAtlasLoader.class);
		spider = new Player(asset.getData().getEntity(0));
		asset.getData().getEntity(0).getAnimation(0).looping = true;
		drawer = new LibGdxDrawer(asset, screen.batch, null);
		
//		source = new Vector2(x, y);
//		destination = new Vector2(source);
		elapsedTime = 0;
		onCreated();
		
	}

	
	public void onCreated() {
		state = SPIDER_INIT;
//		spriteFrame = 0;
		elapsedTime = 0;
		huntFlag = false;
		butterflyMove = false;
//		butterflyX = 0;
//		butterflyY = 0;
		setPosition(240 - 35, 800 + 240);
		spider.setPosition(getX(), getY());
		spider.update();
		System.out.println(getX()+" "+getY());
	}
	
	// cap nhat vi tri buom , di chuyen
	
	public void update(float deltaTime) {
		ButterflyLogic logic = (ButterflyLogic) screen.logic;
		if (logic.checkButterfly) checkList();
		
		
		switch (state) {
			case SPIDER_INIT: setPosition(240 - 35, 800 + 240); break;
			case SPIDER_BEGIN: beginFall(); break;
			case SPIDER_FALL: fall(deltaTime); break;
			case SPIDER_REST: rest(deltaTime); break;
			case SPIDER_MOVE: move(deltaTime); break;
			case SPIDER_CATCH: break;
		}
//		System.out.println(getX()+" "+getY());
		spider.setPosition(getX() + 30, getY());
	}
	
	public void draw(float deltaTime) {
		
//		switch (state) {
//		case SPIDER_MOVE:
//			renderer.render(move, getX() + 15, getY());
//			break;
//		case SPIDER_CATCH:
//			renderer.render(catched, getX() + 15, getY());
//			break;
//		default:
//			
//			renderer.render(idle, getX() + 15, getY());
//			break;
//		}
//		System.out.println(spider.getX()+" "+spider.getY());
		drawer.draw(spider);
		
//		screen.batch.draw(animation.getKeyFrame(elapsedTime),getX(), getY(), getWidth(), getHeight());
//		Iterator<Integer> entries = butterflies.keys().iterator();
//		DiamondOfButterfly preButterfly = null;
//		while (entries.hasNext()) {
//		  Integer entry = entries.next();
//		  DiamondOfButterfly butterfly2 = (DiamondOfButterfly) butterflies.get(entry);
//		  if (preButterfly != null) {
//			  DiamondOfButterfly butterfly1 = preButterfly;
//			 
//			  float x1 = butterfly1.getPosX();
//			  float y1 = butterfly1.getPosY();
//			  float x2 = butterfly2.getPosX();
//			  float y2 = butterfly2.getPosY();
//			  Animation temp = null;
//			  float x = Math.min(x1, x2);
//			  float y = Math.min(y1, y2);
//			  float width = 0;
//			  float height = 0;
//				if ((x2 - x1) * (y2 - y1) > 0) {
//					temp = minorCrossAn;
//					width = Math.abs(x2 - x1);
//					height = Math.abs(y2 - y1);
//				} else if ((x2 - x1) * (y2 - y1) < 0) {
//					temp = mainCrossAn;
//					width = Math.abs(x2 - x1);
//					height = Math.abs(y2 - y1);
//				} else if (x2 == x1) {
//					temp = colAn;
//					width = screen.DIAMOND_WIDTH;
//					height = Math.abs(y2 - y1);
//					x = x - width / 2;
//
//				} else {
//					temp = rowAn;
//					width = Math.abs(x2 - x1);
//					height = screen.DIAMOND_HEIGHT;
//					y = y - height / 2;
//				}
//				screen.batch.draw(temp.getKeyFrame(0), x, y, width, height);
//		  }
//		  preButterfly = butterfly2;
//		}
	}
	
	private void beginFall() {
		state = SPIDER_FALL;
		spider.setAnimation(asset.getData().getEntity(0).getAnimation(0));
		
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						setPosition(240 - 35, 800 + 240);
//						idle.seek(0);
						spider.setTime(0);
					}
				}))
				.push(Tween.to(this, GameObjectAccessor.POS_XY, 1f)
						.target(240 - 35, 650).ease(Elastic.OUT))
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						state = SPIDER_REST;
						spider.setTime(0);
					}
				}))
				.start(screen.moveSystem);
		
	}
	
	private void beginFall(final float x, final float y) {
		state = SPIDER_FALL;
		spider.setAnimation(asset.getData().getEntity(0).getAnimation(0));
		
		Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						setPosition(x, y);
//						idle.seek(0);
						spider.setTime(0);
					}
				}))
				.push(Tween.to(this, GameObjectAccessor.POS_XY, 1f)
						.target(240 - 35, 650).ease(Elastic.OUT))
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						state = SPIDER_REST;
						spider.setTime(0);
					}
				}))
				.start(screen.moveSystem);
		
	}
	
	public void fall(float deltaTime) {
		spider.update();
	}
	
	public void rest(float deltaTime) {
		float vX = 0, vY = 0 , speedY = 0, speedX = 0;
		if (butterflyMove) {
			Gdx.app.log("test", "beginHunt");
			huntFlag = true;
			butterflyMove = false;
			catchedButterfly = null;
		} 
		if (huntFlag) {
			catchedButterfly = null;
			DiamondOfButterfly butterfly = null;
		
			for (Integer key: butterflies.keys()) {
				butterfly = (DiamondOfButterfly) butterflies.get(key);
				float x = butterfly.getPosX();
				float y = butterfly.getPosY();
				int cell = touchCell(butterfly.position);
				int row = cell / 8; int col = cell % 8;
				if (catchedButterfly == null) catchedButterfly = butterfly;
				else
				if (y > catchedButterfly.getPosY()) 
					catchedButterfly = butterfly;
			}
			
			if (catchedButterfly != null) {
				float x1 = catchedButterfly.getX();
				float x2 = getX();
				float speed = 15f/0.05f;
				float time = Math.abs(x1 - x2) / (speed);
//				Gdx.app.log("test", "beginHunt "+catchedButterfly.getX()+" "+catchedButterfly.getY());
				Timeline.createSequence()
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						state = SPIDER_MOVE;
						spider.setAnimation(asset.getData().getEntity(0).getAnimation(1));
						spider.setTime(0);
//						move.seek(0);
					}
				}))
				.push(Tween.to(this, GameObjectAccessor.POS_XY, time)
						.target(x1, getY()).ease(Linear.INOUT))
				.push(Tween.call(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// TODO Auto-generated method stub
						if (catchedButterfly != null) {
							if (catchedButterfly.getPosY() < screen.gridPos.y + 480) {
								state = SPIDER_REST;
								spider.setAnimation(asset.getData().getEntity(0).getAnimation(0));
								spider.setTime(0);
							} else {
								state = SPIDER_CATCH;
//								catched.seek(Integer.MAX_VALUE);
								spider.setAnimation(asset.getData().getEntity(0).getAnimation(1));
								spider.setTime(spider.getAnimation().length / 2);
								Assets.playSound(screen.butterflyCatched);
							}
						} else {
							state = SPIDER_REST;
						}		
					}
					
				}))
				.start(screen.moveSystem);
			}
			huntFlag = false;
		}
		
		elapsedTime = Math.max(elapsedTime - deltaTime, 0);
		
//		System.out.println("time ="+elapsedTime);
		
//		if (idle.getTime() == idle.blueprint.getLength() - 1 && times == LOOP_TIMES) {
//			elapsedTime = STEP_TIME;
//			idle.seek(0);
//			times = 0;
//		} else if (idle.getTime() == idle.blueprint.getLength() - 1) {
//			idle.seek(0);
//			times++;
//		}
//		
//		if (elapsedTime == 0) {
//			idle.animate((int) (0.3f * deltaTime * 1000));
//		}
		
//		if (spider.getTime() == spider.getAnimation().length && times == LOOP_TIMES) {
//			elapsedTime = STEP_TIME;
//			spider.setTime(0);
//			times = 0;
//			System.out.println("lap" + times);
//		} else if (spider.getTime() == spider.getAnimation().length) {
//			spider.setTime(0);
//			times++;
//			System.out.println("lap" + times);
//		}
		
//		if (elapsedTime == 0) 
		{
			spider.update((int) (0.3f * deltaTime * 1000));//idle.animate((int) (0.3f * deltaTime * 1000));
		}
	}
	
	public void move(float deltaTime) {
		spider.update((int) (0.6f * deltaTime * 1000));
//		move.animate((int) (0.6f * deltaTime * 1000));
	}
	
	public void setHunt(boolean flag) {
		huntFlag = flag;
	}
	
	public boolean isHunted() {
		return (state == SPIDER_CATCH);
	}
	
	public void repairButterfly() {
		for (int i = 0 ; i < remove.length; i++) 
			remove[i] = false;
		for (Integer pos: butterflies.keys()) {
			int row = pos / 8;
			int col = pos % 8;
			if (screen.logic.diamondType(screen.grid[row][col]) != IDiamond.BUTTERFLY_DIAMOND) 
				remove[pos] = true;
		}
		for (int i = 0; i < remove.length; i++) {
			if (remove[i]) butterflies.remove(i);
		}
	}
	
	public void addButterfly(int position) {
		if (butterflies.containsKey(position)) {
			Gdx.app.log("test", "butterfly exist in "+position);
		} else {
			if (screen.diamonds.get(position).getDiamondType() == Diamond.BUTTERFLY_DIAMOND)
			butterflies.put(position, screen.diamonds.get(position));
			else Gdx.app.log("test", "can't add another diamond in "+position);
		}
	}
	
	public void removeButterfly(int position) {
		if (butterflies.containsKey(position)) {
			butterflies.remove(position);
		}
	}
	
	public void save(IFunctions iFunctions) {
		screen.gGame.iFunctions.putFastInt("spider state "+screen.GAME_ID, state);
		screen.gGame.iFunctions.putFastFloat("spider time "+screen.GAME_ID, elapsedTime);
		screen.gGame.iFunctions.putFastBool("spider hunterFlag "+screen.GAME_ID, huntFlag);
		screen.gGame.iFunctions.putFastBool("spider butterflyMove "+screen.GAME_ID, butterflyMove);
		screen.gGame.iFunctions.putFastString("spider position "+screen.GAME_ID, position.x+"|"+position.y);
		String data = "";
		for (Integer key: butterflies.keys()) {
			data += "|"+key;
		}
		if (data != "") data = data.substring(1);
		screen.gGame.iFunctions.putFastString("spider butterflies "+screen.GAME_ID, data);
	}
	
	public void parse(IFunctions iFunctions) {
		state = screen.gGame.iFunctions.getFastInt("spider state "+screen.GAME_ID, 0);
		elapsedTime = screen.gGame.iFunctions.getFastFloat("spider time "+screen.GAME_ID, 0);
		huntFlag = screen.gGame.iFunctions.getFastBool("spider hunterFlag "+screen.GAME_ID, false);
		butterflyMove = screen.gGame.iFunctions.getFastBool("spider butterflyMove "+screen.GAME_ID, false);
		String data = screen.gGame.iFunctions.getFastString("spider position "+screen.GAME_ID, "");
		if (data != "") {
			String split[] = data.split("\\|");
			setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
			spider.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
			spider.update();
		}
		
		data = screen.gGame.iFunctions.getFastString("spider butterflies "+screen.GAME_ID, "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length; i++)
				butterflies.put(Integer.parseInt(split[i]), screen.diamonds.get(Integer.parseInt(split[i])));
		}
		
		state = SPIDER_BEGIN;
		
//		switch (state) {
//		case SPIDER_INIT:
//			break;
//		case SPIDER_BEGIN:
//			break;
//		case SPIDER_FALL:
//			beginFall(getX(), getY());
//			break;
//		case SPIDER_REST:
//			break;
//		case SPIDER_MOVE:
//			state = SPIDER_REST;
//			break;
//		case SPIDER_CATCH:
//			break;
//		}
	}
	
	public void checkList() {// loai bo cac quan buom bi an
	}
	
	public int touchCell(Vector2 Point) {
		int j = (int) (Point.x - screen.gridPos.x) / 60;
		int i = (int) (Point.y - screen.gridPos.y) / 60;
		
		return i * 8 + j;
	}
	
	public boolean inGrid(int i , int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
}
