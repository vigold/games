package vn.sunnet.qplay.diamondlink.logiceffects;


import java.util.Random;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;









public class Little_Disappear extends Effect {
	
	Random random = new Random();
	int saveValue = -1;
	
	public Little_Disappear(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen);
		this.screen = screen;
		this.logic = logic;
		type = LITTLE_DISAPPEAR;
		Priority = 2;
	}
	
	@Override
	public void recycleEffect() {
		// TODO Auto-generated method stub
		super.recycleEffect();
		saveValue = -1;
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		for (int i = 0; i < realTarget.size(); i++) {
			int value = realTarget.get(i).intValue();
			Diamond diamond = (Diamond) screen.diamonds.get(value);
			diamond.setAction(Diamond.GRADUALLY_DISAPPEAR);
			diamond.setAnimationTime(iFunctions.getFastFloat(prefix+" disappear "+i, 0));
			System.out.println("parse "+getClass().toString()+" cell "+value);
		}
	}
	
	@Override
	public void save(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.save(iFunctions, prefix);
		// TO-Do can save action cua cac diamond
		for (int i = 0; i < realTarget.size(); i++) {
			int value = realTarget.get(i).intValue();
			Diamond diamond = (Diamond) screen.diamonds.get(value);
			iFunctions.putFastFloat(prefix+" disappear "+i, diamond.getAnimationTime());
		}
	}
	
	public void concurrentResolve() {
		int i = source[0] / 8; int j = source[0] % 8;
		int dType = logic.diamondType(logic.grid[i][j]);
		int dColor = logic.diamondColor(logic.grid[i][j]);
		int maxCol = logic.maxCol(i, j, dColor); int minCol = logic.minCol(i,j, dColor);
		int maxRow = logic.maxRow(i, j, dColor); int minRow = logic.minRow(i, j, dColor);
		int effectType = this.getType();
		if (maxRow - minRow > 1) {// tam anh huong doc
			for (int t = minRow ; t < maxRow + 1; t++) {
				logic.effectOf[t][j].setEffect(this, false);
				this.addM_Targer(new Integer(t * 8 + j));	
			}
			if (1 < effectType) logic.repairCombine(maxRow, minRow, maxCol, minCol, this);
		// tao o day nhung SOIL_EXPLODE
		} else 
		if (maxCol - minCol > 1) {// tam anh huong ngang
			int k = -1 , l = -1;
			for (int t = minCol ; t < maxCol + 1; t++) {
				logic.effectOf[i][t].setEffect(this, false);
				this.addM_Targer(new Integer(i * 8 + t));
			}
			// chi xet khi la hieu ung hop vien
			if (effectType > 1) logic.repairCombine(maxRow, minRow, maxCol, minCol, this);
			// tao o day nhung SOIL_EXPLODE
		}
	}
	
	public void toConcurrentEffect(){
//		for (int i = 0; i < mirrorTarget.size(); i++) {
//			Integer integer = mirrorTarget.get(i);
//			int cell = integer.intValue();
//			int t = cell / 8;
//			int j = cell % 8;
//			if (t - 1 >= 0) {
//				if (isRock(t - 1, j)) 
//					logic.newEffect(t - 1, j, (Effect) this.getPreEffect(), 1);
//				if (screen.GAME_ID == GameScreen.MISSION_DIAMOND)
//				if (((MissionDiamond)screen).mission != null) {
//					if (((MissionDiamond)screen).mission.getType() == Mission.FIND_SHODE) {
//						FindShoe mission = (FindShoe) ((MissionDiamond)screen).mission;
//						mission.removeRocks(t - 1, j);
//					}
//				}	
//			}
//			if (t + 1 < 8) {
//				if (isRock(t + 1, j)) 
//					logic.newEffect(t + 1, j, (Effect) this.getPreEffect(), 1);
//				if (screen.GAME_ID == GameScreen.MISSION_DIAMOND)
//				if (((MissionDiamond)screen).mission != null) {
//					if (((MissionDiamond)screen).mission.getType() == Mission.FIND_SHODE) {
//						FindShoe mission = (FindShoe) ((MissionDiamond)screen).mission;
//						mission.removeRocks(t + 1, j);
//					}
//				}	
//				
//			}
//			if (j - 1 >= 0) {
//				if (isRock(t, j - 1)) 
//					logic.newEffect(t, j - 1, (Effect) this.getPreEffect(), 1);
//				if (screen.GAME_ID == GameScreen.MISSION_DIAMOND)
//				if (((MissionDiamond)screen).mission != null) {
//					if (((MissionDiamond)screen).mission.getType() == Mission.FIND_SHODE) {
//						FindShoe mission = (FindShoe) ((MissionDiamond)screen).mission;
//						mission.removeRocks(t, j - 1);
//					}
//				}	
//			}	
//
//			if (j + 1 < 8) {
//				if (isRock(t, j + 1)) 
//					logic.newEffect(t, j + 1, (Effect) this.getPreEffect(), 1);
//				if (screen.GAME_ID == GameScreen.MISSION_DIAMOND)
//				if (((MissionDiamond)screen).mission != null) {
//					if (((MissionDiamond)screen).mission.getType() == Mission.FIND_SHODE) {
//						FindShoe mission = (FindShoe) ((MissionDiamond)screen).mission;
//						mission.removeRocks(t, j + 1);
//					}
//				}	
//			}	
//		}

	}
	
	public void init() {
		int preType = 0;
		
		handleInBeginEffect();
		
		if (getPreEffect() != null) screen.logic.numberCombo++;
		Sound sound3  = null;
		switch (screen.logic.numberCombo) {
		case 1:
			sound3 = screen.manager.get(SoundAssets.THREE_COMBINE1_SOUND, Sound.class);
			break;
		case 2:
			sound3 = screen.manager.get(SoundAssets.THREE_COMBINE2_SOUND, Sound.class);
			break;
		case 3:
			sound3 = screen.manager.get(SoundAssets.THREE_COMBINE3_SOUND, Sound.class);
			break;
		case 4:
			sound3 = screen.manager.get(SoundAssets.THREE_COMBINE4_SOUND, Sound.class);
			break;
		default:
			sound3 = screen.manager.get(SoundAssets.THREE_COMBINE5_SOUND, Sound.class);
			break;
		}
		if (sound3 != null)
		Assets.playSound(sound3);
		
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;			
			IDiamond diamond = screen.diamonds.get(cell);
			diamond.setAction(Diamond.GRADUALLY_DISAPPEAR);
			if (logic.diamondType(screen.grid[row][col]) != IDiamond.NORMAL_DIAMOND) 
				diamond.setFinish(IDiamond.GRADUALLY_DISAPPEAR);
		}

		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.LITTLE_DISAPPEAR, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			
			changeStatusBeforeRun(row, col);
		}
		
		step = RUNNING_STEP;
	}
	
	public void run(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
			animationTime += deltaTime;
			for (int i = 0 ; i < realTarget.size() ; i++) {
				Integer integer = realTarget.get(i);
				int cell = integer.intValue();
				IDiamond diamond = screen.diamonds.get(cell);
				if (isAffected(cell) && !diamond.isFinished(IDiamond.GRADUALLY_DISAPPEAR))
				diamond.update(deltaTime);	
			}
			if (isFinishing()) freeEffect();
		}
	}
	
	public void draw(float deltaTime) {
		//Log.d("test", "drawEffect");
		if (step > INIT_STEP && step < FINISH_STEP)
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			IDiamond diamond = screen.diamonds.get(cell);
			if (!diamond.isFinished(IDiamond.GRADUALLY_DISAPPEAR) && isAffected(cell)){	
//				Gdx.app.log("test", "tai cell bien mat 3 "+cell+" "+logic.gridFlag[cell / 8][cell % 8]);
				TextureRegion tRegion = diamond.getSprite().getKeyFrame(diamond.getTime());
	        	drawAnimation(tRegion, diamond.getCenterPosition(), diamond.getBound());
//				diamond.draw(deltaTime, screen.batch);
			} 
		}
	}
	
	
	
	public void freeEffect() {
		int generateCell = -1;
		handleInEndEffect();
		boolean isAffectSource = false;
		for (int i = 0 ; i < realTarget.size(); i++) {		
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8;
			int col = cell % 8;
			
			if (isAffected(row, col)) {
				if (generateCell == -1) generateCell = cell;
				if (cell == source[0]) isAffectSource = true;
				logic.effectOf[row][col].effectTarget = null;
				if (logic.grid[row][col] != -1) {
//					System.out.println("an disppear "+row+" "+col);
					saveValue = logic.grid[row][col];
					eatCell(row, col);
					screen.colHeight[col]--;
					System.out.println(source[0]+" an 3 o "+row+" "+col+" "+screen.colHeight[col]+" "+screen.fallingNum[col]);
				}
				logic.grid[row][col] = -1;
			}
		}
		
		if (generateCell != -1)
			createSpecialGem(generateCell / 8, generateCell % 8);
//		if (screen.vip != null)
//			if (screen.vip.id == VipCard.WHITE_TIGER) {
//				int posibility = random.nextInt(100 / 10);
////				if (screen.GAME_ID == GameScreen.MINE_DIAMOND) 
//					posibility = random.nextInt(100 / 5);
//				if (posibility == 0 && screen.vip.getDeplay() == 0) 
//				{
//					screen.vip.resetDelay();
//					int row = source[0] / 8;
//					int col = source[0] % 8;
//					int value = logic.getDiamondValue(IDiamond.HYPER_CUBE, random.nextInt(screen.COLOR_NUM), 0);
//					logic.grid[row][col] = value;
//					Diamond dDiamond = (Diamond) screen.diamonds.get(source[0]);
//					dDiamond.setDiamondValue(logic.grid[row][col]);
//					dDiamond.setAction(Diamond.FRAME_CHANGE);
//					dDiamond.setSize(IDiamond.DIAMOND_WIDTH, IDiamond.DIAMOND_HEIGHT);
//					if (isAffectSource)
//						screen.colHeight[col]++;
//					System.out.println("tao vien "+row+" "+col+" "+logic.gridFlag[row][col]+" "+screen.colHeight[col]);
//					logic.gridFlag[row][col] = Operator.onBit(Effect.FIXED_POS,
//							logic.gridFlag[row][col]);
//					System.out.println("tao vien "+row+" "+col+" "+logic.gridFlag[row][col]+" "+screen.colHeight[col]);
//					if (logic.effectOf[row][col].effectTarget != null) logic.effectOf[row][col].effectTarget = null;
//				}
//			}
		// loai bo goc
		int row = source[0] / 8; int col = source[0] % 8;
		logic.effectOf[row][col].effectIn[this.type] = null;
		
		
		// ghi lai trang thai gridType
		for (int i = 0 ; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
				logic.gridFlag[row][col] = Operator.offBit(
						Effect.LITTLE_DISAPPEAR, logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) {
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS,
						logic.gridFlag[row][col]);
			} 
//			else
//				logic.gridFlag[row][col] = Operator.onBit(Effect.FIXED_POS,
//						logic.gridFlag[row][col]);
		}
		// loai bo con tro
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
		if (screen.logic.diamondType(saveValue) == IDiamond.PEARL_DIAMOND) {
			Diamond diamond = (Diamond) screen.diamonds.get(source[0]);
			Gdx.app.log("test", "bound pearl"+diamond.bounds.width +" "+diamond.bounds.height);
		}
		
	}
	
	public boolean isAffected(int row, int col) {
		Effect effect = (Effect) logic.effectOf[row][col].effectTarget;
		return this.equals(effect);
	}
	
	public boolean isAffected(int cell) {
		int row = cell / 8;
		int col = cell % 8;
		Effect effect = (Effect) logic.effectOf[row][col].effectTarget;
		return this.equals(effect);
	} 
	
	public boolean isFinishing() {
		float frameDuration = 0.05f;
		int frameNumber = (int)(animationTime / frameDuration);
		return (frameNumber > 5);
	}
	
	public boolean isFinished() {
		return step == FINISH_STEP;
	}
	
	
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		screen.combo.inc(1);
		int time = screen.combo.get();
		int score = 100 / 10;
		
		boolean eatXScore = false;
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8;
			int col = cell % 8;
			if (isAffected(cell) && screen.grid[row][col] > -1) {
				
				int value = screen.grid[row][col];
				int dType = logic.diamondType(value);
				int dCost = logic.diamondCost(value);
				if (dType == IDiamond.X_SCORE_GEM) {
					eatXScore  = true;
					time += dCost;
				}
			}
		}
		if (eatXScore) {
			screen.xScore.activeGenerate();
		}
		
//		if (screen.GAME_ID == GameScreen.MINE_DIAMOND) score /= 2;
		screen.levelScore += time * score * mirrorTarget.size();
		
		int color = screen.logic.diamondColor(sourceValue);
		int row = source[0] / 8;
		int col = source[0] % 8;
		float x = screen.gridPos.x + col * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
		float y = screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
		
//		if (screen.vip != null) {
//			switch (screen.vip.id) {
//			case VipCard.GREEN_DRAGON:
//				break;
//			case VipCard.WHITE_TIGER:
//				break;
//			case VipCard.RED_PHOENIX:
//				if (screen.combo.get() % 10 == 0)
//					screen.showMascot("RedPhoenix", screen.gridPos.x + col
//							* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//							* Diamond.DIAMOND_HEIGHT);	
//				break;
//			case VipCard.BLACK_TORTOISE:
//				break;
//			}
//		}
		
		screen.showComboScore(time, score * mirrorTarget.size(), color, x, y);
		screen.showSense(time, x, y);
		
//		createSpecialGem();
	}
	
}
