package vn.sunnet.qplay.diamondlink.modules;





import java.util.ArrayList;
import java.util.List;


import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.CellExplode;
import vn.sunnet.qplay.diamondlink.logiceffects.ChainedThunderItem;
import vn.sunnet.qplay.diamondlink.logiceffects.ColThunder;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.logiceffects.EffectInfo;
import vn.sunnet.qplay.diamondlink.logiceffects.EffectPool;
import vn.sunnet.qplay.diamondlink.logiceffects.ExplodeItem;
import vn.sunnet.qplay.diamondlink.logiceffects.IEffect;
import vn.sunnet.qplay.diamondlink.logiceffects.RCThunderItem;
import vn.sunnet.qplay.diamondlink.logiceffects.RowThunder;
import vn.sunnet.qplay.diamondlink.logiceffects.SoilExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.scenario.animation.SplineInterpolator;










public class GameLogic {
	//Logic State
	public int state = 0;
	public static int ON_BEGIN = 0;
	public static int ON_RUNNING = 1;
	public static int ON_END = 2;
	//public static int ON_INIT = 3;
	public static int ON_REST = 3;
	// belongs to recycle Objects
	public PoolDiamond savedDiamonds = null;
	public EffectPool [] savedEffects = null;
	//
	public ArrayList<IEffect> effects = null;
	public EffectInfo effectOf[][] = new EffectInfo[8][8];
	public int grid[][] = null;
	public int gridFlag[][] = null;
	// Indices relative to BFS
	public int saveFirst = 0;
	public int first = 0;
	int last = 0;
	
	// Flag relative to swap diamonds
	public boolean isChange = false;
	// Count thunders effects exist
	public int SpecialEffect = 0;
	
	public GameScreen screen;
	
	public int numberCombo = 0;
	
	public GameLogic(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		
		initFactory();	
		onCreated();
	}
	
	public void initFactory() {
		if (savedDiamonds != null) savedDiamonds = null;
		savedDiamonds = new PoolDiamond(64);
		if (savedEffects != null) savedEffects = null;
		savedEffects = new EffectPool[Effect.MAX_EFFECT];
		
		for (int i = 0 ; i < Effect.MAX_EFFECT ; i++) {
			savedEffects[i] = new EffectPool(i, 10, screen);
		}
	}
	
	public void initParams() {
		state = ON_BEGIN;
		saveFirst = 0;
		first = 0;
		last = 0;
		// Flag relative to swap diamonds
		isChange = false;
		// Count thunders effects exist
		SpecialEffect = 0;
		
		effects = screen.checkCellEffect;
		grid = screen.grid;
		gridFlag = screen.inGridFlag;
		first = 0;
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++) {
				
				if (effectOf[i][j] != null) effectOf[i][j] = null;
				effectOf[i][j] = new EffectInfo();
			}
	}
	
	public void onCreated() {
		initFactory();
		initParams();
	}
	
	public boolean isChange() {
		return isChange;
	}
	
	public void init() {
		state = ON_BEGIN;
		//Log.d("test", "Logicinit");
	}
	
	public void update(float deltaTime) {
//		System.out.println("state = "+state+"------------------------------------");
		switch (state) {
			case 0: onBegin(); 
					onRunning(deltaTime); 
					//if (state == ON_END) onEnd(); 
			break;
			case 1: onRunning(deltaTime); break;
			case 2: onEnd(); break;
			case 3: break;
		}
	}

	public void onBegin() {// // goi ham nay khi can khoi tao sau onCreated
		isChange = false;
		//if (screen.stateGame == ClassicDiamond.DIAMOND_ANIMATION)
		state = ON_RUNNING;
	}
	
	public void onEnd() {
		if (effects.size() > 0) {
		//effects.clear();
			
			for (int i = 0 ; i < effects.size() ; i++) {
				IEffect effect = effects.get(i);
//				if (effect.getType() == Effect.CELL_EXPLODE)
//				Gdx.app.log("Saved", ""+effect.getType()+" "+effect.getSource(0));
				if (effect.getType() < Effect.MAX_EFFECT)
				savedEffects[effect.getType()].free(effect); // tham chieu ve pool
				effects.remove(i); // tham chieu trong arraylist
				i--;
				effect = null; // tham chieu trong vong lap
			}
		}
		first = 0;
		saveFirst = first;
		
		//Log.d("test", "EndLogic");
	}
	
	public void onRunning(float deltaTime) {
		//Log.d("test", "Logicrunning");
		input(deltaTime);
		process(deltaTime);
		output(deltaTime);
	}
	
	private void output(float deltaTime) {
		// TODO Auto-generated method stub
		// dau vao la mang checkCellEffect
		for (int i = 0; i < 8 ; i++) 
			for (int j = 0 ; j < 8 ; j++) 
			if (effectOf[i][j] != null) {
				EffectInfo info = effectOf[i][j];
				IEffect effect = (IEffect) info.effectTarget;
				int index = effects.indexOf(effect);
				if ( index > saveFirst - 1 && index < first) effect.addTarget(i * 8 +j);
			}
		
//		createdEffect.clear();
		
		for (int i = saveFirst ; i < effects.size() ; i++) {
			Effect effect = (Effect) effects.get(i);
			if (effect.step == Effect.INIT_STEP) {
//				System.out.println("tao effect"+effect.getType()+" "+effect.getSource(0) / 8+" "+effect.getSource(0) % 8);
				effect.update(deltaTime);
			}
		}
		
		if (!isEndCheck()) {
			state = ON_END;
		}
	}
	
	public boolean isCombineSource(int row, int col) {
		if ( effectOf[row][col].effectIn[Effect.ROW_COL_COMBINE] != null) return true;
		if ( effectOf[row][col].effectIn[Effect.FIVE_COL_COMBINE] != null) return true;
		if ( effectOf[row][col].effectIn[Effect.FIVE_ROW_COMBINE] != null) return true;
		if ( effectOf[row][col].effectIn[Effect.FOUR_ROW_COMBINE] != null) return true;
		if ( effectOf[row][col].effectIn[Effect.FOUR_COl_COMBINE] != null) return true;
		return false;
	} 
	
	private void process(float deltaTime) {
		// TODO Auto-generated method stub
//		Log.d("test", "Logicprocess");
		saveFirst = first;
//		System.out.println("process "+first+" "+effects.size());
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
//				System.out.println("tao effect loai"+effect.getType()+" tai "+i +" "+ j);
			}
			if (effect.step == Effect.INIT_STEP) {
				effect.concurrentResolve();// tuong tranh o
				effect.toConcurrentEffect();// den hieu ung xay ra dong thoi
			}
		}
		
	}

	public void repairCombine(int maxRow, int minRow, int maxCol, int minCol,
			Effect effect) { // sua lai combine khi dong thoi xay ra ROW_COL
								// Combine va Combine khac

		if (maxRow - minRow > 1 && maxCol - minCol > 1) {
			// khi dau vao la ROWCOL_COMBINE
			// Log.d("Effect", "cai gi the"+effect.type);
			int cell = effect.source[0];
			int i = CellRow(cell);
			int j = CellCol(cell);
			int k = -1, l = -1;
			int t = 0;
			int type = 0;
			for (t = minRow; t < maxRow + 1; t++) {
				IEffect effect1 = effectOf[t][j]
						.getEffect(Effect.FOUR_COl_COMBINE);
				IEffect effect2 = effectOf[t][j]
						.getEffect(Effect.FIVE_COL_COMBINE);
				if (effect1 != null || effect2 != null) {
					if (effect1 != null) {
						if (effect1.getStepEffect() > Effect.BEGIN_STEP) {
							type = Effect.FOUR_COl_COMBINE;
							break;
						}
					} else if (effect2 != null) {
						if (effect2.getStepEffect() > Effect.BEGIN_STEP) {
							type = Effect.FIVE_COL_COMBINE;
							break;
						}
					}
				}
			}
			if (t != maxRow + 1)
				repairColCombine(maxRow, minRow, j, j,
						(Effect) effectOf[t][j].getEffect(type));

			for (t = minCol; t < maxCol + 1; t++) {
				IEffect effect1 = effectOf[t][j]
						.getEffect(Effect.FOUR_ROW_COMBINE);
				IEffect effect2 = effectOf[t][j]
						.getEffect(Effect.FIVE_ROW_COMBINE);
				if (effect1 != null || effect2 != null) {
					if (effect1 != null) {
						if (effect1.getStepEffect() > Effect.BEGIN_STEP) {
							type = Effect.FOUR_ROW_COMBINE;
							break;
						}
					} else if (effect2 != null) {
						if (effect2.getStepEffect() > Effect.BEGIN_STEP) {
							type = Effect.FIVE_ROW_COMBINE;
							break;
						}
					}
				}
			}
			if (t != maxCol + 1)
				repairRowCombine(i, i, maxCol, minCol,
						(Effect) effectOf[i][t].getEffect(type));
			for (t = i; t < maxRow + 1; t++)
				if (effectOf[t][j].getEffect(Effect.FOUR_COl_COMBINE) != null
						|| effectOf[t][j].getEffect(Effect.FIVE_COL_COMBINE) != null
						|| effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) != null)
					effectOf[t][j].setEffect(effect, false);
				else
					break;
			for (t = i; t > minRow - 1; t--)
				if (effectOf[t][j].getEffect(Effect.FOUR_COl_COMBINE) != null
						|| effectOf[t][j].getEffect(Effect.FIVE_COL_COMBINE) != null
						|| effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) != null)
					effectOf[t][j].setEffect(effect, false);
				else
					break;
			for (t = j; t < maxCol + 1; t++)
				if (effectOf[i][t].getEffect(Effect.FOUR_ROW_COMBINE) != null
						|| effectOf[i][t].getEffect(Effect.FIVE_ROW_COMBINE) != null
						|| effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) != null)
					effectOf[i][t].setEffect(effect, false);
				else
					break;
			for (t = j; t > minCol - 1; t--)
				if (effectOf[i][t].getEffect(Effect.FOUR_ROW_COMBINE) != null
						|| effectOf[i][t].getEffect(Effect.FIVE_ROW_COMBINE) != null
						|| effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) != null)
					effectOf[i][t].setEffect(effect, false);
				else
					break;

		} else if (maxRow - minRow > 1) {// theo cot
			repairColCombine(maxRow, minRow, maxCol, minCol, effect);
		} else if (maxCol - minCol > 1) {// theo hang
			repairRowCombine(maxRow, minRow, maxCol, minCol, effect);
		}
	}

	public void repairRowCombine(int maxRow, int minRow, int maxCol,
			int minCol, Effect effect) { // / effect thuoc loai ROWCOMBINE
		if (effect == null) {
//			Gdx.app.log("RepairRowCombine", "EFFECT NULL "+maxRow +" "+ minRow+" "+maxCol +" "+minCol);
			return;
		}
		if (effect.source == null) {
//			Gdx.app.log("RepairRowCombine", "KHONG CO NGUON "+maxRow +" "+ minRow+" "+maxCol +" "+minCol);
			return;
		}
		// Loi Null PointerException o dong duoi
		int cell = effect.source[0];
		int i = CellRow(cell);
		int j = CellCol(cell);
		int k = -1;
		int l = -1;
		boolean repair = false;
		for (int t = minCol; t < maxCol + 1; t++)
			if (effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) != null) {// tim
																			// mot
																			// effect
																			// Row_Col_Combine
																			// dau
																			// tien
				// k = i; l = t;
				repair = true;
				if (t - 1 > minCol - 1) {
					if (t - 1 != j) {// chung hang
						boolean use = false;
						// phai kiem tra tiep
						if ((effectOf[i][t - 1]
								.getEffect(Effect.ROW_COL_COMBINE)) == null)
							use = true;
						if (use) {
							k = i;
							l = t - 1;
							break;
						}
					} else
						break;
				} else if (t + 1 < maxCol + 1) {
					if (t + 1 != j) {
						boolean use = false;
						// phai kiem tra tiep
						if ((effectOf[i][t + 1]
								.getEffect(Effect.ROW_COL_COMBINE)) == null)
							use = true;
						if (use) {
							k = i;
							l = t + 1;
							break;
						}
					} else
						break;
				}
			}

		if (repair) {// khi trong qua trinh xet khong xua hien RowColCombine

			for (int t = minCol; t < maxCol + 1; t++) {
				if (effect.equals((Effect) effectOf[i][t].effectTarget)) {// chi
																			// lai
																			// bo
																			// nhung
																			// thanh
																			// phan
																			// nao
																			// co
																			// effectTarget
																			// =
																			// effect

					effectOf[i][t].effectTarget = null;
				}
			}
			if (k == -1 && l == -1) {
				k = i;
				l = j;
			}
			// go goc cu
			// effectOf[i][j].effectIn[effect.type] = new Effect();
			effectOf[i][j].effectIn[effect.type] = null;
			// nap goc moi
			effectOf[k][l].effectIn[effect.type] = effect;

			effect.setSource(k * 8 + l);
			if (effectOf[k][l].effectTarget == null)
				effectOf[k][l].effectTarget = effect;
			else if (effectOf[k][l].effectTarget.getType() == Effect.ROW_COL_COMBINE)
				effectOf[k][l].effectTarget = effect;
			// sua nhan anh huong truc tiep
			int t = 0;
			for (t = l; t > minCol - 1; t--)
				if (effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) == null)
					effectOf[i][t].setEffect(effect, false);
				else
					break;
			IEffect temp = null;
			if (t != minCol - 1) {
				while (t > minCol - 1) {
					if (effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) != null)
						temp = effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE);
					effectOf[i][t].setEffect(temp, false);
					t--;
				}
			}
			for (t = l; t < maxCol + 1; t++)
				if (effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) == null)
					effectOf[i][t].setEffect(effect, false);
				else
					break;
			if (t != maxCol + 1) {
				while (t < maxCol + 1) {
					if (effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE) != null)
						temp = effectOf[i][t].getEffect(Effect.ROW_COL_COMBINE);
					effectOf[i][t].setEffect(temp, false);
					t++;
				}
			}
		}
	}

	public void repairColCombine(int maxRow, int minRow, int maxCol,
			int minCol, Effect effect) {// khi effect vao la loai hop theo cot
		if (effect == null) {
//			Gdx.app.log("RepairColCombine", "eFFECT LOI "+maxRow +" "+ minRow+" "+maxCol +" "+minCol);
			return;
		}
		if (effect.source == null ) {
//			Gdx.app.log("RepairColCombine", "KHONG NGUON "+maxRow +" "+ minRow+" "+maxCol +" "+minCol);
			return;
		}
		int cell = effect.source[0];
		int i = CellRow(cell);
		int j = CellCol(cell);
		int k = -1;
		int l = -1;
		boolean repair = false;

		for (int t = minRow; t < maxRow + 1; t++)
			if (effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) != null) {// chung
																			// cot
				// k = i; l = t;
				repair = true;
				if (t - 1 > minRow - 1) {
					if (t - 1 != i) {
						boolean use = false;
						// phai kiem tra tiep
						if ((effectOf[t - 1][j]
								.getEffect(Effect.ROW_COL_COMBINE)) == null)
							use = true;
						if (use) {
							k = t - 1;
							l = j;
							break;
						}
					} else
						break;
				} else if (t + 1 < maxRow + 1) {
					if (t + 1 != i) {
						boolean use = false;
						// phai kiem tra tiep
						if ((effectOf[t + 1][j]
								.getEffect(Effect.ROW_COL_COMBINE)) == null)
							use = true;
						if (use) {
							k = t + 1;
							l = j;
							break;
						}
					} else
						break;
				}
			} // sua roi

		if (repair) {

			for (int t = minRow; t < maxRow + 1; t++) {
				if (effect.equals((Effect) effectOf[t][j].effectTarget)) {
					// effectOf[t][j].effectTarget = new Effect();
					effectOf[t][j].effectTarget = null;
				}
			}
			if (k == -1 && l == -1) {
				k = i;
				l = j;
			}
			// go goc
			// effectOf[i][j].effectIn[effect.type] = new Effect();
			effectOf[i][j].effectIn[effect.type] = null;
			// nap goc moi
			effectOf[k][l].effectIn[effect.type] = effect;
			effect.setSource(k * 8 + l);
			if (effectOf[k][l].effectTarget == null)
				effectOf[k][l].effectTarget = effect;
			else if (effectOf[k][l].effectTarget.getType() == Effect.ROW_COL_COMBINE)
				effectOf[k][l].effectTarget = effect;
			// sua nhan anh huong truc tiep
			int t = 0;
			IEffect temp = null;
			for (t = k; t > minRow - 1; t--)
				if (effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) == null)
					effectOf[t][j].setEffect(effect, false);
				else
					break;
			if (t != minRow - 1) {
				while (t > minRow - 1) {
					if (effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) != null)
						temp = effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE);
					effectOf[t][j].setEffect(temp, false);
					t--;
				}
			}

			for (t = k; t < maxRow + 1; t++)
				if (effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) == null)
					effectOf[t][j].setEffect(effect, false);
				else
					break;

			if (t != maxRow + 1) {

				while (t < maxRow + 1) {
					if (effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE) != null)
						temp = effectOf[t][j].getEffect(Effect.ROW_COL_COMBINE);
					effectOf[t][j].setEffect(temp, false);
					t++;
				}
			}
		}
	}
	
//	private void input(float deltaTime) {
//		// TODO Auto-generated method stub
//		// loai bo cac effect giong nhau
//		if (first < effects.size()) {
//			int i = first;
//			
//			while (i < effects.size()) {	
//				Effect effect1 = (Effect) effects.get(i);
//				int Cell1 = effect1.getSource(0);
////				System.out.println("temp effect "+Cell1+" flag = "+gridFlag[Cell1 / 8][Cell1 % 8]);
//				if (effect1.type == Effect.TEMP_EFFECT && certainCell(gridFlag[Cell1 / 8][Cell1 % 8])) {
////					System.out.println("temp effect hop le "+Cell1+" flag = "+gridFlag[Cell1 / 8][Cell1 % 8]);
//					int i1 = CellRow(Cell1); int j1 = CellCol(Cell1);
//					int dType = diamondType((int) grid[i1][j1]);
//					int dColor = diamondColor((int) grid[i1][j1]);
//					int maxCol1 = maxCol(i1, j1, dColor); int minCol1 = minCol(i1,j1, dColor);
//					int maxRow1 = maxRow(i1, j1, dColor); int minRow1 = minRow(i1, j1, dColor);
//					
//					int findEffect = -1;
//					int check = 0;
//					if (maxRow1 - minRow1 > 1 || maxCol1 - minCol1 > 1) {
//						int j = i + 1;
//						while (j < effects.size()) {
//							Effect effect2 = (Effect) effects.get(j);
//							int Cell2 = effect2.getSource(0);
//							if (effect2.type == Effect.TEMP_EFFECT && certainCell(gridFlag[Cell2 / 8][Cell2 % 8])) {
//								int i2 = CellRow(Cell2); int j2 = CellCol(Cell2);
//								dType = diamondType((int) grid[i2][j2]);
//								dColor = diamondColor((int) grid[i2][j2]);
//								int maxCol2 = maxCol(i2, j2, dColor); int minCol2 = minCol(i2, j2, dColor);
//								int maxRow2 = maxRow(i2, j2, dColor); int minRow2 = minRow(i2, j2, dColor);
//								
//								if (equalsOfRegions(i1,j1,maxRow1,minRow1,maxCol1,minCol1,i2,j2,maxRow2,minRow2,maxCol2,minCol2)) {
//									effects.remove(j);
//								} else {
//									if (maxRow2 - minRow2 <= 1 && maxCol2 - minCol2 <= 1) effects.remove(j);
//									else {
//										if (check == 0) findEffect = j;
//										check++;
//										j++;
//									}
//								}
//							} else {
//								if (effect2.type == Effect.TEMP_EFFECT) effects.remove(j);
//								else j++;
//							}
//						}
//						if (check > 1) i = findEffect;
//						else i = effects.size();
//						//if (j == effects.size() && !check) i = effects.size();
//					} else effects.remove(i);
//				} else {
//					if (effect1.type == Effect.TEMP_EFFECT) {
////						System.out.println("temp effect ko hop le"+Cell1+" flag = "+gridFlag[Cell1 / 8][Cell1 % 8]);
//						effects.remove(i);
//					}
//					else i++;
//				}
//				//i++;
//			}
//			
//		}
//	
//	}
	
	private void input(float deltaTime) {
		// TODO Auto-generated method stub
		// loai bo cac effect giong nhau
		//int i = first;
		//Log.d("test", "Logicinput");
		if (first < effects.size()) {
			int i = first;
			
			while (i < effects.size()) {	
				Effect effect1 = (Effect) effects.get(i);
			
				if (effect1.type == Effect.TEMP_EFFECT) {
					int Cell1 = effect1.getSource(0);
					int i1 = CellRow(Cell1); int j1 = CellCol(Cell1);
					int dType = diamondType((int) grid[i1][j1]);
					int dColor = diamondColor((int) grid[i1][j1]);
					int maxCol1 = maxCol(i1, j1, dColor); int minCol1 = minCol(i1,j1, dColor);
					int maxRow1 = maxRow(i1, j1, dColor); int minRow1 = minRow(i1, j1, dColor);
					
					int findEffect = -1;
					int check = 0;
					if (maxRow1 - minRow1 > 1 || maxCol1 - minCol1 > 1) {
						int j = i + 1;
						while (j < effects.size()) {
							Effect effect2 = (Effect) effects.get(j);
							if (effect2.type == Effect.TEMP_EFFECT) {
								int Cell2 = effect2.getSource(0);
								int i2 = CellRow(Cell2); int j2 = CellCol(Cell2);
								dType = diamondType((int) grid[i2][j2]);
								dColor = diamondColor((int) grid[i2][j2]);
								int maxCol2 = maxCol(i2, j2, dColor); int minCol2 = minCol(i2, j2, dColor);
								int maxRow2 = maxRow(i2, j2, dColor); int minRow2 = minRow(i2, j2, dColor);
								
								if (equalsOfRegions(i1,j1,maxRow1,minRow1,maxCol1,minCol1,i2,j2,maxRow2,minRow2,maxCol2,minCol2)) {
									effects.remove(j);
								} else {
									if (maxRow2 - minRow2 < 1 && maxCol2 - minCol2 < 1) effects.remove(j);
									else {
										if (check == 0) findEffect = j;
										check++;
										j++;
									}
								}
							} else j++;
						}
						if (check > 1) i = findEffect;
						else i = effects.size();
						//if (j == effects.size() && !check) i = effects.size();
					} else effects.remove(i);
				} else i++;
				//i++;
			}
			
		}
	
	}
	
	private boolean equalsOfRegions(int i1, int j1, int maxRow1, int minRow1,
			int maxCol1, int minCol1, int i2, int j2, int maxRow2, int minRow2,
			int maxCol2, int minCol2) {
		// TODO Auto-generated method stub
		// chieu ngang
		if (maxRow1 - minRow1 <= 1 && maxCol1 - minCol1 <= 1) return false;
		if (maxRow2 - minRow2 <= 1 && maxCol2 - minCol2 <= 1) return false;
		if (maxRow1 - minRow1 <= 1) maxRow1 = minRow1 = i1; // khi loi ra 1 hang ma nho hon <= 1 thi khong tinh
		if (maxCol1 - minCol1 <= 1) maxCol1 = minCol1 = j1;
		if (maxRow2 - minRow2 <= 1) maxRow2 = minRow2 = i2;
		if (maxCol2 - minCol2 <= 1) maxCol2 = minCol2 = j2;
		if (maxRow1 == maxRow2 && minRow1 == minRow2 && maxCol1 == maxCol2 && minCol1 == minCol2) return true;
		// chieu chu thap
		
		return false;
	}

	private boolean containsOfRegions(int i1, int j1, int maxRow1, int minRow1,
			int maxCol1, int minCol1, int i2, int j2, int maxRow2, int minRow2,
			int maxCol2, int minCol2) { // region 1 in region 2
		// TODO Auto-generated method stub
		// chieu ngang
		if (maxRow1 - minRow1 < 1 && maxCol1 - minCol1 < 1) return false;
		if (maxRow2 - minRow2 < 1 && maxCol2 - minCol2 < 1) return false;
		if (maxRow1 == maxRow2 && minRow1 == minRow2 && maxCol1 == maxCol2 && minCol1 == minCol2) return true;
		// chieu chu thap
		return false;
	}
	
	public boolean newEffect(int row, int col, Effect sEffect, int destroyPow){
		effectOf[row][col] = allocateEffectInfo(row, col);
		if (!effectOf[row][col].containsEffect(Effect.SOIL_EXPLORE) ) {
			SoilExplode temp = (SoilExplode) allocateEffect(Effect.SOIL_EXPLORE);
			temp.setType(Effect.SOIL_EXPLORE);
			temp.setSource(row * 8 + col);
			temp.setDestroyPow(destroyPow);
			temp.preEffect = sEffect; 
			if (sEffect != null){
				sEffect.nextEffect = temp;
				temp.depth = sEffect.depth + 1;
			}
			
			effects.add(temp);
			effectOf[row][col].setEffect(temp, true);
			effectOf[row][col].effectTarget = temp; 
			
			if (sEffect != null)
			if (sEffect.getType() == Effect.CHAIN_THUNDER || sEffect.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			
			if (temp.getType() == Effect.CHAIN_THUNDER || temp.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			return true;
		}
		return false;
	}
	
	public boolean newEffect(int row, int col , int eType, Effect sEffect) {
		effectOf[row][col] = allocateEffectInfo(row, col);
		if (!effectOf[row][col].containsEffect(eType) ) {
			Effect temp = allocateEffect(eType);
			temp.setType(eType);
			temp.setSource(row * 8 + col);
			temp.preEffect = sEffect; 
			
			temp.setsValue(grid[row][col]);;
			
			if (sEffect != null) {
				sEffect.nextEffect = temp;
				temp.depth = sEffect.depth + 1;
			}
			effects.add(temp);
			effectOf[row][col].setEffect(temp, true);
			
			if (sEffect != null)
			if (sEffect.getType() == Effect.CHAIN_THUNDER || sEffect.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			
			if (temp.getType() == Effect.CHAIN_THUNDER || temp.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			
			return true;
		}
		return false;
	}
	
	public boolean newEffect(int row, int col , int eType, int createValue, Effect sEffect) {
		effectOf[row][col] = allocateEffectInfo(row, col);
		if (!effectOf[row][col].containsEffect(eType) ) {
			Effect temp = allocateEffect(eType);
			temp.setType(eType);
			temp.setSource(row * 8 + col);
			temp.preEffect = sEffect; 
			
			temp.setsValue(createValue);;
			
			if (sEffect != null) {
				sEffect.nextEffect = temp;
				temp.depth = sEffect.depth + 1;
			}
			effects.add(temp);
			effectOf[row][col].setEffect(temp, true);
			
			if (sEffect != null)
			if (sEffect.getType() == Effect.CHAIN_THUNDER || sEffect.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			
			if (temp.getType() == Effect.CHAIN_THUNDER || temp.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			
			return true;
		}
		return false;
	}
	
	public boolean newEffect(int row, int col , int eType, Effect sEffect, boolean isVipActive) {
		effectOf[row][col] = allocateEffectInfo(row, col);
		if (!effectOf[row][col].containsEffect(eType) ) {
			Effect temp = allocateEffect(eType);
			temp.setType(eType);
			temp.setSource(row * 8 + col);
			temp.preEffect = sEffect; 
			temp.setAutoActive(isVipActive);
			
			if (sEffect != null) {
				sEffect.nextEffect = temp;
				temp.depth = sEffect.depth + 1;
			}
			effects.add(temp);
			effectOf[row][col].setEffect(temp, true);
			
			if (sEffect != null)
			if (sEffect.getType() == Effect.CHAIN_THUNDER || sEffect.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
//			switch (temp.getType()) {
//			case Effect.CHAIN_THUNDER:
//			case Effect.CHAIN_THUNDER_ITEM:
//				
//			case Effect.EXPLODE:
//			case Effect.EXPLODE_ITEM:
//				
//			case Effect.RCTHUNDER_ITEM:
//			case Effect.ROW_COL_THUNDER:
//				
//			case Effect.ROW_THUNDER:
//			case Effect.ROW_THUNDER_ITEM:
//			case Effect.COL_THUNDER:
//				
//			case Effect.FIVE_COL_COMBINE:
//			case Effect.FIVE_ROW_COMBINE:
//			case Effect.FOUR_COl_COMBINE:
//			case Effect.FOUR_ROW_COMBINE:
//				effectOf[row][col].effectTarget = temp;
//				break;
//			}
			
			if (temp.getType() == Effect.CHAIN_THUNDER || temp.getType() == Effect.CHAIN_THUNDER_ITEM) {
				effectOf[row][col].effectTarget = temp;
			}
			
			return true;
		}
		return false;
	}
	
	public boolean toNextEffect(int row, int col, Effect sEffect) {
		int type = -1;
		Effect temp = null;
		effectOf[row][col] = allocateEffectInfo(row, col);
		int tType = diamondType(grid[row][col]);
		int tColor = diamondColor(grid[row][col]);
		//effectOf[s][t] = allocateEffectInfo(s, t);
		if (tType < 2)
		effectOf[row][col].setEffect(sEffect, false);
		else {
			switch (tType) {
			case IDiamond.FIRE_DIAMOND:
				type = Effect.EXPLODE;
				break;
			case IDiamond.FIVE_COLOR_DIAMOND:
				type = Effect.CHAIN_THUNDER;
				break;
			case IDiamond.BLINK_DIAMOND:
				type = Effect.ROW_COL_THUNDER;
				break;
			case IDiamond.RT_DIAMOND:
				type = Effect.ROW_THUNDER;
				break;
			case IDiamond.CT_DIAMOND:
				type = Effect.COL_THUNDER;
				break;
			case IDiamond.HYPER_CUBE:
				type = Effect.EXTRA_CHAIN_THUNDER;
				break;
			case IDiamond.LASER_DIAMOND:
				type = Effect.CROSS_LASER;
				break;
			}
			if (type > -1)
			return newEffect(row, col, type, sEffect);
		}
		return false;
	}
	
	public int maxRow(int sourceRow, int sourceCol, int sourceColor) {
		int beginRow = sourceRow;
		int type = diamondType(grid[sourceRow][sourceCol]);
		if (!isValidCell(sourceRow, sourceCol))
			return beginRow;// nguon khong la cac loai kim cuong can xet
		while (beginRow + 1 < 8) {
			int u = beginRow + 1;
			if (certainCell(gridFlag[u][sourceCol])) {
				int dType = diamondType(grid[u][sourceCol]);
				int dColor = diamondColor(grid[u][sourceCol]);

				if (isValidCell(u, sourceCol) && (dColor == sourceColor)) {
					if ((dType == IDiamond.PEARL_DIAMOND || type == IDiamond.PEARL_DIAMOND)
							&& dType != type)
						break;
					else
						beginRow++;
				} else
					break;
			} else
				break;
		}

		return beginRow;
	}
	
	public int minRow(int sourceRow, int sourceCol, int sourceColor) {
		int beginRow = sourceRow;
		int type = diamondType(grid[sourceRow][sourceCol]);
		if (!isValidCell(sourceRow, sourceCol))
			return beginRow;// nguon khong la cac loai kim cuong can xet
		while (beginRow - 1 > -1) {
			int u = beginRow - 1;
			if (certainCell(gridFlag[u][sourceCol])) {
				int dType = diamondType(grid[u][sourceCol]);
				int dColor = diamondColor(grid[u][sourceCol]);

				if (isValidCell(u, sourceCol) && (dColor == sourceColor)) {
					if ((dType == IDiamond.PEARL_DIAMOND || type == IDiamond.PEARL_DIAMOND)
							&& dType != type)
						break;
					else
						beginRow--;
				} else
					break;
			} else
				break;
		}

		return beginRow;
	}
	
	public int maxCol(int sourceRow, int sourceCol, int sourceColor) {
		int beginCol = sourceCol;
		int type = diamondType(grid[sourceRow][sourceCol]);
		if (!isValidCell(sourceRow, sourceCol))
			return beginCol;// nguon khong la cac loai kim cuong can xet
		while (beginCol + 1 < 8) {
			int v = beginCol + 1;
			if (certainCell(gridFlag[sourceRow][v])) {
				int dType = diamondType(grid[sourceRow][v]);
				int dColor = diamondColor(grid[sourceRow][v]);

				if (isValidCell(sourceRow, v) && dColor == sourceColor) {
					if ((dType == IDiamond.PEARL_DIAMOND || type == IDiamond.PEARL_DIAMOND)
							&& dType != type)
						break;
					else
						beginCol++;
				} else
					break;
			} else
				break;
		}

		return beginCol;
	} 
	
	public int minCol(int sourceRow, int sourceCol, int sourceColor) {
		int beginCol = sourceCol;
		int type = diamondType(grid[sourceRow][sourceCol]);
		if (!isValidCell(sourceRow, sourceCol))
			return beginCol;// nguon khong la cac loai kim cuong can xet
		while (beginCol - 1 > -1) {
			int v = beginCol - 1;

			if (certainCell(gridFlag[sourceRow][v])) {
				int dType = diamondType(grid[sourceRow][v]);
				int dColor = diamondColor(grid[sourceRow][v]);

				if (isValidCell(sourceRow, v) && dColor == sourceColor) {
					if ((dType == IDiamond.PEARL_DIAMOND || type == IDiamond.PEARL_DIAMOND)
							&& dType != type)
						break;
					else
						beginCol--;
				} else
					break;
			} else
				break;
		}

		return beginCol;
	}
	
	
	
	public int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	public int diamondColor(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
	}
	
	public int diamondCost(int i) {
		return i / (screen.COLOR_NUM * screen.TYPE_NUM);
	}
	
	public int getDiamondValue(int type, int color, int cost) {
		return (cost * screen.COLOR_NUM * screen.TYPE_NUM) + (type * screen.COLOR_NUM + color);
	}
	
	public boolean neighbourCell(int i,int j) {
		return (i == j - 8) || (i == j + 8) || (i == j - 1 && j % 8 != 0) || (i == j + 1 && i % 8 != 0);
	} 
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}
	
	public int touchCell(float x, float y) {
		int j = (int) (x - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		int i = (int) (y - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		j = (int) (x - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		i = (int) (y - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		return i * 8 + j;
	}
	
	public int touchCell(Vector2 Point) {
		return touchCell(Point.x, Point.y);
	}

	
	
	public EffectInfo allocateEffectInfo(int i, int j) {
		EffectInfo temp = null;
		if (effectOf[i][j] == null) temp = new EffectInfo();
		else temp = effectOf[i][j];
		return temp;
	} 
	
	public boolean isEndCheck() {
		if (effects.size() == 0) return false;

		for (int i = 0 ; i < effects.size() ; i++) {
			Effect effect = (Effect) effects.get(i);
		
			if (!effect.isFinished()) return true;
		}
		return false;
	}
	
	public boolean isSpecialEffect() {
		return (SpecialEffect > 0);
	}
	
	public boolean isValidCell(int row, int col) {
		if (grid[row][col] == -1) return false;
		int dType = diamondType(grid[row][col]);
		return dType != Diamond.FIVE_COLOR_DIAMOND
				&& dType != Diamond.HYPER_CUBE && dType != Diamond.LAVA
				&& dType != Diamond.BLUE_GEM && dType != Diamond.DEEP_BLUE_GEM
				&& dType != Diamond.PINK_GEM && dType != Diamond.RED_GEM
				&& dType != Diamond.MARK_DIAMOND
				&& dType != Diamond.SOIL_DIAMOND
				&& dType != Diamond.ROCK_DIAMOND
				&& dType != IDiamond.BOX_DIAMOND
				&& dType != IDiamond.LASER_DIAMOND;
	}
	
	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public Diamond allocateDiamond(float x, float y, float width, float height, GameScreen screen) {
		return new Diamond(x, y, width, height, screen);
	}
	
	public class PoolDiamond{

	    private final List<IDiamond> freeObjects;
	    
	    private final int maxSize;

	    public PoolDiamond(int maxSize) {
	   
	        this.maxSize = maxSize;
	        this.freeObjects = new ArrayList<IDiamond>(maxSize);
	    }

	    public IDiamond createObject(float x, float y, float width, float height, GameScreen screen) {
	    	return ((GameScreen)screen).logic.allocateDiamond(x, y, width, height, screen);
	    }
	    
	    public IDiamond newObject(float x, float y, float width, float height, GameScreen screen) {
	        IDiamond object = null;

	        if (freeObjects.size() == 0)
	            object = createObject(x, y, width, height, screen);
	        else {
	            object = freeObjects.remove(freeObjects.size() - 1);
	            Diamond diamond = (Diamond) object;
	            diamond.recycleDiamond(x, y, width, height, screen);
	        }

	        return object;
	    }

	    public void free(IDiamond object) {
	        if (freeObjects.size() < maxSize)
	            freeObjects.add(object);
	    }
	    
	}
	
	public Effect allocateEffect(int type) {
		Effect effect = null;
		effect = (Effect) savedEffects[type].obtain();
		effect.recycleEffect();
//		if (type < Effect.SOIL_EXPLORE + 1) {
//			
//		} else {
//			switch (type) {
//				case Effect.EXPLODE_ITEM:
//					effect = new ExplodeItem(this, screen);
//					break;
//				case Effect.CELL_EXPLODE:
//					effect = new CellExplode(this, screen);
//					break;
//				case Effect.RCTHUNDER_ITEM:
//					effect= new RCThunderItem(this, screen);
//					break;
//				case Effect.CHAIN_THUNDER_ITEM:
//					effect= new ChainedThunderItem(this, screen);
//					break;
//				case Effect.ROW_THUNDER:
//					effect = new RowThunder(this, screen);
//					break;
//				case Effect.COL_THUNDER:
//					effect = new ColThunder(this, screen);
//					break;
//				default:
//					break;
//			}
//		}
		return effect;
	}
	
	public boolean existCell(int i, int j) {
		if (!inGrid(i, j)) return false;
		return (grid[i][j] > -1);
	}
	
	public boolean inGrid(int i , int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
	
	public void save(IFunctions iFunctions)
	{
		iFunctions.putFastInt("logic state "+screen.GAME_ID, state);
		iFunctions.putFastInt("logic special "+screen.GAME_ID, SpecialEffect);
		// save Effects
		int index = -1;
		for (int i = 0 ; i < effects.size(); i++) {
			Effect effect = (Effect) effects.get(i);
			if (!effect.isFinished()) {
				index++;
				effect.save(iFunctions, "effect "+screen.GAME_ID+" "+index);
				System.out.println("effect "+effect.getSource(0)+" "+effect.getClass().toString());
			}
		}
		iFunctions.putFastInt("effects "+screen.GAME_ID, index + 1);
		
		// gridInfo
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (effectOf[i][j] != null) {
					IEffect effects[] = effectOf[i][j].effectIn;
					String data = "";
					for (int k = 0; k < effects.length; k++)
					{
						if (effects[k] != null) data += "|"+effects[k].getType();
					}
					if (data != "")
						data = data.substring(1);
					iFunctions.putFastString("info "+screen.getGameID()+" "+i+" "+j, data);
					
					data = "";
					for (int k = 0; k < Effect.MAX_EFFECT; k++)
					{
						data +="|"+effectOf[i][j].amountOfEffect[k];
					}
					if (data != "") data = data.substring(1);
					iFunctions.putFastString("amount "+screen.getGameID()+" "+i+" "+j, data);
					
					data ="";
					if (effectOf[i][j].effectTarget != null)
						data += effectOf[i][j].effectTarget.getType()+"|"+effectOf[i][j].effectTarget.getSource(0);
					iFunctions.putFastString("info target "+screen.getGameID()+" "+i+" "+j, data);
//					for (int i = 0; i < effectOf[i][j].effectIn)
				}
			}
	}
	
	
	public void parse(IFunctions iFunctions) {
		ObjectMap<String, Effect> maps = new ObjectMap<String, Effect>();
		maps.clear();
		effects.clear();
		state = iFunctions.getFastInt("logic state "+screen.GAME_ID, 0);
		SpecialEffect = iFunctions.getFastInt("logic special "+screen.GAME_ID, 0);
		int count = iFunctions.getFastInt("effects "+screen.GAME_ID, 0);
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				int type = iFunctions.getFastInt("effect "+screen.GAME_ID+" "+i+" type", 0);
				Effect effect = allocateEffect(type);
				effect.parse(iFunctions, "effect "+screen.GAME_ID+" "+i);
				effects.add(effect);
				System.out.println("parse effect "+effect.getSource(0)+" "+effect.getClass().toString());
				maps.put(type+" "+effect.getSource(0), effect);
			}
		}
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				effectOf[i][j] = allocateEffectInfo(i, j);
				String data = iFunctions.getFastString("info "+screen.getGameID()+" "+i+" "+j, "");;
				if (data != "") {
					String split[] = data.split("\\|");
					for (int k = 0; k < split.length; k++) {
						int type = Integer.parseInt(split[k]);
						effectOf[i][j].effectIn[type] = maps.get(type+" "+(i * 8 + j));
					}
				}
				data = iFunctions.getFastString("amount "+screen.getGameID()+" "+i+" "+j, "");
				if (data != "") {
					String split[] = data.split("\\|");
					effectOf[i][j].sumOfEffect = 0;
					for (int k = 0; k < split.length; k++) {
						effectOf[i][j].amountOfEffect[k] = Integer.parseInt(split[k]);
						effectOf[i][j].sumOfEffect += effectOf[i][j].amountOfEffect[k];
					}
				}
				
				data = iFunctions.getFastString("info target "+screen.getGameID()+" "+i+" "+j, "");
				if (data != "") {
					String split[] = data.split("\\|");
					int type = Integer.parseInt(split[0]);
					int source = Integer.parseInt(split[1]);
					effectOf[i][j].effectTarget = maps.get(type+" "+source);
					System.out.println(effectOf[i][j].effectTarget.getClass()+" goc "+effectOf[i][j].effectTarget.getSource(0)+" chiem "+i+" "+j);
				}
			}
	}
	
}
