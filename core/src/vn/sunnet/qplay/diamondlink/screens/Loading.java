package vn.sunnet.qplay.diamondlink.screens;



import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class Loading extends AbstractScreen {
	
	OrthographicCamera  camera = new OrthographicCamera();
	SpriteBatch batch = new SpriteBatch();
	
	Music music;
	
	DiamondLink diamondLink;
	float percent = 0;
	
	private Runnable handleWhenComplete;
	
	private Texture texturebkg, texturebeegame;
	private Sprite spritebkg, spritebeeGAME;
	private Texture layer_0, layer_1;
	private String error = "";
	private BitmapFont font;
	private float eclapsedTime = 0;
	private Texture full;
	private Texture empty;
	private float yScroll = 0;
	private Texture logo;


	public Loading(int width, int height, DiamondLink game , Runnable hanleWhenComplete) {
		super(width, height, game);
		// TODO Auto-generated constructor stub
		camera.setToOrtho(false, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		music = Gdx.audio.newMusic(Gdx.files.internal("data/Musics/Loading.mp3"));
		music.setLooping(true);
		diamondLink = (DiamondLink) game;
		camera.setToOrtho(false, width, height);
		this.handleWhenComplete = hanleWhenComplete;
		
		texturebkg = new Texture(Gdx.files.internal("data/Loading/bkg.png"));
		texturebkg.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		texturebeegame = new Texture(Gdx.files.internal("data/Loading/beegameicon.png"));
		texturebeegame.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		layer_0 = new Texture(Gdx.files.internal("data/Loading/Layer0.jpg"));
		layer_0.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		layer_1 = new Texture(Gdx.files.internal("data/Loading/Layer1.png"));
		layer_1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		full = new Texture(Gdx.files.internal("data/Loading/Full.png"));
		full.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		empty = new Texture(Gdx.files.internal("data/Loading/Empty.png"));
		empty.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		logo = new Texture(Gdx.files.internal("data/Loading/Logo.png"));
		logo.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		font = new BitmapFont(Gdx.files.internal("data/Loading/ItemFont.fnt"), false);

		TextureRegion region2 = new TextureRegion(texturebkg);
		TextureRegion region3 = new TextureRegion(texturebeegame);

		spritebkg = new Sprite(region2);
		spritebeeGAME = new Sprite(region3);
		spritebeeGAME.setPosition(
				480 / 2 - spritebeeGAME.getWidth() / 2,
				800 / 2 - spritebeeGAME.getHeight() / 2);
		spritebkg.setPosition(0, 0);
		yScroll = 400 - layer_1.getHeight() / 2;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		percent = 0;
//		Assets.playMusic(music);
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Assets.pauseMusic(music);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Assets.pauseMusic(music);
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
//		Assets.playMusic(music);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		batch.dispose();
		music.dispose();
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.disableBlending();
		batch.enableBlending();
		batch.setProjectionMatrix(camera.combined);
		
		
		
		
		
		
		try {
			if (diamondLink.getAssetManager().update() && percent > 0.99f && eclapsedTime > 3f) {
				if (diamondLink.getScreenBeforePaused() == null) {
					System.out.println("dissssssssssssssssssssssssssssss");
					diamondLink.setScreen(diamondLink.getLogin());
					System.out.println("dissssssssssssssssssssssssssssss");
					if (handleWhenComplete != null)
					handleWhenComplete.run();

				} else {
					diamondLink.setScreen(diamondLink.getScreenBeforePaused());
					diamondLink.setScreenBeforePaused(null);
				}
			} 
		} catch (Exception e) {
			error = e.getMessage();
		}
		
		eclapsedTime += delta;
		if (eclapsedTime > 3f) {
			percent = Interpolation.linear.apply(percent, diamondLink.getAssetManager().getProgress(), 0.1f);
			batch.begin();
			
			
			batch.draw(layer_0, 0, 0);
//			batch.setColor(0f, 0f, 0f, 0.5f);
			batch.draw(layer_1, 240 - layer_1.getWidth() / 2, yScroll);
			batch.setColor(1f, 1f, 1f, 1f);
			yScroll += delta * 50;
			if (yScroll > 800) {
				yScroll = -layer_1.getHeight();
			}
			batch.draw(logo, 240 - logo.getWidth() / 2, 364);
			batch.draw(empty, 240 - empty.getWidth() / 2, 321);
			batch.draw(full, 1 + 240 - empty.getWidth() / 2, 321, (empty.getWidth() * percent), full.getHeight());
			batch.end();
		} else {
			batch.begin();
			for (int i = 0; i < 480; i++) {
				spritebkg.setPosition(i, 0);
				spritebkg.setSize(1, 800);
				spritebkg.draw(batch);
			}
			spritebeeGAME.setColor(1f, 1f, 1f, 1f);
			spritebeeGAME.draw(batch);
			// spritelogo.draw(batch);
			batch.end();
		}
		
		batch.begin();
		font.setScale(0.5f);
		font.drawWrapped(batch, error, 0, 240, 240);
		batch.end();
		
		super.render(delta);
	}

	@Override
	public void playMusic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseMusic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

}
