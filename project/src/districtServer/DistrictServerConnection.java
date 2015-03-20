package districtServer;

import constants.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DistrictServerConnection {

	private DatagramPacket request;
    private DatagramSocket aSocket;

	/**
	 * This function begins the main server listening loop, it takes messages
	 * it receives, corrupts them a little then forwards it to the main server.
	 * The response from the main server is forwarded to the client
	 * @return data in packet
	 */
	public byte[] beginListening(int districtServerPort){
        System.out.println("GOT HERE");
		try {
            aSocket = new DatagramSocket(districtServerPort);
            byte[] buffer = new byte[Constants.PACKET_SIZE];

            request = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(request);
            aSocket.close();
            return new String(request.getData()).trim().getBytes();

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
	 */
	public void send(String returnCode){
		try {
            DatagramSocket aSocket = new DatagramSocket();
            byte[] buffer = (returnCode).getBytes();

            DatagramPacket response = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
            aSocket.send(response);
            aSocket.close();


		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
	}
	
	/**
	 * Sends the result of the previous request back to a specified port/address.
	 * @param returnCode the result of the most recent request
	 */
	public void send(String returnCode, InetAddress address, int port){
		try {
			try {
				DatagramSocket aSocket = new DatagramSocket();
				byte[] buffer = (returnCode).getBytes();
				
				System.out.println(address + " "  + port);
				DatagramPacket response = new DatagramPacket(buffer, buffer.length, address, port);
				aSocket.send(response);
				aSocket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			}

		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
	}

	public DatagramPacket getRequest() {
		return request;
	}

    public int getPort() {
        if (aSocket == null) return -1;
        return aSocket.getLocalPort();
    }
}