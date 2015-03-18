package tests;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MockServer extends Thread {

    private int port;
    private int packetSize;

    public MockServer(int port, int packetSize) {
        this.port = port;
        this.packetSize = packetSize;
    }

    @Override
    public void run(){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(port);
            byte[] buffer = new byte[packetSize];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            aSocket.setSoTimeout(2000);
            aSocket.receive(request);
            byte[] response = request.getData();
            DatagramPacket reply = new DatagramPacket(response, response.length,
                                        request.getAddress(), request.getPort());
            aSocket.send(reply);
        } catch (Exception ignored) {

        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }
}
