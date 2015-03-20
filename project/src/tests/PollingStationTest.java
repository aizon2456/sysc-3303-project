package tests;

import constants.Constants;
import org.junit.Before;
import org.junit.Test;
import pollingStation.PollingStation;

import static org.junit.Assert.*;

public class PollingStationTest {

    private static final String[] DISTRICT_SERVER_ADDRESSES =
            new String[] {"127.0.0.1", "127.0.0.2", "127.0.0.3", "127.0.0.4", "127.0.0.5", "127.0.0.6"};
    private static final int[] DISTRICT_PORTS = new int[] {2015, 2016, 2017, 2018, 2019, 2020};

    private static final String FIRST_NAME  = "John";
    private static final String LAST_NAME   = "Doe";
    private static final String SIN         = "123 456 789";
    private static final String LOGIN_NAME  = "JohnDoe";
    private static final String PASSWORD    = "password123";
    private static final String CANDIDATE   = "987 654 321";

    private int serverNumber = 0;

    private PollingStation pollingStationServer;

    @Before
    public void setup() {
        pollingStationServer = new PollingStation(DISTRICT_SERVER_ADDRESSES[serverNumber], DISTRICT_PORTS[serverNumber]);
        (new MockServer(DISTRICT_PORTS[serverNumber], Constants.PACKET_SIZE)).start(); // create mock server
        serverNumber++;
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
        pollingStationServer.setLogin(LOGIN_NAME);
        String response = pollingStationServer.voteFor(CANDIDATE);

        System.out.println("LOGIN: " + response);
        assertFalse("", response.contains(LOGIN_NAME));
        assertFalse("", response.contains(CANDIDATE));
    }



    @Test
    public void testVoteInvalidSubmission() {

    }
}
