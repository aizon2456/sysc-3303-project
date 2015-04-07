package centralServer;

public class CentralServerControllerLauncher {
	public static void main(String args[]) {
		// args contains the port number
		int portNumber = 0;
		try{portNumber = Integer.parseInt(args[0]);}
		catch(NumberFormatException e){
			System.out.println("The port number must be an integer value.");
			System.exit(1);
		}
		catch(ArrayIndexOutOfBoundsException e){ // args.length == 0
			System.out.println("Usage: java CentralServer <Port Number>");
			System.exit(1);
		}
		
		if (args.length > 1) {
			System.out.println("Usage: java CentralServer <Port Number>");
			System.exit(1);
		}
		else if (portNumber < 1025 || portNumber > 65535) {
			System.out.println("The port number must be between 1025 and 65535.");
			System.exit(1);
		}
		System.out.println("Launching Central Server Controller.");
		new CentralServerController(portNumber);
	}
	
}
