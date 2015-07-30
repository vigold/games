package vn.sunnet.qplay.diamondlink.actions;



import com.badlogic.gdx.math.Vector2;

public class Delay extends IntervalAction {

	public Delay(float d) {
		super(d);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float deltaTime, Vector2 target) {
		elapsed += deltaTime;
	}
	
}
