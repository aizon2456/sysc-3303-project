package tests;

import java.util.ArrayList;

import districtServer.Candidate;
import districtServer.DistrictServer;
import districtServer.Voter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constants.Constants;
import static org.junit.Assert.*;

public class DistrictServerTest {

	
	private DistrictServer districtServer;
	private String delimiter = String.valueOf(Constants.PACKET_DELIMITER);
	private ArrayList<Voter> voters;
	private ArrayList<Candidate> candidates;
	private final String login1 = "login1";
	private final String login2 = "login2";
	private final String password = "password";
	
	@Before
	public void setUp() throws Exception {
		districtServer = new DistrictServer("Brant", 2000, 3000, "127.0.0.1");
		voters = districtServer.getVoters();
		candidates = districtServer.getCandidates();
	}
	
	@Test
	public void testParsePacketDataAndPerformCorresspondingAction(){
		int randomVoter = (int) (Math.random()*(voters.size()-1));
		
		Voter voter = voters.get(randomVoter);
		String message = Constants.packetType.REGISTER 	+ delimiter + voter.getFirstName()
														+ delimiter + voter.getLastName()
														+ delimiter + voter.getSocialInsuranceNumber()
														+ delimiter + login1
														+ delimiter + password;
		

		String test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(Constants.returnCodes.REG_SUCCESS.name(), test);
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.LOGIN_EXISTS.name());
		
		message = Constants.packetType.REGISTER 	+ delimiter + voter.getFirstName()
													+ delimiter + voter.getLastName()
													+ delimiter + voter.getSocialInsuranceNumber()
													+ delimiter + login2
													+ delimiter + password;
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.ALREADY_REGISTERED.name());

		message = Constants.packetType.REGISTER 	+ delimiter + voter.getFirstName()
													+ delimiter + "TotallyFakeName"
													+ delimiter + voter.getSocialInsuranceNumber()
													+ delimiter + login2
													+ delimiter + password;
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.NON_EXISTENT.name());
		
		message = Constants.packetType.LOGIN 	+ delimiter + login1 
												+ delimiter  + password;
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.LOGIN_SUCCESS.name() + districtServer.getDelimitedCandidates());
		
		message = Constants.packetType.LOGIN 	+ delimiter + "TotallyFakeLogin"
												+ delimiter + password;
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.WRONG_CREDENTIALS.name());

		Candidate candidate = candidates.get(0);
		message = Constants.packetType.VOTE + delimiter + login1
											+ delimiter + candidate.getSocialInsuranceNumber();
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.VOTE_SUCCESS.name());
		
		test = districtServer.parsePacketDataAndPerformCorrespondingAction(message.getBytes());
		
		assertEquals(test, Constants.returnCodes.ALREADY_VOTED.name());
	}
	
	@After
	public void tearDown() throws Exception{
		System.out.println("tearing down");
	}
}
