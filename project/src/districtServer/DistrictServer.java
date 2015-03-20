package districtServer;

import constants.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * 
 * @author Nikola Neskovic (100858043), Ian Wong, Kevin Rosengren (100848909), Jonathon Penny
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
		districtServerConnection = new DistrictServerConnection(districtServerPort);
	}

	@Override
	public void run(){
		byte[] packetData;
        //noinspection InfiniteLoopStatement
        while(true){
			//TODO based on some criteria this has to send a message to the central server too.
			LOGGER.info("Listening for requests!");
			packetData = districtServerConnection.beginListening();
			LOGGER.info("Received request: " + new String(packetData));

			Constants.returnCodes returnCode = parsePacketDataAndPerformCorrespondingAction(packetData);
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
            writer.println("Candidates:");
            for(Candidate candidate : candidates){
                writer.println(candidate.toString());
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            if (writer != null)
                writer.close();
        }
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
	 * @param packetData: received data packet
	 * @return The result of the operation based on the contents of the packet. 
	 */
	public Constants.returnCodes parsePacketDataAndPerformCorrespondingAction(byte[] packetData){
		String request = new String(packetData);
		String[] packetDataInformation = request.split(Constants.PACKET_DELIMITER);
		String firstName, lastName, sin, login, password;
		Constants.returnCodes status = null;
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
		return status;
	}

	/**
	 * Attempts to register a user based on the registration information provided.
	 * @param firstName: voter's first name
	 * @param lastName:
	 * @param sin:
	 * @param login:
	 * @param password:
	 * @return the result of the register operation. Register will fail if the login name already exits, if the 
	 * user is already registered or if the user does not exist.
	 */
	private Constants.returnCodes register(String firstName, String lastName, String sin, String login, String password){
        for (Voter voter2 : voters) {
            if (voter2.getLoginName().equals(login))
                return Constants.returnCodes.LOGIN_EXISTS;
        }

		Voter voter = new Voter(firstName, lastName, sin, districtName);

		for(int i = 0; i < voters.size(); i++){
			if(voter.equals(voters.get(i))){
				voter = voters.get(i);
				if(!voter.getLoginName().equals("") || !voter.getPassword().equals("")){
					LOGGER.warning(firstName + " " + lastName + " is already registered.");
					return Constants.returnCodes.ALREADY_REGISTERED;
				}
				voter.setLoginName(login);
				voter.setPassword(password);
				break;
			}else if(i == voters.size() -1){
				LOGGER.warning(voter.toString() + " does not exist.");
				return Constants.returnCodes.NON_EXISTENT;
			}

		}
		
		LOGGER.info(voter.toString() + " has been successfully registered.");
		return Constants.returnCodes.SUCCESS;
	}

	/**
	 * logs in a user with the correct login/password combination.
	 * @param login:
	 * @param password:
	 * @return return the result of the login operation. If the login credential are incorrect the method
	 * will return WRONG_CREDENTIALS, otherwise it will return success.
	 */
	private Constants.returnCodes loginUser(String login,String password){

        for (Voter voter : voters) {
            if (voter.getLoginName().equals(login) && voter.getPassword().equals(password)) {
                LOGGER.info(login + " has successfully logged in.");
                return Constants.returnCodes.SUCCESS;
            }
        }
		LOGGER.warning(login + " has input the wrong credentials.");

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

        for (Candidate candidate1 : candidates) {
            if (candidate1.getSocialInsuranceNumber().equals(sin))
                candidate = candidate1;
        }
        for (Voter voter1 : voters) {
            if (voter1.getLoginName().equals(login))
                voter = voter1;
        }
        assert voter != null;
        if(voter.hasVoted()){
			LOGGER.warning(login + " has already voted.");
			return Constants.returnCodes.ALREADY_VOTED;
		}
        assert candidate != null;
        candidate.incrementNumVotes();
		voter.setVoted(true);

		printCandidateStatusesToFile();
		LOGGER.info("Vote successful! Candidate statuses file updated.");

		return Constants.returnCodes.SUCCESS;
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