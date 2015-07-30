package vn.sunnet.qplay.diamondlink.actions;

import com.badlogic.gdx.math.Vector2;



public class AccelAction {
	public Vector2 velocity;
    public Vector2 accel;
    public Vector2 startPosition;
    public Vector2 position;
    public float eclapsedTime = 0;

    
    public AccelAction(Vector2 v0, Vector2 a, Vector2 start) {
		// TODO Auto-generated constructor stub
    	this.velocity = v0;
    	this.accel = a;
    	this.startPosition = start;
    	this.position = startPosition;
    	this.eclapsedTime = 0;
	}
    
    public void setParams(Vector2 v0, Vector2 a, Vector2 start) {
    	this.velocity = new Vector2(v0);
    	this.accel = new Vector2(a);
    	this.startPosition = new Vector2(start);
    	this.position = new Vector2(start);
    	this.eclapsedTime = 0;
    }
    
    public void setVelocity(Vector2 v0) {
    	this.velocity = v0;
    	this.eclapsedTime = 0;
    }
    
    public void setAccel(Vector2 a) {
    	this.accel = a;
    	this.eclapsedTime = 0;
    }
    
    public void setStartPos(Vector2 position) {
    	this.startPosition.set(position.x, position.y);
    	this.position.set(position.x, position.y);
    	this.eclapsedTime = 0;
    }
    
    public void resetTime() {
    	eclapsedTime = 0;
    }
    
    public Vector2 update(float deltaTime) {
    	eclapsedTime += deltaTime;
    	position.x = startPosition.x + velocity.x * eclapsedTime + accel.x * (eclapsedTime * eclapsedTime) / 2f;
    	position.y = startPosition.y + velocity.y * eclapsedTime + accel.y * (eclapsedTime * eclapsedTime) / 2f;
    	return position;
    }
    
    public float getPosX() {
    	return position.x;
    }
    
    public float getPosY() {
    	return position.y;
    }
}
