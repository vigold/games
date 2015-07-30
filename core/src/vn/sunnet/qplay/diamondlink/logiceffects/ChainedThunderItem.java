package vn.sunnet.qplay.diamondlink.logiceffects;

import com.badlogic.gdx.audio.Sound;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.childreneffects.ParticleExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;




public class ChainedThunderItem extends ChainedThunder {

	public ChainedThunderItem(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		type = CHAIN_THUNDER_ITEM;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

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
		
		sound = screen.manager.get(SoundAssets.CHAINED_THUNDER_SOUND, Sound.class);
		Assets.playSound(sound);

		logic.SpecialEffect++;
		IDiamond diamond = null;
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		diamond = screen.diamonds.get(cell);
		//diamond.isFinish = false;
		diamond.setFinish(false);
		mirrorTarget.add(new Integer(source[0]));
		if (source.length > 1)
		mirrorTarget.add(new Integer(source[1]));
		for (int i = 0 ; i < realTarget.size(); i++) {
			Integer integer = realTarget.get(i);
			cell = integer.intValue();
			if (cell != source[0] && cell != source[1]) mirrorTarget.add(new Integer(cell));
		}

		handleInBeginEffect();
		
		explodes = new ParticleExplode[mirrorTarget.size()];
		
		
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			diamond = screen.diamonds.get(cell);
			diamond.setFinish(false);
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.CHAIN_THUNDER_ITEM, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
		
			changeStatusBeforeRun(row, col);
		}
		
		step = RUNNING_STEP;
		CHAIN_INTERVAL = mirrorTarget.size() - 1; // so luon cac tia set day chuyen
		System.out.println(" "+CHAIN_INTERVAL);
		EXPLODE_INTERVAL = mirrorTarget.size();// so luong no
		FINISH_STEP_TRAVEL = RUNNING_STEP + CHAIN_INTERVAL + EXPLODE_INTERVAL;
	}
	
	@Override
	public void concurrentResolve() {
		// TODO Auto-generated method stub
		int i = source[0] / 8; int j = source[0] % 8;
		int dType = logic.diamondType(logic.grid[i][j]);
		int dColor = logic.diamondColor(logic.grid[i][j]);
		int sourceCell1 = 0; int k = 0; int l = 0;
		int beginRow = 0, endRow = 0;
		int beginCol = 0, endCol = 0;
		if (this.source.length > 1) { 
			sourceCell1 = this.getSource(1);
			k = sourceCell1 / 8; l = sourceCell1 % 8;
			dType = logic.diamondType(logic.grid[k][l]); dColor = logic.diamondColor(logic.grid[k][l]);
		} else {
			sourceCell1 = this.getSource(0);
			k = sourceCell1 / 8; l = sourceCell1 % 8;
			if (this.getsValue() > -1) {
				if (screen.grid[k][l] == - 1) {// bi == -1
					this.setsValue(random.nextInt(screen.COLOR_NUM));
					dType = logic.diamondType(this.getsValue()); dColor = logic.diamondColor(this.getsValue());
				} else { //khac -1
					this.setsValue(screen.grid[k][l]);
					dType = logic.diamondType(this.getsValue()); dColor = logic.diamondColor(this.getsValue());
				}
			} else {
				this.setsValue(random.nextInt(screen.COLOR_NUM));
				dType = logic.diamondType(this.getsValue()); dColor = logic.diamondColor(this.getsValue());
			}
		}
		
		for (int s = i - 1 ; s > -1; s--)
			for (int t = 0; t < 8; t++) {
				if (certainCell(logic.gridFlag[s][t]) && logic.diamondColor(logic.grid[s][t]) == dColor 
						&& isValidCell(s, t)) { 
						logic.effectOf[s][t].setEffect(this, false);	
						if (this.source.length == 1) {
							this.setSource(sourceCell1,s * 8 + (t));
							logic.effectOf[s][t].setEffect(this, true);
						}
					}
			}
		for (int s = i ; s < 8; s++)
			for (int t = 0; t < 8; t++) {
				if (certainCell(logic.gridFlag[s][t]) && logic.diamondColor(logic.grid[s][t]) == dColor 
						&& isValidCell(s, t)) { 
						logic.effectOf[s][t].setEffect(this, false);	
						if (this.source.length == 1) {
							this.setSource(sourceCell1,s * 8 + (t));
							logic.effectOf[s][t].setEffect(this, true);
						}
					}
			}
		
		k = this.source[0] / 8; l = this.source[0] % 8;
		logic.effectOf[k][l].setEffect(this, false);
		logic.effectOf[k][l].setEffect(this, true);
		if (this.source.length > 1) {
			k = this.source[1] / 8; l = this.source[1] % 8;
			logic.effectOf[k][l].setEffect(this, true);
			logic.effectOf[k][l].setEffect(this, false);
		}
	}
	
	@Override
	public void freeEffect() {
		// TODO Auto-generated method stub
		int cell = 0 , row = 0 , col = 0;
		Effect temp = null;
		// giai phong effectInfo
		handleInEndEffect();
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			//temp = (Effect) logic.effectOf[row][col].effectTarget;
			if (isAffected(cell)) {
				if (logic.grid[row][col] != -1) 
					screen.colHeight[col]--;
				logic.grid[row][col] = -1;
				logic.effectOf[row][col].effectTarget = null;
			}
		}
		// giai phong source
		cell = source[0]; row = cell / 8; col = cell % 8;
		logic.effectOf[row][col].effectIn[Effect.CHAIN_THUNDER_ITEM] = null;
		if (source.length > 1) {
			cell = source[1]; row = cell / 8; col = cell % 8;
			logic.effectOf[row][col].effectIn[Effect.CHAIN_THUNDER_ITEM] = null;
		}
		cell = source[0]; row = cell / 8; col = cell % 8;
		
		
		
		// giai phong gridFlag
		for (int i = 0 ; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
			logic.gridFlag[row][col] = Operator.offBit(Effect.CHAIN_THUNDER_ITEM, logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}

		}
		logic.SpecialEffect--;
		// loai bo on tro
		/*CombatDiamond pScreen = (CombatDiamond) screen;
		pScreen.gGameData.gCharacterInfo.gSkillStatus[2] = Character.OFF;*/
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP_TRAVEL + 1;
	}

}
