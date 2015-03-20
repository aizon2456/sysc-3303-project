package tests;

import districtServer.Voter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VoterTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String SOCIAL_INSURANCE_NUMBER = "123 456 789";
    private static final String DIFF_SOCIAL_INSURANCE_NUMBER = "987 654 321";
    private static final String DISTRICT = "Ottawa-Carleton, Gatineau";
    private static final String OTHER_DISTRICT = "Ontario";
    private static final String LOGIN_NAME = "JohnDoe1";
    private static final String PASSWORD = "password123";

    private static final String DIFF_FIRST_NAME = "Jane";
    private static final String DIFF_LAST_NAME = "Smith";


    private Voter voter;

    @Before
    public void setUp() throws Exception {
        voter = new Voter(FIRST_NAME, LAST_NAME, SOCIAL_INSURANCE_NUMBER, DISTRICT);
    }

    @Test
    public void testValidInitialization() {
        assertNotNull(voter);
    }

    @Test
    public void testVote() throws Exception {
        // TODO: complete this test
        voter.vote();
    }

    @Test
    public void testIsRegistered() throws Exception {
        assertFalse("", voter.isRegistered());
    }

    @Test
    public void testSetRegistered() throws Exception {
        voter.setRegistered(true);
        assertTrue("Expected voter registration to be true", voter.isRegistered());
        voter.setRegistered(false);
        assertFalse("Expected voter registration to be false", voter.isRegistered());
    }

    @Test
    public void testGetDistrict() throws Exception {
        assertEquals("", voter.getDistrict(), DISTRICT);
    }

    @Test
    public void testSetDistrict() throws Exception {
        voter.setDistrict(OTHER_DISTRICT);
        assertEquals("", voter.getDistrict(), OTHER_DISTRICT);
        assertFalse("", voter.getDistrict().contains(DISTRICT));
    }

    @Test
    public void testGetLoginName() throws Exception {
        assertEquals("", voter.getLoginName());
    }

    @Test
    public void testSetLoginName() throws Exception {
        voter.setLoginName(LOGIN_NAME);
        assertEquals(voter.getLoginName(), LOGIN_NAME);
    }

    @Test
    public void testGetPassword() throws Exception {
        assertEquals("", voter.getPassword());
    }

    @Test
    public void testSetPassword() throws Exception {
        voter.setPassword(PASSWORD);
        assertEquals(voter.getPassword(), PASSWORD);
    }

    @Test
    public void testHasVoted() throws Exception {
        assertFalse("", voter.hasVoted());
    }

    @Test
    public void testSetVoted() throws Exception {
        voter.setVoted(true);
        assertTrue("", voter.hasVoted());
    }

    @Test
    public void testCompareSameVoter() throws Exception {
        Voter sameVoter = new Voter(FIRST_NAME, LAST_NAME, SOCIAL_INSURANCE_NUMBER, DISTRICT);
        assertTrue(voter.equals(sameVoter));
    }

    @Test
    public void testCompareDifferentVoters() throws Exception {
        Voter differentVoter = new Voter(FIRST_NAME, LAST_NAME, DIFF_SOCIAL_INSURANCE_NUMBER, DISTRICT);
        assertFalse(voter.equals(differentVoter));
    }

    @Test
    public void testCompareNullVoter() throws Exception {
        assertFalse(voter.equals(null));
    }

    @Test
    public void testValidFirstName() throws Exception {
        assertEquals(voter.getFirstName(), FIRST_NAME);
    }

    @Test
    public void testSetFirstName() throws Exception {
        voter.setFirstName(DIFF_FIRST_NAME);
        assertEquals(voter.getFirstName(), DIFF_FIRST_NAME);
    }

    @Test
    public void testValidLastName() throws Exception {
        assertEquals(voter.getLastName(), LAST_NAME);
    }

    @Test
    public void testSetLastName() throws Exception {
        voter.setLastName(DIFF_LAST_NAME);
        assertEquals(voter.getLastName(), DIFF_LAST_NAME);
    }

    @Test
    public void testValidSocialInsuranceNumber() throws Exception {
        assertEquals(voter.getSocialInsuranceNumber(), SOCIAL_INSURANCE_NUMBER);
    }

    @Test
    public void testSetInsuranceNumber() throws Exception {
        voter.setSocialInsuranceNumber(DIFF_SOCIAL_INSURANCE_NUMBER);
        assertEquals(voter.getSocialInsuranceNumber(), DIFF_SOCIAL_INSURANCE_NUMBER);
    }

}