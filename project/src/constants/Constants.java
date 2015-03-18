package constants;

/**
 * Constants class.
 */
public final class Constants {
    
	public static final int PACKET_SIZE			= 256;
	public static final char PACKET_DELIMITER 	= '|';
	public static final char PACKET_END			= '\0';
	
	/**
	 * Time in seconds that the central/district servers have to sit without
	 * input before assuming that the election is over. 
	 */
	public static final int ELECTION_OVER 		= 30000; // 30 seconds
        
    public static enum packetType {
        REGISTER, LOGIN, VOTE, RESULT, UPDATE, NO_RESPONSE
    }
    
    public static enum returnCodes {
        SUCCESS, NON_EXISTENT, ALREADY_REGISTERED,
        WRONG_CREDENTIALS, ALREADY_VOTED, VOTING_DONE
    }
}
