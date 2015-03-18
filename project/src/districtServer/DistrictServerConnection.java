package districtServer;

import java.net.*;
import java.io.*;
import constants.Constants;

public class DistrictServerConnection {

	private DatagramPacket request;
	
	public DistrictServerConnection(){}

	/**
	 * This function begins the main server listening loop, it takes messages
	 * it receives, corrupts them a little then forwards it to the main server.
	 * The response from the main server is forwarded to the client
	 * @param districtServerPort which is the Client port
	 * @param centralServerPort which is the Main Server's port
	 * @param centralIP which is the Main Server's host IP Address
	 * @return
	 */
	public byte[] beginListening(int districtServerPort){
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(districtServerPort);
			byte[] buffer = new byte[Constants.PACKET_SIZE];

			request = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(request);
			return request.getData();
			
		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		return null;
	}
	
	public void send(Constants.returnCodes returnCode, int districtServerPort){
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(districtServerPort);
			byte[] buffer = new byte[Constants.PACKET_SIZE];

			DatagramPacket response = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
			aSocket.send(response);
			request = null;
			
		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
}