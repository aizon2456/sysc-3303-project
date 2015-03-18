package tests;

import constants.Constants;
import org.junit.Before;
import org.junit.Test;
import pollingStation.PollingStationServer;

import static org.junit.Assert.assertTrue;

public class TestPollingStationServer {

    private static final String DISTRICT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DISTRICT_PORT = 2015;

    private static final String FIRST_NAME  = "John";
    private static final String LAST_NAME   = "Doe";
    private static final String SIN         = "123 456 789";
    private static final String LOGIN_NAME  = "JohnDoe";
    private static final String PASSWORD    = "password123";
    private static final String CANDIDATE   = "987 654 321";


    private PollingStationServer pollingStationServer;

    @Before
    public void setup() {
        pollingStationServer = new PollingStationServer(DISTRICT_SERVER_ADDRESS, DISTRICT_PORT);
    }

    @Test
    public void testVoterRegistration() {
        (new MockServer(DISTRICT_PORT, Constants.DATA_SIZE)).start(); // create mock server
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
        (new MockServer(DISTRICT_PORT, Constants.DATA_SIZE)).start(); // create mock server
        String response = pollingStationServer.login(LOGIN_NAME, PASSWORD);

        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(PASSWORD));
    }

    @Test
    public void testVoterUnsuccessfulLogin() {

    }

    @Test
    public void testVoteSubmission() {
        (new MockServer(DISTRICT_PORT, Constants.DATA_SIZE)).start(); // create mock server
        String response = pollingStationServer.vote(LOGIN_NAME, CANDIDATE);

        assertTrue("", response.contains(LOGIN_NAME));
        assertTrue("", response.contains(CANDIDATE));
    }

    @Test
    public void testVoteInvalidSubmission() {

    }
}
