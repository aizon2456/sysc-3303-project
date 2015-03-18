package tests;

import org.junit.Before;
import pollingStation.PollingStationServer;

public class TestPollingStationServer {

    private static final String DISTRICT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DISTRICT_PORT = 2015;
    private static final String MESSAGE = "Hello";

    private PollingStationServer pollingStationServer;

    @Before
    public void setup() {
        pollingStationServer = new PollingStationServer(DISTRICT_SERVER_ADDRESS, DISTRICT_PORT);
    }
}
