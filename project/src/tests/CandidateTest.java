package tests;

import districtServer.Candidate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CandidateTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String SOCIAL_INSURANCE_NUMBER = "123 456 789";
    private static final int NUMBER_OF_VOTES = 10;

    private Candidate candidate;

    @Before
    public void setUp() throws Exception {
        candidate = new Candidate(FIRST_NAME, LAST_NAME, SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    public void testGetNumVotes() throws Exception {
        assertEquals(candidate.getNumVotes(), 0);
    }

    @Test
    public void testSetNumVotes() throws Exception {
        candidate.setNumVotes(NUMBER_OF_VOTES);
        assertEquals(candidate.getNumVotes(), NUMBER_OF_VOTES);
    }

    @Test
    public void testIncrementNumVotes() throws Exception {
        candidate.incrementNumVotes();
        assertEquals(candidate.getNumVotes(), 1);
    }
}