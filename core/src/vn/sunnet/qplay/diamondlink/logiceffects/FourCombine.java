package vn.sunnet.qplay.diamondlink.logiceffects;

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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;









public class FourCombine extends Effect {
	
	private float limitTime = -1;

	public FourCombine(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen);
		//type = FOUR_COMBINE;
		this.screen = screen;
		this.logic = logic;
		Priority = 3;
	}
	
	@Override
	public void recycleEffect() {
		// TODO Auto-generated method stub
		super.recycleEffect();
		limitTime = -1;
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step  < RUNNING_STEP) return;
		limitTime = iFunctions.getFastFloat(prefix+" limitTime", 0);
		for (int i = 0; i < realTarget.size(); i++) {
			int value = realTarget.get(i).intValue();
			Diamond diamond = (Diamond) screen.diamonds.get(value);
			String data = iFunctions.getFastString(prefix+" fourcombine "+i, "");
			if (data != "") {
				String split[] = data.split("\\|");
				int dValue = Integer.parseInt(split[0]);
				float sourX = Float.parseFloat(split[1]);
				float sourY = Float.parseFloat(split[2]);
				float x = Float.parseFloat(split[3]);
				float y = Float.parseFloat(split[4]);
				float desX = Float.parseFloat(split[5]);
				float desY = Float.parseFloat(split[6]);
				float time = Float.parseFloat(split[7]);
				diamond.setDiamondValue(dValue);
				diamond.setSource(sourX, sourY);
				diamond.setPosition(x, y);
				diamond.setDestination(desX, desY);
				diamond.setAction(Diamond.COMBINE_MOVE);
				diamond.setAnimationTime(time);
			}
		}
	}
	
	@Override
	public void save(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.save(iFunctions, prefix);
		iFunctions.putFastFloat(prefix+" limitTime", limitTime);
		// TO-Do can save action cua cac diamond
		for (int i = 0; i < realTarget.size(); i++) {
			int value = realTarget.get(i).intValue();
			Diamond diamond = (Diamond) screen.diamonds.get(value);
			float x = diamond.getPosX();
			float y = diamond.getPosY();
			float sourX = diamond.getSourX();
			float sourY = diamond.getSourY();
			float desX = diamond.getDesX();
			float desY = diamond.getDesY();
			float time = diamond.getAnimationTime();
			iFunctions.putFastString(prefix+" fourcombine "+i, diamond.getDiamondValue()+"|"+sourX+"|"+sourY+"|"+x+"|"+y+"|"+desX+"|"+desY+"|"+time);
		}
	}
	
	public void concurrentResolve() {
		int i = source[0] / 8; int j = source[0] % 8;
		int dType = logic.diamondType(logic.grid[i][j]);
		int dColor = logic.diamondColor(logic.grid[i][j]);
		int maxCol = logic.maxCol(i, j, dColor); int minCol = logic.minCol(i,j, dColor);
		int maxRow = logic.maxRow(i, j, dColor); int minRow = logic.minRow(i, j, dColor);
		int effectType = this.getType();
		if (maxRow - minRow > 2) {// tam anh huong doc
			for (int t = minRow ; t < maxRow + 1; t++) {
				logic.effectOf[t][j].setEffect(this, false);
				this.addM_Targer(new Integer(t * 8 + j));
			
			}
			if (1 < effectType) logic.repairColCombine(maxRow, minRow, maxCol, minCol, this);
		// tao o day nhung SOIL_EXPLODE
			
		} else 
		if (maxCol - minCol > 2) {// tam anh huong ngang
			int k = -1 , l = -1;
			for (int t = minCol ; t < maxCol + 1; t++) {
				
				logic.effectOf[i][t].setEffect(this, false);
				this.addM_Targer(new Integer(i * 8 + t));
				
			}
			// chi xet khi la hieu ung hop vien
			if (effectType > 1) logic.repairRowCombine(maxRow, minRow, maxCol, minCol, this);
			// tao o day nhung SOIL_EXPLODE
		
		}
	}
	
	public void toConcurrentEffect() {	
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
	
	public void init(){	
		
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
		
		Sound sound = screen.manager.get(SoundAssets.FOUR_COMBINE_SOUND, Sound.class);
		Assets.playSound(sound);
		IDiamond sDiamond = screen.diamonds.get(this.source[0]);
		float maxTime = -1;
		////Log.d("test", "FourCombine "+sDiamond.position.x+"  "+sDiamond.position.x+" "+realTarget.size());
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			IDiamond dDiamond = screen.diamonds.get(cell);
			dDiamond.setSource(
					CordXOfCell(cell, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(cell, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT));
			dDiamond.setDestination(
					CordXOfCell(source[0], screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(source[0], screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT));
			
			dDiamond.setAction(Diamond.COMBINE_MOVE);
			if (source[0] == cell) dDiamond.setFinish(Diamond.COMBINE_MOVE);
			// tinh thoi gian limit
			int row1 = source[0] / 8;
			int col1 = source[0] % 8;
			float v = (screen.DIAMOND_WIDTH * 1f) / (3 * 0.05f);//20f/0.05f;
			if (row1 == row) {
				maxTime = (Math.abs(col - col1) * screen.DIAMOND_HEIGHT * 1f) / v;
			} else {
				maxTime = (Math.abs(row - row1) * screen.DIAMOND_WIDTH * 1f) / v;
			}
			if (limitTime < maxTime) limitTime  = maxTime;
						//
			
			//eatCell(row, col);
			if (logic.diamondType(logic.grid[row][col]) == IDiamond.BUTTERFLY_DIAMOND) 
				dDiamond.setFinish(IDiamond.COMBINE_MOVE);		
		}
		
		handleInBeginEffect();
		
		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(type, logic.gridFlag[row][col]);
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
				if (isAffected(cell) && !diamond.isFinished(IDiamond.COMBINE_MOVE))
				diamond.update(deltaTime);
			}
			if (isFinishing()) freeEffect();
		}
	}
	
	public void draw(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP)
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			IDiamond diamond = screen.diamonds.get(cell);
			if (isAffected(cell))
			if ((!diamond.isFinished(IDiamond.COMBINE_MOVE) && cell != source[0]) || cell == source[0]) {
				diamond.draw(deltaTime, screen.batch);
			}
		}
	}
	
	
	
	public boolean isFinishing() {
		return (animationTime > limitTime);

	}
	
	public void freeEffect() {
		int generateCell = -1;
		handleInEndEffect();
		Gdx.app.log("test", "4 combine finish "+source[0]);
		for (int i = 0 ; i < realTarget.size() ; i++) {// co the khong co effect target nao nen phai xu ly source o ngoai
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			IDiamond dDiamond = screen.diamonds.get(cell);
			if (isAffected(cell)) {
				
				logic.effectOf[row][col].effectTarget = null;
				if (cell != source[0]) {
					if (generateCell == -1) generateCell = cell;
					if (logic.grid[row][col] != -1) {
						screen.colHeight[col]--;
						eatCell(row, col);
					}
					logic.grid[row][col] = -1;
				} else {
					if (logic.grid[row][col] != -1) eatCell(row, col);
				}
				
			}
		}
		
		if (generateCell != -1)
			createSpecialGem(generateCell / 8, generateCell % 8);
		
		int cell = source[0];
		IDiamond dDiamond = screen.diamonds.get(cell);
		int row = cell / 8; int col = cell % 8;
		
		logic.effectOf[row][col].effectIn[this.type] = null;
				
		int dColor = logic.diamondColor(logic.grid[row][col]);
		int dType = 0;
		if (type == FOUR_ROW_COMBINE)
			dType = IDiamond.RT_DIAMOND;
		else
			dType = IDiamond.CT_DIAMOND;
		logic.grid[row][col] = logic.getDiamondValue(dType, dColor, 0);
//		MyAnimation animation = gAssets.getDiamondAnimation(logic.grid[row][col], screen.getGameID());
		dDiamond.setDiamondValue(logic.grid[row][col]);
//		dDiamond.setSprite(animation);
		dDiamond.setAction(IDiamond.REST);
		changeStatusBehindEnd(row, col);
		
		if (screen.getGameID() == GameScreen.MISSION_DIAMOND) {
//			MissionDiamond gScreen = (MissionDiamond) screen;
//			Mission gMission = gScreen.gMission;
//			if (gMission.type == Mission.COLLECTON) {
//				int color = logic.diamondColor(logic.grid[row][col]);
//				if (color == gMission.sameColor)
//					gMission.sameDiamond++;
//			}
		}
			
		
		for (int i = 0; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
			logic.gridFlag[row][col] = Operator.offBit(type, logic.gridFlag[row][col]);
//			if (screen.GAME_ID == GameScreen.ADVENTURE_DIAMOND && logic.effectOf[row][col].getSumOfEffect() == 0)
//				((CombatDiamond) screen).gCertainCol[col] = Operator.offBit(row, ((CombatDiamond) screen).gCertainCol[col]);
			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}
			if (cell == source[0])  logic.gridFlag[row][col] = Operator.onBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
		}
		
		// loai bo con tro
		
		
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
		// xem xet o vua moi tao
		cell = source[0];
		row = cell / 8;
		col = cell % 8;
		Effect effect = screen.logic.allocateEffect(Effect.TEMP_EFFECT);
		effect.setSource(row * 8 + col);
		screen.logic.effects.add(effect);
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
	
	public boolean isFinished() {
		return step == FINISH_STEP;
	}
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		screen.combo.inc(1);
		int time = screen.combo.get();
		int score = 600/10;
		
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
		screen.levelScore += time * score;
		
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
		
		screen.showComboScore(time, score, color, x, y);
		screen.showSense(time, x, y);
		
//		createSpecialGem();
	}
}
