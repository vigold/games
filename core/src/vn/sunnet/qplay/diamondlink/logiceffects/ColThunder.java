package vn.sunnet.qplay.diamondlink.logiceffects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

public class ColThunder extends Effect {
	
	final float TIME_PER_EXPLODE = 0.15f;
	final float THUNDER_TIME = 1.0f;
	ParticleExplode explodes[];
	MyAnimation explode;
	int topRow;
	int bottomRow;

	public ColThunder(GameLogic logic, GameScreen screen) {
		super(logic, screen);
		// TODO Auto-generated constructor stub
		this.logic = logic;
		this.screen = screen;
		type = COL_THUNDER;
		Priority = 1;
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
	}
	
	@Override
	public void parse(IFunctions iFunctions, String prefix) {
		// TODO Auto-generated method stub
		super.parse(iFunctions, prefix);
		if (step < RUNNING_STEP) return;
		switch (screen.logic.diamondColor(getsValue())) {
		case IDiamond.WHITE:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"WhiteThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.BLUE:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"BlueThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.GREEN:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"GreenThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.ORANGE:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"OrangeThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.RED:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"RedThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.YELLOW:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"YeallowThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.PINK:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"PinkThunder", AnimationAssets.frameDuration);
			break;
		}
		animation.setPlayMode(Animation.PlayMode.LOOP);
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		explodes = new ParticleExplode[mirrorTarget.size()];
		topRow = iFunctions.getFastInt(prefix+" topRow", 0);
		bottomRow = iFunctions.getFastInt(prefix+" bottomRow", 0);
		String data = iFunctions.getFastString(prefix+" explodes", "");
		
		if (data != "") {
			String split[] = data.split("\\|");
			for (int i = 0; i < split.length / 3; i++) {
				float x = Float.parseFloat(split[i * 3]);
				float y = Float.parseFloat(split[i * 3 + 1]);
				float time = Float.parseFloat(split[i * 3 + 2]);
				MyAnimation animation = gAssets.getEffectAnimation("Explode", 0.1f);
				animation.setPlayMode(Animation.PlayMode.NORMAL);
				
				ParticleEffect effect = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
				explodes[i + Math.max(bottomRow, 0)] = new ParticleExplode(explode, effect, x, y);
				explodes[i + Math.max(bottomRow, 0)].time = time;
				explodes[i + Math.max(bottomRow, 0)].isNew = false;
			}
		}
		
	}
	
	@Override
	public void concurrentResolve() {
		// TODO Auto-generated method stub
		int i = source[0] / 8; int j = source[0] % 8;
		for (int k = 0; k < 8; k++) {
			if (!canEffect(k, j)) continue;
			logic.effectOf[k][j].setEffect(this, false);
			this.addM_Targer(new Integer(k * 8 + j));
		}
	}
	
	
	@Override
	public void init() {
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
		
		switch (screen.logic.diamondColor(getsValue())) {
		case IDiamond.WHITE:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"WhiteThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.BLUE:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"BlueThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.GREEN:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"GreenThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.ORANGE:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"OrangeThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.RED:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"RedThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.YELLOW:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"YeallowThunder", AnimationAssets.frameDuration);

			break;
		case IDiamond.PINK:
			animation = screen.gGame.getAssets().getEffectAnimation(
					"PinkThunder", AnimationAssets.frameDuration);
			break;
		}
		animation.setPlayMode(Animation.PlayMode.LOOP);
		explodes = new ParticleExplode[mirrorTarget.size()];
		explode = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
		bottomRow = topRow = source[0] / 8;
		for (int i = 0 ; i < realTarget.size() ; i++) {
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;			
			IDiamond diamond = screen.diamonds.get(cell);
		}
		
		handleInBeginEffect();

		for (int i = 0 ; i < mirrorTarget.size() ;i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8; int col = cell % 8;
			logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			logic.gridFlag[row][col] = Operator.onBit(Effect.COL_THUNDER, logic.gridFlag[row][col]);
			logic.effectOf[row][col].incEffect(type);
			if (isAffected(cell)) {// vien no duoc tao ra cung voi hieu ung no
				logic.toNextEffect(row, col, this);
			}
			canCreateSoilExplode(row, col);
		}
		
		step = RUNNING_STEP;
	}
	
	@Override
	public void run(float deltaTime) {
		// TODO Auto-generated method stub
		if (step > INIT_STEP && step < FINISH_STEP) {
			animationTime += deltaTime;
//			for (int i = 0 ; i < realTarget.size() ; i++) {
//				Integer integer = realTarget.get(i);
//				int cell = integer.intValue();
//				IDiamond diamond = screen.diamonds.get(cell);
//				if (isAffected(cell) && !diamond.isFinished(IDiamond.GRADUALLY_DISAPPEAR))
//				diamond.update(deltaTime);	
//			}
			if (bottomRow > -1) {
				int row = bottomRow;
				int col = source[0] % 8;
				if (explodes[row] == null) {
					MyAnimation animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
				
					ParticleEffect effect = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
					float x = screen.gridPos.x + col * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
					float y = screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
					explodes[row] = new ParticleExplode(animation, effect, x, y);
				} else {
					if (explodes[row].getTime() > TIME_PER_EXPLODE) {
						bottomRow--;
					}
				}
			}
			if (topRow < 8) {
				int row = topRow;
				int col = source[0] % 8;
				if (explodes[row] == null) {
					MyAnimation animation = gAssets.getEffectAnimation("Explode", AnimationAssets.frameDuration);
					
					ParticleEffect effect = gAssets.getParticleEffect(ParticleAssets.EXPLODE_SPRAY);
					float x = screen.gridPos.x + col * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
					float y = screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
					explodes[row] = new ParticleExplode(animation, effect, x, y);
				} else {
					if (explodes[row].getTime() > TIME_PER_EXPLODE) {
						topRow++;
					}
				}
			}
			
			for (ParticleExplode explode: explodes) {
				if (explode != null) explode.update(deltaTime);
			}
			
			if (isFinishing()) freeEffect();
		}
	}
	
	public void freeEffect() {
		// loai bo cac effectinfo
		handleInEndEffect();
		boolean isAffectSource = false;
		int generateCell = -1;
		for (int i = 0 ; i < realTarget.size(); i++) {		
			Integer integer = realTarget.get(i);
			int cell = integer.intValue();
			int row = cell / 8;
			int col = cell % 8;
			
			if (isAffected(row, col)) {
				if (generateCell == -1) generateCell = cell;
				logic.effectOf[row][col].effectTarget = null;
				if (logic.grid[row][col] != -1) {
					eatCell(row, col);
					screen.colHeight[col]--;
					if (cell == source[0]) isAffectSource = true;
				}
				logic.grid[row][col] = -1;
			}
		}
		if (generateCell != -1)
			createSpecialGem(generateCell / 8, generateCell % 8);
//		if (getAutoActive()) {
//			if (random.nextInt(3) == 0) {
//				int row = source[0] / 8;
//				int col = source[0] % 8;
//				if (screen.grid[row][col] == -1)
//					screen.colHeight[col]++;
//				int value = screen.logic.getDiamondValue(
//						IDiamond.LASER_DIAMOND,
//						0, 0);
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
		
		// loai bo goc
		int row = source[0] / 8; int col = source[0] % 8;
		logic.effectOf[row][col].effectIn[this.type] = null;
		
		
		
		// ghi lai trang thai gridType
		for (int i = 0 ; i < mirrorTarget.size(); i++) {
			Integer integer = mirrorTarget.get(i);
			int cell = integer.intValue();
			row = cell / 8;
			col = cell % 8;
			logic.effectOf[row][col].decEffect(type);
			if (logic.effectOf[row][col].getAmountOfEffect(type) == 0)
			logic.gridFlag[row][col] = Operator.offBit(Effect.COL_THUNDER, logic.gridFlag[row][col]);

			if (logic.grid[row][col] == -1) { 
				logic.gridFlag[row][col] = Operator.offBit(Effect.FIXED_POS, logic.gridFlag[row][col]);
			}
		}
		
		nextEffect = null;
		preEffect = null;
		step = FINISH_STEP;
		screen.logic.SpecialEffect--;
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
				int row = cell / 8;
				IDiamond diamond = screen.diamonds.get(cell);
				if (isAffected(cell) && (row < bottomRow || row > topRow)){		
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
				screen.batch.draw(region, x - 128 / 2, screen.gridPos.y + h * 4, 128, h * 4);
				screen.batch.draw(region, x - 128 / 2, screen.gridPos.y, 128, h * 4);
			}
			screen.batch.setColor(0.4f, 0.4f, 0.4f ,0.4f);
		}
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
//		return (bottomRow < 0) && (topRow > 7);
		int row = source[0] / 8;
		int len = Math.max(row, 7 - row + 1);
		return animationTime > (len - 1) * TIME_PER_EXPLODE + explode.getAnimationDuration();
//		float frameDuration = 0.05f;
//		int frameNumber = (int)(animationTime / frameDuration);
//		return (frameNumber > 5);
	}
	
	public boolean isFinished() {
		return step == FINISH_STEP;
	}
	
	public void canCreateSoilExplode(int i, int j) {
		if (isRock(i, j) || isGemOrMark(i, j)) {
			System.out.println("an vien dat++++++++++++++"+logic.diamondType(screen.grid[i][j])+" "+i+" "+j);
			logic.newEffect(i, j, this, 2);
		}
	}
	
	@Override
	protected void handleInEndEffect() {
		// TODO Auto-generated method stub
		screen.combo.inc(1);
		int time = screen.combo.get();
		int score = 100 / 10;
		
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
		
		int cell = source[0]; int row = cell / 8; int col = cell % 8;
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
		
		row = source[0] / 8;
		col = source[0] % 8;
		float x = screen.gridPos.x + col * Diamond.DIAMOND_WIDTH + Diamond.DIAMOND_WIDTH / 2;
		float y = screen.gridPos.y + row * Diamond.DIAMOND_HEIGHT + Diamond.DIAMOND_HEIGHT / 2;
		
		screen.showComboScore(time, score * mirrorTarget.size(), color, x, y);
		screen.showSense(time, x, y);
//		createSpecialGem();
	}
}
