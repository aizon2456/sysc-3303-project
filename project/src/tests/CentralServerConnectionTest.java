package tests;

import constants.Constants;

import org.junit.Before;
import org.junit.Test;

import centralServer.CentralServerConnection;
import static org.junit.Assert.assertTrue;

public class CentralServerConnectionTest {

    private static final String DISTRICT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DISTRICT_PORT = 2016;
    private static final String MESSAGE = "|Burlington|John|Smith|4";

    private CentralServerConnection connection;

    @Before
    public void setUp() {
        connection = new CentralServerConnection(DISTRICT_PORT);
    }

    @Test
    public void testConnection() {
    	Thread t = new Thread(new Runnable() {    		
    	    public void run() {
    	        byte[] responseBytes = connection.receiveCandidateVotes();
    	        String response = new String(responseBytes);
    	        assertTrue("Expected packet to contain: " + MESSAGE + ", actually contained: "
    	        + response, response.equals(MESSAGE));
    	    }
    	});
    	t.start();
        (new MockServer(DISTRICT_PORT, Constants.PACKET_SIZE, MESSAGE, DISTRICT_SERVER_ADDRESS)).start(); // create mock server
        
    }
    
}