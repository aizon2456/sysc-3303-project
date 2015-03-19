package districtServer;

import constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DistrictServerConnection {

	private DatagramPacket request;

	public DistrictServerConnection(){}

	/**
	 * This function begins the main server listening loop, it takes messages
	 * it receives, corrupts them a little then forwards it to the main server.
	 * The response from the main server is forwarded to the client
	 * @param districtServerPort which is the Client port
	 * @return data in packet
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
	
	/**
	 * Sends the result of the previous request back to the original sender.
	 * @param returnCode the result of the most recent request
	 * @param districtServerPort the port that the message is being sent from
	 */
	public void send(Constants.returnCodes returnCode, int districtServerPort){
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(districtServerPort);

			byte[] buffer;
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(returnCode.name().getBytes());
            byteStream.write(Constants.PACKET_END);
            buffer = byteStream.toByteArray();

			DatagramPacket response = new DatagramPacket(buffer, buffer.length, request.getAddress(), request.getPort());
			aSocket.send(response);
			
		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
	
	public DatagramPacket getRequest() {
		return request;
	}
}