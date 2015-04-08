package districtServer;

import constants.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DistrictServerConnection {

    private DatagramPacket request;
    private DatagramSocket pollingStationSocket;
    private DatagramSocket centralServerSocket;

    public DistrictServerConnection(int districtServerPort) {
        try {
            pollingStationSocket = new DatagramSocket(districtServerPort);
            centralServerSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Socket Error: " + e.getMessage());
        }
    }

    /**
     * This function begins the main server listening loop, it takes messages
     * it receives, corrupts them a little then forwards it to the main server.
     * The response from the main server is forwarded to the client
     * @return data in packet
     */
    public byte[] beginListening(){
        try {
            byte[] buffer = new byte[Constants.PACKET_SIZE];

            request = new DatagramPacket(buffer, buffer.length);
            pollingStationSocket.receive(request);
            return new String(request.getData()).trim().getBytes();

        } catch (SocketException e) {
            System.out.println("Socket Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
        return null;
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
}