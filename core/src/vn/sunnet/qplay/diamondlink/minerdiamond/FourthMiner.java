package vn.sunnet.qplay.diamondlink.minerdiamond;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.phases.Phase;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;
import vn.sunnet.qplay.diamondlink.screens.groups.MinerRunning;




public class FourthMiner extends Phase {
	private MinerDiamond screen;
	private DiamondLink Instance;
	private int timeBonus = 30;
	private float timePlused = 0;
	private float timeDelta = 0.3f;

	private int sCell = -1;
	private int dCell = -1;
	
	public FourthMiner(MinerDiamond screen) {
		// TODO Auto-generated constructor stub
		super();
		this.screen = screen;
		this.Instance = (DiamondLink) screen.gGame;
	}

	@Override
	public void onBegin() {
		// TODO Auto-generated method stub
		branch = -1;
		sCell = -1; 
		dCell = -1;	
		timePlused = 0;

		System.out.println("FourthMiner begin");
		screen.addInputProcessor(this);
		
		screen.setWheel(MinerRunning.WHEEL_ON);
		
		for (int i = 0; i < 64; i++) {
			IDiamond diamond = screen.diamonds.get(i);
			diamond.setSource(screen.gridPos.x + (i % 8) * screen.DIAMOND_WIDTH
					+ screen.DIAMOND_WIDTH / 2, screen.gridPos.y + (i / 8)
					* screen.DIAMOND_HEIGHT + screen.DIAMOND_HEIGHT / 2);
			diamond.setDestination(diamond.getSourX(), diamond.getSourY() + 2
					* screen.DIAMOND_HEIGHT);
			if (i == 0) {
				System.out.println(diamond.getPosX() + "kim cuong tren ban di chuyen tu"
						+ diamond.getDesX());
			}
			diamond.setAction(Diamond.FLY);
		}
		MinerGeneration m = (MinerGeneration) screen.generate;
		m.generateDownGridBuff(screen.depth);
		
		MinerDiamond mScreen = screen;
		for (int i = 0; i < 16 ; i++){
			IDiamond diamond = mScreen.diamondButtom.get(i);
			diamond.setSource(screen.gridPos.x + (i % 8) * screen.DIAMOND_WIDTH
					+ screen.DIAMOND_WIDTH / 2, screen.gridPos.y + (i / 8 - 2)
					* screen.DIAMOND_HEIGHT + screen.DIAMOND_HEIGHT / 2);
			diamond.setDestination(diamond.getSourX(), diamond.getSourY() + 2
					* screen.DIAMOND_HEIGHT);
			if (i == 0) {
				System.out.println(diamond.getPosX() + "kim cuong o duoi di chuyen tu"
						+ diamond.getDesX());
			}
			diamond.setDiamondValue(MinerGeneration.DownGridBuff[i / 8][i % 8]);
			diamond.setAction(Diamond.FLY);
		}
		timeDelta = 30 / (2 * DiamondLink.HEIGHT / (20 / 0.05f));
	}
	
	@Override
	public void onRunning() {
		// TODO Auto-generated method stub
		ActionHandle(); 
		screen.timeRemain = Math.min(screen.timeRemain + deltaTime * timeDelta, screen.timeLevel);
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		
		screen.depth += 20;
		if (!isOverSoilLimit()) {
			screen.removeInputProcessor(this);

			screen.setWheel(MinerRunning.WHEEL_OFF);

			MinerGeneration mGeneration = (MinerGeneration) screen.generate;

			for (int i = 0; i < 64; i++) {
				IDiamond diamond = screen.diamonds.get(i);
				diamond.setAction(Diamond.REST);
			}
			screen.setWheel(MinerRunning.WHEEL_OFF);
			branch = GameScreen.DIAMOND_REST;

			screen.gamePhase[branch].setState(ON_BEGIN);

			screen.stateGame = branch;

			screen.update(0);
		} else {
			setState(ON_BEGIN);
		}

	}
	
	private void ActionHandle() {	
		MinerDiamond mScreen = screen;
		if (getState() == ON_RUNNING){

			screen.timeRemain += deltaTime;
			for (int i = 0 ; i < 16 ; i++) {
				IDiamond diamond = mScreen.diamondButtom.get(i);
				diamond.update(deltaTime);
			}
			
			for (int i = 0 ; i < 64 ; i++) { 
				IDiamond diamond = mScreen.diamonds.get(i);
				diamond.update(deltaTime);
			}
			
			if (IsDoneUpSoil()){
				phaseState = ON_END;
				DoneUpSoil();
			}
		}	
	}
	
	private boolean IsDoneUpSoil(){
		DiamondOfMiner diamond;
		for (int i = 0; i < 64; i ++){
			diamond = (DiamondOfMiner) screen.diamonds.get(i);
			if (diamond.getPosY() < diamond.getDestination().y) return false;
		}
		return true;
	}
	
	private void DoneUpSoil(){
		DiamondOfMiner diamond = null;
		MinerDiamond mScreen = screen;
		MineLogic mLogic = (MineLogic) screen.logic;
		MinerGeneration m = (MinerGeneration) screen.generate;
		
		for (int j = 0; j < 8; j++){
			m.UpGridBuff[3][j] = m.UpGridBuff[2][j];
			m.UpGridBuff[2][j] = m.UpGridBuff[1][j];
			m.UpGridBuff[1][j] = mScreen.grid[7][j];
			m.UpGridBuff[0][j] = mScreen.grid[6][j];	
		}

		for (int i = 63; i >= 16 ; i--){		
			mScreen.grid[i / 8][ i % 8] = mScreen.grid[i / 8 - 2][ i % 8];
		}
		
		for (int i = 0; i < 16 ; i++){
			mScreen.grid[i / 8][ i % 8] = screen.diamondButtom.get(i).getDiamondValue();//MinerGeneration.DownGridBuff[i / 8][i % 8];
		}	
		
		// Create value for 2 row in the Soil
		
		
		//Khoi tao 2 hang long dat moi
		mScreen.diamondButtom.clear();
		MyAnimation animation = null;
		for (int i = 0; i < 16; i++) {
			diamond = mLogic.allocateDiamond(screen.gridPos.x + (i % 8)
					* screen.DIAMOND_WIDTH + screen.DIAMOND_WIDTH / 2,
					screen.gridPos.y + (i / 8 - 2) * screen.DIAMOND_HEIGHT
							+ screen.DIAMOND_HEIGHT / 2, screen.DIAMOND_WIDTH,
					screen.DIAMOND_HEIGHT, mScreen);
			diamond.setDestination(screen.gridPos.x + (i % 8)
					* screen.DIAMOND_WIDTH + screen.DIAMOND_WIDTH / 2,
					screen.gridPos.y + (i / 8) * screen.DIAMOND_HEIGHT
							+ screen.DIAMOND_HEIGHT / 2);
			
			diamond.setDiamondValue(MinerGeneration.DownGridBuff[i / 8][i % 8]);

			diamond.setAction(Diamond.REST);
			mScreen.diamondButtom.add(diamond);
		}
		
		screen.diamonds.clear();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				diamond = mLogic.allocateDiamond(screen.gridPos.x + j
						* screen.DIAMOND_WIDTH + screen.DIAMOND_WIDTH / 2,
						screen.gridPos.y + i * screen.DIAMOND_HEIGHT
								+ screen.DIAMOND_HEIGHT / 2,
						screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT, mScreen);
				diamond.setDestination(screen.gridPos.x + j
						* screen.DIAMOND_WIDTH + screen.DIAMOND_WIDTH / 2,
						screen.gridPos.y + j * screen.DIAMOND_HEIGHT
								+ screen.DIAMOND_HEIGHT / 2);
			
				diamond.setDiamondValue(screen.grid[i][j]);

				diamond.setAction(Diamond.REST);
				mScreen.diamonds.add(diamond);
			}
	}
	
	public void draw(float deltaTime) {
        for (int i = 63 ; i >= 0 ; i--)
        {	
        	IDiamond diamond = screen.diamonds.get(i);
        	diamond.draw(deltaTime, screen.batch);
//        	if (i == 0) System.out.println("draw 4 Miner posX "+diamond.getPosX());
        }
        
        MinerDiamond mScreen = screen;
        for (int i = 0; i < 16; i++){
        	Diamond diamond = (Diamond) mScreen.diamondButtom.get(i);
        	diamond.draw(deltaTime, screen.batch);
        	if (!isSoilOrMarkOrGem(screen.grid[0][i % 8])) {
        		screen.batch.draw(screen.soilUp,
						diamond.getX(), diamond.getY()
								+ screen.DIAMOND_HEIGHT,
						screen.DIAMOND_WIDTH, 15);
        	}
        }
        drawExposureSoilDiamond();
	}
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}
	
	private void drawExposureSoilDiamond(){		
		Diamond diamond;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {

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
	
	private void RefreshMap(){
		MinerGeneration mGeneration = (MinerGeneration) screen.generate;
		MineLogic mLogic = (MineLogic) screen.logic;
		MinerDiamond mScreen = screen;
		DiamondOfMiner diamond;
		MyAnimation animation = null;
		int[] temp = mGeneration.RefreshMap(screen.grid);
		screen.diamonds.clear();
		
		for (int j = 0 ; j < 8 ; j++)
		for (int i = 0 ; i < 8 ; i++)
		if (mGeneration.isDiamond(screen.grid[i][j])){		
			diamond = mLogic.allocateDiamond(j * 60 + 30, 260 + i * 60 + 30, 60, 60, mScreen);			
			diamond.setDestination(screen.gridPos.x + j * 60 + 30, screen.gridPos.y + j * 60 + 30);
			
			diamond.setDiamondValue(screen.grid[i][j]);
			diamond.setAction(Diamond.FALL);
			mScreen.diamonds.add(diamond);
			screen.inGridFlag[i][j] = 0;
		}		
		for (int j = 0; j < 8; j++)
			screen.colHeight[j] = temp[j];
	
	}
	
	@Override
	public void save(IFunctions iFunctions) {
		super.save(iFunctions);
		iFunctions.putFastInt("phaseState "+screen.GAME_ID, phaseState);
		iFunctions.putFastInt("branch "+screen.GAME_ID, branch);
		iFunctions.putFastFloat("timeDelta "+screen.GAME_ID, timeDelta);
		
		String data ="";
		for (int i = 0; i < screen.diamonds.size(); i++) {
			Diamond diamond = (Diamond) screen.diamonds.get(i);
			float x = diamond.getPosX();
			float y = diamond.getPosY();
			float sourX = diamond.getSourX();
			float sourY = diamond.getSourY();
			float desX = diamond.getDesX();
			float desY = diamond.getDesY();
			int dValue = diamond.getDiamondValue();
			data += "|"+dValue+"|"+sourX+"|"+sourY+"|"+x+"|"+y+"|"+desX+"|"+desY;
		}
		if (data != "") data = data.substring(1);
		iFunctions.putFastString("miner diamonds "+screen.GAME_ID, data);
		data ="";
		System.out.println("save Game bottoms"+screen.diamondButtom.size());
		for (int i = 0; i < screen.diamondButtom.size(); i++) {
			Diamond diamond = (Diamond) screen.diamondButtom.get(i);
			float x = diamond.getPosX();
			float y = diamond.getPosY();
			float sourX = diamond.getSourX();
			float sourY = diamond.getSourY();
			float desX = diamond.getDesX();
			float desY = diamond.getDesY();
			int dValue = diamond.getDiamondValue();
			data += "|"+dValue+"|"+sourX+"|"+sourY+"|"+x+"|"+y+"|"+desX+"|"+desY;
		}
		if (data != "") data = data.substring(1);
		iFunctions.putFastString("miner diamondbottoms "+screen.GAME_ID, data);
	}
	
	@Override
	public void parse(IFunctions iFunctions) {
		// TODO Auto-generated method stub
		super.parse(iFunctions);
		phaseState = iFunctions.getFastInt("phaseState "+screen.GAME_ID, 0);
		branch = iFunctions.getFastInt("branch "+screen.GAME_ID, 0);
		timeDelta = iFunctions.getFastFloat("timeDelta "+screen.GAME_ID, 0);
		
		String data = iFunctions.getFastString("miner diamonds "+screen.GAME_ID, "");
		if (phaseState == Phase.ON_BEGIN) return;
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length / 7; i++) {
				int dValue = Integer.parseInt(split[i * 7]);
				float sourX = Float.parseFloat(split[i * 7 + 1]);
				float sourY = Float.parseFloat(split[i * 7 + 2]);
				float x = Float.parseFloat(split[i * 7 + 3]);
				float y = Float.parseFloat(split[i * 7 + 4]);
				float desX = Float.parseFloat(split[i * 7 + 5]);
				float desY = Float.parseFloat(split[i * 7 + 6]);
				
				System.out.println(sourX+" "+sourY+" "+x+" "+y+" "+desX+" "+desY);
				Diamond diamond = (Diamond) screen.diamonds.get(i);
				diamond.setDiamondValue(dValue);
				diamond.setSource(sourX, sourY);
				diamond.setCenterPosition(x, y);
				diamond.setDestination(desY, desY);
				diamond.setAction(Diamond.FLY);
			}
		}
		
		data = iFunctions.getFastString("miner diamondbottoms "+screen.GAME_ID, "");
		
		if (data != "") {
			
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length; i++)
				System.out.println("why "+i+" "+split[i]);
			System.out.println("parse "+split.length / 7);
			for (int i = 0; i < Math.min(split.length / 7, 16); i++) {
				int dValue = Integer.parseInt(split[i * 7]);
				float sourX = Float.parseFloat(split[i * 7 + 1]);
				float sourY = Float.parseFloat(split[i * 7 + 2]);
				float x = Float.parseFloat(split[i * 7 + 3]);
				float y = Float.parseFloat(split[i * 7 + 4]);
				float desX = Float.parseFloat(split[i * 7 + 5]);
				float desY = Float.parseFloat(split[i * 7 + 6]);
				Diamond diamond = (Diamond) screen.diamondButtom.get(i);
				diamond.setDiamondValue(dValue);
				diamond.setSource(sourX, sourY);
				diamond.setCenterPosition(x, y);
				diamond.setDestination(desY, desY);
				diamond.setAction(Diamond.FLY);
			}
		}
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

	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean touchDownInTouchGame(int screenX, int screenY,
			int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchDownInTouchItem(int screenX, int screenY,
			int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchUpInTouchGame(int screenX, int screenY, int pointer,
			int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchUpInTouchItem(int screenX, int screenY, int pointer,
			int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchDraggedInTouchGame(int screenX, int screenY,
			int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean touchDraggedInTouchItem(int screenX, int screenY,
			int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean certainCell(int value) {
		return (Operator.getBit(Effect.FIXED_POS, value) > 0);// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
}
