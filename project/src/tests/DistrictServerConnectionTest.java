package tests;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

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

		String message2 = Constants.returnCodes.LOGIN_EXISTS.name() + Constants.PACKET_END;

		Thread m = new Thread(new MockServer(portNum, Constants.PACKET_SIZE)); // create mock server
		m.start();

		districtServer.send(message2);
		try {
			DatagramSocket testSocket = new DatagramSocket();
			byte[] testBuffer = new byte[Constants.PACKET_SIZE];
			DatagramPacket testPacket = new DatagramPacket(testBuffer, testBuffer.length);

			try {
				testSocket.receive(testPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			assertEquals(new String(testPacket.getData()),message2);
			testSocket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception{

		System.out.println("tearing down");
	}
}
