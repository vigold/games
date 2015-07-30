package vn.sunnet.qplay.diamondlink.logiceffects;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;




public class ExplodeItem extends Explode {
	public ExplodeItem(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		type = EXPLODE_ITEM;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
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
			logic.gridFlag[row][col] = Operator.onBit(Effect.EXPLODE_ITEM, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			changeStatusBeforeRun(row, col);
		}
		
		step = RUNNING_STEP;
	}
	
	@Override
	public void freeEffect() {
		// TODO Auto-generated method stub
//		Gdx.app.log("test", "sizie"+mirrorTarget.size()+" "+realTarget.size());
		// loai bo gridInfo
		handleInEndEffect();
		for (int i = 0; i < mirrorTarget.size(); i++) {
			// screen.levelScore += 10;
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8;
			int col = cell % 8;
			IDiamond dDiamond = screen.diamonds.get(cell);
			if (isAffected(cell)) {
				logic.effectOf[row][col].effectTarget = null;
				// if (!certainCell(logic.gridFlag[row][col])) {
				if (logic.grid[row][col] != -1) {
					screen.colHeight[col]--;
					eatCell(row, col);
				}
				logic.grid[row][col] = -1;
				// }
			}
		}
		// loai bo nguon
		int cell = source[0];
		IDiamond dDiamond = screen.diamonds.get(cell);
		int row = cell / 8;
		int col = cell % 8;

		logic.effectOf[row][col].effectIn[this.type] = null;

		
		
		// loai bo cac gridFLag
		for (int i = 0; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
				logic.gridFlag[row][col] = Operator.offBit(Effect.EXPLODE_ITEM,
						logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) {
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS,
						logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(
//						Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}

		}
		
		// loai bo con tro
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
	}
}
