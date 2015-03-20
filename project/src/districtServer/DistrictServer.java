package districtServer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import constants.Constants;


/**
 * 
 * @author Nikola Neskovic (100858043), Ian Wong, Kevin Rosengren (100848909), Jonathan Penny (100884228)
 * @since March 17th 2015
 */
public class DistrictServer extends Thread{

	private String districtName, centralServerIP;
	private int districtServerPort, centralServerPort;
	private ArrayList<Voter> voters;
	private ArrayList<Candidate> candidates;
	private DistrictServerConnection districtServerConnection;
	private final static Logger LOGGER = Logger.getLogger(DistrictServer.class.getName()); 

	public DistrictServer(String districtName, int districtServerPort, int centralServerPort, String centralServerIP){

		this.districtName = districtName;
		this.districtServerPort = districtServerPort;
		this.centralServerPort = centralServerPort;
		this.centralServerIP = centralServerIP;

		setUpFileLogger(districtName);
		buildVoterCandidateList(districtName);
		districtServerConnection = new DistrictServerConnection();
	}

	@Override
	public void run(){
		byte[] packetData;
		while(true){
			//TODO based on some criteria this has to send a message to the central server too.
			LOGGER.info("Listening for requests!");
			packetData = districtServerConnection.beginListening(districtServerPort);
			LOGGER.info("Received request: " + new String(packetData));

			String returnCode = parsePacketDataAndPerformCorresspondingAction(packetData);
			LOGGER.info("Result of packet: " + returnCode);

			districtServerConnection.send(returnCode);
			int port = districtServerConnection.getRequest().getPort();
			LOGGER.info("Message sent to port: " + port + " at address: " + districtServerConnection.getRequest().getAddress());

		}
		//districtServerConnection.kill();
	}

	private void printCandidateStatusesToFile(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(districtName + "_candidate_statuses.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("Candidates:");
		for(Candidate candidate : candidates){
			writer.println(candidate.toString());
		}
		writer.close();
	}

	private void setUpFileLogger(String logName){
		FileHandler fh;  

		try {  

			// This block configure the logger with handler and formatter  
			String logLocation = System.getProperty("user.dir") + "\\" + logName + ".log";
			System.out.println(logLocation);
			fh = new FileHandler(logLocation);  
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("Logger initialized");  
	}

	/**
	 * Parses the packet data to determine what the user is requesting.
	 * @param packetData
	 * @return The result of the operation based on the contents of the packet. 
	 */
	public String parsePacketDataAndPerformCorresspondingAction(byte[] packetData){
		String request = new String(packetData);
		String delimiter = Constants.PACKET_DELIMITER;
		String[] packetDataInformation = request.split(delimiter);
		String firstName = null, lastName = null, sin = null, login = null, password = null;
		String status = null;
		try{
			if(packetDataInformation[0].equals(Constants.packetType.REGISTER.name())){
				LOGGER.info("REGISTER packet received");
				firstName = packetDataInformation[1];
				lastName = packetDataInformation[2];
				sin = packetDataInformation[3];
				login = packetDataInformation[4];
				password = packetDataInformation[5];
				status = register(firstName, lastName, sin, login, password);
			}else if(packetDataInformation[0].equals(Constants.packetType.LOGIN.name())){
				LOGGER.info("LOGIN packet received");
				login = packetDataInformation[1];
				password = packetDataInformation[2];
				status = loginUser(login, password);
			}else if(packetDataInformation[0].equals(Constants.packetType.VOTE.name())){
				LOGGER.info("VOTE packet received");
				login = packetDataInformation[1];
				sin = packetDataInformation[2];
				status = vote(login, sin);
			}else{
				LOGGER.severe("INCORRECT PACKET FORMAT");
			}
		}catch(ArrayIndexOutOfBoundsException e){
			return Constants.returnCodes.INVALID_NUM_ARGUMENTS.name();
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
	private String register(String firstName, String lastName, String sin, String login, String password){
		for(int i = 0; i < voters.size(); i++){
			if(voters.get(i).getLoginName().equals(login))
				return Constants.returnCodes.LOGIN_EXISTS.name();
		}

		Voter voter = new Voter(firstName, lastName, sin, districtName);
		for(int i = 0; i < voters.size(); i++){
			if(voter.equals(voters.get(i))){
				voter = voters.get(i);
				if(voter.getLoginName() != "" || voter.getPassword() != ""){
					LOGGER.warning(firstName + " " + lastName + " is already registered.");
					return Constants.returnCodes.ALREADY_REGISTERED.name();
				}
				voter.setLoginName(login);
				voter.setPassword(password);
				break;
			}else if(i == voters.size() -1){
				LOGGER.warning(voter.toString() + " does not exist.");
				return Constants.returnCodes.NON_EXISTENT.name();
			}

		}
		
		LOGGER.info(voter.toString() + " has been successfully registered.");
		return Constants.returnCodes.SUCCESS.name();
	}

	/**
	 * logs in a user with the correct login/password combination.
	 * @param login
	 * @param password
	 * @return return the result of the login operation. If the login credential are incorrect the method
	 * will return WRONG_CREDENTIALS, otherwise it will return succes. 
	 */
	private String loginUser(String login,String password){
		for(int i = 0; i < voters.size(); i++){
			if(voters.get(i).getLoginName().equals(login) && voters.get(i).getPassword().equals(password)){
				LOGGER.info(login + " has succesfully logged in.");
				return Constants.returnCodes.SUCCESS.name() + getDelimitedCandidates();
			}
		}
		LOGGER.warning(login + " has input the wrong credentials.");
		return Constants.returnCodes.WRONG_CREDENTIALS.name();
	}
	
	/**
	 * Returns a string for the return send packet
	 * @return The string of candidates delimited by a constant character
	 */
	public String getDelimitedCandidates() {
		String result = "";
		for (int i = 0; i < candidates.size(); i++) {
			result += Constants.PACKET_DELIMITER + candidates.get(i).getFirstName() + 
					" " + candidates.get(i).getLastName() + Constants.PACKET_DELIMITER
					+ candidates.get(i).getSocialInsuranceNumber();
		}
		return result;
	}

	/**
	 * Adds a vote to a candidate (sin) and set vote to TRUE for the voter.
	 * @param login the login name of the user who is voting.
	 * @param sin The social insurance number of the candidate being voted for.
	 * @return return the result of the vote operation. If a user has already voted the ALREADY_VOTED enum
	 * will be return. Otherwise the SUCCESS enum will be return.
	 */
	private String vote(String login, String sin){
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
		if(voter.hasVoted()){
			LOGGER.warning(login + " has already voted.");
			return Constants.returnCodes.ALREADY_VOTED.name();
		}
		candidate.incrementNumVotes();
		voter.setVoted(true);

		printCandidateStatusesToFile();
		LOGGER.info("Vote successful! Candidate statuses file updated.");

		return Constants.returnCodes.SUCCESS.name();
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
		else{System.out.println("Invalid District Name"); System.exit(1); LOGGER.severe("Invalid District Name"); System.exit(1);}
		LOGGER.info("District name is valid: " + districtName);

		voters = fileInfoReader.buildVoterList(districtServerName);
		LOGGER.info("List of voters generated.");
		LOGGER.info("Number of voters: " + voters.size());

		candidates = fileInfoReader.buildCandidateList(districtServerName);
		LOGGER.info("List of candidates generated");
		LOGGER.info("Number of candidates " + candidates.size());
	}

	public ArrayList<Voter> getVoters() {
		return voters;
	}

	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}
}