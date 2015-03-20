package districtServer;

import constants.Constants;

import java.util.logging.Logger;

public class DistrictServerLauncher {
	
	private final static Logger LOGGER = Logger.getLogger(DistrictServerLauncher.class.getName()); 
	
	/** @param args String name describing the district of the server: Ottawa-Carleton, Gatineau
	 */
	public static void main(String[] args) {


        // Instantiate the various values that need to be passed into the DistrictServer
        String districtName = args[0];
        String centralServerIP = "";
        int districtServerPort = 0;
        int centralServerPort = 0;

        if(args.length < 3){
            System.out.println("Invalid number of arguments!");
            LOGGER.severe("Invalid number of arguments!");
            printUsageInstructions();
            System.exit(1);
        }

        try{districtServerPort = Integer.parseInt(args[1].trim());}
        catch(NumberFormatException e){
            System.out.println("Invalid district port format! " + args[1].trim());
            LOGGER.severe("Invalid district port format! " + args[1].trim());
            printUsageInstructions();
            System.exit(1);
        }

        if(districtServerPort <= 1024 || districtServerPort >= 65536){
            System.out.println("Invalid district port number! " + districtServerPort);
            LOGGER.severe("Invalid district port number! " + districtServerPort);
            printUsageInstructions();
            System.exit(1);
        }

        try{centralServerPort = Integer.parseInt(args[2].trim());}
        catch(NumberFormatException e){
            System.out.println("Invalid central port format! " + args[2].trim());
            LOGGER.severe("Invalid central port format! " + args[2].trim());
            printUsageInstructions();
            System.exit(1);
        }

        if(centralServerPort <= 1024 || centralServerPort >= 65536){
            System.out.println("Invalid central port number! " + centralServerPort);
            LOGGER.severe("Invalid central port number! " + centralServerPort);
            printUsageInstructions();
            System.exit(1);
        }

        try{
            centralServerIP = args[3];

            if(!centralServerIP.matches(Constants.IPV4_REGEX)){
                System.out.println("Invalid central server IP address! " + centralServerIP);
                LOGGER.severe("Invalid central server IP address! " + centralServerIP);
                printUsageInstructions();
                System.exit(1);
            }
        }
        catch(IndexOutOfBoundsException e){LOGGER.warning("No central server IP specified");centralServerIP = "127.0.0.1";}

        (new DistrictServer(districtName, districtServerPort, centralServerPort, centralServerIP)).start();

	}

	
	private static void printUsageInstructions(){
		System.out.println("Usage: \" java DistrictServerLauncher.java <districtName> <districtServerPort >= 1025> <centralServerPort >= 1025> [<centralServerIP>]");
	}
}
