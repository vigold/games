package vn.sunnet.qplay.diamondlink.actions;

import vn.sunnet.qplay.diamondlink.math.BezierConfig;

import com.badlogic.gdx.math.Vector2;




public class BezierAction extends IntervalAction{// di chuyen theo doan
	private BezierConfig config;
    
    private float duration = 0;
    private float elapsedTime = 0;

    public BezierAction(float t, BezierConfig c) {
    	super(t);
    	duration = t;
        config = c;
        originOfCordinates = new Vector2(0, 0);
    }


    public void update(float deltaTime, Vector2 target) {
    	elapsedTime+= deltaTime;
//    	Log.d("Bezier", "total = "+elapsedTime+"  dura = "+duration);
    	updateBezier(Math.min(1, elapsedTime / duration), target);
    }

    public void updateBezier(float t, Vector2 target) {// t = Math
        float xa = config.startPosition.x;
        float xb = config.controlPoint_1.x;
        float xc = config.controlPoint_2.x;
        float xd = config.endPosition.x;

        float ya = config.startPosition.y;
        float yb = config.controlPoint_1.y;
        float yc = config.controlPoint_2.y;
        float yd = config.endPosition.y;

        float x = bezierat(xa, xb, xc, xd, t);
        float y = bezierat(ya, yb, yc, yd, t);
        target.set(originOfCordinates.x + x, originOfCordinates.y + y);
    }

    // Bezier cubic formulae :
    //	((1 - t) + t)3 = 1 expands to (1 - t)3 + 3t(1-t)2 + 3t2(1 - t) + t3 = 1
    private static float bezierat(float a, float b, float c, float d, float t) {
        return (float) (Math.pow(1 - t, 3) * a +
                3 * t * (Math.pow(1 - t, 2)) * b +
                3 * Math.pow(t, 2) * (1 - t) * c +
                Math.pow(t, 3) * d);
    }

}
