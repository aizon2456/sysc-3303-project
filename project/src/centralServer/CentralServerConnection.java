package centralServer;

// TCP using UDP: Server
import constants.Constants;

import java.net.*;
import java.io.*;

public class CentralServerConnection {
    /**
     * Handles the receiving of packets from DistrictServers
     * @param portNo The port number over which packets are transferred
     * @return The data in string representation which can be parsed
     */
	public String receiveCandidateVotes(int portNo) {
		DatagramSocket aSocket = null;
		String returnValue = "";
		try {
			aSocket = new DatagramSocket(portNo);
			byte[] buffer = new byte[Constants.PACKET_SIZE];
			
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			
			try {
				aSocket.receive(request);
		    } catch (SocketTimeoutException e) {
		       // election must be done
		       return null;
		    }     
			
			returnValue = parsePacket(request.getData());
		} 
		catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} 
		catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} 
		finally {
			if (aSocket != null)
				aSocket.close();
		}
		return returnValue;
	}
	
	/**
	 * This function takes in the data received, parses out the information
	 * then passes it all to the CentralServer.
	 * 
	 * @param response The packet being passed in by the current DistrictServer
	 * @return The data string
	 */
	public String parsePacket(byte[] response){
		
		String resp = new String(response);
		String data = "";
		
		for (int r = 0; r < resp.length(); r++) {
            if ((char)response[r] == Constants.PACKET_END) {
                break;
            }
            else {
                data += (char)response[r];
            }
		}
		
		return data;
	}
}
