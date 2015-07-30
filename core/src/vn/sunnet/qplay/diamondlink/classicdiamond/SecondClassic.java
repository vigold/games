package vn.sunnet.qplay.diamondlink.classicdiamond;

import vn.sunnet.qplay.diamondlink.phases.DiamondMove;



public class SecondClassic extends DiamondMove {

	public SecondClassic(ClassicDiamond gameScreen) {
		super(gameScreen);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		super.onBegin();
		screen.solution.resetTime();
	}

}
