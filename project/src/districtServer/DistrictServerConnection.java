package districtServer;

import java.net.*;
import java.io.*;

import constants.Constants;

public class DistrictServerConnection {

	private DatagramPacket request;

//	public DistrictServerConnection(int districtServerPort){
//		try {
//			aSocket = new DatagramSocket(districtServerPort);
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		};
//	}

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
		try {
			DatagramSocket aSocket;
			try {
				aSocket = new DatagramSocket(districtServerPort);
				
				byte[] buffer = new byte[Constants.PACKET_SIZE];

				request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);			
				aSocket.close();
				return new String(request.getData()).trim().getBytes();
			} catch (SocketException e) {
				e.printStackTrace();
			};
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
	public void send(String returnCode){
		try {
			try {
				DatagramSocket aSocket = new DatagramSocket();
				byte[] buffer = (returnCode + Constants.PACKET_END).getBytes();
				
				DatagramPacket response = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
				aSocket.send(response);
				aSocket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			};

		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
	}
	
	/**
	 * Sends the result of the previous request back to a specified port/address.
	 * @param returnCode the result of the most recent request
	 * @param districtServerPort the port that the message is being sent from
	 */
	public void send(String returnCode, InetAddress address, int port){
		try {
			try {
				DatagramSocket aSocket = new DatagramSocket();
				byte[] buffer = (returnCode + Constants.PACKET_END).getBytes();
				
				System.out.println(address + " "  + port);
				DatagramPacket response = new DatagramPacket(buffer, buffer.length, address, port);
				aSocket.send(response);
				aSocket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			};

		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
	}

	public DatagramPacket getRequest() {
		return request;
	}
}