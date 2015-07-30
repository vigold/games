package vn.sunnet.qplay.diamondlink.utils;

public interface SubmitScoreEventListener {
	void onSubmitScoreSucess(String resultString);
	void onSubmitScoreFailure(String resultString);
}
