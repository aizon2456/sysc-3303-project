package districtServer;

import java.net.*;
import java.io.*;

import constants.Constants;

public class DistrictServerConnection {

	private DatagramPacket request;
	private DatagramSocket aSocket;

	public DistrictServerConnection(int districtServerPort){
		try {
			aSocket = new DatagramSocket(districtServerPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

	/**
	 * This function begins the main server listening loop, it takes messages
	 * it receives, corrupts them a little then forwards it to the main server.
	 * The response from the main server is forwarded to the client
	 * @param districtServerPort which is the Client port
	 * @param centralServerPort which is the Main Server's port
	 * @param centralIP which is the Main Server's host IP Address
	 * @return
	 */
	public byte[] beginListening(){
		try {
			byte[] buffer = new byte[Constants.PACKET_SIZE];

			request = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(request);
			return request.getData();

		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
		return null;
	}

	/**
	 * Sends the result of the previous request back to the original sender.
	 * @param returnCode the result of the most recent request
	 * @param districtServerPort the port that the message is being sent from
	 */
	public void send(Constants.returnCodes returnCode){
		try {
			byte[] buffer = new byte[Constants.PACKET_SIZE];

			DatagramPacket response = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
			aSocket.send(response);

		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
	}

	public void kill(){
		if (aSocket != null)
			aSocket.close();
	}

	public DatagramPacket getRequest() {
		return request;
	}
}