package districtServer;

import constants.Constants;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class DistrictServerConnection {

	private DatagramPacket request;
    private DatagramSocket pollingStationSocket;
    private DatagramSocket centralServerSocket;
    
    private Queue<byte[]> packetResponses;
    
    public DistrictServerConnection(int districtServerPort) {
    	try {
			pollingStationSocket = new DatagramSocket(districtServerPort);
			centralServerSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		}
    	packetResponses = new LinkedList<byte[]>();
    }

	/**
	 * Starts the thread to poll for packets
	 */
	public void beginListening(){
		Thread receivePackets = new Thread(new getPackets());
		receivePackets.start();
	}

	/**
	 * Sends the result of the previous request back to the original sender.
	 * @param returnCode the result of the most recent request
	 */
	public void send(String returnCode){
		try {
			if (request == null) {
				throw new IOException("There is no saved packet.");
			}
			
            byte[] outputBytes = returnCode.getBytes();

            DatagramPacket response = new DatagramPacket(outputBytes, outputBytes.length, 
            												request.getAddress(), request.getPort());
            pollingStationSocket.send(response);


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
				byte[] ouputBytes = returnCode.getBytes();
				DatagramPacket response = new DatagramPacket(ouputBytes, ouputBytes.length, address, port);
				pollingStationSocket.send(response);
			} 
			catch (SocketException e) {
				e.printStackTrace();
			}

		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		} 
	}

	/**
	 * Sends a message to the central server according to 
	 * @param output The string to put in the outgoing packet
	 */
	public void updateCentralServer(String output, InetAddress address, int port) {
		try {
			DatagramPacket outPacket = new DatagramPacket(output.getBytes(), output.getBytes().length, address, port);
			centralServerSocket.send(outPacket);
		} 
		catch (IOException e) {
			System.out.println("IO Error: " + e.getMessage());
		}
	}
	
	public DatagramPacket getRequest() {
		return request;
	}

    public int getPort() {
        if (pollingStationSocket == null) return -1;
        return pollingStationSocket.getLocalPort();
    }
    
    /**
     * Gets the packet from queue.
     *
     * @return The packet's data from queue in byte array form
     */
    public byte[] getPacketFromQueue() {
    	byte[] packetData;
    	while(true){
    		synchronized(packetResponses) {
    			if (packetResponses.size() > 0) {
    				packetData = packetResponses.remove();
    				break;
    			}
    		}
    	}
    	return packetData;	
    }
    
    public class getPackets implements Runnable {

        public void run() {
        	try {
				pollingStationSocket.setSoTimeout(20000);
			} catch (SocketException e) {
				System.out.println("Socket Error: " + e.getMessage());
			}
        	while (true) {
	        	try {
	                byte[] buffer = new byte[Constants.PACKET_SIZE];
	
	                request = new DatagramPacket(buffer, buffer.length);
	                pollingStationSocket.receive(request);
	                
	                synchronized(packetResponses) {
	                	packetResponses.add(new String(request.getData()).trim().getBytes());
	                }
	
	    		} catch (SocketException e) {
	    			System.out.println("Socket Error: " + e.getMessage());
	    		} catch (SocketTimeoutException e) {
	    			System.out.println("Socket has timed out. Ending the vote for this district.");
	    			break;
	    		} catch (IOException e) {
	    			System.out.println("IO Error: " + e.getMessage());
	    		}
        	}
        	pollingStationSocket.close();
        }
    }
}