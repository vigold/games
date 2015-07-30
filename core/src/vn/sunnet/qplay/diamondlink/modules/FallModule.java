package vn.sunnet.qplay.diamondlink.modules;

import java.util.ArrayList;
import java.util.List;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.screens.GameScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;



public class FallModule {
	
	public final static int ON_BEGIN = 0;
	public final static int ON_FALLING = 1;
	public final static int ON_END = 2;
	public int state = ON_END;
	public ArrayList<IDiamond> fallDiamonds = new ArrayList<IDiamond>();
	// quan ly roi kieu cu chi rang buoc roi ngoai
	public IDiamond[] HeadLast = new IDiamond[8];// 
	public IDiamond[] TailLast = new IDiamond[8];//
	public IDiamond[] HeadCur = new IDiamond[8];
	public IDiamond[] TailCur = new IDiamond[8];
	
	// quan ly roi kieu moi rang buoc ca roi trong lan roi ngoai
	public IDiamond[] outGridHead = new IDiamond[8];
	public IDiamond[] outGridTail = new IDiamond[8];
	public IDiamond[] inGridHead = new IDiamond[8];
	public IDiamond[] inGridTail = new IDiamond[8];
	
	public GameScreen screen = null;
	
	public Assets gAssets = null;
	
	public boolean fallDown = false;
	
	private MyAnimation debugAnimation = null;
	
	private Sound sound;
	
	public FallModule(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		this.gAssets = this.screen.gGame.getAssets();
		onCreated();
	}
	
	/********************************Methods Of LifeCircle*********************************/
	
	public void onCreated() {
		for (int i = 0 ; i < 8 ; i++) {
			HeadLast[i] = null;
			TailLast[i] = null;
			HeadCur[i] = null;
			TailCur[i] = null;
			inGridHead[i] = null;
			inGridTail[i] = null;
			outGridHead[i] = null;
			outGridTail[i] = null;
		}
		fallDiamonds.clear();
		
	}
	
	public void update(float deltaTime) {
		switch (state) {
			case ON_BEGIN:
				fallBegin();
				fallRunning(deltaTime);
				break;
			case ON_FALLING:
				fallRunning(deltaTime);
				break;
			case ON_END:
				break;
		}
	}

	protected void fallRunning(float deltaTime) {
		// TODO Auto-generated method stub
		fallDown = false; // flag check fall
		boolean generateE = false;
		IDiamond nextDiamond = null;
		IDiamond diamond = null;
		int first = 0;
		boolean fallInRow[] = new boolean[8];
		for (int i = 0; i < 8; i++) {
			fallInRow[i] = false;
		}
	
		if (screen.logic.SpecialEffect == 0)
		for (int i = 0 ; i < 8 ; i++) {
			diamond = inGridHead[i];
			if (diamond == null) diamond = outGridHead[i];
			while (diamond != null) {
				diamond.update(deltaTime);
				first++;
				nextDiamond = (IDiamond) diamond.getNextDiamond();
				if (diamond.isFinished(IDiamond.FALL)) {
					int cell = touchCell(diamond.getCenterPosition());
					int row = CellRow(cell);
					int col = CellCol(cell);
					if (!fallInRow[row]) {
						
						fallInRow[row] = true;
					}
					if (diamond.isDestination()) {
//						Log.d("Fault1", "FALL END is des from in for"+diamond.getSourX()+" "+diamond.getSourY()+"to"+diamond.getPosX()+" "+diamond.getPosY());
						fallDown = true;
						CorrectDes(diamond);
						generateE = true;
						screen.logic.savedDiamonds.free(diamond);
						diamond = null;
					} else {
						fallDown = true;
						// khong den noi duoc dinh truoc
						boolean delete = InCorrectDes(diamond);
						generateE = true;
						if (delete) {
							screen.logic.savedDiamonds.free(diamond);
							//fallDiamonds.remove(i);
							diamond = null;
						}
					}
				} 
				diamond = nextDiamond;
			}
		}
		
		diamond = null; nextDiamond = null;
		if (generateE) {
			if (screen.logic.state == GameLogic.ON_END) {
				screen.logic.state = GameLogic.ON_RUNNING;
			}
			Assets.playSound(sound);
		}
		
		if (isFallEnd()) {
			state = ON_END;
		}
	}

	
	
	public void portraitFallBegin() {
		int i = 0, j = 0;
		int[][] temp = screen.generate.generatePart(screen.colHeight,
				screen.grid, screen.inGridFlag);
		float bufferX = screen.gridPos.x;
		float bufferY = screen.gridPos.y + 480;
		// quan ly xau chuoi diamond
		IDiamond diamondCur = null;
		IDiamond diamondPre = null;
		IDiamond firstDiamond = null;

		// nap du lieu sinh
		boolean condition = true;
		for (i = 0; i < 8; i++)
			if (screen.colHeight[i] + screen.fallingNum[i] > 0) {
				condition = false;
				break;
			}

		if (condition) {
			temp = screen.generate.generateAll();
		} else {
			temp = screen.generate.generatePart(screen.colHeight, screen.grid,
					screen.inGridFlag);
		}

		for (i = 0; i < 8; i++)
			savedNums[i] = screen.fallingNum[i];

		// chuan bi roi
		for (j = 0; j < 8; j++) {
			fallBeginInGrid(j);
			fallBeginOutGrid(j, temp);
		}

		state = ON_FALLING;
	}
	
	protected void fallBeginInGrid(int j) {
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
						
						IDiamond diamond = screen.diamonds.get(i * 8 + j);
						
						diamondCur.setDiamondValue(screen.logic.grid[i][j]);
						diamondCur.setActivieTime(diamond.getActiveTime());
						diamondCur.setAction(Diamond.FALL);
						
						changeStatusBeforeFall(i, j);
//						if (diamondType(screen.logic.grid[i][j]) == IDiamond.SOIL_DIAMOND) {
//							MissionDiamond lScreen  = (MissionDiamond) screen;
//							((SharpenDiamond)lScreen.mission).remove(i, j);
//						}
						
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
	
	public void changeStatusBeforeFall(int row, int col) {
		screen.ai.removeCell(row, col);
	}
	
	protected void fallBeginOutGrid(int j, int[][] temp) {
		int i = 0;
		int empty = -1;
		IDiamond diamondCur = null;
		IDiamond diamondPre = null;
		IDiamond firstDiamond = null;
		float bufferX = screen.fallPos.x;
		float bufferY = screen.fallPos.y;
		if (screen.colHeight[j] + screen.fallingNum[j] < 8) {
			if (screen.fallingNum[j] == 0) 
				for (i = 0 ; i < 8 ; i++) {
					screen.gridBuf[i][j] = temp[i][j];
				}
			if (screen.colHeight[j] < 0) screen.colHeight[j] = 0;
			
			for (i = 7 ; i > screen.colHeight[j] - 1 ; i--) // kiem tra truong hop trong luc roi di thi bi chan ton tai exception
				if (screen.inGridFlag[i][j] != Effect.EMPTY
						|| diamondType(screen.grid[i][j]) == IDiamond.ROCK_DIAMOND)
					break;
		
			if (7 - i > screen.fallingNum[j]) { // neu nhu bi chan ma so luong roi khong vuot qua so o trong danh cho roi lan nay thi ko tao
				firstDiamond = null;
				diamondCur = null;
				diamondPre = null;
				int add = (7 - i) - screen.fallingNum[j]; // so o can roi tiep
				int pos = 7 - add + 1;
				
				// che do quan ly cu
				bufferY = screen.fallPos.y;
				// che do quan ly moi
				firstDiamond = null;
				diamondCur = null;
				diamondPre = null;
				if (outGridTail[j] != null) {
					diamondPre = outGridTail[j];
					bufferY = Math.max(diamondPre.getPosY() + screen.DIAMOND_HEIGHT / 2, bufferY);
				} else {
					if (inGridTail[j] != null) {
						diamondPre = inGridTail[j];
						bufferY = Math.max(diamondPre.getPosY() + screen.DIAMOND_HEIGHT / 2, bufferY);
					}
				}
				
				float beginX = bufferX;
				float beginY = bufferY;
				
				int number = screen.fallingNum[j] + screen.colHeight[j];
				
				for (i = pos ; i < 8 ; i++) { // cac hang vi tri can roi tu duoi len tren
//					Log.d("test", "roi ngoai toi "+i+"  "+j+" toi "+" co "+screen.grid[i][j]);
					diamondCur = screen.logic.savedDiamonds.newObject(
							beginX + j * screen.DIAMOND_WIDTH
									+ screen.DIAMOND_WIDTH / 2, beginY
									+ screen.DIAMOND_HEIGHT / 2,
							screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT,
							screen);
//					screen.lastFallNum[j]++;
					diamondCur.setDestination(screen.gridPos.x + j
							* screen.DIAMOND_WIDTH + screen.DIAMOND_WIDTH
							/ 2, screen.gridPos.y + i
							* screen.DIAMOND_HEIGHT + screen.DIAMOND_HEIGHT
							/ 2);
					diamondCur.setDiamondValue(temp[number][j]);
//					MyAnimation animation = gAssets.getDiamondAnimation(
//							temp[number][j], screen.getGameID());
					
//					diamondCur.setSprite(animation);
					diamondCur.setAction(Diamond.FALL);
					fallDown = true;
					screen.fallingNum[j]++;
					beginY += screen.DIAMOND_HEIGHT * 2;
					number++;
					// phan xau chuoi
					
					if (firstDiamond == null) firstDiamond = diamondCur;
					if (diamondPre != null) {
						diamondPre.setNextDiamond(diamondCur);
					}
					if (i == pos) HeadCur[j] = diamondCur;
					else if (i == 7) TailCur[j] = diamondCur;
					diamondCur.setPreDiamond(diamondPre);
					diamondPre = diamondCur;
					
				}
				// theo vao danh sach cu danh sach moi chac chan o sau
				if (outGridHead[j] == null) {// khong ton tai danh sach ngoai roi truoc do
					outGridHead[j] = firstDiamond;
					outGridTail[j] = diamondCur;
				} else {// da ton tai danh sach roi ngoai truoc do
					outGridTail[j].setNextDiamond(firstDiamond);
					firstDiamond.setPreDiamond(outGridTail[j]);
					// cap nhat lai duoi
					outGridTail[j] = diamondCur;
				}
				// moc noi vao danh sach roi trong khi co thay doi
				if (inGridHead[j] != null) {
					outGridHead[j].setPreDiamond(inGridTail[j]);
					inGridTail[j].setNextDiamond(outGridHead[j]);
				}
			}
		}	
	}
	
	public void fallBeginDueToExplode(int row, int col) {
		if (screen.logic.SpecialEffect > 0) return;
		int i = 0, j = 0;
		//To do
		int minCol = Math.max(0, col - 1);
		int maxCol = Math.min(7, col + 1);
		for (j = minCol; j < maxCol + 1; j++) {
			int beginRow = row + 2;
			int action = Diamond.UP_AND_FALL;
			if (j == col) action = Diamond.ENHANCE_UP_AND_FALL;
			boolean isFallInGrid = false;
			for (i = row - 2 ; i > -1; i--) {
				if (certainCell(screen.inGridFlag[i][j])) {
					break;
				}
			}
			int endRow = Math.max(i, 0);

			IDiamond firstDiamond = null;
			IDiamond diamondCur = null;
			IDiamond diamondPre = null;
			// roi trong
			for (i = beginRow; i < 8; i++) {
				if (certainCell(screen.inGridFlag[i][j])) {
					isFallInGrid = true;
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
							screen.gridPos.y + endRow
									* screen.DIAMOND_HEIGHT
									+ screen.DIAMOND_HEIGHT / 2);
					IDiamond diamond = screen.diamonds.get(i * 8 + j);
					diamondCur.setDiamondValue(screen.logic.grid[i][j]);
					diamondCur.setActivieTime(diamond.getActiveTime());
					diamondCur.setAction(action);
					
					screen.inGridFlag[i][j] = 0;
					screen.logic.grid[i][j] = -1;
					endRow++;
					// phan xau chuoi
					
					if (firstDiamond == null) firstDiamond = diamondCur;
					diamondCur.setPreDiamond(diamondPre);
					if (diamondPre != null) diamondPre.setNextDiamond(diamondCur);
					diamondPre = diamondCur;
				} else break;
			}
//			Gdx.app.log("test", "End check in Grid "+ i);
			if (!isFallInGrid) {
				
				IDiamond diamond = inGridHead[j];
				if (diamond == null) diamond = outGridHead[j];
//				Gdx.app.log("test", "no khong co roi trong");
				while (diamond != null) {
					float y = screen.gridPos.y + row * screen.DIAMOND_HEIGHT
							+ screen.DIAMOND_HEIGHT / 2;
					if (diamond.getPosY() > y)
						diamond.setAction(action);
					else
						diamond.setAction(Diamond.ENHANCE_FALL);
					diamond = diamond.getPreDiamond();
				}
			} else {// co xay ra roi trong
//				Gdx.app.log("test", "no co roi trong");
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
				
				IDiamond diamond = inGridHead[j];
				if (diamond == null) diamond = outGridHead[j];
				while (diamond != null) {
					diamond.setAction(action);
					diamond = diamond.getPreDiamond();
				}
			}
		}
//		state = ON_FALLING;
	}
	
	public void fallBegin() {
		// TODO Auto-generated method stub
		sound = screen.manager.get(SoundAssets.FALL_DOWN_SOUND, Sound.class);
		if (debugAnimation == null) debugAnimation = screen.assets.getEffectAnimation("CThunder", 0.05f);
		portraitFallBegin();
	}
	
	public void CorrectDes(IDiamond diamond) {
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
		
		temp.setActivieTime(diamond.getActiveTime());
//		if (diamondType(screen.logic.grid[row][col]) == IDiamond.SOIL_DIAMOND) {
//			MissionDiamond lScreen  = (MissionDiamond) screen;
//			((SharpenDiamond)lScreen.mission).add(row, col);
//		}
		
		if (!inGrid(row1, col1)) {// ngoai gird
			screen.colHeight[col]++;
			screen.fallingNum[col]--;
			System.out.println("roi xuong tai"+" "+row+" "+col+" "+screen.colHeight[col]+" "+screen.fallingNum[col]);
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
	
	public boolean InCorrectDes(IDiamond diamond) {
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
			
			temp.setDiamondValue(diamond.getDiamondValue());
			temp.setAction(Diamond.REST);
			temp.getBound().width = screen.DIAMOND_WIDTH; temp.getBound().height = screen.DIAMOND_HEIGHT;
			temp.setCenterPosition(diamond.getPosX(), diamond.getPosY());
			screen.inGridFlag[row][col] = Operator.onBit(Effect.FIXED_POS, screen.inGridFlag[row][col]);
			changeStatusBehindFall(row, col);
			
			temp.setActivieTime(diamond.getActiveTime());
			
			if (!inGrid(row1, col1)) {
				screen.colHeight[col]++;
				
				screen.fallingNum[col]--;
				
				System.out.println("roi xuong tai"+" "+row+" "+col+" "+screen.colHeight[col]+" "+screen.fallingNum[col]);
				
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
		}
		return false;
	}
	
	public void changeStatusBehindFall(int row, int col) {
		screen.ai.addCell(row, col);
	}
	
	public void draw(float deltaTime) {
		Diamond diamond = null;
		Color preColor = screen.batch.getColor();
		if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1f);
		for (int i = 0 ; i < 8 ; i++) {
			diamond = (Diamond) inGridHead[i];
			if (diamond == null) diamond = (Diamond) outGridHead[i];
			while (diamond != null) {
				
				int cell = touchCell(diamond.getPosX(),diamond.getPosY());
	        	int col = cell % 8;
	        	TextureRegion region = null;
	        	Texture texture = null;
	        	int value = 0;
	        	
	        	if (screen.logic.SpecialEffect > 0) diamond.setColorMode(GameObject.BATCH_COLOR);
        		else diamond.setColorMode(GameObject.OBJECT_COLOR);
        		diamond.draw(deltaTime, screen.batch);	
	        	
//        		if (diamond.getPreDiamond() != null) {
//	        		region = debugAnimation.getKeyFrame(diamond.getTime());
////	        		texture = region.texture;
//	        		IDiamond preDiamond = diamond.getPreDiamond();
//	        		float x = (diamond.getPosX() + preDiamond.getPosX()) / 2;
//	        		float y = (diamond.getPosY() + preDiamond.getPosY()) / 2;
//	        		float width = 60;
//	        		float height = diamond.getPosY() - preDiamond.getPosY();
//	        		//Log.d("Draw", ""+x+" "+y+" "+width+" "+height);
//	        		screen.batch.draw(region, diamond.getPosX() - 30, preDiamond.getPosY(), width, height);
////	        		drawAnimation(texture, region, x, y, width, height);
//	        	}
        		
				diamond = (Diamond) diamond.getNextDiamond();
			}
		}
		screen.batch.setColor(preColor);
	}
	
	protected void drawAnimation(TextureRegion region, float x, float y, float width, float height) {
		screen.batch.draw(region, x - width / 2, y - height / 2, width, height);
	}
	
	protected void drawAnimation(TextureRegion region, float x, float y, float width, float height, float angle) {

		screen.batch.draw(region, x - width / 2, y - height / 2, width / 2 , height / 2 ,
				width, height, 1f, 1f, angle, true);
	}
	
	protected void drawAnimation(TextureRegion region, Vector2 pos, Rectangle bound) {
		screen.batch.draw(region,pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width, bound.height);
	}
	
	protected void drawAnimation(TextureRegion region, Vector2 pos, Rectangle bound, float angle) {
		screen.batch.draw(region, pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width / 2  , bound.height / 2 , 
				bound.width, bound.height, 1f, 1f, angle, true);
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
	
	public int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	public int diamondColor(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
	}
	
	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public boolean inGrid(int i , int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
	
	public boolean isFallEnd() {
		for (int i = 0 ; i < 8 ; i++) {
			if (inGridHead[i] != null || outGridHead[i] != null) return false;
		}
		return true;
	}
	
	private int savedNums[] = new int[8];
	
	public void save(IFunctions iFunctions) {
		iFunctions.putFastInt("fall state "+screen.GAME_ID, state);
		for (int i = 0; i < 8; i++) {
			String data = "";
			Diamond cur = null;
			cur = (Diamond) inGridHead[i];
			if (cur == null) cur = (Diamond) outGridHead[i];
			int head1 = -1;
			int tail1 = -1;
			int head2 = -1;
			int tail2 = -1;
			int index = -1;
			while (cur != null) {
				index++;
				if (cur == inGridHead[i]) head1 = index;
				if (cur == inGridTail[i]) tail1 = index;
				if (cur == outGridHead[i]) head2 = index;
				if (cur == outGridTail[i]) tail2 = index;
				float x = cur.getPosX();
				float y = cur.getPosY();
				float sourX = cur.getSourX();
				float sourY = cur.getSourY();
				float desX = cur.getDesX();
				float desY = cur.getDesY();
				float accelPosX = cur.accelStartPos.x;
				float accelPosY = cur.accelStartPos.y;
				float time = cur.getAnimationTime();
				data += "|"+cur.getDiamondValue()+"|"+sourX+"|"+sourY+"|"+x+"|"+y+"|"+desX+"|"+desY+"|"+accelPosX+"|"+accelPosY+"|"+time;
				cur = (Diamond) cur.getNextDiamond();
			}
			if (data != "")
				data = data.substring(1);
			iFunctions.putFastString("fall "+i+" "+screen.GAME_ID, data);
			data = head1+"|"+tail1+"|"+head2+"|"+tail2;
			iFunctions.putFastString("fall extras "+i+" "+screen.GAME_ID, data);
		}
	}
	
	public void parse(IFunctions iFunctions) {
		state = iFunctions.getFastInt("fall state "+screen.GAME_ID, 0);
		for (int i = 0; i < 8; i++) {
			String data = iFunctions.getFastString("fall extras "+i+" "+screen.GAME_ID, "");
			String split[] = data.split("\\|");
			int head1 = Integer.parseInt(split[0]);
			int tail1 = Integer.parseInt(split[1]);
			int head2 = Integer.parseInt(split[2]);
			int tail2 = Integer.parseInt(split[3]);
			
			data = iFunctions.getFastString("fall "+i+" "+screen.GAME_ID, "");
			inGridHead[i] = null;
			inGridTail[i] = null;
			outGridHead[i] = null;
			outGridTail[i] = null;
			Diamond cur = null;
			Diamond pre = null;
			
			if (data != "") {
				System.out.println("fall module parse"+data);
				split = data.split("\\|");
				for (int j = 0; j < split.length / 10; j++) {
					
					int value = Integer.parseInt(split[j * 10]);
					float sourX = Float.parseFloat(split[j * 10 + 1]);
					float sourY = Float.parseFloat(split[j * 10 + 2]);
					float x = Float.parseFloat(split[j * 10 + 3]);
					float y = Float.parseFloat(split[j * 10 + 4]);
					float desX = Float.parseFloat(split[j * 10 + 5]);
					float desY = Float.parseFloat(split[j * 10 + 6]);
					float accelX = Float.parseFloat(split[j * 10 + 7]);
					float accelY = Float.parseFloat(split[j * 10 + 8]);
					float time = Float.parseFloat(split[j * 10 + 9]);
					cur = screen.logic.allocateDiamond(x, y, 60, 60, screen);
					cur.setDiamondValue(value);
					cur.setSource(sourX, sourY);
					cur.setCenterPosition(x, y);
					cur.setDestination(desX, desY);
					cur.setAction(Diamond.FALL);
					cur.accelStartPos.set(accelX, accelY);
					cur.setAnimationTime(time);
					if (pre != null) {
						pre.setNextDiamond(cur);
						cur.setPreDiamond(pre);
					}
					if (j == head1) inGridHead[i] = cur;
					if (j == tail1) inGridTail[i] = cur;
					if (j == head2) outGridHead[i] = cur;
					if (j == tail2) outGridTail[i] = cur;
					pre = cur;
				}
			}
		}
	}
	
}
