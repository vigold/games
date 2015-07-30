package vn.sunnet.qplay.diamondlink.logiceffects;


import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.utils.Pool;

public class EffectPool extends Pool {

	GameScreen gScreen;
	int gType;
	
	public EffectPool(int pEffectType, int pMax, GameScreen pScreen) {
		// TODO Auto-generated constructor stub
		super(0, pMax);
		gScreen = pScreen;
		this.gType = pEffectType;
	}
	
	@Override
	public IEffect newObject() {
		// TODO Auto-generated method stub
		Effect lEffect = null;
		
		switch (gType) {
		case Effect.TEMP_EFFECT:
			lEffect = new TempEffect(gScreen.logic, gScreen);
			break;
		case Effect.LITTLE_DISAPPEAR:
			lEffect = new Little_Disappear(gScreen.logic, gScreen);
			break;
		case Effect.FOUR_ROW_COMBINE:
		case Effect.FOUR_COl_COMBINE:
			lEffect = new FourCombine(gScreen.logic, gScreen);
			break;
		case Effect.FIVE_ROW_COMBINE:
		case Effect.FIVE_COL_COMBINE:
			lEffect = new FiveCombine(gScreen.logic, gScreen);
			break;
		case Effect.ROW_COL_COMBINE:
			lEffect = new RCCombine(gScreen.logic, gScreen);
			break;
		case Effect.EXPLODE:
			lEffect = new Explode(gScreen.logic, gScreen);
			break;
		case Effect.ROW_COL_THUNDER:
			lEffect = new RCThunder(gScreen.logic, gScreen);
			break;
		case Effect.CHAIN_THUNDER:
			lEffect = new ChainedThunder(gScreen.logic, gScreen);
			break;
		case Effect.SOIL_EXPLORE:
			lEffect = new SoilExplode(gScreen.logic, gScreen);
			break;
		case Effect.EXPLODE_ITEM:
			lEffect = new ExplodeItem(gScreen.logic, gScreen);
			break;
		case Effect.CELL_EXPLODE:
			lEffect = new CellExplode(gScreen.logic, gScreen);
			break;
		case Effect.RCTHUNDER_ITEM:
			lEffect = new RCThunderItem(gScreen.logic, gScreen);
			break;
		case Effect.CHAIN_THUNDER_ITEM:
			lEffect = new ChainedThunderItem(gScreen.logic, gScreen);
			break;
		case Effect.ROW_THUNDER:
			lEffect = new RowThunder(gScreen.logic, gScreen);
			break;
		case Effect.COL_THUNDER:
			lEffect = new ColThunder(gScreen.logic, gScreen);
			break;
		case Effect.ROW_THUNDER_ITEM:
			lEffect = new RowThunderItem(gScreen.logic, gScreen);
			break;
		case Effect.EXTRA_CHAIN_THUNDER:
			lEffect = new ExtraChainedThunder(gScreen.logic, gScreen);
			break;
		case Effect.CROSS_LASER:
			lEffect = new CrossLaser(gScreen.logic, gScreen);
			break;
		case Effect.CREAT_NEW_DIAMOND:
			lEffect = new CreateNewDiamond(gScreen.logic, gScreen);
			break;
		}
		return lEffect;
	}

}
