package tests;

import constants.Constants;
import org.junit.Before;
import org.junit.Test;
import pollingStation.PollingStation;

import static org.junit.Assert.assertTrue;

public class PollingStationServerTest {

    private static final String DISTRICT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DISTRICT_PORT = 2015;

    private static final String FIRST_NAME  = "John";
    private static final String LAST_NAME   = "Doe";
    private static final String SIN         = "123 456 789";
    private static final String LOGIN_NAME  = "JohnDoe";
    private static final String PASSWORD    = "password123";
    private static final String CANDIDATE   = "987 654 321";


    private PollingStation pollingStationServer;

    @Before
    public void setup() {
        pollingStationServer = new PollingStation(DISTRICT_SERVER_ADDRESS, DISTRICT_PORT);
        (new MockServer(DISTRICT_PORT, Constants.PACKET_SIZE)).start(); // create mock server
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
    public void testInvalidRegistrationInformation() {

    }

    @Test
    public void testVoterSuccessfulLogin() {
        String response = pollingStationServer.login(LOGIN_NAME, PASSWORD);

        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(PASSWORD));
    }

    @Test
    public void testVoterUnsuccessfulLogin() {

    }

    @Test
    public void testVoteSubmission() {
        String response = pollingStationServer.vote(LOGIN_NAME, CANDIDATE);

        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(CANDIDATE));
    }

    @Test
    public void testVoteInvalidSubmission() {

    }
}
