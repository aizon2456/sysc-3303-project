package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import constants.Constants;
import centralServer.CentralServer;
import static org.junit.Assert.*;

public class CentralServerTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String DISTRICT = "Ottawa-Carleton, Gatineau";
    private static final int votes = 27;

    private CentralServer server;
    private Map<String,Map<String,Integer>> testMap;

    @Before
    public void setUp() throws Exception {
        server = new CentralServer();
        testMap = new HashMap<String,Map<String,Integer>>();
    }

    @Test
    public void testProperMapping() {
        //server.updateVotes();
    }
}
