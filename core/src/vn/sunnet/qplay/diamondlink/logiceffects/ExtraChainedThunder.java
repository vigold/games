package vn.sunnet.qplay.diamondlink.logiceffects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.ChainedThunder.ChainThunder;
import vn.sunnet.qplay.diamondlink.logiceffects.childreneffects.ParticleExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;

import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class ExtraChainedThunder extends Effect {

	public int FINISH_STEP_TRAVEL = 0;
	public int CHAIN_INTERVAL = 0;
	public int EXPLODE_INTERVAL = 0;
	public boolean beginFlag = true;
	public float timeStep = 0f;
	ArrayList<ChainThunder> thunders = new ArrayList<ExtraChainedThunder.ChainThunder>();

	Random random = new Random();
	Sound sound;
	int countExplode = 3;
	
	public class ChainThunder {
		float time = 0;
		Animation animation = null;
		float x = 0;
		float y = 0;
		float width = 0;
		float height = 0;
		float angle = 0;
		public ChainThunder(Animation animation, float x, float y , float width, float height, float angle) {
			// TODO Auto-generated constructor stub
			this.animation = animation; 
			this.x = x; this.y = y;
			this.width = width; this.height = height;
			this.angle = angle;
			if (random.nextInt(2) == 0)
			this.animation.setPlayMode(Animation.PlayMode.LOOP);
			else this.animation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
		}
		
		public void update(float deltaTime) {
			//Log.d("test", "chain"+time);
			time += deltaTime;
		}
	}
	
	@Override
	public void save(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.save(iFunctions, prefix);
		iFunctions.putFastBool(prefix+" beginFlag", beginFlag);
		iFunctions.putFastFloat(prefix+" timeStep", timeStep);
		
		String data = "";
		for (ChainThunder thunder: thunders) 
		{
			data += "|"+thunder.x+"|"+thunder.y+"|"+thunder.width+"|"+thunder.height+"|"+thunder.angle+"|"+thunder.time;
		}
		if (data != "") data = data.substring(1);
		iFunctions.putFastString(prefix+" thunders", data);
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		beginFlag = iFunctions.getFastBool(prefix+" beginFlag", false);
		timeStep = iFunctions.getFastFloat(prefix+" timeStep", 0);
		thunders = new ArrayList<ExtraChainedThunder.ChainThunder>();
		thunders.clear();
		String data = iFunctions.getFastString(prefix+" thunders", "");
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length / 6; i++) {
				float x = Float.parseFloat(split[ i * 6]);
				float y = Float.parseFloat(split[ i * 6 + 1]);
				float w = Float.parseFloat(split[ i * 6 + 2]);
				float h = Float.parseFloat(split[ i * 6 + 3]);
				float angle = Float.parseFloat(split[ i * 6 + 4]);
				float time = Float.parseFloat(split[ i * 6 + 5]);
				switch ((int)angle) {
				case 45:
					animation = gAssets.getEffectAnimation("SMCThunder", AnimationAssets.frameDuration);
					break;
				case 135:
					animation = gAssets.getEffectAnimation("MCThunder", AnimationAssets.frameDuration);
					break;
				case 0:
					animation = gAssets.getEffectAnimation("CThunder", AnimationAssets.frameDuration);
					break;
				default:
					animation = gAssets.getEffectAnimation("RThunder", AnimationAssets.frameDuration);
					break;
				}
				ChainThunder thunder = new ChainThunder(animation, x, y, w, h, angle);
				thunder.time = time;
				thunders.add(thunder);
			}
		}
		CHAIN_INTERVAL = mirrorTarget.size() - 1;
		EXPLODE_INTERVAL = mirrorTarget.size();
		FINISH_STEP_TRAVEL = RUNNING_STEP + CHAIN_INTERVAL + EXPLODE_INTERVAL;
	}
	
	@Override
	public void recycleEffect() {
		// TODO Auto-generated method stub
		super.recycleEffect();
		beginFlag = true;
		thunders.clear();
		timeStep = 0;
		countExplode = 0;
	} 
	
	public ExtraChainedThunder(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen);
		this.logic = logic;
		this.screen = screen;
		this.gAssets = screen.gGame.getAssets();
		type = EXTRA_CHAIN_THUNDER;
		thunders.clear();
		Priority = 2;
	}
	
	public void concurrentResolve() {
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
			dType = logic.diamondType(this.getsValue()); dColor = logic.diamondColor(this.getsValue());
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
	
	public void toConcurrentEffect() {
		
	}
	
	public void init() {
		
		
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
		
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			diamond = screen.diamonds.get(cell);
			diamond.setFinish(false);
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.EXTRA_CHAIN_THUNDER, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			
			changeStatusBeforeRun(row, col);
		}
		
		step = RUNNING_STEP;
		CHAIN_INTERVAL = mirrorTarget.size() - 1;
		EXPLODE_INTERVAL = mirrorTarget.size();
		FINISH_STEP_TRAVEL = RUNNING_STEP + CHAIN_INTERVAL + EXPLODE_INTERVAL;

	}
	
	public void beginStepChain() {
		float x = 0 , y = 0, width = 0 , height = 0, angle = 0;
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		int row = 0, col = 0, cell = 0 , row1 = 0, col1 = 0;
		Integer integer = null;
		ChainThunder chain = null;
	
		integer = mirrorTarget.get(step - 1); cell = integer.intValue();
		row = cell / 8; col = cell % 8;
		x1 = CordXOfCell(row, col, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
		y1 = CordYOfCell(row, col, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
		integer = mirrorTarget.get(step); cell = integer.intValue();
		row1 = cell / 8; col1 = cell % 8;
		x2 = CordXOfCell(row1, col1, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
		y2 = CordYOfCell(row1, col1, screen.DIAMOND_WIDTH, screen.DIAMOND_HEIGHT);
//		Log.d("test", "chain from "+row+" "+col+" to "+row1+" "+col1);
		x = Math.min(x1, x2);
		y = Math.min(y1, y2);
		if ((x2 - x1) * (y2 - y1) > 0) {
			animation = gAssets.getEffectAnimation("SMCThunder", AnimationAssets.frameDuration);
			width = Math.abs(x2 - x1); height = Math.abs(y2 - y1);
			angle = 45;
		} else 
		if ((x2 - x1) * (y2 - y1) < 0) { 
			animation = gAssets.getEffectAnimation("MCThunder", AnimationAssets.frameDuration);
			width = Math.abs(x2 - x1); height = Math.abs(y2 - y1);
			angle = 135;
		} else 
		if (x2 == x1) {
			animation = gAssets.getEffectAnimation("CThunder", AnimationAssets.frameDuration);
			width = screen.DIAMOND_WIDTH; height = Math.abs(y2 - y1);
			x = x - width / 2;
			angle = 0;
		} else {
			animation = gAssets.getEffectAnimation("RThunder", AnimationAssets.frameDuration);
			width = Math.abs(x2 - x1); height = screen.DIAMOND_HEIGHT;
			y = y - height / 2;
			angle = 90;
		}
//		angle = 90;
		
//		x = (x2 + x1) / 2; y = (y2 + y1) / 2;
//		Gdx.app.log("Chain", x1+" "+y1+" "+x2+" "+y2+" ket qua"+width+" "+height+" ");
		chain = new ChainThunder(animation, x, y, width, height, angle);
		thunders.add(chain);
	}
	
	public boolean endStepChain() {
		if (step - 1 < thunders.size()) {
		ChainThunder chain = thunders.get(step - 1);
			return chain.time > 0.1;
		}
		return true;
	}
	
	public void beginStepExplode() {
		IDiamond diamond = null;
		int cell = 0;
		cell = step - RUNNING_STEP - CHAIN_INTERVAL;

		Integer integer = mirrorTarget.get(step - RUNNING_STEP - CHAIN_INTERVAL);
		cell = integer.intValue();
	
		
		diamond = screen.diamonds.get(cell);
	
		
		eatCell(cell / 8, cell % 8);
		
		screen.logic.newEffect(cell / 8, cell % 8, Effect.EXPLODE, this);

		if (thunders.size() > 0) thunders.remove(0);
	}
	
	public boolean endStepExplode() {
		int cell = 0;
		Integer integer = mirrorTarget.get(step - RUNNING_STEP - CHAIN_INTERVAL);
		cell = integer.intValue();
		
		IDiamond diamond = screen.diamonds.get(cell);
		return timeStep > 0.15f;
	}
	
	public void transferStep() {
		//Log.d("test", "transferStep ChainThunder"+step);
		IDiamond diamond = null;
		if (step < RUNNING_STEP + CHAIN_INTERVAL) {// tao set chain
			if (beginFlag) {// truoc do khong co buoc nao
				beginStepChain(); beginFlag = false;
				//Assets.step
			}
			else if (endStepChain()) {
				Assets.playSound(sound);
//				Log.d("ChainThunder", "endStepChain"+step+" "+thunder.size());
				timeStep = 0;
				step++; 
//				Log.d("ChainThunder", "endStepChain2"+step+" "+thunder.size());
				if (step == RUNNING_STEP + CHAIN_INTERVAL) {
//					Log.d("ChainThunder", "moc 1 "+step+" "+thunder.size());
					beginFlag = true;
				} else beginStepChain();
			}
		} else {// no dan
			if (beginFlag) {// vua moi tu doan tao set chain chuyen sang
				beginStepExplode(); beginFlag = false;
//				Assets.playSound(Assets.ChainExplode, EXPLODE_INTERVAL);
			} else 
			if (endStepExplode()) {
				//Assets.playSound(Assets.ChainExplode, 0);
				timeStep = 0;
				int cell = 0;
				Integer integer = mirrorTarget.get(step - RUNNING_STEP - CHAIN_INTERVAL);
				cell = integer.intValue();
				int row = cell / 8; int col = cell % 8;
				
				diamond = screen.diamonds.get(cell);
				//diamond.isFinish = true;
				diamond.setFinish(true); 
//				Log.d("ChainThunder", step+" "+row+" "+col+" "+logic.grid[row][col]+" stepFinish "+ FINISH_STEP_TRAVEL);
				if (step < FINISH_STEP_TRAVEL) {
					if (!logic.toNextEffect(row, col, this)) {
						//logic.grid[row][col] = -1;
						beginStepExplode(); 
					} else {
						if (thunders.size() > 0) thunders.remove(0);
						//logic.grid[row][col] = -1;
					}
				}
				step++;
			}
		}
	}
	
	public void actStep(float deltaTime) {
		//Log.d("test", "actStep ChainThunder"+step);
		IDiamond diamond = null;
		diamond = screen.diamonds.get(source[0]);
		diamond.update(deltaTime);
		
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			diamond = screen.diamonds.get(cell);
			if (isAffected(cell) && !diamond.isFinished(IDiamond.FRAME_CHANGE))
			diamond.update(deltaTime);
		}
		for (int i = 0 ; i < thunders.size() ; i++) {
			ChainThunder chain = thunders.get(i);
			chain.update(deltaTime);
		}
	}
	
	public void freeEffect() {
//		Log.d("TRAVEL", "freet TRAVEL"+source[0]+"o do sau"+depth);
		int cell = 0 , row = 0 , col = 0;
		Effect temp = null;
		int generateCell = -1;
		handleInEndEffect();
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			//temp = (Effect) logic.effectOf[row][col].effectTarget;
			if (isAffected(cell)) {
				if (generateCell == -1) generateCell = cell;
				if (logic.grid[row][col] != -1) 
					screen.colHeight[col]--;
				logic.grid[row][col] = -1;
				logic.effectOf[row][col].effectTarget = null;
			}
		}
		if (generateCell != -1)
			createSpecialGem(generateCell / 8, generateCell % 8);
		
		// giai phong source
		cell = source[0]; row = cell / 8; col = cell % 8;
		logic.effectOf[row][col].effectIn[Effect.EXTRA_CHAIN_THUNDER] = null;
		if (source.length > 1) {
			cell = source[1]; row = cell / 8; col = cell % 8;
			logic.effectOf[row][col].effectIn[Effect.EXTRA_CHAIN_THUNDER] = null;
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
			logic.gridFlag[row][col] = Operator.offBit(Effect.EXTRA_CHAIN_THUNDER, logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}

		}
		logic.SpecialEffect--;
		// loai bo on tro
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP_TRAVEL + 1;
	}
	
	public void run(float deltaTime) {
		//Log.d("test", "outupdate Chian"+step);
		if (step > INIT_STEP && step < FINISH_STEP_TRAVEL) {
			Diamond diamond = null;
			animationTime += deltaTime;
			timeStep += deltaTime;
			transferStep();
			if (step < FINISH_STEP_TRAVEL) actStep(deltaTime); 
			else freeEffect();
		}
	}
	
	public void draw(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP_TRAVEL) {
//			GLGame game = (GLGame) screen.game;
//			GLGraphics glGraphics = (GLGraphics) game.getGLGraphics();
//			GL10 gl = glGraphics.getGL();
//			gl.glColor4f(1, 1, 1, 1);
			screen.batch.setColor(1f, 1f, 1f, 1f);
			
			TextureRegion region = null;
			for (int i = 0 ; i < mirrorTarget.size() ; i++) {
				Integer integer = mirrorTarget.get(i);
				int cell = integer.intValue();
				IDiamond diamond = screen.diamonds.get(cell);
				if (isAffected(cell))
				if (!diamond.isFinish()) {
					diamond.draw(deltaTime, screen.batch);

				}
			}
			//GLGame glGame = (GLGame) screen.game;
	        //GLGraphics glGraphics = glGame.getGLGraphics();
	        //GL10 gl = glGraphics.getGL();
			//gl.glDisable(GL10.GL_BLEND);
			
			for (int i = 0 ; i < thunders.size() ; i++) {
				ChainThunder chain = thunders.get(i);
				region = chain.animation.getKeyFrame(chain.time);
				screen.batch.draw(region, chain.x, chain.y, chain.width, chain.height);
			}
			screen.batch.setColor(0.4f, 0.4f, 0.4f, 1);
		}
	}
	
	
	
	public boolean isAffected(int cell) {
		int row = cell / 8;
		int col = cell % 8;
		Effect effect = (Effect) logic.effectOf[row][col].effectTarget;
		return this.equals(effect);
	} 
	
	public boolean isFinished() {
		return step == FINISH_STEP_TRAVEL + 1;
	}
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		screen.combo.inc(1);
		int time = screen.combo.get();
		int score = 100/10;
		
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
		
		int row = source[0] / 8;
		int col = source[0] % 8;
		float x = screen.gridPos.x + col * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
		float y = screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
		
//		if (screen.vip != null) {
//			switch (screen.vip.id) {
//			case VipCard.GREEN_DRAGON:
//				break;
//			case VipCard.WHITE_TIGER:
//				screen.showMascot("WhiteTiger", screen.gridPos.x + col
//						* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//						* Diamond.DIAMOND_HEIGHT);
//				break;
//			case VipCard.RED_PHOENIX:
//				if (screen.combo.get() % 10 == 0)
//					screen.showMascot("RedPhoenix", screen.gridPos.x + col
//							* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//							* Diamond.DIAMOND_HEIGHT);	
//				break;
//			case VipCard.BLACK_TORTOISE:
//				break;
//			}
//		}
		
		screen.showComboScore(time, score * mirrorTarget.size(), color, x, y);
		screen.showSense(time, x, y);
		
//		createSpecialGem();
	}
	

}
