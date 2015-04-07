package pollingStation;

// TCP using UDP: Client

import constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PollingStationConnection {

	private DatagramSocket socket;
    private int districtServerPort;
    private String districtServerAddress;

	public PollingStationConnection(String districtServerAddress, int districtServerPort) {
        this.districtServerAddress = districtServerAddress;
        this.districtServerPort = districtServerPort;

		System.out.println("Starting Polling Station...");
		socket = null;
        openSocket();
	}

    public void openSocket() {

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Socket Error: " + e.getMessage());
            System.exit(1);
        }
    }


    public void closeSocket() {
        if (socket != null)
            socket.close();
    }
	
    /**
     * This method takes the portion of the string to send to the server
     * and packages it into the packet. It then sends the packet to the
     * Impairment Server.
     * @param message: message to be sent
     */
	public String sendMessage(String message) {

		byte[] outPacket;
		byte[] data = message.getBytes();

        try {
            InetAddress destinationAddress 	= InetAddress.getByName(districtServerAddress);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(data);
            outPacket = byteStream.toByteArray();
            DatagramPacket request = new DatagramPacket(outPacket, outPacket.length,
                                            destinationAddress, districtServerPort);
            socket.setSoTimeout(5000);
            socket.send(request);
            byte[] buffer = new byte[Constants.PACKET_SIZE];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);

            return new String(reply.getData());

        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }

        return "" + Constants.packetType.NO_RESPONSE;
	}
}