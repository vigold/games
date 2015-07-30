package vn.sunnet.qplay.diamondlink.minerdiamond;

import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.SoilExplode;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;


public class MineLogic extends GameLogic{
	public boolean changeFlag[][] = new boolean[8][8];
	public MineLogic(GameScreen screen) {
		super(screen);
		// TODO Auto-generated constructor stub
		initChangeFlag();
	}

	public void initChangeFlag() {
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++)
				changeFlag[i][j] = false;
	}
	

	public void process(float deltaTime) {
		saveFirst = first;
		while (first != effects.size()) {
		
			Effect effect = (Effect) effects.get(first);
			int sourceCell = effect.getSource(0);
			int i = CellRow(sourceCell); int j = CellCol(sourceCell);
			int dType = diamondType((int) grid[i][j]);
			int dColor = diamondColor((int) grid[i][j]);
			first++;
			int effectType = effect.getType();
			int maxCol = maxCol(i, j, dColor); int minCol = minCol(i,j, dColor);
			int maxRow = maxRow(i, j, dColor); int minRow = minRow(i, j, dColor);
			Effect temp = null;

			if (effect.step == Effect.BEGIN_STEP) {
				effect.step = Effect.INIT_STEP;
			}
			if (effect.step == Effect.INIT_STEP) {
				effect.concurrentResolve();// tuong tranh o
				effect.toConcurrentEffect();// den hieu ung xay ra dong thoi
			}
		}
	}
	
	@Override
	public DiamondOfMiner allocateDiamond(float x, float y, float width, float height,
			GameScreen screen) {
		// TODO Auto-generated method stub
		DiamondOfMiner diamond = new DiamondOfMiner(x, y, width, height, screen);
		return diamond;
	}
	
}
