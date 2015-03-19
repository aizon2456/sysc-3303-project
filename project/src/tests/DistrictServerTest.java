package tests;

import districtServer.DistrictServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistrictServerTest {

	
	private DistrictServer districtServer;
	
	@Before
	public void setUp() throws Exception {
		districtServer = new DistrictServer();
	}
	
	@Test
	public void testSomething(){
		
	}
	
	@After
	public static void tearDown() throws Exception{
		System.out.println("tearing down");
	}
}
