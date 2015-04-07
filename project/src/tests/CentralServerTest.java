package tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import constants.Constants;
import centralServer.CentralServer;
import static org.junit.Assert.*;

public class CentralServerTest {

	private static final String VOTER_NAME = "John Smith";
	private static final String DISTRICT = "Burlington";
	private static final Integer VOTES = 4;
	private static final String MESSAGE1 = "Burlington" + Constants.PACKET_DELIMITER + "John Smith" + Constants.PACKET_DELIMITER + "4";
	private static final String MESSAGE2 = "Brant" + Constants.PACKET_DELIMITER + "John James" + Constants.PACKET_DELIMITER + "6";
	private static final String MESSAGE3 = "Cambridge" + Constants.PACKET_DELIMITER + "John Jacob" + Constants.PACKET_DELIMITER + "7";

	private static final int votes = 27;

	private CentralServer server;
	private Map<String,Map<String,Integer>> testMap;

	@Before
	public void setUp() throws Exception {
		server = new CentralServer();
		Thread t = new Thread(server);
		t.start();
		testMap = new HashMap<String,Map<String,Integer>>();
	}

	@Test
	public void testSetElectionResults() {
		server.setElectionResults(MESSAGE1.getBytes());
		server.setElectionResults(MESSAGE2.getBytes());
		String[] notification = server.setElectionResults(MESSAGE3.getBytes());
		List<String> districts = Arrays.asList(notification);
		
		assertTrue(districts.contains("Burlington"));
		assertTrue(districts.contains("Brant"));
		assertTrue(districts.contains("Cambridge"));
	}

	@Test
	public void testDistrictRequest() {
		server.setElectionResults(MESSAGE1.getBytes());
		testMap = server.districtRequest(DISTRICT);
		assertTrue(testMap.keySet().contains(DISTRICT));
		assertTrue(testMap.get(DISTRICT).get(VOTER_NAME) == VOTES);
	}
}
