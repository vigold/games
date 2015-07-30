package vn.sunnet.qplay.diamondlink.butterflydiamond;

import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.FallModule;



public class ButterflyFall extends FallModule {

	private ButterflyDiamond instance;
	
	public ButterflyFall(ButterflyDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
		instance = screen;
	}
	
	@Override
	protected void fallBeginInGrid(int j) {
		// TODO Auto-generated method stub
		int i = 0;
		int empty = -1;
		IDiamond diamondCur = null;
		IDiamond diamondPre = null;
		IDiamond firstDiamond = null;
	
		if (screen.colHeight[j] < 8) {
			firstDiamond = null;
			diamondCur = null;
			diamondPre = null;
			for (i = 0 ; i < 8 ; i++) { // vong for danh cho grid
				int flag1 = screen.inGridFlag[i][j];
				int value = screen.grid[i][j];
				int dType = diamondType(value);
				if (flag1 == Effect.EMPTY) {
					if (empty == -1) empty = i; 
				} 
				if (empty > -1) {
					if (Operator.getBit(Effect.FIXED_POS, flag1) > 0 && screen.logic.grid[i][j] != -1 && dType != IDiamond.ROCK_DIAMOND) {
						diamondCur = screen.logic.savedDiamonds.newObject(
								screen.gridPos.x + j * screen.DIAMOND_WIDTH
										+ screen.DIAMOND_WIDTH / 2,
								screen.gridPos.y + i
										* screen.DIAMOND_HEIGHT
										+ screen.DIAMOND_HEIGHT / 2,
								screen.DIAMOND_WIDTH,
								screen.DIAMOND_HEIGHT, screen);
						diamondCur.setDestination(screen.gridPos.x + j
								* screen.DIAMOND_WIDTH
								+ screen.DIAMOND_WIDTH / 2,
								screen.gridPos.y + empty
										* screen.DIAMOND_HEIGHT
										+ screen.DIAMOND_HEIGHT / 2);
						fallDown = true;
						diamondCur.setDiamondValue(screen.logic.grid[i][j]);
//						MyAnimation animation = gAssets.getDiamondAnimation(screen.logic.grid[i][j], screen.getGameID());
//						diamondCur.setSprite(animation);
						diamondCur.setAction(Diamond.FALL);
						
						changeStatusBeforeFall(i, j);
//						if (diamondType(screen.logic.grid[i][j]) == IDiamond.SOIL_DIAMOND) {
//							MissionDiamond lScreen  = (MissionDiamond) screen;
//							((SharpenDiamond)lScreen.mission).remove(i, j);
//						}
						
						if (diamondType(screen.logic.grid[i][j]) == IDiamond.BUTTERFLY_DIAMOND) {
							instance.spider.removeButterfly(i * 8 + j);
						}
						
						screen.inGridFlag[i][j] = 0;
						screen.logic.grid[i][j] = -1;
						empty++;
						// phan xau chuoi
						
						if (firstDiamond == null) firstDiamond = diamondCur;
						diamondCur.setPreDiamond(diamondPre);
						if (diamondPre != null) diamondPre.setNextDiamond(diamondCur);
						diamondPre = diamondCur;
						
					} else if (flag1 > Effect.EMPTY || dType == IDiamond.ROCK_DIAMOND) {
						empty = -1;
					}
				}
			}
			
			if (inGridHead[j] == null) {// chua ton tai danh sach truoc do
				inGridHead[j] = firstDiamond;// dau danh sach hien tai la firstDiamond
				inGridTail[j] = diamondCur;// cuoi danh sach hien tai la diamondCur
			} else {// ton tai danh sach truoc do
				if (firstDiamond != null) {// phai ton tai danh sach hien tai
					if (firstDiamond.getPosY() < inGridHead[j].getPosY()) {// danh sach hien taio truoc
						// noi o dau danh sach cu
						inGridHead[j].setPreDiamond(diamondCur); 
						diamondCur.setNextDiamond(inGridHead[j]);
						// sua dau
						inGridHead[j] = firstDiamond;
					} else {// danh sach hien tai o sau
						// noi p duoi danh sach cu
						inGridTail[j].setNextDiamond(firstDiamond);
						firstDiamond.setPreDiamond(inGridTail[j]);
						// sua duoi
						inGridTail[j] = diamondCur;
					}
					// moc noi roi trong vao danh sach roi ngoai
					if (outGridHead[j] != null) {// ton tai danh sach roi ngoai
						outGridHead[j].setPreDiamond(inGridTail[j]);
						inGridTail[j].setNextDiamond(outGridHead[j]);
					}	
				}
			}
			
		}
	}
	
	@Override
	public void CorrectDes(IDiamond diamond) {
		// TODO Auto-generated method stub
		int cell = touchCell(diamond.getCenterPosition());
		int row = CellRow(cell);
		int col = CellCol(cell);
		int cell1 = touchCell(diamond.getSource());
		int row1 = CellRow(cell1);
		int col1 = CellCol(cell1);
		
		IDiamond temp = screen.diamonds.get(cell);
		temp.setSource(diamond.getPosX(), diamond.getPosY());
		temp.setDestination(diamond.getPosX(), diamond.getPosX());

		screen.logic.grid[row][col] = diamond.getDiamondValue();
		temp.setDiamondValue(diamond.getDiamondValue());
//		temp.setSprite(gAssets.getDiamondAnimation(diamond.getDiamondValue(), screen.getGameID()));
		temp.setAction(Diamond.REST);
		temp.getBound().width = screen.DIAMOND_WIDTH; temp.getBound().height = screen.DIAMOND_HEIGHT;
		temp.setCenterPosition(diamond.getPosX(), diamond.getPosY());
		screen.inGridFlag[row][col] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row][col]);
		
		changeStatusBehindFall(row, col);
		
//		if (diamondType(screen.logic.grid[row][col]) == IDiamond.SOIL_DIAMOND) {
//			MissionDiamond lScreen  = (MissionDiamond) screen;
//			((SharpenDiamond)lScreen.mission).add(row, col);
//		}
		
		if (diamondType(screen.logic.grid[row][col]) == IDiamond.BUTTERFLY_DIAMOND) {
			instance.spider.addButterfly(row * 8 + col);
		}
		
		if (!inGrid(row1, col1)) {// ngoai gird
			screen.colHeight[col]++;
			screen.fallingNum[col]--;
			IDiamond diamondPre = diamond.getPreDiamond();
			IDiamond diamondNext = diamond.getNextDiamond();
			if (diamond == outGridHead[col1]) {
				outGridHead[col1] = diamondNext;
				if (outGridHead[col1] == null) {
					outGridHead[col1] = outGridTail[col1] = null;
				}
			} else
			if (diamond == outGridTail[col1]) {
				outGridTail[col1] = diamondPre;
			}
			
			if (diamondPre != null) diamondPre.setNextDiamond(diamondNext);
			if (diamondNext != null) diamondNext.setPreDiamond(diamondPre);
			diamond.setNextDiamond(null);
			diamond.setPreDiamond(null);
			diamond = null;
		} else {// trong grid
			IDiamond diamondPre = diamond.getPreDiamond();
			IDiamond diamondNext = diamond.getNextDiamond();
			if (diamond == inGridHead[col1]) {// neu do la diem dau
				inGridHead[col1] = diamondNext;
				if (inGridHead[col1] == outGridHead[col1]) {// da het danh sach trong grid
					inGridHead[col1] = inGridTail[col1] = null;// xoa danh sach
				}
			} else
			if (diamond == inGridTail[col1]) {// la diem cuoi cua danh sach > 1
				inGridTail[col1] = diamondPre;
			}
			if (diamondPre != null) diamondPre.setNextDiamond(diamondNext);
			if (diamondNext != null) diamondNext.setPreDiamond(diamondPre);
			diamond.setNextDiamond(null);
			diamond.setPreDiamond(null);
			diamond = null;
		}// 
		
		Effect effect = screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(row * 8 + col);
		screen.logic.effects.add(effect);
	}
	
	@Override
	public boolean InCorrectDes(IDiamond diamond) {
		// TODO Auto-generated method stub
		int cell = touchCell(diamond.getCenterPosition());
		int row = CellRow(cell);
		int col = CellCol(cell);
		int cell1 = touchCell(diamond.getSource());
		int row1 = CellRow(cell1);
		int col1 = CellCol(cell1);
		
		if (inGrid(row, col)) {// diem dung o trong
			IDiamond temp = screen.diamonds.get(cell);
			temp.setSource(diamond.getPosX(), diamond.getPosY());
			temp.setDestination(diamond.getPosX(), diamond.getPosY());
			
			screen.logic.grid[row][col] = diamond.getDiamondValue();
			
			if (diamondType(screen.logic.grid[row][col]) == IDiamond.BUTTERFLY_DIAMOND) {
				instance.spider.addButterfly(row * 8 + col);
			}
			
			temp.setDiamondValue(diamond.getDiamondValue());
			temp.setAction(Diamond.REST);
			temp.getBound().width = screen.DIAMOND_WIDTH; temp.getBound().height = screen.DIAMOND_HEIGHT;
			temp.setCenterPosition(diamond.getPosX(), diamond.getPosY());
			screen.inGridFlag[row][col] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row][col]);
			changeStatusBehindFall(row, col);
			
			if (!inGrid(row1, col1)) {
				screen.colHeight[col]++;
				
				screen.fallingNum[col]--;
				
				IDiamond diamondPre = diamond.getPreDiamond();
				IDiamond diamondNext = diamond.getNextDiamond();
				if (diamond == outGridHead[col1]) {
					outGridHead[col1] = diamondNext;
					if (outGridHead[col1] == null) {
						outGridHead[col1] = outGridTail[col1] = null;
					}
				} else
				if (diamond == outGridTail[col1]) {
					outGridTail[col1] = diamondPre;
				}
				
				if (diamondPre != null) diamondPre.setNextDiamond(diamondNext);
				if (diamondNext != null) diamondNext.setPreDiamond(diamondPre);
				diamond.setNextDiamond(null);
				diamond.setPreDiamond(null);
				if (diamond.equals(HeadCur[col1])) HeadCur[col1] = diamondNext;
				if (diamond.equals(TailCur[col1])) HeadCur[col1] = TailCur[col1] = null;	
				diamond = null;
			} else {
				IDiamond diamondPre = diamond.getPreDiamond();
				IDiamond diamondNext = diamond.getNextDiamond();
				
				if (diamond == inGridHead[col1]) {// neu do la diem dau
					inGridHead[col1] = diamondNext;
					if (inGridHead[col1] == outGridHead[col1]) {// da het danh sach trong grid
						inGridHead[col1] = inGridTail[col1] = null;// xoa danh sach
					}
				} else
				if (diamond == inGridTail[col1]) {// la diem cuoi cua danh sach > 1
					inGridTail[col1] = diamondPre;
				}
				if (diamondPre != null) diamondPre.setNextDiamond(diamondNext);
				if (diamondNext != null) diamondNext.setPreDiamond(diamondPre);
				diamond.setNextDiamond(null);
				diamond.setPreDiamond(null);
				diamond = null;
			}
			
			Effect effect = screen.logic.allocateEffect(Effect.TEMP_EFFECT);
			effect.setSource(row * 8 + col);
			screen.logic.effects.add(effect);
			return true;
		} else {// diem dung o ngoai
//			Log.d("Fault1", "lai dung o ngoai "+row+" des "+col+" "+row1+" source "+col1);
					
			//if (col > -1 && col < 8)
			//screen.fallingNum[col]--;
			//screen.lastFallNum[col]--;
		}
		return false;
	}
	
}
