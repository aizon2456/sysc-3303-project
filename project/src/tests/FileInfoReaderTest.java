package tests;

import districtServer.FileInfoReader;
import districtServer.Voter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FileInfoReaderTest {

    private static final String DISTRICT_NAME = "Burlington";

    private FileInfoReader reader;

    @Before
    public void setUp() {
        reader = new FileInfoReader();
    }

    @Test
    public void testBuildVoterList() {
        ArrayList<Voter> voters = reader.buildVoterList(DISTRICT_NAME);
        for (Voter v : voters) {
            System.out.println(v.getLoginName());
        }
    }
}
