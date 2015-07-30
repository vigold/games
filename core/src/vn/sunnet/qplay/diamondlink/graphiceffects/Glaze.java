package vn.sunnet.qplay.diamondlink.graphiceffects;

import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Glaze extends OpenGLEffect {
	
	private final float DELAY = 7f;
	float delayTime = 0;
	float limit = 0;
	float time = 0;
	float v = 400;
	public float angle = 0;
	float s = 0;
	Vector2 anchor;
	public Vector2 p;

	public Glaze(GameScreen screen, ShaderProgram shader) {
		super(screen, shader);
	}
	
	@Override
	public void update(float delta) {
//		System.out.println("glaze status = "+status);
		if (status == END) {
			delayTime = Math.max(delayTime - delta,  0);
			p.set(anchor.x + v * limit, anchor.y + v * limit);
//			System.out.println(p.x+" "+p.y);
			if (delayTime == 0) {
				delayTime = DELAY;
				setStatus(OpenGLEffect.RUNNING);
				setMode(OpenGLEffect.LOOP);
				resetParams(null);
//				System.out.println(status+" theo glaze "+delayTime+" "+mode);
			}
		} else {
			time = Math.max(0, time - delta);
			p = p.set(p.x + v * delta, p.y - v * delta);
		}
		
		
		float reWidth = Gdx.graphics.getWidth();
        float reHeight = Gdx.graphics.getHeight();
        float width = screen.gCamera.viewportWidth;
        float height = screen.gCamera.viewportHeight;
		
		float length = (float) Math.sqrt(reHeight * reHeight + reWidth * reWidth);
		float angle = MathUtils.degreesToRadians * (45);
		angle = angle * (width / height);
		
		Vector3 point = new Vector3(p.x, p.y, 0);
		screen.gCamera.project(point);
		Vector2 temp = new Vector2(point.x, point.y);
		Vector2 pos = temp.cpy().scl(1 / length); 
		
		
		float a = (float) Math.tan(angle);
		float b = -1.0f;
		float c = -a * pos.x + pos.y;
		
		shader.setUniformf("resolution", reWidth, reHeight);
		shader.setUniformf("equation", a, b, c);
		
		if (time == 0)
			if (mode == LOOP)
				setStatus(END);
	}
	
	@Override
	public void setParams(float... params) {
		// TODO Auto-generated method stub
		s = params[0];
		angle = MathUtils.degreesToRadians * params[1];
		anchor = new Vector2(params[2], params[3]);
		
		Gdx.app.log("test", anchor.x+" achor "+anchor.y);
		
		v = 400;
		limit = s / v; 
		p = new Vector2(anchor);
		time = limit;
	}
	
	@Override
	public void resetParams(float... params) {
		// TODO Auto-generated method stub
		p = new Vector2(anchor);
		time = limit;
	}
	
	@Override
	public void setStatus(int status) {
		// TODO Auto-generated method stub
		super.setStatus(status);
		if (status == END) {
//			System.out.println("xet lai roi");
			p.set(anchor.x + v * limit, anchor.y + v * limit);
		}
	}

}
