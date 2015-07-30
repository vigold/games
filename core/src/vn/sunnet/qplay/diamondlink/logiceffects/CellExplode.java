package vn.sunnet.qplay.diamondlink.logiceffects;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;






public class CellExplode extends Effect{

	MyAnimation addAnimation = null;
	
	public CellExplode(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		type = CELL_EXPLODE;
		this.screen = screen;
		this.logic = logic;
		this.gAssets = this.screen.gGame.getAssets();
		Priority = 5;
	}
	
	public void concurrentResolve() {
		int i = source[0] / 8; int j = source[0] % 8;
		int dType = logic.diamondType(logic.grid[i][j]);
		int dColor = logic.diamondColor(logic.grid[i][j]);
		int maxCol = logic.maxCol(i, j, dColor); int minCol = logic.minCol(i,j, dColor);
		int maxRow = logic.maxRow(i, j, dColor); int minRow = logic.minRow(i, j, dColor);
		int beginRow = Math.max(0, i);
		int beginCol = Math.max(0, j);
		int endRow = Math.min(i , 7);
		int endCol = Math.min(j , 7);
		for (int s = beginRow ; s < endRow + 1 ; s++)
			for (int t = beginCol ; t < endCol + 1 ; t++) 
			{
				if (!canEffect(s, t)) continue;
				if (!logic.isCombineSource(s, t))
				logic.effectOf[s][t].setEffect(this, false);
				this.addM_Targer(new Integer(s * 8 + t));
			}
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		addAnimation = gAssets.getEffectAnimation("RearExplode", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.NORMAL);
		addAnimation.setPlayMode(Animation.PlayMode.NORMAL);
	}
	
	public void toConcurrentEffect() {
		// Create Effect SOIL_EXPLORE
		
	}
	
	public void init() {
		
		animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		addAnimation = gAssets.getEffectAnimation("RearExplode", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.NORMAL);
		addAnimation.setPlayMode(Animation.PlayMode.NORMAL);
		
		handleInBeginEffect();
		
		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.CELL_EXPLODE, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
//			if (isAffected(cell)) {
//				eatCell(row, col);
//			}
			changeStatusBeforeRun(row, col);
		}
		
		
		
		
		
		step = RUNNING_STEP;
	}
	
	public void run(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
//			if (countRun == 0) Log.d("EXP", "run EXPLODE "+source[0]+" "+depth);
			
			animationTime += deltaTime;
			if (animationTime / AnimationAssets.frameDuration > 3 && step == RUNNING_STEP) {
				step++;
				//Log.d("test", "creat new effect from Explode");
				for (int i = 0 ; i < mirrorTarget.size() ; i++) {
					Integer integer = mirrorTarget.get(i);
					int cell = integer.intValue();
					int row = cell / 8; int col = cell % 8;
					
					if (isAffected(cell))
//					if (cell != source[0] && !certainCell(logic.gridFlag[row][col])) 
					{// vien no duoc tao ra cung voi hieu ung no
						logic.toNextEffect(row, col, this);
						canCreateSoilExplode(row, col);
					}
				}
			}
			
			if (isFinishing()) freeEffect();
		}
	}
	
	public void draw(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
			int cell = source[0];
			int row = cell / 8; int col  = cell % 8;
			float x = CordXOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			float y = CordYOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//			Gdx.app.log("test", "animation Time by Frame Dura" + animationTime / 0.05f +" "+ animation.getPlayMode()+" "+addAnimation.getPlayMode());
			TextureRegion region = addAnimation.getKeyFrame(animationTime);
			drawAnimation(region, x, y, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			
			region = animation.getKeyFrame(animationTime);
			drawAnimation(region, x, y, 2f * screen.DIAMOND_WIDTH, 2f * screen.DIAMOND_HEIGHT);
		}
	}
	
	
	
	public void freeEffect() {
		int generateCell = -1;
		handleInEndEffect();
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			//screen.levelScore += 10;
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			IDiamond dDiamond = screen.diamonds.get(cell);
			if (isAffected(cell)) {
				logic.effectOf[row][col].effectTarget = null;
				if (generateCell == -1) generateCell = cell;
				if (logic.grid[row][col] != -1) {
					screen.colHeight[col]--;
					eatCell(row, col);
				}
				logic.grid[row][col] = -1;
			}
		}
		// loai bo nguon 
		if (generateCell != -1)
			createSpecialGem(generateCell / 8, generateCell % 8);
		
		int cell = source[0];
		IDiamond dDiamond = screen.diamonds.get(cell);
		int row = cell / 8; int col = cell % 8;
		
		logic.effectOf[row][col].effectIn[this.type] = null;
		
		// loai bo cac gridFLag
		for (int i = 0; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0) 
			logic.gridFlag[row][col] = Operator.offBit(Effect.CELL_EXPLODE, logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}

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
		return animation.isAnimationFinished(animationTime);
	}
	
	public boolean isFinished() {
		return step == FINISH_STEP;
	}
	
	public boolean canCreateSoilExplode(int i, int j) {
		if (isRock(i, j)) {
			return logic.newEffect(i, j, this, 1);
		}
		return false;
	}
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		screen.combo.inc(1);
		int time = screen.combo.get();
		int score = 100 / 10;
		if (screen.GAME_ID == GameScreen.MINE_DIAMOND) score /= 2;
		screen.levelScore += time * score * mirrorTarget.size();
		int color = screen.logic.diamondColor(sourceValue);
		int cell = source[0]; int row = cell / 8; int col = cell % 8;
//		if (screen.vip != null) {
//			switch (screen.vip.id) {
//			case VipCard.GREEN_DRAGON:
//				break;
//			case VipCard.WHITE_TIGER:
//				break;
//			case VipCard.RED_PHOENIX:
//				if (screen.combo.get() % 10 == 0)
//				screen.showMascot("RedPhoenix", screen.gridPos.x + col
//						* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//						* Diamond.DIAMOND_HEIGHT);	
//				break;
//			case VipCard.BLACK_TORTOISE:
//				break;
//			}
//		}
		
		row = source[0] / 8;
		col = source[0] % 8;
		float x = screen.gridPos.x + col * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
		float y = screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
		
		screen.showComboScore(time, score * mirrorTarget.size(), color, x, y);
		screen.showSense(time, x, y);
		
//		createSpecialGem();
	}
}
