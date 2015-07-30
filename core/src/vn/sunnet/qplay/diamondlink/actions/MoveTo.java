package vn.sunnet.qplay.diamondlink.actions;



import com.badlogic.gdx.math.Vector2;



//
// MoveTo
//

public class MoveTo extends IntervalAction {
    private float endPositionX;
    private float endPositionY;
    private float startPositionX;
    private float startPositionY;
    protected float deltaX;
    protected float deltaY;


    public MoveTo(float t, float x1, float y1, float x2, float y2) {
        super(t);
        startPositionX = x1;
        startPositionY = y1;
        endPositionX = x2;
        endPositionY = y2;
        deltaX = endPositionX - startPositionX;
        deltaY = endPositionY - startPositionY;
    }
    
    @Override
    public void update(float t, Vector2 target) {
        target.set(startPositionX + deltaX * t, startPositionY + deltaY * t);
    }
}
