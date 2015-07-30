package vn.sunnet.qplay.diamondlink.gameobjects;



import vn.sunnet.qplay.diamondlink.Assets;
import vn.sunnet.qplay.diamondlink.animations.MyAnimation;
import vn.sunnet.qplay.diamondlink.assets.AnimationAssets;
import vn.sunnet.qplay.diamondlink.assets.ParticleAssets;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;



public class Diamond extends DynamicGameObject implements IDiamond{
	
	static final float WORLD_TO_BOX = 1 / 100f;
	static final float BOX_TO_WORLD = 100f;
	static final float ACTIVE_TIME = 3f;
	
	protected Vector2 source = null; // dat o tam cua vat the
	protected Vector2 destination = null;
	protected float aniamationTime = 0;
	protected float drawTime = 0;

	protected GameScreen screen = null;
	protected IDiamond preDiamond = null;
	protected IDiamond nextDiamond = null;
	
	protected int isAction = 0;
	// Sprites
	protected PooledEffect rearEffect = null;
	protected PooledEffect frontEffect = null;
	protected MyAnimation rearAnimation = null;
	protected MyAnimation animation = null;
	protected MyAnimation frontAnimation = null;
	// actions of diamonds
	
	// types of diamonds
	protected int isType = 0;
	protected int isColor = 0;
	protected int isCost = 0;

	private boolean isFinish = false;
	private int finishFlag = 0;
	private int dValue;
	public Vector2 accelStartPos = null;
	private float acviteTime = 0;
	
	
	public Diamond(float x, float y, float width, float height, GameScreen screen) { // x y la toa do tam
		super(x, y, width, height);
		this.screen = screen;
		if (source != null) source = null;
		if (destination != null) destination = null;
		
		source = new Vector2(x, y);
		destination = new Vector2(source);
		accelStartPos = new Vector2(source);
		isFinish = false;
		acviteTime = 5;
	}
	
	public void recycleDiamond(float x, float y, float width, float height, GameScreen screen) {
		this.bounds.width = width;
		this.bounds.height = height;
		this.screen = screen;
		
		source.set(x,y);
		setCenter(x, y);
		destination.set(x, y);
		
		dValue = 0;
		isType = 0;
		isColor = 0;
		isCost = 0;
		preDiamond = null;
		nextDiamond = null;
		isAction = 0;
		isType = 0;
		isColor = 0;
		aniamationTime = 0;
		isFinish = false;
		finishFlag = 0;
		acviteTime = 5;
	}
	
	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}
	
	public void setDiamondValue(int value) {
		
		this.bounds.width = DIAMOND_WIDTH;
		this.bounds.height = DIAMOND_HEIGHT;
		
		dValue = value;
		isCost = value / (screen.COLOR_NUM * screen.TYPE_NUM);
		value = value % (screen.COLOR_NUM * screen.TYPE_NUM);
		isType = value / screen.COLOR_NUM;
		isColor = value % screen.COLOR_NUM;
		acviteTime = ACTIVE_TIME;
		
	
		this.animation = screen.assets.getDiamondAnimation(value,
				AnimationAssets.frameDuration);
		
		switch (isType) {
		case HYPER_CUBE:
		case FIVE_COLOR_DIAMOND:
		case LASER_DIAMOND:
		case MARK_DIAMOND:
		case LAVA:
		case BLUE_GEM:
		case DEEP_BLUE_GEM:
		case PINK_GEM:
		case RED_GEM:
		case X_SCORE_GEM:
			this.animation.setPlayMode(Animation.PlayMode.LOOP);
			break;
		default:
			this.animation.setPlayMode(Animation.PlayMode.REVERSED);
			break;
		}

		this.rearAnimation = screen.gGame.getAssets().getRearAnimation(isType,
				isColor, AnimationAssets.frameDuration);
		if (this.rearAnimation != null)
			if (rearAnimation != null)
				rearAnimation.setPlayMode(Animation.PlayMode.LOOP);
		if (this.frontAnimation != null)
			this.frontAnimation = null;
		this.frontAnimation = screen.gGame.getAssets().getFrontAnimation(
				isType, isColor, AnimationAssets.frameDuration);
		if (frontAnimation != null)
			frontAnimation.setPlayMode(Animation.PlayMode.LOOP);
		// phan them vao
		if (rearEffect != null) {
			rearEffect.free();
			rearEffect = null;
		}
		rearEffect = getRearParticleEffect(isType);
		if (frontEffect != null) {
			frontEffect.free();
			frontEffect = null;
		}
		frontEffect = getFrontParticleEffect(isType);
	}
	
	public int getDiamondValue() {// Value represents for diamond type and color
		return dValue;
	}
	
	public int getDiamondType() {
		return isType;
	}

	@Override
	public int getDiamondColor() {
		// TODO Auto-generated method stub
		return isColor;
	}
	
	
	private PooledEffect getRearParticleEffect(int pType) {
		switch (pType) {
			case FIRE_DIAMOND:
				return screen.gGame.getAssets().getParticleEffect(ParticleAssets.FIRE);
			case BLINK_DIAMOND:
				return screen.gGame.getAssets().getParticleEffect(ParticleAssets.SMOKE);
		}
		return null;
	}
	
	private PooledEffect getFrontParticleEffect(int pType) {
		switch (pType) {
		case BLUE_GEM:
		case DEEP_BLUE_GEM:
		case PINK_GEM:
		case RED_GEM:
			return screen.gGame.getAssets().getParticleEffect(ParticleAssets.PASS_STAR);
		case X_SCORE_GEM:
			return null;//screen.gGame.getAssets().getParticleEffect(ParticleAssets.SEVEN_COLORS);
		}
		return null;
	}
	
	public void setSource(float xx, float yy) {
		setCenter(xx, yy);
		source.set(xx, yy);
	}
	
	public void setDestination(float xx, float yy) {
		destination.set(xx, yy);
	}
	
	@Override
	public void setCenterPosition(float xx, float yy) {
		// TODO Auto-generated method stub
		setCenter(xx, yy);
	}
	
	public boolean isNoLoopOfAnimation(int type) {
		return isType != FIVE_COLOR_DIAMOND && isType != BUTTERFLY_DIAMOND
				&& isType != HYPER_CUBE && isType != LASER_DIAMOND
				&& isType != MARK_DIAMOND && isType != LAVA
				&& isType != BLUE_GEM && isType != DEEP_BLUE_GEM
				&& isType != PINK_GEM && isType != RED_GEM && isType != X_SCORE_GEM;
	}
	
	public void setAction(int Action) {
		float vX = 0, vY = 0;
		float speedX = 0, speedY = 0;
		finishFlag = 0;
		isAction = Action;
		
		if (isAction == 0) {
			aniamationTime = 0;
			
			if (isNoLoopOfAnimation(isType)) {
				animation.setPlayMode(Animation.PlayMode.REVERSED);
			} else animation.setPlayMode(Animation.PlayMode.LOOP);
			return;
		}
		isFinish = false;
		if (containsAction(FRAME_CHANGE)) {
			animation.setPlayMode(Animation.PlayMode.LOOP);
			aniamationTime = 0;
		}
		if (containsAction(GRADUALLY_DISAPPEAR)) { // Little Disppear
			aniamationTime = 0;
			animation.setPlayMode(Animation.PlayMode.REVERSED);
		}
		if (containsAction(TWO_ASPECT_SWAP)) { 
			animation.setPlayMode(Animation.PlayMode.LOOP);
			float time = 0.25f;
			aniamationTime = 0;
			speedX = (screen.DIAMOND_WIDTH * WORLD_TO_BOX) / time * BOX_TO_WORLD;
			speedY = (screen.DIAMOND_HEIGHT * WORLD_TO_BOX) / time * BOX_TO_WORLD;
		}
		if (containsAction(COMBINE_MOVE)) {
			float time = 0.125f;
			aniamationTime = 0;
			speedX = (screen.DIAMOND_WIDTH * WORLD_TO_BOX) / time * BOX_TO_WORLD;
			speedY = (screen.DIAMOND_HEIGHT * WORLD_TO_BOX) / time * BOX_TO_WORLD;
			animation.setPlayMode(Animation.PlayMode.REVERSED);
		}
		if (containsAction(FALL)) { 
			accel.set(0, -9.8f * BOX_TO_WORLD); // 1 m tuong duong 20 don vi toa do
			aniamationTime = 0;
			speedX = 0;
			speedY = 0;//20f / 0.05f;
			accelStartPos.set(source.x, source.y);
			
			if (isType != FIVE_COLOR_DIAMOND)
			animation.setPlayMode(Animation.PlayMode.REVERSED);
		}
		
		if (containsAction(UP_AND_FALL)) {
			accel.set(0, -9.8f * BOX_TO_WORLD); // 1 m tuong duong 20 don vi toa do
			aniamationTime = 0;
			float time = screen.assets.getEffectTime("Explode", AnimationAssets.frameDuration / 2f);
			float s = 2 * 5 * DIAMOND_HEIGHT;
			speedX = 0;
			speedY = s;//10 * s;
			accelStartPos.set(position.x, position.y);
			if (isType != FIVE_COLOR_DIAMOND)
			animation.setPlayMode(Animation.PlayMode.REVERSED);
			isAction = FALL;
		}
		
		if (containsAction(ENHANCE_UP_AND_FALL)) {
			accel.set(0, -9.8f * BOX_TO_WORLD); // 1 m tuong duong 20 don vi toa do
			aniamationTime = 0;
			float time = screen.assets.getEffectTime("Explode", AnimationAssets.frameDuration / 2f);
			float s = 2 * 6 * DIAMOND_HEIGHT;
			speedX = 0;
			speedY = s;//12 * s;
			accelStartPos.set(position.x, position.y);
			if (isType != FIVE_COLOR_DIAMOND)
			animation.setPlayMode(Animation.PlayMode.REVERSED);
			isAction = FALL;
		}
		
		if (containsAction(ENHANCE_FALL)) {
			accel.set(0, -9.8f * BOX_TO_WORLD); // 1 m tuong duong 20 don vi toa do
			aniamationTime = 0;
			speedX = 0;
			speedY = 0;//20f / 0.05f;
			accelStartPos.set(position.x, position.y);
			
			if (isType != FIVE_COLOR_DIAMOND)
			animation.setPlayMode(Animation.PlayMode.REVERSED);
			isAction = FALL;
		}
		
		if (containsAction(ONE_ASPECT_SWAP)) {
			float time = 0.125f;
			aniamationTime = 0;
			speedX = (screen.DIAMOND_WIDTH * WORLD_TO_BOX) / time * BOX_TO_WORLD;
			speedY = (screen.DIAMOND_HEIGHT * WORLD_TO_BOX) / time * BOX_TO_WORLD;
			animation.setPlayMode(Animation.PlayMode.REVERSED);
		}
		if (Action != UP_AND_FALL && Action != ENHANCE_UP_AND_FALL) {
			if (position.x < destination.x) vX = speedX;
			else if (position.x > destination.x) vX = -speedX;
			if (position.y < destination.y) vY = speedY;
			else if (position.y > destination.y) vY = -speedY;
		} else {
			vX = speedX; vY = speedY;
		}
		
		velocity.set(vX, vY);
	}
	
	public int getAction() {
		return isAction;
	}
	
	public boolean isFinished(int action) {// tra ve true neu co hoat dong va hoat dong do ket thuc
		return ((action & finishFlag) > 0);
	}
	
	public void update(float deltaTime) {
		float xx = 0f;
		float yy = 0f;
		
		aniamationTime = aniamationTime + deltaTime;
		if (isCanActive())
			acviteTime = Math.max(0, acviteTime - deltaTime);
		if (isAction == 0) {return;}
		if (containsAction(FRAME_CHANGE)) {
			if (animation.getPlayMode() == Animation.PlayMode.NORMAL) {
				if (animation.isAnimationFinished(aniamationTime))
					setFinish(FRAME_CHANGE);
			}
		}
		if (containsAction(GRADUALLY_DISAPPEAR)) {
			gradually_disappear(deltaTime);
			acviteTime += deltaTime;
			return;
		}
		if (containsAction(TWO_ASPECT_SWAP)) {
			two_aspect_swap(deltaTime);
			if (isCanActive())
				acviteTime += deltaTime;
			return;
		}
		if (containsAction(COMBINE_MOVE)) {
			combine_move(deltaTime);
			if (isCanActive())
				acviteTime += deltaTime;
			return;
		}
		if (containsAction(FALL)) {
			//fall(deltaTime);
			chainedFall(deltaTime);
			if (isCanActive())
				acviteTime += deltaTime;
			return;
		}
		
		if (containsAction(ONE_ASPECT_SWAP)) {
			one_aspect_swap(deltaTime);
			if (isCanActive())
				acviteTime += deltaTime;
			return;
		}	
		
	}
	
	private void one_aspect_swap(float deltaTime) {
		// TODO Auto-generated method stub
		float xx = position.x;
		float yy = position.y;
		if (velocity.x > 0)
			xx = Math.min(destination.x, xx + deltaTime * velocity.x);
		else if (velocity.x < 0)
			xx = Math.max(destination.x, xx + deltaTime * velocity.x);
		if (velocity.y > 0)
			yy = Math.min(destination.y, yy + deltaTime * velocity.y);
		else if (velocity.y < 0)
			yy = Math.max(destination.y, yy + deltaTime * velocity.y);
		setCenter(xx, yy);
		if (isDestination()) {
			setFinish(ONE_ASPECT_SWAP);
		}
	}

	public boolean isDestination() {
		return (position.x == destination.x) && (position.y == destination.y);
	}
	
	public boolean isFinish() {
		return isFinish;
	}
	
	// actions of Diamond
	
	public void gradually_disappear(float deltaTime) {
		float frameDuration = 0.05f;
		int frameNumber = (int) ( aniamationTime / frameDuration);
		//Log.d("effect", "liitDeeee"+aniamationTime);
		switch (frameNumber) {
		case 0:
			bounds.width = screen.DIAMOND_WIDTH;
			bounds.height = screen.DIAMOND_HEIGHT;
			break;
		case 1:
			bounds.width = (screen.DIAMOND_WIDTH * 1f) / 0.75f;
			bounds.height = (screen.DIAMOND_HEIGHT * 1f) / 0.75f;
			break;
		case 2:
			bounds.width = screen.DIAMOND_WIDTH;
			bounds.height = screen.DIAMOND_HEIGHT;
			break;
		case 3:
			bounds.width = (screen.DIAMOND_WIDTH * 1f) / 1.5f;
			bounds.height = (screen.DIAMOND_HEIGHT * 1f) / 1.5f;
			break;
		case 4:
			bounds.width = (screen.DIAMOND_WIDTH * 1f) / 3f;
			bounds.height = (screen.DIAMOND_HEIGHT * 1f) / 3f;
			break;
		default:
			isFinish = true;
			setFinish(GRADUALLY_DISAPPEAR);
			break;
		}
	}
	
	public void two_aspect_swap(float deltaTime) {
		float xx = position.x;
		float yy = position.y;
		// Log.d("frame",
		// "swap deltaTime"+position.x+" "+position.y+" "+velocity.x+" "+velocity.y);
		if (velocity.x > 0)
			xx = Math.min(destination.x, xx + deltaTime * velocity.x);
		else if (velocity.x < 0)
			xx = Math.max(destination.x, xx + deltaTime * velocity.x);
		if (velocity.y > 0)
			yy = Math.min(destination.y, yy + deltaTime * velocity.y);
		else if (velocity.y < 0)
			yy = Math.max(destination.y, yy + deltaTime * velocity.y);
		setCenter(xx, yy);

		if (isDestination())
			setFinish(TWO_ASPECT_SWAP);
	}
	
	public void combine_move(float deltaTime) {
		float xx = position.x;
		float yy = position.y;
		// Log.d("frame", "deltaTime"+position.x+" "+position.y);
		if (velocity.x > 0)
			xx = Math.min(destination.x, xx + deltaTime * velocity.x);
		else if (velocity.x < 0)
			xx = Math.max(destination.x, xx + deltaTime * velocity.x);
		if (velocity.y > 0)
			yy = Math.min(destination.y, yy + deltaTime * velocity.y);
		else if (velocity.y < 0)
			yy = Math.max(destination.y, yy + deltaTime * velocity.y);
		setCenter(xx, yy);
		isFinish = isDestination();
		if (isDestination())
			setFinish(COMBINE_MOVE);
	}
	
	public void portraitChainedFall(float deltaTime) {
		int i = 0;
		float xx = position.x;
		float yy = position.y;
		int dCell = touchCell(xx, yy);
		int dRow = 0;
		int dCol = 0;
		int sCell = 0;
		int sRow = 0;
		int sCol = 0;

		yy = Math.max(destination.y, accelStartPos.y + velocity.y
				* aniamationTime + accel.y * (aniamationTime * aniamationTime)
				/ 2f);

		if (preDiamond != null)
			yy = Math.max(yy, preDiamond.getPosY() + screen.DIAMOND_HEIGHT);

		if (yy <= position.y)
			if (isDestination()) {// da den noi
				sCell = touchCell(position.x, position.y);
				sRow = sCell / 8;
				sCol = sCell % 8;
				if (sRow > 0) {
					if (screen.inGridFlag[sRow - 1][sCol] == Effect.EMPTY) {
						for (i = sRow - 1; i > -1; i--) {// tim o thap nhat co
															// the toi dc trong
															// thoi diem hien
															// tai
							if (screen.inGridFlag[i][sCol] > Effect.EMPTY)
								break;
						}
						i++;
						destination.set(xx, screen.gridPos.y + i
								* screen.DIAMOND_HEIGHT + screen.DIAMOND_HEIGHT
								/ 2);// cap nhat lai des
						// cap nhat lai vi tri

						yy = Math.max(destination.y, accelStartPos.y
								+ velocity.y * aniamationTime + accel.y
								* (aniamationTime * aniamationTime) / 2f);

						if (preDiamond != null)
							yy = Math.max(yy, preDiamond.getPosY()
									+ screen.DIAMOND_HEIGHT);
					} else {
						if (inGrid(sRow, sCol)) {// chi tai trong o moi dung
													// ngoai thi cho
							// xet vu no
							if (screen.inGridFlag[sRow][sCol] == 0) {
								if (preDiamond == null)
									setFinish(FALL);
							}
						}
					}
				} else {
					if (screen.inGridFlag[sRow][sCol] == 0)
						setFinish(FALL);// ket thuc do diem dung la diem hang 0
				}
			} else {
				if (preDiamond == null) {// tu dua vao moi truong xung quanh
					sCell = touchCell(position.x, position.y);
					sRow = sCell / 8;
					sCol = sCell % 8;
					if (inGrid(sRow - 1, sCol)) {
						if (isBarrier(sRow - 1, sCol)) {
							destination.set(xx, screen.gridPos.y + sRow
									* screen.DIAMOND_HEIGHT
									+ screen.DIAMOND_HEIGHT / 2);
							yy = Math.max(destination.y, accelStartPos.y
									+ velocity.y * aniamationTime + accel.y
									* (aniamationTime * aniamationTime) / 2f);

						}
					}
				}
			}
		// neu bi dung yen thi phai xet lai chuyen dong roi
		if (yy == position.y) {
			accelStartPos.set(position.x, position.y);
			aniamationTime = 0;
			velocity.y = 0;
		}
		setCenter(xx, yy);
	}
	
	private boolean isBarrier(int i, int j) {
		return (screen.inGridFlag[i][j] > Effect.EMPTY && !Operator.hasOnly(
				Effect.UP_TO_GRID, screen.inGridFlag[i][j]));
	}
	
	public void chainedFall(float deltaTime) {
		portraitChainedFall(deltaTime);
	}
	
	@Override
	public void draw(float deltaTime, SpriteBatch pBatch) {
		// TODO Auto-generated method stub
		super.draw(deltaTime, pBatch);
	}
	
	@Override
	protected void inDraw(float delta, SpriteBatch pBatch) {
		// TODO Auto-generated method stub
		behindDiamond(delta, pBatch);
		intoDiamond(delta, pBatch);
		inFrontOfDiamond(delta, pBatch);
	}
	
	public void behindDiamond(float delta, SpriteBatch pBatch) {
		TextureRegion region = null;
		if (rearEffect != null) {
			PooledEffect effect = rearEffect;
			effect.setPosition(getPosX(), getPosY());
			effect.draw(screen.batch, delta);
		}
		
		if (getRearSprite() != null) {
			switch (isType) {
			case SOIL_DIAMOND:
			case MARK_DIAMOND:
			case LAVA:
			case BLUE_GEM:
			case DEEP_BLUE_GEM:
			case PINK_GEM:
			case RED_GEM:
				region = getRearSprite().getKeyFrame(getTime());
				if (scaleX == 1 && scaleY == 1 && rotation == 0)
					pBatch.draw(region, getPosX() - DIAMOND_WIDTH / 2,
							getPosY() - DIAMOND_HEIGHT / 2, DIAMOND_WIDTH,
							DIAMOND_HEIGHT);
				else {
					pBatch.draw(region, getPosX() - DIAMOND_WIDTH / 2,
							getPosY() - DIAMOND_HEIGHT / 2, DIAMOND_WIDTH / 2,
							DIAMOND_HEIGHT / 2, DIAMOND_WIDTH, DIAMOND_HEIGHT,
							scaleX, scaleY, rotation);
				}
				break;

			default:
				if (getDiamondType() != IDiamond.CLOCK_DIAMOND) {
					region = getRearSprite().getKeyFrame(getTime());
					if (scaleX == 1 && scaleY == 1 && rotation == 0)
						pBatch.draw(region, getPosX() - DIAMOND_WIDTH,
								getPosY() - DIAMOND_HEIGHT, 2 * DIAMOND_WIDTH,
								2 * DIAMOND_HEIGHT);
					else {
						pBatch.draw(region, getPosX() - DIAMOND_WIDTH,
								getPosY() - DIAMOND_HEIGHT, DIAMOND_WIDTH,
								DIAMOND_HEIGHT, 2 * DIAMOND_WIDTH,
								2 * DIAMOND_HEIGHT, scaleX, scaleY, rotation);
					}
				}
				break;
			}

		}
	}
	
	public void intoDiamond(float delta, SpriteBatch pBatch) {
		TextureRegion region = getSprite().getKeyFrame(getTime());
		float x = bounds.x;
		float y = bounds.y;
		float w = bounds.width;
		float h = bounds.height;
		
		if (isType == LASER_DIAMOND) {
			w = 1.5f * 60;
			h = 1.5f * 60;
			x = getPosX() - w / 2;
			y = getPosY() - h / 2;
			
		}
		
		float originX = w / 2;
		float originY = h / 2;
		
		if (scaleX == 1 && scaleY == 1 && rotation == 0)
			pBatch.draw(region, x, y, w, h);
		else {
			pBatch.draw(region, x, y, originX, originY,
					w, h, scaleX, scaleY, rotation);
		}
	}
	
	public void inFrontOfDiamond(float delta, SpriteBatch pBatch) {
		drawTime += delta;
		if (getFrontSprite() != null) {
			TextureRegion region = getFrontSprite().getKeyFrame(getTime());
			float x = getPosX();
			float y = getPosY();
			float w = screen.DIAMOND_WIDTH;
			float h = screen.DIAMOND_HEIGHT;
			float w_hour = 2 * 6f/ 60f;
			float w_minute = 2 * 6f;
			if (getDiamondType() == IDiamond.CLOCK_DIAMOND) {
				region = getRearSprite().getKeyFrame(getTime());
				w = 10;//region.getRegionWidth();
				h = 50;

				pBatch.draw(region, x - w / 2 , y - 4 - 10, w / 2, 8, w, h, scaleX,
						scaleY, rotation + 90 + w_minute * drawTime);

				region = getFrontSprite().getKeyFrame(getTime());
				w = 10;//region.getRegionWidth();
				h = 40;

				pBatch.draw(region, x - w / 2 , y - 4 - 10, w / 2, 8, w, h, scaleX,
						scaleY, rotation + w_hour * drawTime);
			} else {
				if (scaleX == 1 && scaleY == 1 && rotation == 0)
					pBatch.draw(region, bounds.x, bounds.y, bounds.width,
							bounds.height);
				else
					pBatch.draw(region, bounds.x, bounds.y, bounds.width / 2,
							bounds.height / 2, bounds.width, bounds.height,
							scaleX, scaleY, rotation);
			}
		}
		
		if (frontEffect != null) {
			PooledEffect effect = frontEffect;
			effect.setPosition(getPosX(), getPosY());
			effect.draw(screen.batch, delta);
		}
		
		if (isType == X_SCORE_GEM) {
			String time = "x"+ (isCost);
			TextBounds bounds = screen.diamondFont.getBounds(time);
			screen.diamondFont.draw(pBatch, time, getPosX()
					- bounds.width / 2, getPosY() + 2 * bounds.height);
			
		}
		
		if (screen.vip != null) {
			if (screen.vip.id == VipCard.BLACK_TORTOISE && !screen.isOverTime()) {
				if (isCanActive()) {
					String time = ""+ Math.round(acviteTime);
					TextBounds bounds = screen.comboFont.getBounds(time);
					screen.comboFont.draw(pBatch, time, getPosX()
							- bounds.width / 2, getPosY() + bounds.height / 2);
				}
			}
		}
	}
	
	public boolean inGrid(int i , int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
	
	public int CellRow(int i) {
		return i / 8;
	}
	
	public int CellCol(int i) {
		return i % 8;
	}
	
	public boolean certainCell(int value) {
		return (Operator.getBit(Effect.FIXED_POS, value) > 0);// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	public int touchCell(float xx, float yy) {
		
		int j = (int) (xx - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		int i = (int) (yy - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
		
		j = (int) (xx - screen.gridPos.x) / screen.DIAMOND_WIDTH;
		i = (int) (yy - screen.gridPos.y) / screen.DIAMOND_HEIGHT;
	
		return i * 8 + j;
	}
	
	public void setFinish(int action) {
		finishFlag |= action;
	}
	
	public boolean containsAction(int action) {
		return ((isAction & action) > 0) || (isAction == REST && action == REST);
	}

	@Override
	public float getSourX() {
		// TODO Auto-generated method stub
		return source.x;
	}

	@Override
	public float getSourY() {
		// TODO Auto-generated method stub
		return source.y;
	}

	@Override
	public float getDesX() {
		// TODO Auto-generated method stub
		return destination.x;
	}

	@Override
	public float getDesY() {
		// TODO Auto-generated method stub
		return destination.y;
	}

	@Override
	public float getPosX() {
		// TODO Auto-generated method stub
		return position.x;
	}

	@Override
	public float getPosY() {
		// TODO Auto-generated method stub
		return position.y;
	}

	@Override
	public MyAnimation getSprite() {
		// TODO Auto-generated method stub
		//Log.d("Diamond", "check"+animation.getMode());
		return animation;
	}

	@Override
	public MyAnimation getRearSprite() {
		// TODO Auto-generated method stub
		return rearAnimation;
	}

	@Override
	public MyAnimation getFrontSprite() {
		// TODO Auto-generated method stub
		return frontAnimation;
	}

	@Override
	public float getTime() {
		// TODO Auto-generated method stub
		return aniamationTime;
	}

	@Override
	public Vector2 getSource() {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public Vector2 getDestination() {
		// TODO Auto-generated method stub
		return destination;
	}

	@Override
	public Vector2 getCenterPosition() {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Rectangle getBound() {
		// TODO Auto-generated method stub
		return bounds;
	}

	@Override
	public void setFinish(boolean flag) {
		// TODO Auto-generated method stub
		isFinish = flag;
	}

	@Override
	public void setNextDiamond(IDiamond diamond) {
		// TODO Auto-generated method stub
		nextDiamond = diamond;
	}

	@Override
	public void setPreDiamond(IDiamond diamond) {
		// TODO Auto-generated method stub
		preDiamond = diamond;
	}

	@Override
	public IDiamond getPreDiamond() {
		// TODO Auto-generated method stub
		return preDiamond;
	}

	@Override
	public IDiamond getNextDiamond() {
		// TODO Auto-generated method stub
		return nextDiamond;
	}

	@Override
	public PooledEffect getRearEffect() {
		// TODO Auto-generated method stub
		return rearEffect;
	}
	
	@Override
	public PooledEffect getFrontEffect() {
		// TODO Auto-generated method stub
		return frontEffect;
	}

	@Override
	public void setActivieTime(float time) {
		// TODO Auto-generated method stub
		acviteTime = time;
	}

	@Override
	public float getActiveTime() {
		// TODO Auto-generated method stub
		return acviteTime;
	}
	
	public boolean isCanActive() {
		return isType == FIRE_DIAMOND || isType == BLINK_DIAMOND
				|| isType == RT_DIAMOND || isType == CT_DIAMOND
				|| isType == FIVE_COLOR_DIAMOND || isType == HYPER_CUBE;
	}
	
	protected boolean isGemOrMarkOrSoil() {
		int dType = isType;
		return dType == Diamond.LAVA || dType == Diamond.BLUE_GEM
				|| dType == Diamond.DEEP_BLUE_GEM || dType == Diamond.PINK_GEM
				|| dType == Diamond.RED_GEM || dType == Diamond.MARK_DIAMOND
				|| dType == Diamond.SOIL_DIAMOND;
	}
	
	public float getAnimationTime() {
		return aniamationTime;
	}
	
	public void setAnimationTime(float time) {
		this.aniamationTime = time;
	}
}
