package districtServer;
import java.util.ArrayList;

import constants.Constants;


/**
 * 
 * @author Nikola Neskovic (100858043), Ian Wong, Kevin Rosengren (100848909), Jonathon Penny
 * @since March 17th 2015
 */
public class DistrictServer {

	private String districtName, centralServerIP;
	private int districtServerPort, centralServerPort;
	private ArrayList<Voter> voters;
	private ArrayList<Candidate> candidates;
	private DistrictServerConnection districtServerConnection;

	public DistrictServer(String[] commandLineArguments){

		validateCommandLineArguments(commandLineArguments);
		buildVoterCandidateList(commandLineArguments[0]);
		districtServerConnection = new DistrictServerConnection();
		byte[] packetData;
		
		while(true){
			//TODO based on some criteria this has to send a message to the central server too.
			packetData = districtServerConnection.beginListening(districtServerPort);
			Constants.returnCodes returnCode = parsePacketDataAndPerformCorresspondingAction(packetData);
			districtServerConnection.send(returnCode, districtServerPort);
		}
	}

	/**
	 * Parses the packet data to determine what the user is requesting.
	 * @param packetData
	 * @return The result of the operation based on the contents of the packet. 
	 */
	public Constants.returnCodes parsePacketDataAndPerformCorresspondingAction(byte[] packetData){
		
		String request = new String(packetData);
		String delimiter = Constants.PACKET_DELIMITER;
		String[] packetDataInformation = request.split(delimiter);
		String firstName = null, lastName = null, sin = null, login = null, password = null;
		Constants.returnCodes status = null;
		
		System.out.println("Request: " + request);
		System.out.println("PacketData " + packetDataInformation.toString());
		
		if(packetDataInformation[0].equals(Constants.packetType.REGISTER.name())){
			firstName = packetDataInformation[1];
			lastName = packetDataInformation[2];
			sin = packetDataInformation[3];
			login = packetDataInformation[4];
			password = packetDataInformation[5];
			status = register(firstName, lastName, sin, login, password);
		}else if(packetDataInformation[0].equals(Constants.packetType.LOGIN.name())){
			login = packetDataInformation[1];
			password = packetDataInformation[2];
			status = loginUser(login, password);
		}else if(packetDataInformation[0].equals(Constants.packetType.VOTE.name())){
			login = packetDataInformation[1];
			sin = packetDataInformation[2];
			status = vote(login, sin);
		}
		return status;
	}

	/**
	 * Attempts to register a user based on the registration information provided.
	 * @param firstName
	 * @param lastName
	 * @param sin
	 * @param login
	 * @param password
	 * @return the result of the register operation. Register will fail if the login name already exits, if the 
	 * user is already registered or if the user does not exist.
	 */
	private Constants.returnCodes register(String firstName, String lastName, String sin, String login, String password){
		for(int i = 0; i < voters.size(); i++){
			if(voters.get(i).getLoginName().equals(login))
				return Constants.returnCodes.LOGIN_EXISTS;
		}

		Voter voter = new Voter(firstName, lastName, sin, districtName);
		if(voters.contains(voter)){
			for(int i = 0; i < voters.size(); i++){
				if(voter.equals(voters.get(i))){
					voter = voters.get(i);
					if(voter.getLoginName() != "" || voter.getPassword() != "")
						return Constants.returnCodes.ALREADY_REGISTERED;
					voter.setLoginName(login);
					voter.setPassword(password);
					break;
				}
			}
		}else return Constants.returnCodes.NON_EXISTENT;
		return Constants.returnCodes.SUCCESS;
	}

	/**
	 * logs in a user with the correct login/password combination.
	 * @param login
	 * @param password
	 * @return return the result of the login operation. If the login credential are incorrect the method
	 * will return WRONG_CREDENTIALS, otherwise it will return succes. 
	 */
	private Constants.returnCodes loginUser(String login,String password){
		for(int i = 0; i < voters.size(); i++){
			if(voters.get(i).getLoginName().equals(login) && voters.get(i).getPassword().equals(password))
				return Constants.returnCodes.SUCCESS;
		}
		return Constants.returnCodes.WRONG_CREDENTIALS;
	}

	/**
	 * Adds a vote to a candidate (sin) and set vote to TRUE for the voter.
	 * @param login the login name of the user who is voting.
	 * @param sin The social insurance number of the candidate being voted for.
	 * @return return the result of the vote operation. If a user has already voted the ALREADY_VOTED enum
	 * will be return. Otherwise the SUCCESS enum will be return.
	 */
	private Constants.returnCodes vote(String login, String sin){
		Candidate candidate = null;
		Voter voter = null;
		for(int i = 0; i < candidates.size(); i++){
			if(candidates.get(i).getSocialInsuranceNumber().equals(sin))
				candidate = candidates.get(i);
		}
		for(int i = 0; i < voters.size(); i++){
			if(voters.get(i).getLoginName().equals(login))
				voter = voters.get(i);
		}
		if(voter.hasVoted())
			return Constants.returnCodes.ALREADY_VOTED;
		
		candidate.incrementNumVotes();
		voter.setVoted(true);
		return Constants.returnCodes.SUCCESS;
	}

	/**
	 * Validates all of the command line arguments to ensure they are all valid variables.
	 * @param commandLineArguments The arguments given at runtime for the districtServer
	 */
	private void validateCommandLineArguments(String[] commandLineArguments){
		if(commandLineArguments.length < 3){
			System.out.println("Invalid number of arguments!");
			printUsageInstructions();
			System.exit(1);
		}

		try{districtServerPort = Integer.parseInt(commandLineArguments[1].trim());}
		catch(NumberFormatException e){
			System.out.println("Invalid district port format! " + commandLineArguments[1].trim());
			printUsageInstructions();
			System.exit(1);
		}

		if(districtServerPort <= 1024 || districtServerPort >= 65536){
			System.out.println("Invalid district port number! " + districtServerPort);
			printUsageInstructions();
			System.exit(1);
		}

		try{centralServerPort = Integer.parseInt(commandLineArguments[2].trim());}
		catch(NumberFormatException e){
			System.out.println("Invalid central port format! " + commandLineArguments[2].trim());
			printUsageInstructions();
			System.exit(1);
		}

		if(centralServerPort <= 1024 || centralServerPort >= 65536){
			System.out.println("Invalid central port number! " + centralServerPort);
			printUsageInstructions();
			System.exit(1);
		}

		try{
			centralServerIP = commandLineArguments[3];

			if(!centralServerIP.matches(Constants.IPV4_REGEX)){
				System.out.println("Invalid central server IP address! " + centralServerIP);
				printUsageInstructions();
				System.exit(1);
			}
		}
		catch(IndexOutOfBoundsException e){System.out.println("No central server IP specified");centralServerIP = "127.0.0.1";}
	}

	/**
	 * Based on the correct districtName the voter and candidate lists will be populated based on districts.
	 * i.e. only candidates belong to Burlington district will be stored by the Burlington district server.
	 * The name is specified at the launching of the district server class.
	 * @param districtServerName the String used to gather the correct voter from the file.
	 */
	private void buildVoterCandidateList(String districtServerName){
		FileInfoReader fileInfoReader = new FileInfoReader();
		if(fileInfoReader.isValidDistrictName(districtServerName)){this.districtName = districtServerName;}	
		else{System.out.println("Invalid District Name"); System.exit(1);}

		voters = fileInfoReader.buildVoterList(districtServerName);
		candidates = fileInfoReader.buildCandidateList(districtServerName);

		System.out.println("Number of voters: " + voters.size());
		System.out.println("Number of candidates " + candidates.size());
	}

	private void printUsageInstructions(){
		System.out.println("Usage: \" java DistrictServerLauncher.java <districtName> <districtServerPort >= 1025> <centralServerPort >= 1025> [<centralServerIP>]");
	}

}