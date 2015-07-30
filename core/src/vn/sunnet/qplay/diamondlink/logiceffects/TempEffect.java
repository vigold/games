package vn.sunnet.qplay.diamondlink.logiceffects;

import com.badlogic.gdx.Gdx;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;




public class TempEffect extends Effect {
	
	GameScreen screen;
	GameLogic logic;
	
	
	public TempEffect(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen);
		this.screen = screen;
		this.logic = logic;
		type = TEMP_EFFECT;
	}
	
	public void concurrentResolve() {
		
	}
	
	public void toConcurrentEffect() {
		int i = source[0] / 8;
		int j = source[0] % 8;
		int dType = logic.diamondType(logic.grid[i][j]);
		int dColor = logic.diamondColor(logic.grid[i][j]);
		int maxCol = logic.maxCol(i, j, dColor);
		int minCol = logic.minCol(i, j, dColor);
		int maxRow = logic.maxRow(i, j, dColor);
		int minRow = logic.minRow(i, j, dColor);
		boolean isCombine = false;
		boolean isCombineRow = false;
		boolean isCombineCol = false;
		Gdx.app.log("test", maxRow+" "+maxCol+" "+minRow+" "+minCol);
		if (maxRow - minRow > 1 || maxCol - minCol > 1)
			if (getSwapFlag())
				logic.isChange = true;
		if (maxRow - minRow > 1) {
			 
			for (int t = minRow; t < maxRow + 1; t++) {
				int tType = logic.diamondType(logic.grid[t][j]);
				int tColor = logic.diamondColor(logic.grid[t][j]);
				if (tType == Diamond.FIRE_DIAMOND) {
					logic.newEffect(t, j, Effect.EXPLODE, this);
				} else if (tType == Diamond.BLINK_DIAMOND) {
					// SpecialEffect++;
					isCombineCol = true;
					logic.newEffect(t, j, Effect.ROW_COL_THUNDER, this);
				} else if (tType == Diamond.RT_DIAMOND) {
					logic.newEffect(t, j, Effect.ROW_THUNDER, this);
				} else if (tType == Diamond.CT_DIAMOND) {
					logic.newEffect(t, j, Effect.COL_THUNDER, this);
				}
			}
			// xu ly ngoc trai
			if (dType == IDiamond.PEARL_DIAMOND) {
				maxRow = Math.min(maxRow - minRow, 2) + i;
				minRow = i + (2 - (maxRow - i));
				Gdx.app.log("test", "ngoc trai"+dType);
			}
		}
		if (maxCol - minCol > 1) {
			for (int t = minCol; t < maxCol + 1; t++) {
				int tType = logic.diamondType(logic.grid[i][t]);
				int tColor = logic.diamondColor(logic.grid[i][t]);
				if (tType == Diamond.FIRE_DIAMOND) {
					logic.newEffect(i, t, Effect.EXPLODE, this);
				} else if (tType == Diamond.BLINK_DIAMOND) {
					isCombineRow = true;
					logic.newEffect(i, t, Effect.ROW_COL_THUNDER, this);
				} else if (tType == Diamond.RT_DIAMOND) {
					logic.newEffect(i, t, Effect.ROW_THUNDER, this);
				} else if (tType == Diamond.CT_DIAMOND) {
					logic.newEffect(i, t, Effect.COL_THUNDER, this);
				}
			}
			// xu ly ngoc trai
			if (dType == IDiamond.PEARL_DIAMOND) {
				if (maxRow - minRow > 1) {
					maxCol = j;
					minCol = j;
				} else {
					maxCol = Math.min(maxCol - minCol, 2) + j;
					minCol = j + (2 - (maxCol - j));
				}
				Gdx.app.log("test", "ngoc trai"+dType);
			}
		}

		if (maxRow - minRow > 1 && maxCol - minCol > 1) {
			logic.newEffect(i, j, Effect.ROW_COL_COMBINE, this);
			isCombine = true;
		}
		if (maxRow - minRow > 1) {
			switch (maxRow - minRow + 1) {
			case 3:
				if (!isCombine && !isCombineCol) { // khi co xua hien o chu L o
													// i j
				// Log.d("check", "thu tao effect3 tai"+i+" "+j);
					if (logic.effectOf[i][j].effectIn[Effect.LITTLE_DISAPPEAR] != null) {
						IEffect temp1 = logic.effectOf[i][j].effectIn[Effect.LITTLE_DISAPPEAR];
						// Log.d("check",
						// "da co DISappear tai"+i+" "+j+" co goc la"+temp1.getSource(0)+" do sau"+temp1.getDepthBFS());
						if (temp1.getPreEffect() != null) {
							IEffect temp2 = temp1.getPreEffect();
							// Log.d("check",
							// "effect cha la goc"+temp2.getSource(0)+"loai "+temp2.getType()+" do sau "+temp2.getDepthBFS());
						}
					} else {
						// Log.d("check", "ko co DISappear tai"+i+" "+j);
					}
					logic.newEffect(i, j, Effect.LITTLE_DISAPPEAR, this);

				}
				break;
			case 4:
				// Log.d("Effect", "in Temp Effect new FOUR_COL "+i+" "+j);
				logic.newEffect(i, j, Effect.FOUR_COl_COMBINE, this);
				break;
			default:
				logic.newEffect(i, j, Effect.FIVE_COL_COMBINE, this);
				break;
			}
		}
		if (maxCol - minCol > 1) {
			switch (maxCol - minCol + 1) {
			case 3:
				if (!isCombine && !isCombineRow) {// khi xua hien o chu L o i j
				// Log.d("check", "thu tao effect3 tai"+i+" "+j);
					if (logic.effectOf[i][j].effectIn[Effect.LITTLE_DISAPPEAR] != null) {
						IEffect temp1 = logic.effectOf[i][j].effectIn[Effect.LITTLE_DISAPPEAR];
						// Log.d("check",
						// "da co DISappear tai"+i+" "+j+" co goc la"+temp1.getSource(0)+" do sau"+temp1.getDepthBFS());
						if (temp1.getPreEffect() != null) {
							IEffect temp2 = temp1.getPreEffect();
							// Log.d("check",
							// "effect cha la goc"+temp2.getSource(0)+"loai "+temp2.getType()+" do sau "+temp2.getDepthBFS());
						}
					} else {
						// Log.d("check", "ko co DISappear tai"+i+" "+j);
					}
					logic.newEffect(i, j, Effect.LITTLE_DISAPPEAR, this);
				}
				break;
			case 4:
				// Log.d("Effect", "in Temp Effect new FOUR_ROW "+i+" "+j);
				logic.newEffect(i, j, Effect.FOUR_ROW_COMBINE, this);
				break;
			default:
				logic.newEffect(i, j, Effect.FIVE_ROW_COMBINE, this);
				break;
			}
		}
		this.setFinish();
	}
	
	public void init() { // Effect duoc tao ra do doi cho 2 kim cuong voi nhau hoac roi xuong
	}
	
	public void run(float deltaTime) {
	}
	
	public void draw(float deltaTime) {
	}
	
	public void setFinish() {
		isFinish = true;
		nextEffect = null;
		preEffect = null;
	}
	
	public boolean isFinished() {
		////Log.d("test", "TempEffect"+isFinish);
		return isFinish;
	}
	
}
