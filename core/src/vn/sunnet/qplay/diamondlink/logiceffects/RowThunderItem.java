package vn.sunnet.qplay.diamondlink.logiceffects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.childreneffects.ParticleExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class RowThunderItem extends RowThunder {

	public RowThunderItem(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		this.type = ROW_THUNDER_ITEM;
		this.logic = logic;
		this.screen = screen;
		Priority = 1;
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		animation = screen.gGame.getAssets().getEffectAnimation("RCThunder", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.LOOP);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		if (getPreEffect() != null) screen.logic.numberCombo++;
		screen.logic.SpecialEffect++;
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
		
		Sound sound = screen.manager.get(SoundAssets.THUNDER_SOUND, Sound.class);
		Assets.playSound(sound);
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		explodes = new ParticleExplode[mirrorTarget.size()];
		animation = screen.gGame.getAssets().getEffectAnimation("RCThunder", AnimationAssets.frameDuration);
		animation.setPlayMode(Animation.PlayMode.LOOP);
		leftCol = rightCol = source[0] % 8;
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;			
			IDiamond diamond = screen.diamonds.get(cell);
			diamond.setAction(Diamond.GRADUALLY_DISAPPEAR);
			if (logic.diamondType(screen.grid[row][col]) != IDiamond.NORMAL_DIAMOND) 
				diamond.setFinish(IDiamond.GRADUALLY_DISAPPEAR);
			
		}

		handleInBeginEffect();
		
		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.ROW_THUNDER_ITEM, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			
			if (isAffected(cell)) {// vien no duoc tao ra cung voi hieu ung no
				logic.toNextEffect(row, col, this);
			}
			
			canCreateSoilExplode(row, col);
		}
		
		step = RUNNING_STEP;
	}
	
	@Override
	public void draw(float deltaTime) {
		//Log.d("test", "drawEffect");
		if (step > INIT_STEP && step < FINISH_STEP) {
			Color preColor = screen.batch.getColor();
			screen.batch.setColor(1f, 1f, 1f, 1f);
			for (int i = 0 ; i < realTarget.size() ; i++) {
				Integer integer = realTarget.get(i);
				int cell = integer.intValue();
				int col = cell % 8;
				IDiamond diamond = screen.diamonds.get(cell);
				if (isAffected(cell) && (col < leftCol || col > rightCol)){		
					diamond.draw(deltaTime, screen.batch);
				} 
			}
			for (int i = 0 ; i < mirrorTarget.size(); i++) {
				if (explodes[i] != null) explodes[i].draw(screen.batch, deltaTime);
			}
			TextureRegion region = animation.getKeyFrame(animationTime);
			int cell = source[0]; int row = cell / 8; int col = cell % 8;
			float x = CordXOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			float y = CordYOfCell(cell, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
			float w = screen.DIAMOND_WIDTH;
			float h = screen.DIAMOND_HEIGHT;
			if (animationTime < THUNDER_TIME) {
				screen.batch.draw(region, screen.gridPos.x , y - (90) / 2, w * 4, (90) / 2, w * 8, 90, 1f, 1f, 0 , false);
			}
			screen.batch.setColor(preColor);
		}
	}
	
	@Override
	public void freeEffect() {
		// TODO Auto-generated method stub
		// loai bo cac effectinfo
		handleInEndEffect();
		for (int i = 0; i < realTarget.size(); i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8;
			int col = cell % 8;

			if (isAffected(row, col)) {
				logic.effectOf[row][col].effectTarget = null;
				if (logic.grid[row][col] != -1) {
					// Gdx.app.log("test", "an disppear "+row+" "+col);
					eatCell(row, col);
					screen.colHeight[col]--;
				}
				logic.grid[row][col] = -1;
			}

		}

		// loai bo goc
		int row = source[0] / 8;
		int col = source[0] % 8;
		logic.effectOf[row][col].effectIn[this.type] = null;

		
		// ghi lai trang thai gridType
		for (int i = 0; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
				logic.gridFlag[row][col] = Operator.offBit(
						Effect.ROW_THUNDER_ITEM, logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) {
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS,
						logic.gridFlag[row][col]);
				// logic.gridFlag[row][col] =
				// Operator.offBit(Effect.FIXED_TO_FALL,
				// logic.gridFlag[row][col]);
			}
		}

		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
		screen.logic.SpecialEffect--;
	}

}
