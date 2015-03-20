package tests;

import districtServer.Candidate;
import districtServer.FileInfoReader;
import districtServer.Voter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FileInfoReaderTest {

    private static final String DISTRICT_NAME = "Burlington";
    private static final String INVALID_DISTRICT_NAME = "NotADistrict";

    private FileInfoReader reader;

    @Before
    public void setUp() {
        reader = new FileInfoReader();
    }

    @Test
    public void testBuildVoterList() {
        ArrayList<Voter> voters = reader.buildVoterList(DISTRICT_NAME);
        assertFalse(voters.isEmpty());
    }

    @Test
    public void testInvalidVoterList() {
        ArrayList<Voter> voters = reader.buildVoterList(INVALID_DISTRICT_NAME);
        assertTrue(voters.isEmpty());
    }

    @Test
    public void testBuildCandidateList() {
        ArrayList<Candidate> candidates = reader.buildCandidateList(DISTRICT_NAME);
        assertFalse(candidates.isEmpty());
    }

    @Test
    public void testInvalidCandidateList() {
        ArrayList<Candidate> candidates = reader.buildCandidateList(INVALID_DISTRICT_NAME);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testForValidDistrict() {
        assertTrue(reader.isValidDistrictName(DISTRICT_NAME));
    }

    @Test
    public void testForInvalidDistrict() {
        assertFalse(reader.isValidDistrictName(INVALID_DISTRICT_NAME));
    }
}
