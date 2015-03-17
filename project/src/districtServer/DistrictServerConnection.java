package districtServer;

// TCP using UDP: Impairment Server
import java.net.*;
import java.io.*;

public class DistrictServerConnection {
    
	public static final int SEND_SIZE 	= 32;
        public static final int PORT_SIZE       = 4;
	public static final int DATA_SIZE 	= 32;
	public static final int COUNT_SIZE	= 4;
	public static final int CHKS_SIZE 	= String.valueOf(DATA_SIZE * 200).length();
	public static final int CHKS_FACTOR     = 2;
	public static final int PACKET_SIZE	= SEND_SIZE + DATA_SIZE + COUNT_SIZE + CHKS_SIZE;
	
	public static void main(String args[]) {
            System.out.println("Starting Impariment Server");
            if (args.length < 3) {
                    System.out.println("Usage: java UDP Impariment Server <Client Port Number> <Server Port Number> <Server Address>");
                    System.out.println("... Server Exiting");
                    System.exit(1);
            }
            System.out.println("Starting Impariment Listening on port: "+ args[0]);
            System.out.println("Forwarding to server at "+ args[2]+" on port: "+ args[1]);

            beginListen(Integer.valueOf(args[0]),args[1],args[2]);
	}
	/**
	 * This function begins the main server listening loop, it takes messages
	 * it receives, corrupts them a little then forwards it to the main server.
	 * The response from the main server is forwarded to the client
	 * @param port which is the Client port
	 * @param fPort which is the Main Server's port
	 * @param fAddr which is the Main Server's host IP Address
	 * @return
	 */
	public static void beginListen(int port, String fPort, String fAddr){
            DatagramSocket aSocket = null;
            try {
                aSocket = new DatagramSocket(port);
                byte[] buffer = new byte[PACKET_SIZE];
                while (true) {
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                    aSocket.receive(request);

                    // dropping packets
                    if(Math.random()< 0.96){
                        String resp  = scrambleAndSendMessage(request.getData(),fAddr,fPort);

                        DatagramPacket reply = new DatagramPacket(resp.getBytes(),
                                        resp.length(), request.getAddress(),
                                        request.getPort());
                        aSocket.send(reply);
                    }
                }
            } catch (SocketException e) {
                System.out.println("Socket Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            } finally {
                if (aSocket != null)
                    aSocket.close();
            }
	}
	/**
	 * This function takes the message sent from the client, corrupts the data
	 * Then send it to the main server and returns the server's response
	 * @param message		-> Message from Client
	 * @param destination 	-> Real Server Address
	 * @param port 			-> Real Server address
	 * @return				-> Response from Real Server
	 */
	public static String scrambleAndSendMessage(byte[] message, String destination, String port){
		String res="";
                DatagramSocket bSocket = null;
		try {
                    int serverPort = Integer.valueOf(port);
                    bSocket = new DatagramSocket();
                    byte[] m = scramble(message);
                    InetAddress aHost = InetAddress.getByName(destination);
                    DatagramPacket request = new DatagramPacket(m, m.length,
                                    aHost, serverPort);

                    bSocket.send(request);

                    byte[] buffer = new byte[PACKET_SIZE];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    bSocket.receive(reply); //reply from actual server		
                    res = new String(reply.getData());
		} catch (SocketException e) {
                    System.out.println("Socket Error: " + e.getMessage());
		} catch (IOException e) {
                    System.out.println("IO Error: " + e.getMessage());
		} finally {
                    if (bSocket != null)
                        bSocket.close();
		}
		return (res);
	}
        
        /**
         * Randomly scrambles the data portion of a packet with incorrect data
         * @param message
         * @return 
         */
	public static byte[] scramble(byte[] message){
                // only mess up the data bits
		for(int i = SEND_SIZE; i < (SEND_SIZE + DATA_SIZE); i++){
                    if(Math.random()>0.95){
                        message[i]+= (int)(Math.random()*15)-7;
                    }	
		}	
		DatagramPacket reply = new DatagramPacket(message, message.length);
		return reply.getData();
	}

}