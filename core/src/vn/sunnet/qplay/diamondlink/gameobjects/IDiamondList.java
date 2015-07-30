package vn.sunnet.qplay.diamondlink.gameobjects;




public interface IDiamondList {
	public void setNextDiamond(IDiamond diamond);
	public void setPreDiamond(IDiamond diamond);
	public IDiamond getPreDiamond();
	public IDiamond getNextDiamond();
}
