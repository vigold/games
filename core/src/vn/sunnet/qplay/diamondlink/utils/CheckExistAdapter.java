package vn.sunnet.qplay.diamondlink.utils;



public class CheckExistAdapter {
	
	CustomHttpClient httpClient;
	CheckExistEventListener listener;
	
	public CheckExistAdapter(CheckExistEventListener listener) {
		this.listener = listener;
		httpClient = new CustomHttpClient("http://viguysentertainment.com/manager/index.php/services/checkexist");
	}
	
	public void checkExist(String deviceid, String gameid, String modeid) {
		httpClient.clearParam();
		httpClient.addParam("deviceid", deviceid);
		httpClient.addParam("gameid", gameid);
		httpClient.addParam("modeid", modeid);
//		System.out.println("checkExist "+deviceid);
		new Request().start();
	}
	
	
	private class Request extends Thread {

		@Override
		public void run() {
			String result;
			try {
				result = httpClient.request();
			} catch (Exception e) {
				listener.onCheckExistEventFailure("Network Connection Error");
				return;
			}
			
			System.out.println("result of post "+result+" "+result.length());
			
		
//			if (result.length() == 6) {
//				System.out.print("pos");
//				listener.onCheckExistEventSuccess(result);
//			} else {
//				System.out.print("not pos");
//				listener.onCheckExistEventFailure(result);
//			}
//			if ("Success".equals(result)) {
//				listener.onSubmitScoreSucess(result);
//			} else {
//				listener.onSubmitScoreFailure(result);
//			}
		};
	};
	
}
