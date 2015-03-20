package tests;

import districtServer.DistrictServer;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        PollingStationConnectionTest.class,
        PollingStationTest.class,
        VoterTest.class,
        CandidateTest.class,
        DistrictServerTest.class,
        DistrictServer.class
})
public class ElectronicVotingTestSuite {

}