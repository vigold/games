package vn.sunnet.qplay.diamondlink.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ClippingGroup extends Group {
	public boolean useClipping;
	private Rectangle scissors;
	private Camera cameraRef;
	private Vector3 tmp;

	public ClippingGroup() {
		this(null, false, null);
	}

	/**
	 * This class is used as a group that can execute clipping on the actors
	 * according to it's own bounds.
	 * 
	 * @param name
	 *            The name of this group.
	 * @param useClipping
	 *            Wether or not to cut off actors that exceed the bounds of the
	 *            group.
	 * @param cameraRef
	 *            A reference to the camera of the stage this group is in.
	 */
	public ClippingGroup(String name, boolean useClipping, Camera cameraRef) {
		super();

		this.useClipping = useClipping;
		this.cameraRef = cameraRef;
		this.scissors = new Rectangle();
		this.tmp = new Vector3();
	}

	/**
	 * This overrides the normal draw method of the Group class so we can
	 * execute clipping based on the bounds of this object.
	 */
	@Override
	   public void draw(Batch batch, float parentAlpha)
	   {
	      if(!useClipping)
	         super.draw(batch, parentAlpha);
	      else
	      {
	          if(!isVisible()) 
	             return;
	      
//	          Gdx.gl.glEnable(GL10.GL_SCISSOR_TEST);
//	          calculateScissors(cameraRef, batch.getTransformMatrix());
//	          applyTransform(batch, transform)
//	          setTransform(batch);
//	          Gdx.gl.glScissor((int)scissors.x, (int)scissors.y, (int)scissors.width, (int)scissors.height);                
//	          drawChildren(batch, parentAlpha);
//	          Gdx.gl.glDisable(GL10.GL_SCISSOR_TEST);
//	          resetTransform(batch);
	      }
	   }

	/**
	 * <Taken from gdx.scenes.scene2d.ui.utils.ScissorStack>
	 * 
	 * Calculates a scissor rectangle in OpenGL ES window coordinates from a
	 * {@link Camera}, a transformation {@link Matrix4} and an axis aligned
	 * {@link Rectangle}. The rectangle will get transformed by the camera and
	 * transform matrices and is then projected to screen coordinates. Note that
	 * only axis aligned rectangles will work with this method. If either the
	 * Camera or the Matrix4 have rotational components, the output of this
	 * method will not be suitable for
	 * {@link GLCommon#glScissor(int, int, int, int)}.
	 * 
	 * @param camera
	 *            the {@link Camera}
	 * @param batchTransform
	 *            the transformation {@link Matrix4}
	 * @param scissor
	 *            the Rectangle to store the result in
	 */
	private void calculateScissors(Camera camera, Matrix4 batchTransform) {
		tmp.set(this.getX(), this.getY(), 0);
		tmp.mul(batchTransform);
		camera.project(tmp);
		scissors.x = tmp.x;
		scissors.y = tmp.y;

		tmp.set(this.getX() + this.getWidth(), this.getY() + this.getHeight(),
				0);
		tmp.mul(batchTransform);
		camera.project(tmp);
		scissors.width = tmp.x - scissors.x;
		scissors.height = tmp.y - scissors.y;
	}

	/**
	 * This overrides the normal hit method, making sure that we can only hit
	 * part of children that are currently clipped.
	 */
	public Actor hit(float x, float y) {
		if (!useClipping
				|| (x > 0 && y > 0 && x < getWidth() && y < getHeight())) // TODO													// ?
			return super.hit(x, y, true);
		else
			return null;
	}
}
