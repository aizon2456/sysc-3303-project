package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        PollingStationConnectionTest.class,
        PollingStationTest.class,
        VoterTest.class,
        CandidateTest.class,
        DistrictServerTest.class,
        DistrictServerConnectionTest.class,
        FileInfoReaderTest.class,
        CentralServerConnectionTest.class,
        CentralServerTest.class,
        CentralServerViewTest.class
})
public class ElectronicVotingTestSuite {

}