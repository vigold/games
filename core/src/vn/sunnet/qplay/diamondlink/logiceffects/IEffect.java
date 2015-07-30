package vn.sunnet.qplay.diamondlink.logiceffects;





public interface IEffect {

	public void concurrentResolve();
	public void toConcurrentEffect();
	
	public void update(float deltaTime);
	public void init();
	public void run(float delTaTime);
	
	public void draw(float deltaTime);
	
	public void setType(int i);
	public int getType();
	
//	public void setEffectColor(int value);
//	public int getEffectColor();
	
	public void setSwapFlag(boolean flag);
	public boolean getSwapFlag();
	
	public void setSource(int... cell);
	public int getSource(int i);
	
	public void addTarget(Integer cell);
	public int getTarget(int pos);
	
	public void setNextEffect(IEffect effect);
	public void setPreEffect(IEffect effect);
	
	public int getStepEffect();
	public boolean isFinished();
	
	public IEffect getNextEffect();
	public IEffect getPreEffect();
	
	public int getDepthBFS();

	public void setAutoActive(boolean status);
	public boolean getAutoActive();
	
}
