package tests;

import districtServer.Candidate;
import districtServer.DistrictServer;
import districtServer.Voter;
import pollingStation.PollingStation;

import java.util.ArrayList;

public class ClientServerCommunicationTest {

    private static final String DISTRICT = "Brant";
    private static final int DISTRICT_SERVER_PORT = 2000;
    private static final int CENTRAL_SERVER_PORT = 3000;
    private static final String CENTRAL_SERVER_IP = "127.0.0.1";
    private static final String DISTRICT_SERVER_ADDRESS = "localhost";

    private static final String LOGIN1 = "login1";
    private static final String LOGIN2 = "login2";
    private static final String PASSWORD1 = "password";
    private static final String PASSWORD2 = "differentPassword";

    private PollingStation pollingStation;
    private DistrictServer districtServer;

    private ArrayList<Voter> voters;
    private ArrayList<Candidate> candidates;

    private int randomVoter, randomCandidate;


//    @Before
//    public void setup() {
//        districtServer = new DistrictServer(DISTRICT, DISTRICT_SERVER_PORT, CENTRAL_SERVER_PORT, CENTRAL_SERVER_IP);
//        districtServer.start();
//        while(districtServer.getPort() == -1);
//        System.out.println("MOVING ON");
//        pollingStation = new PollingStation(DISTRICT_SERVER_ADDRESS, 2000);
//
//        voters = districtServer.getVoters();
//        candidates = districtServer.getCandidates();
//
//        randomVoter = (int) (Math.random()*(voters.size()-1));
//        randomCandidate = (int) (Math.random()*(candidates.size()-1));
//    }

//    @Test
//    public void testVoterSuccessfulLogin() {
//        Voter voter = voters.get(randomVoter);
//        Candidate candidate = candidates.get(randomCandidate);
//
//        pollingStation.register(voter.getFirstName(),
//                                voter.getLastName(),
//                                voter.getSocialInsuranceNumber(),
//                                LOGIN1,
//                                PASSWORD1);
//
//        String response = pollingStation.login(LOGIN1, PASSWORD1);
//        System.out.println(response);
//        assertTrue(response.contains(Constants.returnCodes.LOGIN_SUCCESS.name()));
//    }

//    @Test
//    public void testVoterUnsuccessfulLogin() {
//
//    }
//
//    @Test
//    public void testSuccessfulRegistration() {
//
//    }
//
//    @Test
//    public void testInvalidRegistrationInformation() {
//        // TODO: add more test cases for different error messages
//    }

//    @Test
//    public void testSuccessfulVoteSubmission() {
//
//    }
//
//    @Test
//    public void testInvalidVoteSubmission() {
//        // TODO: add more test cases for different error messages
//    }
//
//    @After
//    public void tearDown() {
//        districtServer = null;
//    }

}
