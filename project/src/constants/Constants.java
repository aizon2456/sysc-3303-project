package constants;

/**
 * Constants class.
 */
public final class Constants {

	public static final int PACKET_SIZE = 256;
	public static final String PACKET_DELIMITER = "&";
	public static final String IPV4_REGEX = "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";
	public static final String TEST_COMPLETE = "Testing Done";
	
	public static enum packetType {
		REGISTER, LOGIN, VOTE, RESULT, NO_RESPONSE
	}

	public static enum returnCodes {
		REG_SUCCESS, NON_EXISTENT, ALREADY_REGISTERED, WRONG_CREDENTIALS, ALREADY_VOTED, LOGIN_EXISTS, LOGIN_SUCCESS, VOTE_SUCCESS,
        INVALID_NUM_ARGUMENTS
	}
}



