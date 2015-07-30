package vn.sunnet.qplay.diamondlink.minerdiamond;

import java.util.List;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.GameObject;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.phases.DiamondAnimation;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;


public class ThirdMiner extends DiamondAnimation {
	
	private MinerDiamond instance;

	public ThirdMiner(MinerDiamond screen) {
		super(screen);
		// TODO Auto-generated constructor stub
		instance = screen;
	}
	
	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		super.onBegin();
		screen.solution.resetTime();
	}
	
	public void ActionHandle() {
		super.ActionHandle();
		if (isComplete() && swapFlag) {// lenh chuyen pha
			if (isOverSoilLimit()) {
				if (!instance.canOver) {
					branch = GameScreen.DIAMOND_UP;
					Gdx.app.log("test", "Animation to Up"+swapFlag);
				} else{
					System.out.println("Due to over");
					branch = GameScreen.DIAMOND_REST;
				}
			} else {
				
				branch = GameScreen.DIAMOND_REST;
				Gdx.app.log("test", "Animation to Rest"+swapFlag);
			}			
			phaseState = ON_END;
		}
	}
	
	
	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1); else 
			screen.batch.setColor(1, 1, 1, 1);
		
		screen.fall.draw(deltaTime);
		for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	
        	TextureRegion region = null;
        	
        	int value = 0;
        	
        	if (i < 64)
        	value = screen.logic.gridFlag[CellRow(i)][CellCol(i)];
        	if (isDrawingCell(i))  {
        		if (screen.logic.SpecialEffect > 0) diamond.setColorMode(GameObject.BATCH_COLOR);
        		else diamond.setColorMode(GameObject.OBJECT_COLOR);
        		if (!isSoilOrMarkOrGem(diamond.getDiamondValue()))
        			diamond.draw(deltaTime, screen.batch);	
        	} else {
//        		Log.d("NO EMPTY", "row = "+(i/8)+" col = "+(i%8)+" "+screen.inGridFlag[i/8][i % 8]);
        	}
        }
		
		for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	Diamond diamond = (Diamond) screen.diamonds.get(i);
        	
        	TextureRegion region = null;
        	
        	int value = 0;
        	
        	if (i < 64)
        	value = screen.logic.gridFlag[CellRow(i)][CellCol(i)];
        	if (isDrawingCell(i))  {
        		if (screen.logic.SpecialEffect > 0) diamond.setColorMode(GameObject.BATCH_COLOR);
        		else diamond.setColorMode(GameObject.OBJECT_COLOR);
        		if (isSoilOrMarkOrGem(diamond.getDiamondValue()))
        			diamond.draw(deltaTime, screen.batch);	
        	} else {
//        		Log.d("NO EMPTY", "row = "+(i/8)+" col = "+(i%8)+" "+screen.inGridFlag[i/8][i % 8]);
        	}
        }
        
        drawExposureSoilDiamond();
        
        for (int i = 0 ; i < screen.logic.effects.size(); i++) {
        	Effect effect = (Effect) screen.logic.effects.get(i);
        	effect.draw(deltaTime);
        }
        
		if (screen.selection > -1)
			drawAnimation(
					screen.gSelection,
					CordXOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT), screen.DIAMOND_WIDTH,
					screen.DIAMOND_HEIGHT);
	}
	
	private  boolean isOverSoilLimit(){
		int type ;
		for (int i = 16; i < 24; i++){
//			 type = screen.logic.diamondType(screen.grid[i / 8][ i % 8]);	 
			 if (isSoilOrMarkOrGem(screen.grid[i / 8][ i % 8]))
			return false;
		}
		return true;			
	}

	private void drawExposureSoilDiamond(){		
		if (screen.logic.SpecialEffect > 0) screen.batch.setColor(0.4f, 0.4f, 0.4f, 1); else 
			screen.batch.setColor(1, 1, 1, 1);
		Diamond diamond;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (certainCell(screen.inGridFlag[i][j]))
					if (isSoilOrMarkOrGem(screen.grid[i][j])) {
						diamond = (Diamond) screen.diamonds.get(i * 8 + j);
						if (i + 1 < 8)
							if (!isSoilOrMarkOrGem(screen.grid[i + 1][j]) || !certainCell(screen.inGridFlag[i + 1][j])) {

								screen.batch.draw(((MinerDiamond)screen).soilUp,
										diamond.getX(), diamond.getY()
												+ screen.DIAMOND_HEIGHT,
										screen.DIAMOND_WIDTH, 15);

							}

						if (j - 1 >= 0)
							if (!isSoilOrMarkOrGem(screen.grid[i][j - 1]) || !certainCell(screen.inGridFlag[i][j - 1])) {
								screen.batch.draw(((MinerDiamond)screen).soilLeft,
										diamond.getX() - 16,
										diamond.getY() - 5, 16, 69);
							}

						if (j + 1 < 8)
							if (!isSoilOrMarkOrGem(screen.grid[i][j + 1]) || !certainCell(screen.inGridFlag[i][j + 1])) {
								screen.batch.draw(((MinerDiamond)screen).soilRight,
										diamond.getX() + screen.DIAMOND_WIDTH,
										diamond.getY() - 5, 12, 69);
							}
					}

			}
	}
	
	private boolean isSoilOrMarkOrGem(int value) {
		if (value == -1) return false;
		int dType = screen.logic.diamondType(value);
		return dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM || dType == Diamond.MARK_DIAMOND
				|| dType == Diamond.SOIL_DIAMOND;
	}

}
