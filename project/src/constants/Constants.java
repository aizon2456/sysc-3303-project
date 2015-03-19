package constants;

/**
 * Constants class.
 */
public final class Constants {

	public static final int PACKET_SIZE = 256;
	public static final char PACKET_END = '*';
	public static final String PACKET_DELIMITER = "`";
	public static final String IPV4_REGEX = "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";

	public static enum packetType {
		REGISTER, LOGIN, VOTE, RESULT, UPDATE, NO_RESPONSE
	}

	public static enum returnCodes {
		SUCCESS, NON_EXISTENT, ALREADY_REGISTERED, WRONG_CREDENTIALS, ALREADY_VOTED, LOGIN_EXISTS
	}
}



