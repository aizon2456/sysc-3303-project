package tests;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MockServer extends Thread {

    private int port;
    private int packetSize;
    private String toSend = "";
    private String address = "";

    public MockServer(int port, int packetSize) {
        this.port = port;
        this.packetSize = packetSize;
    }
    
    public MockServer(int port, int packetSize, String toSend, String address) {
        this.port = port;
        this.packetSize = packetSize;
        this.toSend = toSend;
        this.address = address;
    }

    @Override
    public void run(){
        DatagramSocket aSocket = null;
        try {
            if (toSend.length() == 0) {
            	System.out.println("RECEIVE THEN SEND");
	        	aSocket = new DatagramSocket(port);
	            byte[] buffer = new byte[packetSize];
	            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
	            aSocket.setSoTimeout(2000);
	            aSocket.receive(request);
	            byte[] response = request.getData();
	            DatagramPacket reply = new DatagramPacket(response, response.length,
                                        request.getAddress(), request.getPort());
	            aSocket.send(reply);
            }
            else {
            	System.out.println("SEND ONLY");
            	aSocket = new DatagramSocket();
            	byte[] message = toSend.getBytes();
	            DatagramPacket testPacket = new DatagramPacket(message, message.length,
	            						InetAddress.getByName(address), port);
	            aSocket.send(testPacket);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (aSocket != null)
            	System.out.println("HERE");
                aSocket.close();
        }
    }
}
