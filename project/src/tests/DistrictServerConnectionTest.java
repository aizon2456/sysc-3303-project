package tests;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constants.Constants;
import districtServer.DistrictServerConnection;

public class DistrictServerConnectionTest {

	private int portNum =8000;
	private DistrictServerConnection districtServer;


	@Before
	public void setUp() throws Exception {
		// empty
	}
	@Test
	public void testBeginListening(){
		districtServer = new DistrictServerConnection();

		final String message  = Constants.packetType.REGISTER + Constants.PACKET_DELIMITER + 
				"FirstName" + Constants.PACKET_DELIMITER +"LastName" + Constants.PACKET_DELIMITER
				+ "123456789" + Constants.PACKET_DELIMITER + "LoginId" + Constants.PACKET_DELIMITER
				+ "Password" + Constants.PACKET_END;

		Thread t = new Thread(new Runnable() {
			public void run() {
				String response = new String(districtServer.beginListening(portNum));

				assertEquals(response, message);
			}
		});
		t.start();
		Thread r = new Thread(new MockServer(portNum, Constants.PACKET_SIZE, message, "127.0.0.1"));
		r.start();

		String message2 = Constants.returnCodes.LOGIN_EXISTS.name();

		Thread m = new Thread(new MockServer(portNum + 1, Constants.PACKET_SIZE)); // create mock server
		m.start();
		try {
			districtServer.send(message2, InetAddress.getLocalHost(), portNum+1);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		assertEquals(true,true);

		System.out.println("WE'RE DONE");
	}

	@After
	public void tearDown() throws Exception{

		System.out.println("tearing down");
	}
}
