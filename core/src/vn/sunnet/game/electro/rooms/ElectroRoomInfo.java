package vn.sunnet.game.electro.rooms;



/**
 * Lớp chưa thông tin logic của 1 server room
 * 
 * @author VuH
 * 
 */
public class ElectroRoomInfo {

	/** Zone name của room cha */
	public String fatherZoneName;

	/** Room name của room cha */
	public String fatherRoomName;

	public String zoneName;
	public String roomName;
	public int zoneId;
	public int roomId;

	/** Tên của plugin mà client sẽ giao tiếp */
	public String pluginName;

	/** GameId (nếu là 1 game room) */
	public int gameId;

	public ElectroRoomInfo(String zoneName, String roomName, int zoneId,
			int roomId, int gameId, String pluginName, String fZoneName,
			String fRoomName) {
		this.zoneName = zoneName;
		this.roomName = roomName;
		this.zoneId = zoneId;
		this.roomId = roomId;
		this.gameId = gameId;
		if ((pluginName != null) && !"".equals(pluginName))
			this.pluginName = pluginName;
		else
			this.pluginName = "NodePlugin";

		fatherRoomName = fRoomName;
		fatherZoneName = fZoneName;
	}

	/**
	 * 
	 * Constructor to use when re-constructing object from a parcel
	 * 
	 * @param in
	 *            a parcel from which to read this object
	 */
	

	
	/**
	 * 
	 * Called from the constructor to create this object from a parcel.
	 * 
	 * @param in
	 *            parcel from which to re-create object
	 */

}
