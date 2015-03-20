package tests;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MockServer extends Thread {

    private int port;
    private int packetSize;
    private String toSend = "";
	private String address = "";
    private DatagramSocket aSocket;

    public MockServer(int port, int packetSize) {
        this.port = port;
        this.packetSize = packetSize;
        openSocket();
    }
    
    public MockServer(int port, int packetSize, String toSend, String address) {
        this.port = port;
        this.packetSize = packetSize;
        this.toSend = toSend;
        this.address = address;
        openSocket();
    }

    public void openSocket() {
        try {
            aSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return aSocket.getLocalPort();
    }

    @Override
    public void run(){
        try {
            if (toSend.length() == 0) {
            	System.out.println("RECEIVE THEN SEND");
	            byte[] buffer = new byte[packetSize];
	            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
//	            aSocket.setSoTimeout(2000);
	            aSocket.receive(request);
	            byte[] response = request.getData();
	            DatagramPacket reply = new DatagramPacket(response, response.length,
                                        request.getAddress(), request.getPort());
	            Thread.sleep(2000);
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
                aSocket.close();
        }
    }
    
    public void setToSend(String toSend) {
		this.toSend = toSend;
	}
}
