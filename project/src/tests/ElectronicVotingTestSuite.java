package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestPollingStationConnection.class,
        TestPollingStationServer.class
})
public class ElectronicVotingTestSuite {

}