package vn.sunnet.qplay.diamondlink.phases;

import vn.sunnet.qplay.diamondlink.IFunctions;

import com.badlogic.gdx.InputProcessor;



public abstract class Phase implements Runnable , InputProcessor{
	public static final int ON_BEGIN = 0;
	public static final int ON_RUNNING = 1;
	public static final int ON_END = 2;
	public static final int OFF_BEGIN = 3;
	public static final int OFF_RUNNING = 4;
	public static final int OFF_END = 5;
	public static final int ON_PAUSE = 6;
	public int phaseState = ON_BEGIN;
	protected float deltaTime = 0;
	public int branch = 0;
	
	public abstract void onReset();
	
	public abstract void onBegin();
	
	public abstract void onRunning();
	
	public abstract void onEnd();
	
	public void run() {
		//Log.d("test", "phaseState ="+phaseState);
		switch (phaseState) {
			case 0 : onBegin(); phaseState = ON_RUNNING; break;
		    case 1: onRunning(); break;
		    case 2 : onEnd(); break;
		}
	}
	
	public void update(float time) {
		//Log.d("phase", "update");
		deltaTime = time;
		run();
	}
	
	public void draw(float deltaTime) {}
	
	public int getState() {
		return phaseState;
	}
	
	public void setState(int state) {
		phaseState = state;
	}
	
	public void transferPhase(int phase) {
		phaseState = ON_END;
		branch = phase;
	}
	
	public void setBranch(int branch) {
		this.branch = branch;
	}
	
	public void save(IFunctions iFunctions) {
		
	}
	
	public void parse(IFunctions iFunctions) {
		
	}
	
	public boolean isActive() {
		return false;
	}
	
	protected abstract boolean touchDownInTouchGame(int screenX, int screenY, int pointer, int button);
	protected abstract boolean touchDownInTouchItem(int screenX, int screenY, int pointer, int button);
	protected abstract boolean touchUpInTouchGame(int screenX, int screenY, int pointer, int button);
	protected abstract boolean touchUpInTouchItem(int screenX, int screenY, int pointer, int button);
	protected abstract boolean touchDraggedInTouchGame(int screenX, int screenY, int pointer);
	protected abstract boolean touchDraggedInTouchItem(int screenX, int screenY, int pointer);
}
