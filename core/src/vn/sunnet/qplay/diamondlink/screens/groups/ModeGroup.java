package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.scene2d.ui.MyScrollPane;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.ButtonDescription;
import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ModeGroup extends AbstractGroup {
	
	private ImageButton modes[] = new ImageButton[3];
	private Group groups[] = new Group[3];
	private ScrollPane pane;
	
	private Vector2 lastPoint = new Vector2();
	private int draggingPoint = -1;
	private float total = 0;
	private boolean isMoved = false;
	private float eclapsedTime = 0;
	private Sound press;
	
	

	public ModeGroup(DiamondLink game, AbstractScreen screen) {
		super(game, screen);
		initPrivateAssets();
		initContent();
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void show(Group father) {
		father.addActor(this);
		
	}

	@Override
	public void hide() {
		remove();
	}

	@Override
	protected void initContent() {
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		setBounds(0, 0, 480, 381);
		Table table = new Table();
		Group group = new Group();
//		table.add(group).pad(0, 0, 0, 0).minWidth(312).minHeight(381).fill();
		for (int i = 0; i < 3; i++) {
			final int index = i;
			groups[i] = new Group();
			TextureRegion region = null;
			if (i == 0) region = atlas.findRegion("ClassicLogo", 0);
			else if (i == 1) region = atlas.findRegion("ButterflyLogo", 0);
			else region = atlas.findRegion("MinerLogo", 0);
			TextureRegion region2 = null;
			if (i == 0) region2 = atlas.findRegion("ClassicLogo", 1);
			else if (i == 1) region2 = atlas.findRegion("ButterflyLogo", 1);
			else region2 = atlas.findRegion("MinerLogo", 1);
			modes[index] = new ImageButton(new TextureRegionDrawable(region), new TextureRegionDrawable(region2));
			modes[index].setBounds(312 / 2 - 208 / 2, 381 / 2 - 254 / 2, 208, 254);
			modes[index].setOrigin(modes[index].getWidth() / 2, modes[index].getHeight() / 2);
			groups[i].addActor(modes[index]);
			if (i == 0)
			table.add(groups[index]).pad(0, 240 - 312/2, 0, 0).minWidth(312).minHeight(381).fill();
			else table.add(groups[index]).pad(0, 0, 0, 0).minWidth(312).minHeight(381).fill();
			
			modes[index].addCaptureListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					// TODO Auto-generated method stub
					return true;
				}
				
				@Override
				public void touchUp(InputEvent arg0, float arg1, float arg2,
						int arg3, int arg4) {
					if (modes[index].isChecked()) {
						modes[index].setChecked(false);
						
					}
					if (modes[index].getScaleX() == 1.5f)
						toScreen(index);
				}
			});
			modes[i].setTransform(true);
		}
		group = new Group();
		table.add(group).pad(0, 0, 0, 0).minWidth(312).minHeight(381).fill();
		pane = new ScrollPane(table);
		pane.setBounds(0 , 0, 480, 381);
		pane.setOverscroll(false, false);
		pane.setScrollingDisabled(false, true);
		pane.setVelocityX(pane.getVelocityX() / 100f);
		
		addActor(pane);
//		pane.setPage(312f);
		
		pane.clearListeners();
		addCaptureListener(new ActorGestureListener() {
			
			
			@Override
			public void touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				lastPoint.set(x, y);
				draggingPoint = pointer;
				total = pane.getScrollX();
				
			}
			
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX,
					float deltaY) {
		
				total -= (x - lastPoint.x) / 1.1f;
				pane.setScrollX(total);
				lastPoint.set(x, y);
//				pane.cancelTouchFocusedChild(event);
			}
			
			
			
			
			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				float x = pane.getScrollX();
//				System.out.println("++++++++++++++++"+pane.getScrollX());
				x = Math.max(0, x);
				x = Math.min(x, 0 + 312 * 2);
//				System.out.println("++++++++++++++++"+x);
				int times = Math.round((x - 0f) / 312f);
				x = 0 + times * 312;
				pane.setScrollX(x);
//				System.out.println("++++++++++++++++"+x);
				isMoved = true;
				eclapsedTime = 0;
				
			}
		});
		addListener(new ClickListener() {
			
		});
	}

	@Override
	protected void initPrivateAssets() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);

	}
	
	@Override
	public void act(float delta) { // 0 528 / 852 
		super.act(delta);
		if (isMoved) {
			eclapsedTime += delta;
			if (eclapsedTime > 0.1f) {
				eclapsedTime = 0;
				isMoved = false;
			}
		}
		for (int i = 0 ;i < 3; i++) {
			float anchor = 0 + i * 312;
			float del = Math.abs(pane.getScrollX() - anchor);
			del = Math.min(del, 312);
			float scale = 0.5f * (312 - del) / 312f + 1f;
			modes[i].setScale(scale);
//			modes[i].setScale(Math.max(1f, 1.5f - ))
		}
//		for (int i = 0; i < 3; i++) {
//			System.out.println(i+"i toa do"+groups[i].getX());
//		}
	}

	private void toScreen(final int type) {
		if (DiamondLink.SOUND == 1)
			press.play();
		switch (type) {
		case GameScreen.CLASSIC_DIAMOND:
		case GameScreen.BUTTERFLY_DIAMOND:
		case GameScreen.MINE_DIAMOND:
			if (game.iFunctions.getFastBool("data "+type, false)) {
				screen.createDialog("",
						"Do you want to continue the previous game?", new ButtonDescription("Yes", new Command() {

							@Override
							public void execute(Object data) {
								
								
								
								Gdx.app.postRunnable(new Runnable() {
									
									@Override
									public void run() {
										game.getGame(type).setGameOver();
										game.getFrontGame().setToScreen(game.getGame(type));
										game.setScreen(game.getGame(type));
										game.iFunctions.track("In Menu", "Select Mode", "Mode "+game.getGame(type).getName(), 1);
									}
								});
								
							}
						}), new ButtonDescription("No", new Command() {

							@Override
							public void execute(Object data) {
								
								Gdx.app.postRunnable(new Runnable() {
									
									@Override
									public void run() {
										game.iFunctions.putFastBool("only Level "+type, false);
										game.iFunctions.putFastBool("data "+type, false);
										game.getGame(type).setGameOver();
										game.getFrontGame().setToScreen(game.getGame(type));
										game.setScreen(game.getFrontGame());
										game.iFunctions.track("In Menu", "Select Mode", "Mode "+game.getGame(type).getName(), 1);
									}
								});
								
								
							}
						}), null, null);
				return;
			}
			game.getGame(type).setGameOver();
			game.getFrontGame().setToScreen(game.getGame(type));
			game.setScreen(game.getFrontGame());
			game.iFunctions.track("In Menu", "Select Mode", "Mode "+game.getGame(type).getName(), 1);
			break;
		}
	}
	
	
	
}
