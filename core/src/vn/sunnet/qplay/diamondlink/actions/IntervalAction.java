package vn.sunnet.qplay.diamondlink.actions;

import com.badlogic.gdx.math.Vector2;



public class IntervalAction {
    protected float elapsed;
    private boolean firstTick;
    protected float duration;
    protected Vector2 originOfCordinates;
    
    
    public float getElapsed() {
        return elapsed;
    }

    public IntervalAction(float d) {
        duration = d;
        elapsed = 0.0f;
        firstTick = true;
    }
    
    public void setDuration(float d) {
    	duration = d;
    }

    public boolean isDone() {
        return (elapsed >= duration);
    }
    
    public void setOrigin(Vector2 target) {
    	originOfCordinates.x = target.x;
    	originOfCordinates.y = target.y;
    }

    public void step(float dt, Vector2 target) {
        if (firstTick) {
            firstTick = false;
            elapsed = 0;
        } else
            elapsed += dt;

        update(Math.min(1, elapsed / duration), target);
    }

	public void update(float min, Vector2 target) {
		// TODO Auto-generated method stub
	}

	public boolean isFinished() {
		return getElapsed() > duration;
	}
}













