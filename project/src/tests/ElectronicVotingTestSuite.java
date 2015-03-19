package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        PollingStationConnectionTest.class,
        PollingStationServerTest.class,
        VoterTest.class,
        CandidateTest.class,
        DistrictServerTest.class
})
public class ElectronicVotingTestSuite {

}