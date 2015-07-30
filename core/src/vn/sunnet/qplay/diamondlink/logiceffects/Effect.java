package vn.sunnet.qplay.diamondlink.logiceffects;



import java.util.ArrayList;
import java.util.Random;


import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.butterflydiamond.ButterflyDiamond;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Effect implements IEffect,HandleInCell {
	// belongs to effects
	public final static int MAX_EFFECT = 21;
	public final static int TEMP_EFFECT = 0;
	public final static int LITTLE_DISAPPEAR = 1;
	public final static int FOUR_ROW_COMBINE = 2;
	public final static int FOUR_COl_COMBINE = 3;
	public final static int FIVE_ROW_COMBINE = 4;
	public final static int FIVE_COL_COMBINE = 5;
	public final static int ROW_COL_COMBINE = 6;
	public final static int EXPLODE = 7;
	public final static int ROW_COL_THUNDER = 8;
	public final static int CHAIN_THUNDER = 9;
	public final static int CELL_EXPLODE = 10;
	public final static int RCTHUNDER_ITEM = 11;
	public final static int CHAIN_THUNDER_ITEM = 12;
	public final static int EXPLODE_ITEM = 13;
	public final static int ROW_THUNDER = 14;
	public final static int COL_THUNDER = 15;
	public final static int ROW_THUNDER_ITEM = 16;
	public final static int EXTRA_CHAIN_THUNDER = 17;
	public final static int SOIL_EXPLORE = 18;
	public final static int CROSS_LASER = 19;
	public final static int CREAT_NEW_DIAMOND = 20;
	// 2097152

	// belongs item
	public static int EMPTY = 0;

	// belong to flags
	public static int TWO_ASPECT_SWAP = 21;// doi 2 vien bang touch
	public static int FIXED_POS = 22;
	public static int ONE_ASPECT_SWAP = 24;// doi vien nho buom len
	public static int UP_TO_GRID = 25;
	
	public final static int BEGIN_STEP = -1;
	public final static int INIT_STEP = 0;
	public final static int RUNNING_STEP = 1;
	public final static int FINISH_STEP = 10;
	protected GameScreen screen;
	protected GameLogic logic;
	protected Assets gAssets;
	public MyAnimation animation = null;
	public boolean isFinish = false;
	
	public boolean twoSwapFlag = false;
//	public int effectScore = 0;
	public int step = BEGIN_STEP;
	public float animationTime = 0;
	public int depth = 0;
	
	public int type = 0;
	
	public int[] source;
	public ArrayList<Integer> mirrorTarget = new ArrayList<Integer>(); 
	public ArrayList<Integer> realTarget = new ArrayList<Integer>();
	public IEffect nextEffect = null;
	public IEffect preEffect = null;
	
	public int sourceValue = 0;
	
	public int Priority = 0;
	
	private boolean status;
	
	Random random = new Random();
	
	public Effect(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		type = -1;
		source = null;
		realTarget.clear();
		mirrorTarget.clear();
		gAssets = screen.gGame.getAssets();
		status = false;
		this.logic = logic;
		this.screen = screen;
	}
	
	public void recycleEffect() {
		twoSwapFlag = false;
		step = BEGIN_STEP;
		animationTime = 0;
		isFinish = false;
		depth = 0;
		nextEffect = null;
		preEffect = null;
		sourceValue = 0;
		source = null;
		realTarget.clear();
		mirrorTarget.clear();
		status = false;
	}
	
	public int getsValue() {
		return sourceValue;
	}
	
	public void setsValue(int value) {
		sourceValue = value;
		sourceValue = Math.max(0, sourceValue);
	}
	
	public void setSwapFlag(boolean flag) {
		twoSwapFlag = flag;
	}
	
	public void setType(int i) {
		type = i;
		switch (type) {
			case 0 : 
			case 1 : 
			case 2 :
			case 3 :
			case 4 :
			case 5 :
			case 6 :  
				
			case 7 :source = new int[1];
		}
	}
	
	public void setSource(int ... cell) {
		if (source != null) source = null;
		source = cell; 
	}
	
	public void addTarget(Integer cell) {
		realTarget.add(cell);
	}
	
	public void setNextEffect(IEffect effect) {
		nextEffect = effect;
	}
	
	public void setPreEffect(IEffect effect) {
		preEffect = effect;
	}
	
	public int getType() {
		return type;
	}
	
	public int getSource(int i) {
		return source[i];
	}
	
	public int getTarget(int pos) {
		assert(pos < realTarget.size() && pos > -1);
		Integer integer = realTarget.get(pos);
		return integer.intValue();
	}
	
	public IEffect getNextEffect() {
		return nextEffect;
	}
	
	public IEffect getPreEffect() {
		return preEffect;
	}

	public int getDepthBFS() {
		return depth;
	}
	
	public void setFinish() {}
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void addM_Targer(Integer cell) {
		mirrorTarget.add(cell);
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		switch (step) {
			case INIT_STEP : init(); break;
			default : run(deltaTime);
		}
	}

	@Override
	public boolean getSwapFlag() {
		// TODO Auto-generated method stub
		return twoSwapFlag;
	}

	public boolean certainCell(int value) {
		return (Operator.hasOnly(Effect.FIXED_POS, value));// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	@Override
	public void concurrentResolve() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void toConcurrentEffect() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void run(float deltaTime) {}
	
	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStepEffect() {
		// TODO Auto-generated method stub
		return step;
	}
	
	protected void drawAnimation(TextureRegion region, float x, float y, float width, float height) {
		
		screen.batch.draw(region, x - width / 2, y - height / 2, width, height);
		
	}
	
	protected void drawAnimation(TextureRegion region, float x, float y, float width, float height, float angle) {
		screen.batch.draw(region, x - width / 2, y - height / 2, width / 2 , height / 2 ,
				width, height, 1f, 1f, angle);
		
	}
	
	protected void drawAnimation(TextureRegion region, Vector2 pos, Rectangle bound) {
		
		screen.batch.draw(region,pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width, bound.height);
	
	}
	protected void drawAnimation(TextureRegion region, Vector2 pos, Rectangle bound, float angle) {	
		screen.batch.draw(region, pos.x - bound.width / 2, pos.y - bound.height / 2, bound.width / 2  , bound.height / 2 , 
				bound.width, bound.height, 1f, 1f, angle);
	}
	
	protected float CordXOfCell(int cell, int width, int height) {
		return CordXOfCell(cell / 8, cell % 8, width, height);
	}
	
	protected float CordYOfCell(int cell, int width, int height) {
		return CordYOfCell(cell / 8, cell % 8, width, height);
	}
	
	protected float CordXOfCell(int row, int col, int width, int height) {
		float result = 0;
		result = screen.gridPos.x + col * width + width / 2;
		
		return result;
	}
	
	protected float CordYOfCell(int row, int col, int width, int height) {
		float result = 0;
		result = screen.gridPos.y + row * height + height / 2;
		
		return result;
	}
	
	public boolean isCellValid(int row, int col) {
		if (screen.grid[row][col] == -1)
			return false;
		int type = logic.diamondType(screen.grid[row][col]);
		return (type != IDiamond.ROCK_DIAMOND)
				&& (type != IDiamond.SOIL_DIAMOND)
				&& (type != IDiamond.BOX_DIAMOND)
				&& (type != IDiamond.PEARL_DIAMOND)
				&& (type != IDiamond.FIVE_COLOR_DIAMOND);
	}



	@Override
	public void setAutoActive(boolean status) {
		// TODO Auto-generated method stub
		this.status = status;
	}

	@Override
	public boolean getAutoActive() {
		// TODO Auto-generated method stub
		return status;
	}
	
	public void changeStatusBeforeRun(int row, int col) {
		screen.ai.removeCell(row, col);
	} 
	
	public void changeStatusBehindEnd(int row, int col) {
		screen.ai.addCell(row, col);
	}
	
	protected void eatCellInClassic(int i, int j) {
	
	}
	
	protected void eatCellInAdventure(int i , int j) {
	}
	
	protected void eatCellInButterfly(int i, int j) {
		ButterflyDiamond game = (ButterflyDiamond) screen;
	
		game.spider.butterflyMove = true;
		if (logic.diamondType(screen.grid[i][j]) == IDiamond.BUTTERFLY_DIAMOND) {
			Gdx.app.log("Test", "butterfly out in "+i+" "+j);
			game.showButterflyOutOfGrid(logic.diamondColor(screen.grid[i][j]),
					screen.gridPos.x + j * screen.DIAMOND_WIDTH
							+ screen.DIAMOND_WIDTH / 2, screen.gridPos.y + i
							* screen.DIAMOND_HEIGHT + screen.DIAMOND_HEIGHT / 2);
			game.spider.removeButterfly(i * 8 + j);
			Assets.playSound(game.butterflyCured);
			
			if (game.butterflyNum < 10) screen.levelScore += 5000 / 10;
			else
			if (game.butterflyNum < 20) screen.levelScore += 10000 / 10;
			else if (game.butterflyNum < 30) screen.levelScore += 20000 / 10;
			else if (game.butterflyNum < 40) screen.levelScore += 40000 / 10;
			else screen.levelScore += 80000 / 10;
		}
	}
	
	protected void eatCellInMiner(int i, int j) {
		
	}
	
	
	
	public void eatCell(int i, int j) {
		if (screen.grid[i][j] != -1) {
			int dType = screen.logic.diamondType(screen.grid[i][j]);
			switch (dType) {
			case IDiamond.COIN_DIAMOND:
				Sound sound = screen.manager.get(SoundAssets.EAT_COIN_SOUND,
						Sound.class);
				Assets.playSound(sound);
				screen.showItem(15, "CoinDiamond", screen.gridPos.x + j
						* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
						screen.gridPos.y + i * Diamond.DIAMOND_HEIGHT
								+ Diamond.DIAMOND_HEIGHT / 2, 100, 153);
				break;
			case IDiamond.CLOCK_DIAMOND:
				sound = screen.manager.get(SoundAssets.EAT_TIME_SOUND, Sound.class);
				Assets.playSound(sound);
				screen.showItem(5, "ClockDiamond", screen.gridPos.x + j
						* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
						screen.gridPos.y + i * Diamond.DIAMOND_HEIGHT
								+ Diamond.DIAMOND_HEIGHT / 2,
						DiamondLink.WIDTH / 2, screen.gridPos.y - 30);
				break;
			}
		}
		switch (screen.getGameID()) {
			case GameScreen.CLASSIC_DIAMOND:
				eatCellInClassic(i, j);
				break;
			case GameScreen.BUTTERFLY_DIAMOND:
				eatCellInButterfly(i, j);
				break;
			case GameScreen.MINE_DIAMOND:
				eatCellInMiner(i, j);
				break;
		}
	}
	
	protected void handleClassicInBeginEffect() {
		
	}
	
	protected void handleButterflyInBeginEffect() {
		
	}
	
	protected void handleMineInBeginEffect() {
		
	}
	
	
	
	protected void handleInBeginEffect() {
		switch (screen.getGameID()) {
		case GameScreen.CLASSIC_DIAMOND:
			handleClassicInBeginEffect();
			break;
		case GameScreen.BUTTERFLY_DIAMOND:
			handleButterflyInBeginEffect();
			break;
		case GameScreen.MINE_DIAMOND:
			handleMineInBeginEffect();
			break;
		}
	}
	
	protected void handleClassicInEndEffect() {
		
	}
	
	protected void handleButterflyInEndEffect() {
		
	}
	
	protected void handleMineInEndEffect() {
		
	}
	
	protected void handleInEndEffect() {
		switch (screen.getGameID()) {
		case GameScreen.CLASSIC_DIAMOND:
			handleClassicInEndEffect();
			break;
		case GameScreen.BUTTERFLY_DIAMOND:
			handleButterflyInEndEffect();
			break;
		case GameScreen.MINE_DIAMOND:
			handleMineInEndEffect();
			break;
		}
	}
	
	protected boolean canEffect(int row, int col) {
		int value = logic.grid[row][col];
		int dType = logic.diamondType(value);
		if (value != -1
				&& (dType == IDiamond.PEARL_DIAMOND || dType == IDiamond.BOX_DIAMOND))
			return false;
		return true;
	}
	
	protected boolean canEffect(int cell) {
		return canEffect(cell / 8, cell % 8);
	}
	
	protected boolean isValidCell(int row, int col) {
		if (logic.grid[row][col] == -1) return false;
		int dType = logic.diamondType(logic.grid[row][col]);
		return dType != Diamond.FIVE_COLOR_DIAMOND && dType != Diamond.LAVA
				&& dType != Diamond.BLUE_GEM && dType != Diamond.DEEP_BLUE_GEM
				&& dType != Diamond.PINK_GEM && dType != Diamond.RED_GEM
				&& dType != Diamond.MARK_DIAMOND
				&& dType != Diamond.SOIL_DIAMOND
				&& dType != Diamond.ROCK_DIAMOND
				&& dType != IDiamond.BOX_DIAMOND
				&& dType != IDiamond.PEARL_DIAMOND;
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
	
	protected boolean isRock(int row, int col) {
		if (logic.grid[row][col] == -1)
			return false;
		int dType = logic.diamondType(logic.grid[row][col]);
		return dType == Diamond.SOIL_DIAMOND || dType == Diamond.ROCK_DIAMOND
				|| dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM;
	}

	protected boolean isGemOrMark(int row, int col) {
		if (logic.grid[row][col] == -1)
			return false;
		int dType = logic.diamondType(logic.grid[row][col]);
		return dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM || dType == Diamond.MARK_DIAMOND;
	}
	
	protected void createSpecialGem() {
		int value = -1;
		switch (screen.combo.get()) {
		case 4:
			
			int dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.FIRE_DIAMOND, dColor, 0);
			
			break;
		case 8:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.LASER_DIAMOND, dColor, 0);
			
			break;
		case 12:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.BLINK_DIAMOND, dColor, 0);
			
			break;
		case 16:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.HYPER_CUBE, dColor, 0);
			
			break;
		case 20:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 2);
			
			break;
		case 24:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 4);
			
			break;
		case 28:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 8);
			
			break;
		case 32:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 16);
			
			break;
		case 36:
			dColor = MathUtils.random(7);
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 32);
			
			break;
		default:
			if (screen.combo.get() > 39){
				dColor = MathUtils.random(7);
				value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 64);
				
				break;
			}
			break;
		}
		
		if (value > -1) {
			
			for (int cell : mirrorTarget) {
				int row = cell / 8;
				int col = cell % 8;
				if (isAffected(row, col)) {
					logic.newEffect(row, col, CREAT_NEW_DIAMOND,value, this);
					break;
				}
			}
			
		}
	}
	
	
	protected void createSpecialGem(int row, int col) {
		int value = -1;
		switch (screen.combo.get()) {
		case 4:
			
			int dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.FIRE_DIAMOND, dColor, 0);
			
			break;
		case 8:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.LASER_DIAMOND, dColor, 0);
			
			break;
		case 12:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.BLINK_DIAMOND, dColor, 0);
			
			break;
		case 16:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.HYPER_CUBE, dColor, 0);
			
			break;
		case 20:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 2);
			
			break;
		case 24:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 4);
			System.out.println("sinh 24 value = "+value+" "+dColor+" "+logic.diamondType(value)+" "+logic.diamondCost(value));
			break;
		case 28:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 8);
			
			break;
		case 32:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 16);
			
			break;
		case 36:
			dColor = MathUtils.random(0 , 6);
			if (dColor == Diamond.WHITE)
				if (screen.generate.isDeletedWhite()) dColor = 0;
			if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
				if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
			value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 32);
			
			break;
		default:
			if (screen.combo.get() > 39){
				dColor = MathUtils.random(0 , 6);
				if (dColor == Diamond.WHITE)
					if (screen.generate.isDeletedWhite()) dColor = 0;
				if (screen.GAME_ID == GameScreen.BUTTERFLY_DIAMOND) 
					if (dColor == Diamond.ORANGE) dColor = 6 - Diamond.ORANGE;
				value = logic.getDiamondValue(Diamond.X_SCORE_GEM, dColor, 64);
				
				break;
			}
			break;
		}
		
		if (value > -1) {
			if (logic.grid[row][col] == -1)
				screen.colHeight[col]++;
			logic.grid[row][col] = value;
			logic.gridFlag[row][col] = Operator.onBit(FIXED_POS,
					logic.gridFlag[row][col]);
			Diamond diamond = (Diamond) screen.diamonds.get(row * 8 + col);
			diamond.setDiamondValue(value);
			diamond.setAction(Diamond.REST);
			diamond.setCenter(
					CordXOfCell(row, col, Diamond.DIAMOND_WIDTH,
							Diamond.DIAMOND_HEIGHT),
					CordYOfCell(row, col, Diamond.DIAMOND_WIDTH,
							Diamond.DIAMOND_HEIGHT));
			
			
			Effect effect = screen.logic.allocateEffect(Effect.TEMP_EFFECT);
			effect.setSource(row * 8 + col);
			screen.logic.effects.add(effect);
			
//			System.out.println("Tao o "+row+" "+col+" loai "+logic.diamondType(value)+" "+screen.combo.get()+" "+value+" "+this.toString());
//			System.out.println("vi tri la "+logic.touchCell(diamond.getPosX(), diamond.getPosY())+" "+diamond.getPosX()+" "+diamond.getPosY());
			
		}
	}
	
	public void save(IFunctions iFunctions , String prefix) {
		iFunctions.putFastInt(prefix+" type", type);
		iFunctions.putFastInt(prefix+" depth", depth);
		iFunctions.putFastInt(prefix+" step", step);
		iFunctions.putFastFloat(prefix+" time", animationTime);
		iFunctions.putFastBool(prefix+" swapFlag", twoSwapFlag);
		String data = "";
		for (int i = 0; i < source.length; i++)
			data+= "|"+source[i];
		data = data.substring(1);
		iFunctions.putFastString(prefix+" sources", data);
		iFunctions.putFastInt(prefix+" sourceValue", sourceValue);
		iFunctions.putFastInt(prefix+" priority", Priority);
		data ="";
		for (int i = 0; i < realTarget.size(); i++)
			data += "|"+realTarget.get(i).intValue();
		if (data != "")
			data = data.substring(1);
		iFunctions.putFastString(prefix+" realTargets", data);
		
		data ="";
		for (int i = 0; i < mirrorTarget.size(); i++)
			data += "|"+mirrorTarget.get(i).intValue();
		if (data != "")
			data = data.substring(1);
		iFunctions.putFastString(prefix+" mirrorTargets", data);
	}
	
	public void parse(IFunctions iFunctions, String prefix) {
		type = iFunctions.getFastInt(prefix+" type", 0);
		depth = iFunctions.getFastInt(prefix+" depth", 0);
		step = iFunctions.getFastInt(prefix+" step", 0);
		animationTime = iFunctions.getFastFloat(prefix+" time", 0);
		twoSwapFlag = iFunctions.getFastBool(prefix+" swapFlag", false);
		
		String data = iFunctions.getFastString(prefix+" sources", "");
		if (data != "") {
			String split[] = data.split("\\|");
			source = new int[split.length];
			for (int i = 0; i < split.length; i++)
				source[i] = Integer.parseInt(split[i]);
		}
		
		
		iFunctions.putFastString(prefix+" sources", data);
		sourceValue = iFunctions.getFastInt(prefix+" sourceValue", -1);
		Priority = iFunctions.getFastInt(prefix+" priority", 0);
		
		data = iFunctions.getFastString(prefix+" realTargets", "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length; i++)
				realTarget.add(Integer.parseInt(split[i])) ;
		}
		
		data = iFunctions.getFastString(prefix+" mirrorTargets", "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length; i++)
				mirrorTarget.add(Integer.parseInt(split[i])) ;
		}
		
	}
	
}
