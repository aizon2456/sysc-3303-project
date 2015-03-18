package constants;

import java.util.ArrayList;

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
	public static final int TEST_ELECTION_OVER	= 5000; // 5 seconds
        
    public static enum packetType {
        REGISTER, LOGIN, VOTE, RESULT, UPDATE, NO_RESPONSE
    }
    
    public static enum returnCodes {
        SUCCESS, NON_EXISTENT, ALREADY_REGISTERED,
        WRONG_CREDENTIALS, ALREADY_VOTED, VOTING_DONE
    }
    
    /**
     * Parses through a pipe-delimited string and returns an array of Strings
     * @param piped The input string
     * @return The non-piped parsed string array
     */
    public static String[] parsePipedString(String piped) {
    	ArrayList<String> parse = new ArrayList<String>();
    	
    	String words = "";
    	for (int i = 0; i < piped.length(); i++) {
    		char curr = piped.charAt(i);
    		if (curr == '\0') {
    			break;
    		}
    		else if (curr == '|') {
    			parse.add(words);
    			words = "";
    		}
    		else {
    			words += curr;
    		}
    	}
    	
    	return (String[]) parse.toArray();
    }
}
