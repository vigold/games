package vn.sunnet.qplay.diamondlink.logiceffects;







import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
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
import com.sun.org.apache.regexp.internal.REUtil;


public class Explode extends Effect { // phai code sinh ra hieu ung moi
	
//	public static int countExplode = 0;
	MyAnimation addAnimation = null;
	boolean explode = false;
	
	
	public Explode(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen);
		type = EXPLODE;
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
		int beginRow = Math.max(0, i - 1);
		int beginCol = Math.max(0, j - 1);
		int endRow = Math.min(i + 1, 7);
		int endCol = Math.min(j + 1, 7);
		for (int s = beginRow ; s < endRow + 1 ; s++)
			for (int t = beginCol ; t < endCol + 1 ; t++) 
			{
				if (!canEffect(s, t)) continue;
				if (!logic.isCombineSource(s, t))
				logic.effectOf[s][t].setEffect(this, false);
				this.addM_Targer(new Integer(s * 8 + t));
			}
	}
	
	public void toConcurrentEffect() {
		// Create Effect SOIL_EXPLORE
		
	}
	
	public void init() {
		animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		addAnimation = gAssets.getEffectAnimation("RearExplode", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.NORMAL);
		addAnimation.setPlayMode(Animation.PlayMode.NORMAL);
		
		Sound sound = screen.manager.get(SoundAssets.EXPLODE_SOUND, Sound.class);
		Assets.playSound(sound);
		
		handleInBeginEffect();
		
		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.EXPLODE, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			changeStatusBeforeRun(row, col);
		}
		
		
		
		step = RUNNING_STEP;
		
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
	
	@Override
	public void recycleEffect() {
		// TODO Auto-generated method stub
		super.recycleEffect();
		explode = false;
	}
	
	public void run(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
//			if (!explode) {
//				explode = true;
//				screen.fall.fallBeginDueToExplode(source[0] / 8, source[0] % 8);
//			}
			animationTime += deltaTime;
			if (animationTime / animation.getFrameDuration() > 3 && step == RUNNING_STEP) {
				step++;
				//Log.d("test", "creat new effect from Explode");
				for (int i = 0 ; i < mirrorTarget.size() ; i++) {
					Integer integer = mirrorTarget.get(i);
					int cell = integer.intValue();
					int row = cell / 8; int col = cell % 8;
					if (isAffected(cell))
					{// vien no duoc tao ra cung voi hieu ung no
						logic.toNextEffect(row, col, this);
						canCreateSoilExplode(row, col);
					}
				}
			}
//			System.out.println("animationTime" + animationTime);
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
			if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1f);
			if (getPreEffect() != null) {
				if (getPreEffect().getType() == EXTRA_CHAIN_THUNDER) screen.batch.setColor(1f, 1f, 1f, 1f);
			}
			
			TextureRegion region = addAnimation.getKeyFrame(animationTime);
			drawAnimation(region, x, y, 4 * screen.DIAMOND_WIDTH, 4 * screen.DIAMOND_HEIGHT);
			
			region = animation.getKeyFrame(animationTime);
			drawAnimation(region, x, y, 5 * screen.DIAMOND_WIDTH, 5 * screen.DIAMOND_HEIGHT);
			if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1f);
		}
	}
	
	
	
	public void freeEffect() {

		boolean isAffectSource = false;
		handleInEndEffect();
		int generateCell = -1;
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			//screen.levelScore += 10;
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			IDiamond dDiamond = screen.diamonds.get(cell);
			if (isAffected(cell)) {
				if (generateCell == -1) generateCell = cell;
				logic.effectOf[row][col].effectTarget = null;
				if (logic.grid[row][col] != -1) {
					if (cell == source[0]) isAffectSource = true;
					screen.colHeight[col]--;
					eatCell(row, col);
					System.out.println("no tai " + row + " " + col
							+ " co chieu cao la " + screen.colHeight[col]+" "+screen.fallingNum[col]+" "+logic.grid[row][col]);
				}
				logic.grid[row][col] = -1;
			}
		}
		if (generateCell != -1)
			createSpecialGem(generateCell / 8, generateCell % 8);
//		if (getAutoActive()) {
//			if (random.nextInt(3) == 0) {
//				int row = source[0] / 8;
//				int col = source[0] % 8;
//				if (screen.grid[row][col] == -1)
//					screen.colHeight[col]++;
//				int value = screen.logic.getDiamondValue(
//						IDiamond.LASER_DIAMOND,
//						0, 0);
//				logic.grid[row][col] = value;
//				Diamond dDiamond = (Diamond) screen.diamonds.get(source[0]);
//
//				dDiamond.setDiamondValue(logic.grid[row][col]);
//
//				dDiamond.setAction(Diamond.FRAME_CHANGE);
//				dDiamond.setSize(IDiamond.DIAMOND_WIDTH, IDiamond.DIAMOND_HEIGHT);
//					
//				System.out.println("tao vien "+row+" "+col+" "+logic.gridFlag[row][col]+" "+screen.colHeight[col]);
//				logic.gridFlag[row][col] = Operator.onBit(Effect.FIXED_POS,
//						logic.gridFlag[row][col]);
//				System.out.println("tao vien "+row+" "+col+" "+logic.gridFlag[row][col]+" "+screen.colHeight[col]);
//				if (logic.effectOf[row][col].effectTarget != null) logic.effectOf[row][col].effectTarget = null;
//			}
//		}
		
		// loai bo nguon 
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
			logic.gridFlag[row][col] = Operator.offBit(Effect.EXPLODE, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.offBit(Effect.TWO_ASPECT_SWAP, logic.gridFlag[row][col]);
			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			}
		}
		
		
		// loai bo con tro
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
	}
	
	
	
	public boolean isFinishing() {
		return animation.isAnimationFinished(animationTime);
	}
	
	public boolean isFinished() {
		return step == FINISH_STEP;
	}
	
	public void canCreateSoilExplode(int i, int j) {
		if (isRock(i, j)) {
			logic.newEffect(i, j, this, 2);
		}
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
		
		int cell = source[0]; int row = cell / 8; int col = cell % 8;
//		if (screen.vip != null) {
//			switch (screen.vip.id) {
//			case VipCard.GREEN_DRAGON:
//				screen.vip.combo++;
//				if (screen.vip.combo % 3 == 0) {
//					screen.showMascot("GreenDragon", screen.gridPos.x + col
//							* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//							* Diamond.DIAMOND_HEIGHT);		
//				}
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
//				if (getAutoActive()) {
//					screen.vip.combo++;
////					if (screen.vip.combo % 2 == 0) {
////						screen.showMascot("BlackTortoise", screen.gridPos.x + col
////								* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
////								* Diamond.DIAMOND_HEIGHT);	
////					}
//				}
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
