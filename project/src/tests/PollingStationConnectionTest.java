package tests;

import constants.Constants;
import org.junit.Before;
import org.junit.Test;
import pollingStation.PollingStationConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PollingStationConnectionTest {

    private static final String DISTRICT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DISTRICT_PORT = 2015;
    private static final String MESSAGE = "Hello";

    private PollingStationConnection connection;

    @Before
    public void setUp() {
        connection = new PollingStationConnection(DISTRICT_SERVER_ADDRESS, DISTRICT_PORT);
    }

    @Test
    public void testSendMessage() {
        (new MockServer(DISTRICT_PORT, Constants.PACKET_SIZE)).start(); // create mock server
        String response = connection.sendMessage(MESSAGE);
        assertTrue("Expected packet to contain: " + MESSAGE + ", actually contained: " + response, response.contains(MESSAGE));
    }

    @Test
    public void testTimeoutMessage() {
        String response = connection.sendMessage(MESSAGE);
        assertEquals("", "" + Constants.packetType.NO_RESPONSE, response);
    }
}