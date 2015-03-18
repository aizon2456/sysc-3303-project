package centralServer;

// TCP using UDP: Server
import constants.Constants;

import java.net.*;
import java.io.*;

public class CentralServerConnection {
	static PrintWriter writer;
    public String output = "";
	
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
			
			aSocket.setSoTimeout(Constants.ELECTION_OVER);
			try {
				aSocket.receive(request);
		    } catch (SocketTimeoutException e) {
		       // election must be done
		       return null;
		    }     
			
			returnValue = parseFile(request.getData());
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
	public String parseFile(byte[] response){
		
		String resp 			= new String(response);
		String data             = "";
		
		for (int r = Constants.SEND_SIZE; r < resp.length(); r++) {
            if (r < (Constants.SEND_SIZE + Constants.DATA_SIZE)) { //data
                if ((char)response[r] == Constants.PACKET_END) {
                    r = Constants.SEND_SIZE + Constants.DATA_SIZE - 1;
                }
                else {
                    data += (char)response[r];
                }
            }
            else {
                System.out.println("Invalid character " + (char)response[r] + " at index " + r);
                return "";
            }
		}
		
		return data;
	}
}
