package vn.sunnet.qplay.diamondlink.logiceffects;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.ChainedThunder.ChainThunder;
import vn.sunnet.qplay.diamondlink.logiceffects.childreneffects.ParticleExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

public class CrossLaser extends Effect {
	
	final float TIME_PER_EXPLODE = 0.15f;
	final float START_EXPLODE_TIME = 0.75f;
	ParticleExplode explodes[];
	Laser lasers[];
	
	private MyAnimation explode;
	private float stepTime;
	private int curExplodeIndex = -1;
	

	
	public CrossLaser(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		this.screen = screen;
		this.logic = logic;
		type = CROSS_LASER;
		Priority = 1;
		stepTime = TIME_PER_EXPLODE;
	}
	
	@Override
	public void recycleEffect() {
		// TODO Auto-generated method stub
		super.recycleEffect();
		curExplodeIndex = -1;
		stepTime = TIME_PER_EXPLODE;
	}
	
	@Override
	public void save(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.save(iFunctions, prefix);
		
		iFunctions.putFastFloat(prefix+" stepTime", stepTime);
		iFunctions.putFastInt(prefix+" curExplodeIndex", curExplodeIndex);
		String data = "";
		
		for (ParticleExplode explode: explodes) 
		{
			if (explode != null)
			data += "|"+explode.x+"|"+explode.y+"|"+explode.time;
		}
		if (data != "") data = data.substring(1);
		iFunctions.putFastString(prefix+" explodes", data);
		
		data ="";
		for (Laser laser: lasers) 
		{
			data += "|"+laser.position.x+"|"+laser.position.y+"|"+laser.status+"|"+laser.time+"|"+laser.angle;
		}
		if (data != "") data = data.substring(1);
		iFunctions.putFastString(prefix+" lasers", data);
		
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		stepTime = iFunctions.getFastFloat(prefix+" stepTime", 0);
		curExplodeIndex = iFunctions.getFastInt(prefix+" curExplodeIndex", 0);
		String data = iFunctions.getFastString(prefix+" explodes", "");
		explodes = new ParticleExplode[mirrorTarget.size()];
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length / 3; i++) {
				float x = Float.parseFloat(split[i * 3]);
				float y = Float.parseFloat(split[i * 3 + 1]);
				float time = Float.parseFloat(split[i * 3 + 2]);
				MyAnimation animation = gAssets.getEffectAnimation("Explode", 0.1f);
				animation.setPlayMode(Animation.PlayMode.NORMAL);
				
				ParticleEffect effect = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
				explodes[i] = new ParticleExplode(animation, effect, x, y);
				explodes[i].time = time;
				explodes[i].isNew = false;
			}
		}
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		data = iFunctions.getFastString(prefix+" lasers", "");
		lasers = new Laser[4];
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length / 5; i++) {
				float x = Float.parseFloat(split[i * 5]);
				float y = Float.parseFloat(split[i * 5 + 1]);
				int status = Integer.parseInt(split[i * 5 + 2]);
				float time = Float.parseFloat(split[i * 5 + 3]);
				float angle = Float.parseFloat(split[i * 5 + 4]);
				lasers[i] = new Laser(new Vector2(x, y), angle, screen.assets);
				lasers[i].status = status;
				lasers[i].time = time;
			}
		}
	}
	
	@Override
	public void concurrentResolve() {
		// TODO Auto-generated method stub
		int i = source[0] / 8; int j = source[0] % 8;
		// 1 1
		for (int k = 0; k < 8; k++) {
			int s = i + k;
			int t = j + k;
			if (!inGrid(s, t)) break;
			if (!canEffect(s, t)) continue;
			logic.effectOf[s][t].setEffect(this, false);
			this.addM_Targer(new Integer(s * 8 + t));
		}
		// 1 -1 
		for (int k = 1; k < 8; k++) {
			int s = i + k;
			int t = j - k;
			if (!inGrid(s, t)) break;
			if (!canEffect(s, t)) continue;
			logic.effectOf[s][t].setEffect(this, false);
			this.addM_Targer(new Integer(s * 8 + t));
		}
		// -1 -1
		for (int k = 1; k < 8; k++) {
			int s = i - k;
			int t = j - k;
			if (!inGrid(s, t)) break;
			if (!canEffect(s, t)) continue;
			logic.effectOf[s][t].setEffect(this, false);
			this.addM_Targer(new Integer(s * 8 + t));
		}
		//-1 1
		for (int k = 1; k < 8; k++) {
			int s = i - k;
			int t = j + k;
			if (!inGrid(s, t)) break;
			if (!canEffect(s, t)) continue;
			logic.effectOf[s][t].setEffect(this, false);
			this.addM_Targer(new Integer(s * 8 + t));
		}
	}
	
	@Override
	public void toConcurrentEffect() {
		// TODO Auto-generated method stub
		super.toConcurrentEffect();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		logic.SpecialEffect++;
		handleInBeginEffect();
		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.CROSS_LASER, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			changeStatusBeforeRun(row, col);
		}
		
		explodes = new ParticleExplode[mirrorTarget.size()];
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		lasers = new Laser[4];
		
		float x = screen.gridPos.x + source[0] % 8 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
		float y = screen.gridPos.y + source[0] / 8 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
		for (int i = 0; i < lasers.length; i++)
			lasers[i] = new Laser(new Vector2(x, y), 45 + i * 90, screen.assets);
		
		Sound sound = screen.manager.get(SoundAssets.LASER_SOUND, Sound.class);
		Assets.playSound(sound);
		step = RUNNING_STEP;
	}
	
	@Override
	public void run(float deltaTime) {
		// TODO Auto-generated method stub
		if (step > INIT_STEP && step < FINISH_STEP + 1) {
			for (int i = 0; i < lasers.length; i++) {
				lasers[i].update(deltaTime);
			}
			switch (step) {
			case 1:
				animationTime += deltaTime;
				
				if (animationTime > START_EXPLODE_TIME) {
					animationTime = 0;
					step++;
				}
				break;
			default:
				for (int i = 0 ; i < explodes.length; i++) {
					if (explodes[i] != null) explodes[i].update(deltaTime);
				}
				animationTime += deltaTime;
				if (step < 10)// cung lam la den 9
					if (stepTime >= TIME_PER_EXPLODE) {
						stepTime = 0;
						boolean isRunning = beginStep(step - 2);
						if (isRunning)
							step++;
					}
				stepTime += deltaTime;
//				System.out.println("step "+step+" "+stepTime);
				if (isFinishing())
					freeEffect();
				break;
			}
			
			
		}
	}
	
	private boolean beginStep(int stage) {// khoang cach den tam
		IDiamond diamond = null;
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		int row1 = -1;
		int col1 = -1;
		// 1 1
		row1 = row + stage; col1 = col + stage;
		
		int number = 0;
		if (inGrid(row1, col1)) {
			curExplodeIndex++;
			MyAnimation animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
			
			ParticleEffect particle = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
			float x = screen.gridPos.x + col1 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
			float y = screen.gridPos.y + row1 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
			explodes[curExplodeIndex] = new ParticleExplode(animation, particle, x, y);
			number++;
			Effect effect = (Effect) logic.effectOf[row1][col1].effectTarget;// loi khong an duoc row col thunder thu 2
			// o do phai ton tai hoac bi anh huong boi chinh effect nay hoac chua bi
			if (canEffect(row1, col1))
			if ((this.equals(effect) || effect == null) && logic.grid[row1][col1] != -1) {
				
				if (!logic.toNextEffect(row1, col1, this) && !canCreateSoilExplode(row1, col1)) {
	
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		
		// 1 -1
		
		row1 = row + stage; col1 = col - stage;
		if (inGrid(row1, col1)) {
			number++;
			if (stage > 0) {
				curExplodeIndex++;
				MyAnimation animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
				
				ParticleEffect particle = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
				float x = screen.gridPos.x + col1 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
				float y = screen.gridPos.y + row1 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
				explodes[curExplodeIndex] = new ParticleExplode(animation, particle, x, y);
			}
			
			Effect effect = (Effect) logic.effectOf[row1][col1].effectTarget;
			if (canEffect(row1, col1))
			if ((this.equals(effect) || effect == null) && logic.grid[row1][col1] != -1) {
				if (!logic.toNextEffect(row1, col1, this) && !canCreateSoilExplode(row1, col1)) {
				
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
			
		}
		
		// -1 -1
		
		row1 = row - stage; col1 = col - stage;
		if (inGrid(row1, col1)) {
			number++;
			if (stage > 0) {
				curExplodeIndex++;
				MyAnimation animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
				
				ParticleEffect particle = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
				float x = screen.gridPos.x + col1 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
				float y = screen.gridPos.y + row1 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
				explodes[curExplodeIndex] = new ParticleExplode(animation, particle, x, y);
			}
			
			Effect effect = (Effect) logic.effectOf[row1][col1].effectTarget;
			if (canEffect(row1, col1))
			if ((this.equals(effect) || effect == null) && logic.grid[row1][col1] != -1) {
				if (!logic.toNextEffect(row1, col1, this) && !canCreateSoilExplode(row1, col1)) {
		
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		
		// -1 1
		
		row1 = row - stage; col1 = col + stage;
		if (inGrid(row1, col1)) {
			number++;
			if (stage > 0) {
				curExplodeIndex++;
				MyAnimation animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
				
				ParticleEffect particle = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
				float x = screen.gridPos.x + col1 * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
				float y = screen.gridPos.y + row1 * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
				explodes[curExplodeIndex] = new ParticleExplode(animation, particle, x, y);
			}
			
			Effect effect = (Effect) logic.effectOf[row1][col1].effectTarget;
			if (canEffect(row1, col1))
			if ((this.equals(effect) || effect == null) && logic.grid[row1][col1] != -1) {
				if (!logic.toNextEffect(row1, col1, this) && !canCreateSoilExplode(row1, col1)) {
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		
		return (number > 0);
	}
	
	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		if (step> INIT_STEP && step < FINISH_STEP + 1) {
			screen.batch.setColor(1f, 1f, 1f, 1);
			
			if (step == RUNNING_STEP)
				for (int i = 0; i < mirrorTarget.size(); i++) {
					Integer integer = mirrorTarget.get(i);
					int cell = integer.intValue();
					int row = cell / 8;
					int col = cell % 8;

					IDiamond diamond = screen.diamonds.get(cell);
					if (isAffected(cell))
						if (logic.grid[row][col] != -1) {
							diamond.draw(deltaTime, screen.batch);
						}
				}
			
			for (int i = 0; i < lasers.length; i++) {
				lasers[i].draw(screen.batch, deltaTime);
			}
			for (int i = 0; i < explodes.length; i++) {
				if (explodes[i] != null) explodes[i].draw(screen.batch, deltaTime);
			}
			
			
		}
	}
	
	public int getCurDistance() {
		if (curExplodeIndex == -1) return -1;
		return curExplodeIndex / 4;
	}
	
	public int getDistance(int cell) {
		return getDistance(cell / 8, cell % 8);
	}
	
	public int getDistance(int row, int col) {
		return Math.abs(row - source[0] / 8);
	}
	
	public boolean isFinishing() {
		int row = source[0] / 8;
		int col = source[0] % 8;
		int len = Math.max(Math.max(row, 7 - row + 1), Math.max(col, 7 - col + 1));
		
		return animationTime >= (len - 1) * TIME_PER_EXPLODE
						+ explode.getAnimationDuration();
	}
	
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return step == FINISH_STEP + 1;
	}
	
	private void freeEffect() {
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		cell = source[0]; row = cell / 8; col = cell % 8;
		logic.effectOf[row][col].effectIn[Effect.CROSS_LASER] = null;
		int generateCell = -1;
		handleInEndEffect();
		// giai phong gridFlag
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			
			if (isAffected(cell)) {
				if (generateCell == -1) generateCell = cell;
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
			logic.gridFlag[row][col] = Operator.offBit(Effect.CROSS_LASER, logic.gridFlag[row][col]);
		}
		logic.SpecialEffect--;
//		if (generateCell != -1)
//			createSpecialGem(generateCell / 8, generateCell % 8);
		// loai bo con tro
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP + 1;
		System.out.println("Giai phong crosslaser");
	}
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		screen.combo.inc(1);
		int time = screen.combo.get();
		int score = 100/10 * mirrorTarget.size();
		
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
		screen.levelScore += time * score;
		
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
//				break;
//			case VipCard.RED_PHOENIX:
//				if (screen.combo.get() % 10 == 0)
//					screen.showMascot("RedPhoenix", screen.gridPos.x + col
//							* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//							* Diamond.DIAMOND_HEIGHT);	
//				break;
//			case VipCard.BLACK_TORTOISE:
//				screen.showMascot("BlackTortoise", screen.gridPos.x + col
//						* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
//						* Diamond.DIAMOND_HEIGHT);	
//				break;
//			}
//		}
		
		screen.showComboScore(time, score, color, x, y);
		screen.showSense(time, x, y);
		
//		createSpecialGem();
	}
	
	public boolean isAffected(int row, int col) {
		Effect effect = (Effect) logic.effectOf[row][col].effectTarget;
		return this.equals(effect);
	}
	
	public boolean isAffected(int cell) {
		int row = cell / 8;
		int col = cell % 8;
		Effect effect = (Effect) logic.effectOf[row][col].effectTarget;
		return this.equals(effect);
	} 
	
	private boolean inGrid(int i, int j) {
		return !((i < 0) || (i > 7) || (j < 0) || (j > 7));
	}
	
	public boolean canCreateSoilExplode(int i, int j) {
		if (isRock(i, j) || isGemOrMark(i, j)) {
			return logic.newEffect(i, j, this, 3);
		}
		return false;
	}
	
	class Laser {
		
		final int INIT = 0;
		final int EMISSION = 1;
		final int END = 2;
		int status;
		float time = 0;
		Vector2 position = null;
		float angle = 0;
		
		MyAnimation start = null;
		MyAnimation middle = null;
		
		public Laser(Vector2 postion, float angle, Assets assets) {
			// TODO Auto-generated constructor stub
			this.position = postion;
			this.angle = angle;
			
			start = assets.getEffectAnimation("ChainExplode", 0.07f);
			start.setPlayMode(Animation.PlayMode.NORMAL);
			middle = assets.getEffectAnimation("Laser", 0.07f);
			middle.setPlayMode(Animation.PlayMode.NORMAL);
		}
		
		public void update(float delta) {
			time += delta;
//			System.out.println("time in laser "+time +" "+ status);
			switch (status) {
			case INIT:
				if (start.isAnimationFinished(time))
					status = EMISSION;
				break;
			case EMISSION:
				if (middle.isAnimationFinished(time - start.getAnimationDuration()))
					status = END;
			default:
				break;
			}
		}
		
		public void draw(SpriteBatch batch, float delta) {
			switch (status) {
			case INIT:
				TextureRegion region = start.getKeyFrame(time);
				float rW = region.getRegionWidth();
				float rH = region.getRegionHeight();
				float dW = Diamond.DIAMOND_WIDTH;
				float dH = Diamond.DIAMOND_HEIGHT;
				batch.draw(region, position.x - rW / 2, position.y - dH / 2,
						rW / 2, dH / 2, rW, rH, 1f, 1f, angle);
				break;
			case EMISSION:
				region = middle.getKeyFrame(time - start.getAnimationDuration());
				rW = region.getRegionWidth();
				rH = region.getRegionHeight();
				dW = Diamond.DIAMOND_WIDTH;
				dH = Diamond.DIAMOND_HEIGHT;
				batch.draw(region, position.x - rW / 2, position.y - dH / 2 - 26 * 2,
						rW / 2, dH / 2 + 26 * 2, rW, 800, 1f, 1f, angle);
				break;
			default:
				break;
			}
		}
		
		public boolean isComplete() {
			return status == END;
		}

	}
}
