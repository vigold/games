package vn.sunnet.qplay.diamondlink.logiceffects;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.logiceffects.childreneffects.ParticleExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;




public class RCThunderItem extends RCThunder {

	public RCThunderItem(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		type = RCTHUNDER_ITEM;
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		
		if (step < RUNNING_STEP) return;
		rowThunder = gAssets.getEffectAnimation("RCThunder", AnimationAssets.frameDuration);
		colThunder = gAssets.getEffectAnimation("RCThunder", AnimationAssets.frameDuration);
		rowThunder.setPlayMode(Animation.PlayMode.LOOP);
		colThunder.setPlayMode(Animation.PlayMode.LOOP);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		logic.SpecialEffect++;
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		
		Sound sound = screen.manager.get(SoundAssets.T_THUNDER_SOUND, Sound.class);
		Assets.playSound(sound);
		
		rowThunder = gAssets.getEffectAnimation("RCThunder", AnimationAssets.frameDuration);
		colThunder = gAssets.getEffectAnimation("RCThunder", AnimationAssets.frameDuration);
		rowThunder.setPlayMode(Animation.PlayMode.LOOP);
		colThunder.setPlayMode(Animation.PlayMode.LOOP);
		handleInBeginEffect();
//		Assets.playSound(Assets.ThunderStart,-1);
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.RCTHUNDER_ITEM, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			changeStatusBeforeRun(row, col);
		}
		
		topRow = bottomRow = source[0] / 8;
		leftCol = rightCol = source[0] % 8;
		explodes = new ParticleExplode[mirrorTarget.size()];
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		
		step = RUNNING_STEP;
	}
	
	public void draw(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
			screen.batch.setColor(1f, 1f, 1f, 1);
			TextureRegion region = null;
			int cell = source[0];
			int row = cell / 8;
			int col = cell % 8;
			
			for (int i = 0 ; i < explodes.length; i++) {
				if (explodes[i] != null) explodes[i].draw(screen.batch, deltaTime);
			}
			
			
			cell = source[0]; row = cell / 8; col = cell % 8;
			float x = CordXOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			float y = CordYOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			float w = screen.DIAMOND_WIDTH;
			float h = screen.DIAMOND_HEIGHT;
			if (animationTime < THUNDER_TIME) {
				region = rowThunder.getKeyFrame(animationTime);
	
				screen.batch.draw(region, screen.gridPos.x , y - (90) / 2, w * 4, (90) / 2, w * 8, 90, 1f, 1f, 0 , false);
				//screen.batch.draw(region, screen.gridPos.x + w * 4, y - (128) / 2, w * 2, (128) / 2, w * 4, 128, 1f, 1f, 0 , false);
				
				region = colThunder.getKeyFrame(animationTime + 2 * colThunder.frameDuration);
				// doc
				//screen.batch.draw(region, x - (128) / 2, screen.gridPos.y + h * 4, 128, h * 4);
				screen.batch.draw(region, x - (90) / 2, screen.gridPos.y, 90, h * 8);
			}
			screen.batch.setColor(0.4f, 0.4f, 0.4f, 1f);
		}
	}
	
	@Override
	public void freeEffect() {
		// TODO Auto-generated method stub
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		// giai phong gridInfo
//		Assets.stopSound(Assets.ThunderStart);
		//giai phong source
		cell = source[0]; row = cell / 8; col = cell % 8;
		logic.effectOf[row][col].effectIn[this.type] = null;
		handleInEndEffect();
		// giai phong gridFlag
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			
			if (isAffected(cell)) {
				logic.effectOf[row][col].effectTarget = null;
				if (logic.grid[row][col] != -1) {
					eatCell(row, col);
					screen.colHeight[col]--;
				}
				logic.grid[row][col] = -1;
			}
			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
			logic.gridFlag[row][col] = Operator.offBit(Effect.RCTHUNDER_ITEM, logic.gridFlag[row][col]);
		}
		logic.SpecialEffect--;
		
		// loai bo con tro
		
		/*CombatDiamond pScreen = (CombatDiamond) screen;
		pScreen.gGameData.gCharacterInfo.gSkillStatus[1] = Character.OFF;
		*/
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP + 1;
	}
	
}
