package centralServer;

// TCP using UDP: Server
import constants.Constants;

import java.net.*;
import java.io.*;

public class CentralServerConnection {
	
	DatagramSocket centralServerSocket = null;
    
	public CentralServerConnection(){System.out.println("CentralServerConnection initialized");}
	
	public boolean initializeCentralServerPort(int centralServerPort){
		try {
			centralServerSocket = new DatagramSocket(centralServerPort);
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean initializeCentralServerPort(){
		try {
			centralServerSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
     * Handles the receiving of packets from DistrictServers
     * @param portNo The port number over which packets are transferred
     * @return The data in byte representation which can be parsed
     */
	public byte[] receiveCandidateVotes(int portNo) {
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
	
	public boolean closeCentralServerSocket(){
		centralServerSocket.close();
		return true;
	}
}
