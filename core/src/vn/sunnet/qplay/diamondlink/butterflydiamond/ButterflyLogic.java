package vn.sunnet.qplay.diamondlink.butterflydiamond;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class ButterflyLogic extends GameLogic {

	public boolean changeFlag[][] = new boolean[8][8];
	public boolean checkButterfly = false;// co lien quan den Butterfly
	
	
	public ButterflyLogic(GameScreen screen) {
		super(screen);
		// TODO Auto-generated constructor stub
		initChangeFlag();
	}

	@Override
	public void onCreated() {
		// TODO Auto-generated method stub
		super.onCreated();
		initChangeFlag();
	}
	
	public void initChangeFlag() {
		checkButterfly = false;
		if (changeFlag != null) changeFlag = null;
		changeFlag = new boolean[8][8];
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++)
				changeFlag[i][j] = false;
	}
	
//	public void process(float deltaTime) {
//		
//		saveFirst = first;
//		while (first != effects.size()) {
//			Effect effect = (Effect) effects.get(first);
//			int sourceCell = effect.getSource(0);
//			int i = CellRow(sourceCell); int j = CellCol(sourceCell);
//			int dType = diamondType((int) grid[i][j]);
//			int dColor = diamondColor((int) grid[i][j]);
//			first++;
//			int effectType = effect.getType();
//			int maxCol = maxCol(i, j, dColor); int minCol = minCol(i,j, dColor);
//			int maxRow = maxRow(i, j, dColor); int minRow = minRow(i, j, dColor);
//			Effect temp = null;
//			//if (effectType == Effect.EXPLODE || effectType == Effect.ROW_COL_THUNDER|| effectType == Effect.TRAVEL_THUNDER)
//			Log.d("test", "effectType "+effect.type+" "+i+" "+j+" "+maxRow+" "+minRow+" "+maxCol+" "+minCol);
//			if (effect.step == Effect.BEGIN_STEP)
//			effect.step = Effect.INIT_STEP;
////			if (effectType == 0) {
//////				if (dType == IDiamond.BUTTERFLY_DIAMOND) {
//////					changeFlag[i][j] = true;
//////					checkButterfly = true;
//////					Log.d("SpiderList", "logic is change Butterfly"+i+" "+j);
//////				}
////				isChange = true;
////			}
//			if (effect.step == Effect.INIT_STEP) {
//				effect.concurrentResolve();// tuong tranh o
//				effect.toConcurrentEffect();// den hieu ung xay ra dong thoi
//			}
//		}
//	}
	
	public void onChangeFlag(int i, int j) {
		changeFlag[i][j] = true;
	}
	
	public void offChangeFlag(int i, int j) {
		changeFlag[i][j] = false;
	}
	
	public boolean isChangeFlag(int i, int j) {
		return changeFlag[i][j];
	}
	
	public Diamond allocateDiamond(float x, float y, float width, float height, GameScreen screen) {
		return new DiamondOfButterfly(x, y, width, height, screen);
	}
	
}
