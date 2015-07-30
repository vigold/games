package vn.sunnet.qplay.diamondlink.utils;

public class Actions {
	/*client->server*/
	public static final String GET_USER_INFO = "getUserInfo";
	public static final String GET_RANK_IN_WORLD = "getRankInWorld";
	public static final String GET_NICK_NAME = "getNickName";
	public static final String ADD_CHATS = "addChats";
	public static final String GET_CHATS = "getChats";
	public static final String GET_RANK_IN_FRIENDS = "getRankInFriends";
	public static final String BUY_ITEMS = "buyItems";
	public static final String USE_ITEM = "useItem";
	public static final String REMOVE_CARD = "removeCard";
	public static final String REMOVE_AVATAR = "removeAvatar";
	public static final String ACTIVE_AVATAR = "activeAvatar";
	public static final String ACTIVE_MODE = "activeMode";
	public static final String SEND_GIFT = "sendGift";
	public static final String READ_MAILS = "readMails";
	public static final String DELETE_MAILS = "deleteMails";
	public static final String BUY_TURNS = "buyTurns";
	/*server->client*/
	public static final String SET_CHATS = "setChats";
	public static final String SET_USER_INFO = "setUserInfo";
	public static final String SET_NICK_NAME = "setNickName";
	public static final String SET_RANK_IN_WORLD = "setRankInWorld";
	public static final String SET_RANK_IN_FRIENDS = "setRankInFriends";
	public static final String SEND_MESSAGES = "sendMessages";
	public static final String AUTHORIZE_ITEMS = "authorizeItems";
	public static final String AUTHORIZE_USE = "authorizeUse";
	public static final String AUTHORIZE_REMOVE_AVATAR = "authorizeRemoveAvatar";
	public static final String AUTHORIZE_REMOVE_CARD = "authorizeRemoveCard";
	public static final String AUTHORIZE_ACTIVE = "authorizeActive";
	public static final String AUTHORIZE_MODE = "authorizeMode";
	public static final String AUTHORIZE_GIFT = "authorizeGift";
	public static final String AUTHORIZE_MAILS = "authorizeMails";
	public static final String AUTHORIZE_TURNS = "authorizeTurns";
	/*sever<->client*/
	public static final String UPDATE_SUMMARY = "updateSummary";
	public static final String RECEIVE_MESSAGES = "receiveMessages";
	public static final String START = "start";
}
