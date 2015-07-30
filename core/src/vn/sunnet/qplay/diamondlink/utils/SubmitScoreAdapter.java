package vn.sunnet.qplay.diamondlink.utils;

import org.apache.http.client.HttpClient;



@SuppressWarnings("unused")
public class SubmitScoreAdapter {
	CustomHttpClient httpClient;
	SubmitScoreEventListener listener;
	public SubmitScoreAdapter(SubmitScoreEventListener listener) {
		this.listener = listener;
		httpClient = new CustomHttpClient("http://viguysentertainment.com/manager/index.php/services/userscore");
	}
	
	public void submitScore(String deviceid, String gameid, String modeid, String score, String name) {
		httpClient.clearParam();
		httpClient.addParam("deviceid", deviceid);
		httpClient.addParam("gameid", gameid);
		httpClient.addParam("modeid", modeid);
		httpClient.addParam("score", score);
		httpClient.addParam("name", name);
		new Request().start();
	}
	
	private class Request extends Thread {

		@Override
		public void run() {
			String result;
			try {
				result = httpClient.request();
			} catch (Exception e) {
				listener.onSubmitScoreFailure("Network Connection Error");
				return;
			}
			System.out.println("result = "+result);
//			listener.onSubmitScoreSucess(result);
			if (!"[]".equals(result)) {
				listener.onSubmitScoreSucess(result);
			} else {
				listener.onSubmitScoreFailure("Failed to submit score");
			}
		};
	};
}
