package vn.sunnet.qplay.diamondlink.graphiceffects;

import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class OpenGLEffect {
	
	public ShaderProgram shader;
	GameScreen screen;
	
	protected int mode = LOOP;
	public static final int NORMAL = 0;
	public static final int LOOP = 1;
	
	public static final int RUNNING = 0;
	public static final int END = 1;
	protected int status = END;
	
	public OpenGLEffect(GameScreen screen, ShaderProgram shader) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		this.shader = shader;
	}
	
	public void begin(SpriteBatch batch) {
		if (batch == null) {
			shader.begin();
		} else {
			batch.end();
			if (status == RUNNING)
				batch.setShader(shader);
			batch.begin();
		}
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void resetParams(float ... params) {
		
	}
	
	public void setParams(float ... params) {
		
	}
	
	public void update(float delta) {
		
	}
	
	public void end(SpriteBatch batch) {
		if (batch == null) {
			shader.end();
		} else {
			batch.end();
			batch.setShader(null);
			batch.begin();
		}
	}
	
}
