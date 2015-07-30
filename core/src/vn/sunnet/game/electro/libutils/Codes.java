package vn.sunnet.game.electro.libutils;

public class Codes {

	public static final int CODE_TIMEOUT = -1;

	public static final int CODE_REFUSE = 200;
	public static final int CODE_ACCEPT = 201;
	public static final int CODE_EXIT = -100;

	// Code register
	public static final String REGISTER_PREFIX = "RE";

	public static final int CODE_ERROR = 10;
	public static final int CODE_USER_EXIST = 11;
	public static final int CODE_LENGTH_ERROR = 12;
	public static final int CODE_DISCONNECT_SERVER = 14;
	

	// Code login
	public static final String LOGIN_PREFIX = "LO";

	public static final int CODE_LOGIN_WRONG_INFO = 10;
	public static final int CODE_LOGIN_CONFLICT = 11;
	public static final int CODE_LOGIN_ERROR = 12;
	public static final int CODE_LOGIN_OLD_USER = 13;

	// Code change password
	public static final String PASS_PREFIX = "PA";
	public static final int CODE_PASS_NOT_CORRECT = 10;
	public static final int CODE_CHANGE_PASS_ERROR = 11;

	// Code logout
	public static final String LOGOUT = "OUT";

	// Code exit
	public static final String EXIT = "EXIT";

	// Code roomlist
	public static final String ROOM_LIST_PREFIX = "RL";
	public static final int CODE_GET_ROOM_LIST = 0;
	public static final int CODE_ENTER_ROOM = 1;

	// public static final int CODE_NUMBER_OF_ROOM = 2;
	// public static final int CODE_STATE_OF_ROOM = 3;

	// Code room
	public static final String ROOM_PREFIX = "RO";
	public static final int CODE_GET_TABLE_LIST = 0;
	public static final int CODE_ENTER_TABLE = 1;
	public static final int CODE_TABLE_STATE = 2;
	public static final int CODE_TABLE_BET = 3;

	public static final int CODE_INVALD_TABLE = 1;

	// Code table
	public static final String TABLE_PREFIX = "TA";

	public static final int CODE_READY = 0;
	public static final int CODE_GET_TABLE_INFO = 1;
	public static final int CODE_SET_BET = 2;
	public static final int CODE_READY_STATE = 3;
	public static final int CODE_CHANGE_MONEY = 4;
	
	public static final int CODE_START_GAME = 10;
	public static final int CODE_KICK_BY_MASTER = -10;
	public static final int CODE_KICK_BY_SERVER = -20;

	public static final int CODE_TABLE_INFO_TABLE = 0;
	public static final int CODE_TABLE_P_STATE = 1;
	public static final int CODE_TABLE_P_MONEY = 2;
	public static final int CODE_TABLE_P_APPEARANCE = 3;

	

	// Code game
	public static final String GAME_PREFIX = "GA";

	// public static final int CODE_READY = 0;
	// public static final int CODE_NEXT_TURN = 1;

	public static final int CODE_PLACE = 10;
	public static final int CODE_ACCEPT_PLACING = 11;
	public static final int CODE_SURRENDER = 12;
	public static final int CODE_REFUSE_PLACING = 13;

	public static final int CODE_GAME_OVER = 20;

	public static final int CODE_UNDO = 40;
	public static final int CODE_UNDO_RESPOND = 41;

	public static final int CODE_ALREADY_UNDID = 45;
	public static final int CODE_HAVENT_PLACED = 46;
	public static final int CODE_OPP_UNDO = 47;

	public static final int CODE_REMOVE = 50;
	public static final int CODE_ACCEPT_REMOVE = 51;

	public static final int CODE_PLAYER_TURN = 60;
	public static final int CODE_ACCEPT_PLAYER_TURN = 61;

	public static final int CODE_PAUSE_GAME = 70;

	public static final int CODE_CHAT = 100;
	
	public static final int CODE_CHANGE_MAP = 200;

	public static final String ERROR_PREFIX = "ER";
	public static final int CODE_ERROR_WRONG_SYNTAX = 0;

	public static final String SUGGEST_PREFIX = "SU";

	public static final String NOTICE_PREFIX = "NO";

	// info
	public static final String PREFIX_PLAYER_INFO = "I";

	// payment info
    public static final String PREFIX_PAYMENT_INFO = "PMINFO";

    // scratch
    public static final String PREFIX_SCRATCH = "SCRATCH";
    
    public static final int CODE_SCRATCH_INFO_ERROR = 0;
    public static final int CODE_SCRATCH_REQUEST_ERROR = 1;
    public static final int CODE_SCRATCH_PENALTY = 2;

    // SMS
    public static final String PREFIX_SMS = "SMS";

	// Top
	public static final String PREFIX_TOP_RICHEST = "TR";
	public static final String PREFIX_TOP_SCORE = "TS";

	// Friend
	public static final String PREFIX_FRIEND_LIST = "LF";
	public static final String PREFIX_FRIEND_MESSAGE = "MSG";
	public static final String PREFIX_MAKE_FRIEND = "MF";
	public static final String PREFIX_MAKE_FRIEND_REPLY = "RMF";

	// Find table
	public static final String PREFIX_FIND_TABLE = "AF";


	
}
