package vn.sunnet.qplay.diamondlink.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;




public class GameObject {
	
	public static int OBJECT_COLOR = 0;
	public static int BATCH_COLOR = 1;
	public int colorMode = OBJECT_COLOR;
	
    public Vector2 position;
    public Rectangle bounds;
    
    float originX, originY;
	float scaleX = 1, scaleY = 1;
	float rotation;
	final Color color = new Color(1, 1, 1, 1);
	Color preColor;
    
    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x,y);
        this.bounds = new Rectangle(x-width/2, y-height/2, width, height);
    }
    
    public float getX () {
		return bounds.x;
	}

	public void setX (float x) {
		this.bounds.x = x;
		this.position.x = x + bounds.width / 2;
	}

	public float getY () {
		return bounds.y;
	}

	public void setY (float y) {
		this.bounds.y = y;
		this.position.y = y + bounds.height / 2;
	}
	
	public void setCenter(float x, float y) {
		setPosition(x - bounds.width / 2, y - bounds.height / 2);
	}

	/** Sets the x and y. */
	public void setPosition (float x, float y) {
		setX(x);
		setY(y);
	}

	public void translate (float x, float y) {
		setX(getX() + x);
		setY(getY() + y);
	}

	public float getWidth () {
		return bounds.width;
	}

	public void setWidth (float width) {
		bounds.width = width;
	}

	public float getHeight () {
		return bounds.height;
	}

	public void setHeight (float height) {
		bounds.height = height;
	}

	/** Returns y plus height. */
	public float getTop () {
		return bounds.y + bounds.height;
	}

	/** Returns x plus width. */
	public float getRight () {
		return bounds.x + bounds.width;
	}

	/** Sets the width and height. */
	public void setSize (float width, float height) {
		bounds.width = width;
		bounds.height = height;
		bounds.x = position.x - width / 2;
		bounds.y = position.y - height / 2;
	}

	/** Adds the specified size to the current size. */
	public void size (float size) {
		setSize(getWidth() + size, getHeight() + size);
	}

	/** Adds the specified size to the current size. */
	public void size (float width, float height) {
		setSize(getWidth() + width, getHeight() + height);
	}

	/** Set bounds the x, y, width, and height. */
	public void setBounds (float x, float y, float width, float height) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
		position.x = x + width / 2;
		position.y = y + height / 2;
	}

	public float getOriginX () {
		return originX;
	}

	public void setOriginX (float originX) {
		this.originX = originX;
	}

	public float getOriginY () {
		return originY;
	}

	public void setOriginY (float originY) {
		this.originY = originY;
	}

	/** Sets the originx and originy. */
	public void setOrigin (float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
	}

	public float getScaleX () {
		return scaleX;
	}

	public void setScaleX (float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY () {
		return scaleY;
	}

	public void setScaleY (float scaleY) {
		this.scaleY = scaleY;
	}

	/** Sets the scalex and scaley. */
	public void setScale (float scale) {
		this.scaleX = scale;
		this.scaleY = scale;
	}

	/** Sets the scalex and scaley. */
	public void setScale (float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	/** Adds the specified scale to the current scale. */
	public void scale (float scale) {
		scaleX += scale;
		scaleY += scale;
	}

	/** Adds the specified scale to the current scale. */
	public void scale (float scaleX, float scaleY) {
		this.scaleX += scaleX;
		this.scaleY += scaleY;
	}

	public float getRotation () {
		return rotation;
	}

	public void setRotation (float degrees) {
		this.rotation = degrees;
	}

	/** Adds the specified rotation to the current rotation. */
	public void rotate (float amountInDegrees) {
		rotation += amountInDegrees;
	}

	public void setColorMode(int mode) {
		this.colorMode = mode;
	}
	
	public void setColor (Color color) {
		this.color.set(color);
	}

	public void setColor (float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}

	/** Returns the color the actor will be tinted when drawn. The returned instance can be modified to change the color. */
	public Color getColor () {
		return color;
	}
	
	public void update(float delta) {}
	
	private void beginDraw(float delta, SpriteBatch pBatch) {
		preColor = pBatch.getColor();
		if (colorMode == OBJECT_COLOR)
			pBatch.setColor(getColor());
		else pBatch.setColor(preColor.r, preColor.g, preColor.b, getColor().a);
	}
	
	private void endDraw(float delta, SpriteBatch pBatch) {
		pBatch.setColor(preColor);
	}
	
	protected void inDraw(float delta, SpriteBatch pBatch) {
		
	}
    
	public void draw(float delta, SpriteBatch pBatch) {
    	beginDraw(delta, pBatch);
    	inDraw(delta, pBatch);
    	endDraw(delta, pBatch);
    }

}
