package vn.sunnet.qplay.diamondlink.utils;



public class ChangeNameAdapter {
	CustomHttpClient httpClient;
	ChangeNameEventListenter listener;
	
	public ChangeNameAdapter(ChangeNameEventListenter listener) {
		this.listener = listener;
		httpClient = new CustomHttpClient("http://viguysentertainment.com/manager/index.php/services/updatename");
	}
	
	public void changeName(String deviceid, String name) {
		httpClient.clearParam();
		httpClient.addParam("deviceid", deviceid);
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
				listener.onChangeNameEventFailure("Network Connection Error");
				return;
			}
			
			System.out.println("result of changeName "+result);
			if (result.equals("False")) listener.onChangeNameEventFailure("Network Connection Error");
			else listener.onChangeNameEventSuccess(result);
		
		};
	};
}
