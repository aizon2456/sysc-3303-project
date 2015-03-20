package tests;

import constants.Constants;
import org.junit.Before;
import org.junit.Test;
import pollingStation.PollingStation;

import static org.junit.Assert.*;

public class PollingStationTest {

    private static final String FIRST_NAME  = "John";
    private static final String LAST_NAME   = "Doe";
    private static final String SIN         = "123 456 789";
    private static final String LOGIN_NAME  = "JohnDoe";
    private static final String PASSWORD    = "password123";
    private static final String CANDIDATE   = "987 654 321";

    private PollingStation pollingStationServer;

    @Before
    public void setup() {
        MockServer server = new MockServer(0, Constants.PACKET_SIZE);
        server.start(); // create mock server
        pollingStationServer = new PollingStation("localhost", server.getPort());
    }

    @Test
    public void testVoterRegistration() {
        String response = pollingStationServer.register(FIRST_NAME, LAST_NAME, SIN, LOGIN_NAME, PASSWORD);

        assertTrue("", response.contains(FIRST_NAME));
        assertTrue("", response.contains(LAST_NAME));
        assertTrue("", response.contains(SIN));
        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(PASSWORD));
    }

    @Test
    public void testVoterLogin() {
        String response = pollingStationServer.login(LOGIN_NAME, PASSWORD);

        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(PASSWORD));
    }

    @Test
    public void testVoteSubmission() {
        pollingStationServer.setLogin(LOGIN_NAME);
        String response = pollingStationServer.voteFor(CANDIDATE);

        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(CANDIDATE));
    }
}
