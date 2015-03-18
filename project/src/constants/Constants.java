package constants;

/**
 * Constants class.
 */
public final class Constants {
    
	public static final int SEND_SIZE 	= 16;
    public static final int PORT_SIZE   = 4;
	public static final int DATA_SIZE 	= 256;
	public static final int PACKET_SIZE	= SEND_SIZE + DATA_SIZE;
        
        public static enum packetType {
            REGISTER, LOGIN, VOTE, RESULT, UPDATE
        }
        
        public static enum returnCodes {
            SUCCESS, NON_EXISTENT, ALREADY_REGISTERED,
            WRONG_CREDENTIALS, ALREADY_VOTED
        }
}
