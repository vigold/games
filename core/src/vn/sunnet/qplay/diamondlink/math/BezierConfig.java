package vn.sunnet.qplay.diamondlink.math;

import com.badlogic.gdx.math.Vector2;



public class BezierConfig {
    //! startPosition of the bezier
    public Vector2 startPosition;
    //! end position of the bezier
    public Vector2 endPosition;
    //! Bezier control point 1
    public Vector2 controlPoint_1;
    //! Bezier control point 2
    public Vector2 controlPoint_2;
    
    public BezierConfig(Vector2 start, Vector2 end, Vector2 control1, Vector2 control2) {
    	this.startPosition = start;
    	this.endPosition = end;
    	this.controlPoint_1 = control1;
    	this.controlPoint_2 = control2;
    }
}
