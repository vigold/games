package vn.sunnet.qplay.diamondlink.logiceffects;

import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.IFunctions;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.childreneffects.ParticleExplode;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GameLogic;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.org.apache.regexp.internal.REUtil;



public class RCThunder extends Effect {
	
	final float TIME_PER_EXPLODE = 0.15f;
	final float THUNDER_TIME = 1.0f;
	MyAnimation rowThunder = null;
	MyAnimation colThunder = null;
	ParticleExplode explodes[] = null;
	MyAnimation explode = null;
	int topRow = 0;
	int bottomRow = 0;
	int leftCol = 0;
	int rightCol = 0;
	float stepTime = 0;
	int curExplodeIndex = -1;
	boolean upCell = false, downCell = false, leftCell = false,
			rightCell = false;
	
	public RCThunder(GameLogic logic,GameScreen screen) {
		// TODO Auto-generated constructor stub
		super(logic, screen);
		this.logic = logic;
		this.screen = screen;
		type = ROW_COL_THUNDER;
		Priority = 1;
	}
	
	@Override
	public void recycleEffect() {
		// TODO Auto-generated method stub
		super.recycleEffect();
		upCell = false; downCell = false; rightCell = false;
		leftCell = false;
		stepTime = TIME_PER_EXPLODE;
		curExplodeIndex = -1;
	}
	
	@Override
	public void save(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.save(iFunctions, prefix);
		String data = "";
		for (ParticleExplode explode: explodes) 
		{
			if (explode != null)
			data += "|"+explode.x+"|"+explode.y+"|"+explode.time;
		}
		if (data != "") data = data.substring(1);
		iFunctions.putFastString(prefix+" explodes", data);
		
		iFunctions.putFastInt(prefix+" topRow", topRow);
		iFunctions.putFastInt(prefix+" bottomRow", bottomRow);
		iFunctions.putFastInt(prefix+" leftCol", leftCol);
		iFunctions.putFastInt(prefix+" rightCol", rightCol);
		iFunctions.putFastFloat(prefix+" stepTime", stepTime);
		iFunctions.putFastInt(prefix+" curExplodeIndex", curExplodeIndex);
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		switch (screen.logic.diamondColor(getsValue())) {
		case IDiamond.WHITE:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("WhiteThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("WhiteThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.BLUE:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("BlueThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("BlueThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.GREEN:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("GreenThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("GreenThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.ORANGE:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("OrangeThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("OrangeThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.RED:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("RedThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("RedThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.YELLOW:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("YeallowThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("YeallowThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.PINK:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("PinkThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("PinkThunder", AnimationAssets.frameDuration);
			break;
		}
		

		rowThunder.setPlayMode(Animation.PlayMode.LOOP);
		colThunder.setPlayMode(Animation.PlayMode.LOOP);
		
		topRow = iFunctions.getFastInt(prefix+" topRow", 0);
		bottomRow = iFunctions.getFastInt(prefix+" bottomRow", 0);
		leftCol = iFunctions.getFastInt(prefix+" leftCol", 0);
		rightCol = iFunctions.getFastInt(prefix+" rightCol", 0);
		stepTime = iFunctions.getFastFloat(prefix+" stepTime", 0);
		curExplodeIndex = iFunctions.getFastInt(prefix+" curExplodeIndex", 0);
		
		String data = iFunctions.getFastString(prefix+" explodes", "");
		explodes = new ParticleExplode[mirrorTarget.size()];
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
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
	}
	
	public void concurrentResolve() {
		int i = source[0] / 8; int j = source[0] % 8;
		int dType = logic.diamondType(logic.grid[i][j]);
		int dColor = logic.diamondColor(logic.grid[i][j]);
		int maxCol = logic.maxCol(i, j, dColor); int minCol = logic.minCol(i,j, dColor);
		int maxRow = logic.maxRow(i, j, dColor); int minRow = logic.minRow(i, j, dColor);
		for (int t = 0 ; t < 8 ; t++) 
		{
			if (canEffect(t, j)) {
				logic.effectOf[t][j].setEffect(this, false);
				this.addM_Targer(new Integer(t * 8 + j));
			}
			if (canEffect(i, t)) {
				logic.effectOf[i][t].setEffect(this, false);
				if (t != j) this.addM_Targer(new Integer(i * 8 + t));
			}
		}
	}
	
	public void toConcurrentEffect() {
	}
	
	public void init() {
		logic.SpecialEffect++;
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		
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
		
		Sound sound = screen.manager.get(SoundAssets.T_THUNDER_SOUND, Sound.class);
		Assets.playSound(sound);
		
		switch (screen.logic.diamondColor(getsValue())) {
		case IDiamond.WHITE:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("WhiteThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("WhiteThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.BLUE:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("BlueThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("BlueThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.GREEN:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("GreenThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("GreenThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.ORANGE:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("OrangeThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("OrangeThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.RED:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("RedThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("RedThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.YELLOW:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("YeallowThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("YeallowThunder", AnimationAssets.frameDuration);
			break;
		case IDiamond.PINK:
			rowThunder = screen.gGame.getAssets().getEffectAnimation("PinkThunder", AnimationAssets.frameDuration);
			colThunder = screen.gGame.getAssets().getEffectAnimation("PinkThunder", AnimationAssets.frameDuration);
			break;
		}
		

		rowThunder.setPlayMode(Animation.PlayMode.LOOP);
		colThunder.setPlayMode(Animation.PlayMode.LOOP);

		handleInBeginEffect();
		for (int i = 0 ; i < mirrorTarget.size() ; i++) {
			Integer integer = mirrorTarget.get(i);
			cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.ROW_COL_THUNDER, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			
			changeStatusBeforeRun(row, col);
		}
		
		topRow = bottomRow = source[0] / 8;
		leftCol = rightCol = source[0] % 8;
		explodes = new ParticleExplode[mirrorTarget.size()];
		explode = explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		
		step = RUNNING_STEP;
	}
	
	public boolean beginStep(int stage) {// tra ve true neu buoc nay co the khoi tao con false thi khoi tao buoc tiep
		IDiamond diamond = null;
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		int row1 = -1;
		int col1 = -1;
		
		row1 = row; col1 = col - stage;
		
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
					leftCell = true;
					
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		row1 = row; col1 = col + stage;
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
					rightCell = true;
					
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		row1 = row - stage; col1 = col;
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
					downCell = true;
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		row1 = row + stage; col1 = col;
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
					upCell = true;
				
					logic.effectOf[row1][col1].setEffect(this, false);
				}
			}
		}
		
		return (number > 0);
	}
	
	public void run(float deltaTime) {
		
		if (step > INIT_STEP && step < FINISH_STEP) {

			animationTime += deltaTime;
			for (int i = 0 ; i < explodes.length; i++) {
				if (explodes[i] != null) explodes[i].update(deltaTime);
			}
			System.out.println("animation "+animationTime+" "+step);
			if (step < 9)
				if (stepTime >= TIME_PER_EXPLODE) {
					stepTime = 0;
					boolean isRunning = beginStep(step - 1);
					if (isRunning)
						step++;
				}
			stepTime += deltaTime;
			
			if (isFinishing())
				freeEffect();
		}
	}
	
	public void draw(float deltaTime) {
		if (step > INIT_STEP && step < FINISH_STEP) {
			screen.batch.setColor(1f, 1f, 1f, 1);
			TextureRegion region = null;
			int cell = source[0];
			int row = cell / 8;
			int col = cell % 8;
			
//			for (int i = 0 ; i < mirrorTarget.size() ; i++) {
//				Integer integer = mirrorTarget.get(i);
//				cell = integer.intValue(); row = cell / 8; col = cell % 8;
//				Effect effect = (Effect) logic.effectOf[row][col].effectTarget;
//				{
//					IDiamond diamond = screen.diamonds.get(cell);
//					if (isAffected(cell))
//					if (!diamond.isFinished(Diamond.GRADUALLY_DISAPPEAR) && logic.grid[row][col] != -1) {
//						diamond.draw(deltaTime, screen.batch);
//					}
//				}
//			}
			
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
	
				screen.batch.draw(region, screen.gridPos.x , y - (128) / 2, w * 2, (128) / 2, w * 4, 128, 1f, 1f, 0 , false);
				screen.batch.draw(region, screen.gridPos.x + w * 4, y - (128) / 2, w * 2, (128) / 2, w * 4, 128, 1f, 1f, 0 , false);
				
				region = colThunder.getKeyFrame(animationTime + 2 * colThunder.frameDuration);
				// doc
				screen.batch.draw(region, x - (128) / 2, screen.gridPos.y + h * 4, 128, h * 4);
				screen.batch.draw(region, x - (128) / 2, screen.gridPos.y, 128, h * 4);
			}
			screen.batch.setColor(0.4f, 0.4f, 0.4f, 1f);
		}
	}
	
	public boolean inGrid(int i, int j) {
		return ( i > -1) && (i < 8) && (j > -1) && (j < 8);
	}

	public void freeEffect() {
		int cell = source[0];
		int row = cell / 8;
		int col = cell % 8;
		int generateCell = -1;
		boolean isAffectSource = false;
		
		
		//giai phong source
		cell = source[0]; row = cell / 8; col = cell % 8;
		logic.effectOf[row][col].effectIn[Effect.ROW_COL_THUNDER] = null;
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
					if (source[0] == cell) isAffectSource = true;
				}
				logic.grid[row][col] = -1;
			}
			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
//				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_TO_FALL, logic.gridFlag[row][col]);
			}
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
			logic.gridFlag[row][col] = Operator.offBit(Effect.ROW_COL_THUNDER, logic.gridFlag[row][col]);
		}
		logic.SpecialEffect--;
		if (generateCell > -1)
		createSpecialGem(generateCell / 8, generateCell % 8);
//		if (getAutoActive()) {
//			if (random.nextInt(3) == 0) {
//				row = source[0] / 8;
//				col = source[0] % 8;
//				if (screen.grid[row][col] == -1)
//					screen.colHeight[col]++;
//				int value = logic.getDiamondValue(IDiamond.LASER_DIAMOND, 0, 0);
//				logic.grid[row][col] = value;
//				Diamond dDiamond = (Diamond) screen.diamonds.get(source[0]);
//
//				dDiamond.setDiamondValue(logic.grid[row][col]);
//
//				dDiamond.setAction(Diamond.FRAME_CHANGE);
//				dDiamond.setSize(IDiamond.DIAMOND_WIDTH, IDiamond.DIAMOND_HEIGHT);
//					
//				System.out.println("tao vien "+row+" "+col+" "+logic.gridFlag[row][col]+" "+screen.colHeight[col]);
//				logic.gridFlag[row][col] = Operator.onBit(Effect.FIXED_POS,
//						logic.gridFlag[row][col]);
//				System.out.println("tao vien "+row+" "+col+" "+logic.gridFlag[row][col]+" "+screen.colHeight[col]);
//				if (logic.effectOf[row][col].effectTarget != null) logic.effectOf[row][col].effectTarget = null;
//			}
//		}
		
		// loai bo con tro
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP + 1;
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
	
	public boolean isFinishing() {
		int row = source[0] / 8;
		int col = source[0] % 8;
		int len = Math.max(Math.max(row, 7 - row + 1), Math.max(col, 7 - col + 1));
		
		return animationTime >= (len - 1) * TIME_PER_EXPLODE
						+ explode.getAnimationDuration();
	}
	
	public boolean isFinished() {
		return step == FINISH_STEP + 1;
	}
	
	public boolean canCreateSoilExplode(int i, int j) {
		if (isRock(i, j) || isGemOrMark(i, j)) {
			System.out.println("an vien dat++++++++++++++"+logic.diamondType(screen.grid[i][j])+" "+i+" "+j);
			return logic.newEffect(i, j, this, 2);
		}
		return false;
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
//				if (getAutoActive()) {
//					screen.vip.combo++;
////					if (screen.vip.combo % 2 == 0) {
////						screen.showMascot("BlackTortoise", screen.gridPos.x + col
////								* Diamond.DIAMOND_WIDTH, screen.gridPos.y + row
////								* Diamond.DIAMOND_HEIGHT);	
////					}
//				}
//				break;
//			}
//		}
		
		screen.showComboScore(time, score, color, x, y);
		screen.showSense(time, x, y);
		
//		createSpecialGem();
	}
	
}
