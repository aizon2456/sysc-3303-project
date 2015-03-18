package constants;

// TCP using UDP: Server
import java.net.*;
import java.io.*;

public class Constants {
	public static final int SEND_SIZE 	= 32;
	public static final int PORT_SIZE       = 4;
	public static final int DATA_SIZE 	= 32;
	public static final int COUNT_SIZE	= 4;
	public static final int CHKS_SIZE 	= String.valueOf(DATA_SIZE * 200).length();
	public static final int CHKS_FACTOR     = 2;
	public static final int PACKET_SIZE	= SEND_SIZE + DATA_SIZE + COUNT_SIZE + CHKS_SIZE;

	public static String currentClientId = "";
	public static int currentPacketNum = 1;
	public static String output = "";

	public static void main(String args[]) {

	}
}