package vn.sunnet.qplay.diamondlink.classicdiamond;

import com.badlogic.gdx.Gdx;

import vn.sunnet.qplay.diamondlink.phases.DiamondAnimation;


public class ThirdClassic extends DiamondAnimation {

	public ThirdClassic(ClassicDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		super.onBegin();
		screen.solution.resetTime();
	}
	
	public void ActionHandle() {
		super.ActionHandle();
		if (isComplete() && swapFlag) {// lenh chuyen pha
			branch = ClassicDiamond.DIAMOND_REST;
			phaseState = ON_END;
			Gdx.app.log("test", "Animation to Rest"+swapFlag);
		}
	}
	
}
