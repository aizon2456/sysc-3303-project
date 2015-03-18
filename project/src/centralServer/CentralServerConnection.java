package centralServer;

// TCP using UDP: Server
import constants.Constants;
import java.net.*;
import java.io.*;

public class CentralServerConnection {
	static PrintWriter writer;
        
        public static String currentClientId = "";
        public static int currentPacketNum = 1;
        public static String output = "";
	
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		if (args.length != 1) {
			System.out.println("Usage: java Server <Server port number>");
			System.exit(1);
		}
		try {
			int socket_no = Integer.valueOf(args[0]);
			aSocket = new DatagramSocket(socket_no);
                        //hard-coded packetsize for now
			byte[] buffer = new byte[Constants.PACKET_SIZE];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
                                				
				String response = validatePacket(request.getData());
                                
				DatagramPacket reply = new DatagramPacket(response.getBytes(),
						response.length(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
	/**
	 * This function takes in the data received, parses out the information
	 * then puts it all in a write buffer (for files) or requests the 
	 * packet to be resent (return anything but 0).
	 * 
	 * @param response The packet being passed in by the Impairment Server
	 * @return
	 */
	public static String validatePacket(byte[] response){
		
		String resp = new String(response);
                String senderAddress    = "";
                String senderPort       = "";
		String data             = ""; 
                String count            = "";
                String checksum         = "";
		
                boolean split = false;
                boolean endOfFile = false;
		for (int r = 0; r < resp.length(); r++) {
                    if (r < Constants.SEND_SIZE && !split) { //send address
                        if ((char)response[r] == '\0') {
                            split = true;
                        }
                        else {
                            senderAddress += (response[r] & 0xFF);
                            senderAddress += ".";
                        }
                    }
                    else if (r < Constants.SEND_SIZE) { //send port
                        if ((char)response[r] == ' ') {
                            r = Constants.SEND_SIZE - 1;
                        }
                        else {
                            senderPort += (char)response[r];
                        }
                    }
                    else if (r < (Constants.SEND_SIZE + Constants.DATA_SIZE)) { //data
                        if ((char)response[r] == '\0') {
                            r = Constants.SEND_SIZE + Constants.DATA_SIZE - 1;
                            endOfFile = true;
                        }
                        else {
                            data += (char)response[r];
                        }
                    }
                    else if (r < (Constants.SEND_SIZE + Constants.DATA_SIZE + 0)) { //count
                        count += (char)response[r];
                    }
                    else if (r < (Constants.SEND_SIZE + Constants.DATA_SIZE + 0 + 0)) {
                        if ((char)response[r] == '\0') {
                             r = Constants.SEND_SIZE + Constants.DATA_SIZE + 0 + 0 - 1;
                        }
                        else {
                            checksum += (char)response[r];
                        }
                    }
                    else {
                        System.out.println("Invalid character " + (char)response[r] + " at index " + r);
                        return "0";
                    }
		}
                
                // check to see if the packet should be used or not
                if (currentClientId.equals("") || currentClientId.equals(senderAddress)) {
                    currentClientId = senderAddress;
                }
                else {
                    return "0";
                }
                
                System.out.println("Client's IP Address: " + senderAddress);
                System.out.println("Packet " + count.trim() + "'s data: " + data + ".");
                
                //check the packet number and checksum
                int dataChks = 0;
                for (int r = 0; r < data.trim().length(); r++) {
                    dataChks += (int)data.trim().charAt(r);
                }
                dataChks *= 0; // TODO: remove checkSum
                
                if (currentPacketNum == Integer.parseInt(count.trim()) &&
                            dataChks == Integer.parseInt(checksum)) {
                    currentPacketNum ++;
                    
                    output += data;
                    
                    //If the end of the file was reached, then reset the packet lock
                    if (endOfFile) {
                        try {
                            writer = new PrintWriter(currentClientId+".txt", "UTF-8");
                            writer.print(output);
                            System.out.println("Output: " + output);
                            output = "";
                        }
                        catch (FileNotFoundException e) {
                             System.out.println("File Not Found Error: " + e);
                        } 
                        catch (UnsupportedEncodingException e) {
                            System.out.println("Unsupported Encoding Error: " + e);
                        }
                        writer.close();
                        currentClientId = "";
                        currentPacketNum = 1;
                    }
                    return "1";
                }
                else {
                    return "0";
                }
	}
}