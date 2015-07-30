package vn.sunnet.qplay.diamondlink.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class StonePiece extends Image {
	
	final int PIXEL_PER_METER = 10;
	final float Po = 30;
	float P = 0;
	float A = 0;
	float Ao = 0;
	float F = 0;
	float m = 1;
	float aX = 0;
	float aY = 0;
	float voX = 0;
	float voY = 0;
	float g = -10;
	float t = 0;
	
	Vector2 position = null;
	
	public StonePiece(TextureRegion region, Vector2 pos, Vector2 explosionCenter, float explosionR) {
		// TODO Auto-generated constructor stub
		super(region);
		float len = pos.cpy().sub(explosionCenter).len();
		len = len / PIXEL_PER_METER;
		explosionR = explosionR / PIXEL_PER_METER;
		
		position = new Vector2(pos);
		setX(pos.x - getPrefWidth() / 2);
		setY(pos.y - getPrefHeight() / 2);
		
		A = 1 / (len * len);
		Ao = 1 / (explosionR * explosionR);
		P = Po * Ao / A;
		F = P * A;
		float angle = MathUtils.atan2(pos.y - explosionCenter.y, pos.x
				- explosionCenter.x);
		aX = F / m * MathUtils.cos(angle);
		aY = F / m * MathUtils.sin(angle) + PIXEL_PER_METER * 2;
		
		voX = 2 * aX;
		voY = aY;
		
		t = 0;
	}
	
	@Override
	public void act(float delata) {
		// TODO Auto-generated method stub
		super.act(delata);
		t += delata;
		float x = position.x + voX * t * PIXEL_PER_METER;
		float y = position.y + voY * PIXEL_PER_METER * t + g * t * t / 2
				* (PIXEL_PER_METER * PIXEL_PER_METER);
		setX(x - getPrefWidth() / 2);
		setY(y - getPrefHeight() / 2);
		if (getX() < -getPrefWidth() || getX() > getStage().getWidth()
				|| getY() < -getPrefHeight() || getY() > getStage().getHeight()) {

			remove();
//			System.out.println("giai phong stone roi" + " " + getX() + " "
//					+ getY());
		}
	}
	
	
	
}
