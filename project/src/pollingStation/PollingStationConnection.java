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

	private DatagramSocket aSocket;
    private int districtServerPort;
    private String districtServerAddress;

	public PollingStationConnection(String districtServerAddress, int districtServerPort) {
        this.districtServerAddress = districtServerAddress;
        this.districtServerPort = districtServerPort;

		System.out.println("Starting Polling Station...");
		aSocket	= null;

		try {
			aSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
            System.exit(1);
		}

	}

    public void closeSocket() {
        if (aSocket != null)
            aSocket.close();
    }
	
    /**
     * This method takes the portion of the string to send to the server
     * and packages it into the packet. It then sends the packet to the
     * Impairment Server.
     * @param bytes: message to be sent
     */
	public String sendMessage(String bytes) {

		byte[] outPacket;
		byte[] send; //this address
		byte[] data = bytes.getBytes();

        try {
            InetAddress destinationAddress 	= InetAddress.getByName(districtServerAddress);
            InetAddress source = InetAddress.getLocalHost();

            byte[] sendAddressBytes = source.getAddress();
            byte[] sendPortBytes = String.format("%" + Constants.PORT_SIZE + "d", districtServerPort).getBytes();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(sendAddressBytes); //address
            byteStream.write((byte) '\0');
            byteStream.write(sendPortBytes); //port
            send = byteStream.toByteArray();

            byteStream = new ByteArrayOutputStream();
            byteStream.write(send);
            byteStream.write(data);
            outPacket = byteStream.toByteArray();
            DatagramPacket request = new DatagramPacket(outPacket, outPacket.length,
                                            destinationAddress, districtServerPort);
            aSocket.setSoTimeout(1000);
            aSocket.send(request);
            byte[] buffer = new byte[Constants.DATA_SIZE];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);

            return new String(reply.getData());

        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }

        return "" + Constants.packetType.NO_RESPONSE;
	}
}