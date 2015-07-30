package vn.sunnet.qplay.diamondlink.logiceffects;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class CreateNewDiamond extends Effect {

	public CreateNewDiamond(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		this.type = CREAT_NEW_DIAMOND;
		this.Priority = 11;
		this.logic = logic;
		this.screen = screen;
	}
	
	@Override
	public void concurrentResolve() {
		int row = source[0] / 8;
		int col = source[0] % 8;
		logic.effectOf[row][col] = logic.allocateEffectInfo(row, col);
		logic.effectOf[row][col].setEffect(this, false);
		int value = getsValue();
		if (logic.grid[row][col] == -1) screen.colHeight[col]++;
		logic.grid[row][col] = value;
		logic.gridFlag[row][col] = 0;
		logic.gridFlag[row][col] = Operator.onBit(FIXED_POS, logic.gridFlag[row][col]);
		IDiamond diamond = screen.diamonds.get(source[0]);
		diamond.setDiamondValue(value);
		diamond.setAction(Diamond.REST);
		isFinish = true;
		System.out.println("Tao o"+row+" "+col+" 1 vien "+logic.diamondType(value));
	}
	
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return isFinish;
	}

}
