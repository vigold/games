package vn.sunnet.qplay.diamondlink.utils;

public interface GetLeaderBoardEventListener {
	void onGetLeaderBoardSuccess(String resultString);
	void onGetLeaderBoardFailure(String resultString);
}
