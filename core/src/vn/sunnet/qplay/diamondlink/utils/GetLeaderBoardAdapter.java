package vn.sunnet.qplay.diamondlink.utils;

public class GetLeaderBoardAdapter {
	
	CustomHttpClient httpClient;
	GetLeaderBoardEventListener listener;
	
	
	public GetLeaderBoardAdapter(GetLeaderBoardEventListener listener) {
		this.listener = listener;
		httpClient = new CustomHttpClient("http://viguysentertainment.com/manager/index.php/services/topscore");
	}
	
	public void getLeaderBoard(String deviceid, String gameid, String modeid) {
		httpClient.clearParam();
		httpClient.addParam("modeid", modeid);
//		httpClient.addParam("gameid", gameid);
		httpClient.addParam("deviceid", deviceid);
		System.out.println("modeid " + modeid);
		new Request().start();
	}
	
	private class Request extends Thread {

		@Override
		public void run() {
			String result;
			try {
				result = httpClient.request();
			} catch (Exception e) {
				listener.onGetLeaderBoardFailure("Network Connection Error");
				return;
			}
			System.out.println("getList ");
			System.out.println(""+result);
			listener.onGetLeaderBoardSuccess(result);
//			System.out.println(result);
//			if ("Success".equals(result)) {
//				listener.onSubmitScoreSucess(result);
//			} else {
//				listener.onSubmitScoreFailure(result);
//			}
		};
	};

}
