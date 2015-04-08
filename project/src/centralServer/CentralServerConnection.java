package centralServer;

// TCP using UDP: Server
import constants.Constants;

import java.net.*;
import java.io.*;

public class CentralServerConnection {
	
	DatagramSocket centralServerSocket = null;
    
	public CentralServerConnection(int centralServerPort){
		System.out.println("CentralServerConnection initialized");
		try {
			centralServerSocket = new DatagramSocket(centralServerPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Handles the receiving of packets from DistrictServers
     * @return The data in byte representation which can be parsed
     */
	public byte[] receiveCandidateVotes() {
		try {

			byte[] buffer = new byte[Constants.PACKET_SIZE];
			
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			
			centralServerSocket.setSoTimeout(30000);
			try {
				centralServerSocket.receive(request);
		    } catch (SocketTimeoutException e) {
		       // election must be done
		       return null;
		    }     
			
			String r = new String(request.getData()).trim();
			return r.getBytes();
		} 
		catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
			return null;
		} 
	}
}
