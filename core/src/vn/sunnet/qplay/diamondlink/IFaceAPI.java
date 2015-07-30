package vn.sunnet.qplay.diamondlink;

public interface IFaceAPI {
	
	/**
	 * login Facebook
	 * 
	 * @param onStart
	 *            The method excuted when starting login (may be null)
	 * @param onError
	 *            The method excuted when login failed (may be null)
	 * @param onCancel
	 *            The method excuted when login cancel by user (may be null)
	 * @param onComplete
	 *            The method excuted when login completed (may be null)
	 */
	void login(Runnable onStart, Runnable onError, Runnable onCancel,
			Runnable onComplete);

	/**
	 * logout Facebook
	 * 
	 * @param onStart
	 *            The method excuted when starting logout (may be null)
	 * @param onError
	 *            The method excuted when logout failed (may be null)
	 * @param onCancel
	 *            The method excuted when logout canceled by user (may be null)
	 * @param onComplete
	 *            The method excuted when logout completed (may be null)
	 */
	void logout(Runnable onStart, Runnable onError, Runnable onCancel,
			Runnable onComplete);

	/**
	 * check status login in Facebook
	 * 
	 * @return true when facebook isLoggedIn otherwise false
	 */
	boolean isLoggedin();

	/**
	 * get UID of the face account
	 * 
	 * @return
	 */
	String getUserID();

	/**
	 * get UIDs of the friends of the face account
	 * 
	 * @return
	 */
	String[] getFriendsID();

	/**
	 * login(if isLoggedin return false)-> getInfo of the Face account
	 * 
	 * @param onStart
	 *            The method excuted when starting (may be null)
	 * @param onError
	 *            The method excuted when connect failed (may be null)
	 * @param onCancel
	 *            The method excuted when connect canceled by user (may be null)
	 * @param onComplete
	 *            The method excuted when connect completed (may be null)
	 */
	void connectToFace(Runnable onStart, Runnable onError, Runnable onCancel,
			Runnable onComplete);

	/**
	 * login(if isLoggedin return false)-> show the driends list dialog to
	 * invite
	 * 
	 * @param slogan
	 *            The message send to friends (may be null)
	 * @param iconLink
	 *            The link of the game icon (may be null)
	 * @param downloadLink
	 *            The downloadlink (may be null)
	 * @param onStart
	 *            The method excuted when starting (may be null)
	 * @param onError
	 *            The method excuted when invite failed (may be null)
	 * @param onCancel
	 *            The method excuted when invite canceled by user (may be null)
	 * @param onComplete
	 *            The method excuted when invite completed (may be null)
	 */
	void inviteFriends(String slogan, String iconLink, String downloadLink,
			Runnable onStart, Runnable onError, Runnable onCancel,
			Runnable onComplete);

	/**
	 * login(if isLoggedin return false)-> post slogan in the face account
	 * @param message
	 * 			  The message of user
	 * @param slogan
	 *            The message feed posted
	 * @param description
	 * 			  The description of link (may be null)
	 * @param caption
	 * 			  The caption of link (may be null)
	 * @param iconLink
	 *            The link of the game icon (may be null)
	 * @param downloadLink
	 *            The downloadlink (may be null)
	 * @param onStart
	 *            The method excuted when starting (may be null)
	 * @param onError
	 *            The method excuted when post failed (may be null)
	 * @param onCancel
	 *            The method excuted when post canceled by user (may be null)
	 * @param onComplete
	 *            The method excuted when post completed (may be null)
	 */
	void postToWall(String message, String slogan, String description, String caption, String iconLink, String downloadLink, Runnable onStart, Runnable onError, Runnable onCancel,
			Runnable onComplete);
}
