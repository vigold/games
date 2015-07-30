package vn.sunnet.qplay.diamondlink.logiceffects;

import java.util.Random;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen.Command;
import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.minerdiamond.MinerDiamond;
import vn.sunnet.qplay.diamondlink.minerdiamond.MinerGeneration;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.sun.xml.internal.bind.v2.model.core.ID;

public class SoilExplode extends Effect { // phai code sinh ra hieu ung moi
	public int destroyPow = 1;
	Random rand = new Random();
	int saveValue = 0;
	
	public SoilExplode(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen); 
		this.setType(SOIL_EXPLORE);
		this.screen = screen;
		this.logic = logic;
		Priority = 10;
	}
	
	public void setDestroyPow(int destroyPow){
		this.destroyPow = destroyPow;
	}
	
	public void concurrentResolve() {
		int i = source[0] / 8; 
		int j = source[0] % 8;
			if (!logic.isCombineSource(i, j)){
				logic.effectOf[i][j].setEffect(this, false);
				this.addM_Targer(new Integer(i * 8 + j));
			}	
	}
	
	public void toConcurrentEffect() {
	}
	
	@Override
	public void save(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.save(iFunctions, prefix);
		iFunctions.putFastInt(prefix+" destroyPow", destroyPow);
		iFunctions.putFastInt(prefix+" saveValue", saveValue);
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		destroyPow = iFunctions.getFastInt(prefix+" destroyPow", 0);
		saveValue = iFunctions.getFastInt(prefix+" saveValue", 0);
		animation = gAssets.getEffectAnimation("CellExplode", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.NORMAL);
	}
	
	public void init() {
		animation = gAssets.getEffectAnimation("CellExplode", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.NORMAL);
//		Assets.playSound(Assets.BigExplode, 0);
		handleInBeginEffect();
		for (int i = 0; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; 
			int col = cell % 8;
			if (isAffected(cell))
				eatCell(row, col);
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.SOIL_EXPLORE, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
		}
		int row = source[0] / 8;
		int col = source[0] % 8;
		saveValue = logic.grid[row][col];
		int behindValue = isValueBehindExplode(saveValue);
		if ( behindValue != -1) {
			IDiamond diamond = screen.diamonds.get(source[0]);
			diamond.setDiamondValue(behindValue);
			diamond.setAction(IDiamond.REST);
		} else if (isGem(saveValue)) {
			((MinerDiamond) screen).gemNum++;
		}
		
		((MinerDiamond) screen).showSmoke(screen.gridPos.x + col
				* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
				screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT
						+ Diamond.DIAMOND_HEIGHT / 2);

		
		step = RUNNING_STEP;
	}
	
	public void run(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
			animationTime += deltaTime;
			if (animationTime / AnimationAssets.frameDuration > 5 && step == RUNNING_STEP) {
				step++;
				for (int i = 0 ; i < realTarget.size() ; i++) {
					Integer integer = realTarget.get(i);
					int cell = integer.intValue();
					int row = cell / 8; 
					int col = cell % 8;
					if (isAffected(cell))
						eatCell( row, col);
				}
			}
			if (isFinishing()) freeEffect();
		}
	}
	
	public void draw(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
			int cell = source[0];
			int row = cell / 8; int col  = cell % 8;
			float x = CordXOfCell(cell, 60, 60);
			float y = CordYOfCell(cell, 60, 60);
			
			Diamond diamond = (Diamond) screen.diamonds.get(cell);
			if (isValueBehindExplode(saveValue) != -1 && !isThunder(getPreEffect())) {
				diamond.draw(deltaTime, screen.batch);
				int i = row;
				int j = col;
				diamond = (Diamond) screen.diamonds.get(i * 8 + j);
				if (i + 1 < 8)
					if (!isGemOrMarkOrSoil(screen.grid[i + 1][j])
							|| !certainCell(screen.inGridFlag[i + 1][j])) {

						screen.batch.draw(((MinerDiamond) screen).soilUp,
								diamond.getX(), diamond.getY()
										+ screen.DIAMOND_HEIGHT,
								screen.DIAMOND_WIDTH, 15);

					}

				if (j - 1 >= 0)
					if (!isGemOrMarkOrSoil(screen.grid[i][j - 1])
							|| !certainCell(screen.inGridFlag[i][j - 1])) {
						screen.batch
								.draw(((MinerDiamond) screen).soilLeft,
										diamond.getX() - 16,
										diamond.getY() - 5, 16, 69);
					}

				if (j + 1 < 8)
					if (!isGemOrMarkOrSoil(screen.grid[i][j + 1])
							|| !certainCell(screen.inGridFlag[i][j + 1])) {
						screen.batch.draw(((MinerDiamond) screen).soilRight,
								diamond.getX() + screen.DIAMOND_WIDTH,
								diamond.getY() - 5, 12, 69);
					}
			}
			
			TextureRegion region = animation.getKeyFrame(animationTime);
			drawAnimation(region, x, y, 140, 140, 270);
		}
	}
	
	public boolean isThunder(IEffect effect) {
		return effect instanceof RowThunder || effect instanceof ColThunder
				|| effect instanceof RCThunder
				|| effect instanceof RCThunderItem
				|| effect instanceof RowThunderItem;
	}
	
	public void freeEffect() {
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		

		int value = saveValue;
		int dColor = logic.diamondColor(saveValue);
		int dType = logic.diamondType(saveValue);
		
		handleInEndEffect();
		
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			cell = integer.intValue();
			row = cell / 8; 
			col = cell % 8;
			IDiamond dDiamond = screen.diamonds.get(cell);
			if (isAffected(cell)) {	
				logic.effectOf[row][col].effectTarget = null;	
				{
					System.out.println("truoc no dat tai " + row + " " + col
							+ " co chieu cao la " + screen.colHeight[col]+" "+screen.fallingNum[col]);
					if (logic.grid[row][col] > -1)
					screen.colHeight[col]--;
					logic.grid[row][col] = -1;
					System.out.println("sau no dat tai " + row + " " + col
							+ " co chieu cao la " + screen.colHeight[col]+" "+screen.fallingNum[col]);
				}
				
			}
		}
		
		cell = source[0];
		Diamond dDiamond = (Diamond) screen.diamonds.get(cell);
		row = cell / 8; 
		col = cell % 8;
		
		logic.effectOf[row][col].effectIn[this.type] = null;
		
		logic.grid[row][col] = isValueBehindExplode(saveValue);
		if (logic.grid[row][col] > -1) {
			dDiamond.setDiamondValue(logic.grid[row][col]);
			dDiamond.setAction(Diamond.REST);
			dDiamond.bounds.width = screen.DIAMOND_WIDTH;
			dDiamond.bounds.height = screen.DIAMOND_HEIGHT;
			dDiamond.setCenter(dDiamond.getPosX(), dDiamond.getPosY());
			screen.colHeight[col]++;
			System.out.println("tao dat tai " + row + " " + col
					+ " co chieu cao la " + screen.colHeight[col]);
		}
		
		for (int i = 0; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
			logic.gridFlag[row][col] = Operator.offBit(type, logic.gridFlag[row][col]);
			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			}
			if (logic.grid[row][col] != -1)  logic.gridFlag[row][col] = Operator.onBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
		}
		
		// loai bo con tro
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
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
		return animationTime > 0.05  * 5;
	}
	
	public boolean isFinished() {
		return step == FINISH_STEP;
	}
	
	private void newSoil(int cell, int value){
		int row = cell / 8; 
		int col = cell % 8;
		logic.grid[row][col] = value;
		IDiamond dDiamond = screen.diamonds.get(cell);
//		MyAnimation animation = gAssets.getDiamondAnimation(logic.grid[row][col], screen.getGameID());
		dDiamond.setDiamondValue(value);
//		dDiamond.setSprite(animation);
	}
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		int i = source[0] / 8;
		int j = source[0] % 8;
		
		int type = screen.logic.diamondType(saveValue);
		int color = screen.logic.diamondColor(saveValue);
		System.out.println("SoilExplode____________"+type+" "+color);
		screen.levelScore += MinerGeneration.getScoreOfDiamond(type, color);
		final int value = MinerGeneration.getCoinOfDiamond(type, color);
		final int score = MinerGeneration.getScoreOfDiamond(type, color) / 10;
		switch (type) {
		case IDiamond.SOIL_DIAMOND:
			switch (color) {
			case 6:
			case 5:
			case 4:
				((MinerDiamond) screen).showGoldFly(color, screen.gridPos.x + j
						* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
						screen.gridPos.y + i * Diamond.DIAMOND_HEIGHT
								+ Diamond.DIAMOND_HEIGHT / 2, 203, 153,
						new Command() {
							@Override
							public void execute(Object data) {
								// TODO Auto-generated method stub
								
								screen.addCoins += value;
							}
						});
				break;
			case 3:
			case 2:
			case 1:
			case 0:
				((MinerDiamond) screen).showRockFall(screen.gridPos.x + j
						* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
						screen.gridPos.y + i * Diamond.DIAMOND_HEIGHT
								+ Diamond.DIAMOND_HEIGHT / 2);
			}
			break;
		case IDiamond.MARK_DIAMOND:
			((MinerDiamond) screen).showMarkFly(score, screen.gridPos.x + j
					* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
					screen.gridPos.y + i * Diamond.DIAMOND_HEIGHT
							+ Diamond.DIAMOND_HEIGHT / 2);
			break;
		case IDiamond.LAVA:
		case IDiamond.BLUE_GEM:
		case IDiamond.DEEP_BLUE_GEM:
		case IDiamond.PINK_GEM:
		case IDiamond.RED_GEM:
			if (isValueBehindExplode(saveValue) == -1) {
				System.out.println("saveValue = "+saveValue);
			((MinerDiamond) screen).showGemFly(saveValue % (screen.COLOR_NUM * screen.TYPE_NUM), screen.gridPos.x + j
					* Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2,
					screen.gridPos.y + i * Diamond.DIAMOND_HEIGHT
							+ Diamond.DIAMOND_HEIGHT / 2, 203, 153,
					new Command() {

						@Override
						public void execute(Object data) {
							// TODO Auto-generated method stub
							screen.addCoins += value;
						}
					});
			}
			break;
		}
		
	}
	
	private  boolean isStone(int value){
		int dType = logic.diamondType(value);
		return (dType == IDiamond.SOIL_DIAMOND || dType == IDiamond.ROCK_DIAMOND);
	}
	
	private int isValueBehindExplode(int value) {
		int isCost = value / (screen.COLOR_NUM * screen.TYPE_NUM);
		value = value % (screen.COLOR_NUM * screen.TYPE_NUM);
		int dType = value / screen.COLOR_NUM;
		int dColor = value % screen.COLOR_NUM;
		switch (dType) {
		case IDiamond.SOIL_DIAMOND:
			switch (dColor) {
			case 3:
			case 2:
			case 1:
			case 0:
				dColor = Math.max(dColor - destroyPow, -1);
				return (dColor == -1 ? -1: dType * screen.COLOR_NUM + dColor);
			}
			break;
		case IDiamond.MARK_DIAMOND:
		case IDiamond.DEEP_BLUE_GEM:
		case IDiamond.BLUE_GEM:
		case IDiamond.PINK_GEM:
		case IDiamond.RED_GEM:
			switch (dColor) {
			case 2:
			case 1:
			case 0:
				dColor = Math.max(dColor - destroyPow, -1);
				return (dColor == -1 ? -1: dType * screen.COLOR_NUM + dColor);
			}
			return -1;
		
		}
		return -1;
	}
	

	
	private boolean isGemOrMarkOrSoil(int value) {
		if (value == -1) return false;
		int dType = screen.logic.diamondType(value);
		return dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM || dType == Diamond.MARK_DIAMOND
				|| dType == Diamond.SOIL_DIAMOND;
	}
	
	private boolean isGem(int value) {
		if (value == -1) return false;
		int dType = screen.logic.diamondType(value);
		return dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM;
	}
}
