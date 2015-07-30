package vn.sunnet.qplay.diamondlink.logiceffects;



public class EffectInfo {
	/*
	 * real effect in cell
	 * source effects this cell
	 * 
	 */
	public IEffect effectIn[]  = new IEffect[Effect.MAX_EFFECT];
	public IEffect effectTarget = null;
	public int amountOfEffect[] = new int[Effect.MAX_EFFECT];// so luong tung loai effect dong thoi tac dong den cell
	public int sumOfEffect = 0; // tong so luong cac effect tac dong den cell
//	public int type = 0;
//	
//	public boolean isSource = false;
	
	public EffectInfo() {
		// TODO Auto-generated constructor stub
		for (int i = 0 ; i < effectIn.length ; i++)
			effectIn[i] = null;
		for (int i = 0 ; i < amountOfEffect.length ; i++)
			amountOfEffect[i] = 0;
		sumOfEffect = 0;
	}
	
	public int getSumOfEffect() {
		return sumOfEffect;
	}
	
	public int getAmountOfEffect(int type) {
		return amountOfEffect[type];
	}
	
	public void incEffect(int type) {
		if (type > amountOfEffect.length - 1) return;
		amountOfEffect[type] += 1;
		sumOfEffect += 1;
	}
	
	public void decEffect(int type) {
		if (type > amountOfEffect.length - 1) return;
		amountOfEffect[type] -= 1;
		sumOfEffect -= 1;
	}
	
	public void setEffect(IEffect effect, boolean enableSource) {
		if (enableSource) {
			if (!containsEffect(effect.getType())) {
				effectIn[effect.getType()] = effect;
			}
		} else {
			if (effectTarget == null) effectTarget = effect;
			else {
				
				Effect temp1 = (Effect) effectTarget;
				Effect temp2 = (Effect) effect;
				if (effect == effectTarget) return;
				
				if (temp2.getType() == Effect.CREAT_NEW_DIAMOND)  {
					effectTarget = effect;
					return;
				}
				
				if (temp1.getType() == Effect.CHAIN_THUNDER_ITEM || temp1.getType() == Effect.CHAIN_THUNDER) return;
				if (temp2.getType() == Effect.CHAIN_THUNDER || temp2.getType() == Effect.CHAIN_THUNDER_ITEM) {
					effectTarget = effect;
					return;
				}
				
				if (temp1.type == temp2.type && temp2.depth < temp1.depth) effectTarget = temp2;
				
				else if (temp1.type == temp2.type) return;
				if (temp1.depth == temp2.depth) {
					
					if (isCombineEffect(temp1) && (temp2.getType() == Effect.EXPLODE || temp2.getType() == Effect.EXPLODE_ITEM)) {
						if (temp1 == effectIn[temp1.getType()]) return;
					}
					
					if (isCombineEffect(temp2) && (temp1.getType() == Effect.EXPLODE || temp1.getType() == Effect.EXPLODE_ITEM)) {
						if (temp2 == effectIn[temp2.getType()]) {
							effectTarget = temp2;
							return;
						}
					}
					
					if (temp2.Priority > temp1.Priority) effectTarget = temp2;
				} else { /// temp2 la xay ra sau temp1
					
				}
			}
		}
	}
	
	private boolean isCombineEffect(Effect pEffect) {
		int type = pEffect.getType();
		return (type == Effect.ROW_COL_COMBINE) || 
				(type == Effect.FOUR_COl_COMBINE) || (type == Effect.FOUR_ROW_COMBINE)||
				(type == Effect.FIVE_COL_COMBINE) || (type == Effect.FIVE_ROW_COMBINE);
	}
	
	public IEffect getEffect(int i) {// lay effect loai i co nguon tai cell
		return effectIn[i];
	}
	
	public boolean containsEffect(int i) {// ham xet da co ton tai effect  loai i  co source la o nay chua
		return (effectIn[i] != null);
	}
	
}
