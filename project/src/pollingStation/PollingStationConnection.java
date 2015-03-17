package pollingStation;

// TCP using UDP: Client
import java.net.*;
import java.io.*;

public class PollingStationConnection {
	static DatagramSocket aSocket;
	static String arguments[];
		
	public static final int SEND_SIZE 	= 32;
        public static final int PORT_SIZE       = 4;
	public static final int DATA_SIZE 	= 32;
	public static final int COUNT_SIZE	= 4;
	public static final int CHKS_SIZE 	= String.valueOf(DATA_SIZE * 200).length();
	public static final int CHKS_FACTOR     = 2;
	public static final int PACKET_SIZE	= SEND_SIZE + DATA_SIZE + COUNT_SIZE + CHKS_SIZE;
	
	public static void main(String args[]) {
		System.out.println("Starting Client...");
		// args give message contents and server hostname
		arguments =	args;
		aSocket	  = null;
		if (args.length != 3) {
			System.out.println("Usage: java Client <File Name> <Impairment Server Address> <Client Port Number>");
			System.exit(1);
		}
		try {
			aSocket = new DatagramSocket();
		} 
                catch (SocketException e) {
			System.out.println("Socket Error: " + e.getMessage());
                        System.exit(1);
		}

        String write="";
        FileInputStream fileI;
        try {
                fileI = new FileInputStream((String)args[0]);

        int content;
            while ((content = fileI.read()) != -1) {
                write += ((char) content);
                }
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file: "+ (String)args[0]);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Could read from file: "+ (String)args[0]);
            e.printStackTrace();
            System.exit(1);
        }

        sendMessage(write);	
		
		System.out.println("\nEnding Client");
		if (aSocket != null)
			aSocket.close();
	}
	
	/**
         * Creates the packet's data based on the static size of the data
         * section allocated above. It then calls sendPacket to actually 
         * send the message in pieces.
         * @param input the string from the file
         */
	static void sendMessage (String input){
            input += '\0';
            for(int i =0; i * DATA_SIZE < input.length(); i++){
                int top = ((i+1)*DATA_SIZE);
                if(top > input.length()){
                    //pad the data with bytes if required
                    for(int t=input.length(); t<top; t++){
                        input+=" ";
                    }
                    top = input.length();
                }	
                sendPacket(input.substring(i*DATA_SIZE, top), i+1);
            }
	}
	
        /**
         * This method takes the portion of the string to send to the server
         * and packages it into the packet. It then sends the packet to the
         * Impairment Server.
         * @param bytes
         * @param packetNum 
         */
	static void sendPacket(String bytes, int packetNum){
		
		byte[] outPacket 	= new byte[PACKET_SIZE];
		byte[] send             = new byte[SEND_SIZE]; //this address	
		byte[] data		= bytes.getBytes();
		byte[] count		= String.format("%" + COUNT_SIZE + "d", packetNum).getBytes(); //current packet number
		byte[] chks		= new byte[CHKS_SIZE];
		
                //loop until desired response is achieved
                while (true) {
                    try {
                        InetAddress aDest 	= InetAddress.getByName(arguments[1]);
                        InetAddress aSrc  	= InetAddress.getLocalHost();
                        int destPort 		= Integer.valueOf(arguments[2]);

                        byte[] sendAddressBytes = aSrc.getAddress();
                        byte[] sendPortBytes = String.format("%" + PORT_SIZE + "d", destPort).getBytes();
                        String sendPadChars = "";
                        for (int u = 0; u < (SEND_SIZE - new String(sendAddressBytes).length() - new String(sendPortBytes).length() - 1); u++) {
                            sendPadChars += " ";
                        }                   
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        byteStream.write(sendAddressBytes); //address
                        byteStream.write((byte) '\0');
                        byteStream.write(sendPortBytes); //port
                        byteStream.write(sendPadChars.getBytes());
                        send = byteStream.toByteArray();
                        
                        int checksum = 0;
                        for (int r = 0; r < bytes.trim().length(); r++) {
                                checksum += (int)bytes.trim().charAt(r);
                        }
                        chks = String.format("%" + CHKS_SIZE + "d", checksum * CHKS_FACTOR).getBytes();

                        byteStream = new ByteArrayOutputStream();
                        byteStream.write(send);
                        byteStream.write(data);
                        byteStream.write(count);
                        byteStream.write(chks);
                        outPacket = byteStream.toByteArray();

                        DatagramPacket request = new DatagramPacket(outPacket, outPacket.length,
                                        aDest, destPort);
                        aSocket.setSoTimeout(1000);
                        aSocket.send(request);
                        byte[] buffer = new byte[1]; //response is a single byte
                        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                        aSocket.receive(reply);
                        
                        if (new String(reply.getData()).trim().equals("1")) {
                            System.out.println("The Server returned 1, success on packet " + packetNum);
                            break;
                        }
                        System.out.println("The Server returned 0, failure! Resending packet " + packetNum + "...");
                    } 
                    catch (IOException e) {
                            System.out.println("IO Error: " + e.getMessage());
                    }
                }
	}
}