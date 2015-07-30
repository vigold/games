package vn.sunnet.qplay.diamondlink.minerdiamond;

import java.util.List;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.phases.DiamondMove;




public class SecondMiner extends DiamondMove {

	private MinerDiamond instance;
	
	public SecondMiner(MinerDiamond screen) {
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

	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	
        	IDiamond diamond = screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	if (!isSoilOrMarkOrGem(diamond.getDiamondValue()))
        		diamond.draw(deltaTime, screen.batch);
        }
		
		for (int i = screen.diamonds.size() - 1 ; i > -1  ; i--)
        {
        	
        	IDiamond diamond = screen.diamonds.get(i);
        	int row = i / 8;
        	int col = i % 8;
        	if (isSoilOrMarkOrGem(diamond.getDiamondValue()))
        	diamond.draw(deltaTime, screen.batch);
        }
        
		if (screen.selection > -1)
			drawAnimation(
					screen.gSelection,
					CordXOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT),
					CordYOfCell(screen.selection, screen.DIAMOND_WIDTH,
							screen.DIAMOND_HEIGHT), screen.DIAMOND_WIDTH,
					screen.DIAMOND_HEIGHT);
		drawExposureSoilDiamond();
	}
	
	private void drawExposureSoilDiamond(){		
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
