package pollingStation;

import constants.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class PollingStation extends Observable {

    private String delimiter = String.valueOf(Constants.PACKET_DELIMITER);
    private PollingStationConnection stationConnection;
    private String currentLogin;
    private Boolean testActive; // suppress GUI actions

    public PollingStation(String districtServerAddress, int districtServerPort) {
        stationConnection = new PollingStationConnection(districtServerAddress, districtServerPort);
        testActive = false;
    }

    public void setLogin(String login) {
        currentLogin = login;
    }

    /**
     * Register a voter
     * @param firstName: voter's first name
     * @param lastName: voter's last name
     * @param sin: voter's Social Insurance Number
     * @param login: voter's login name
     * @param password: voter's password
     */
    public String register(String firstName, String lastName, String sin, String login, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.REGISTER + delimiter + firstName + delimiter
                + lastName + delimiter + sin + delimiter + login + delimiter + password);
        if (!testActive)
        	updateObservers(parseResponse(response));
        return response;
    }

    /**
     * voter username
     * @param username: username name
     * @param password: voter's password
     */
    public String login(String username, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.LOGIN + delimiter + username + delimiter
                + password);

        String[] packetDataInformation = parseResponse(response);

        if (packetDataInformation[0].contains(Constants.returnCodes.WRONG_CREDENTIALS.name())) {
            currentLogin = "";
        } else if (packetDataInformation[0].contains(Constants.returnCodes.LOGIN_SUCCESS.name())) {
            currentLogin = username;
        }

        if (!testActive)
        	updateObservers(packetDataInformation);
        return response;
    }

    /**
     * register voteFor
     */
    public String voteFor(String candidateSIN) {
        String response = stationConnection.sendMessage(Constants.packetType.VOTE + delimiter + currentLogin + delimiter
                + candidateSIN);
        if (!testActive)
        	updateObservers(parseResponse(response));
        return response;
    }

    private String[] parseResponse(String response) {
        return response.trim().split(Constants.PACKET_DELIMITER);
    }

    
    public void testParse() {
		String csvFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "testcases.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String firstName, lastName, socialInsuranceNumber, login;
		String endString = "";
		int index = 1;
		final String testpass = "testpass";
		final String success = "success!\n";
		final String failure = "failure.\n";
	 
		testActive = true;
		
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] voterInfo = line.split(cvsSplitBy);
				firstName = voterInfo[0].trim();
				lastName = voterInfo[1].trim();
				socialInsuranceNumber = voterInfo[2].trim();
				login = ((int)(Math.random() * 9 + 1) == 100) ? "login" + (index - 1) : "login" + index;
				// if register is set to true, try to register
				if (Boolean.valueOf(voterInfo[4])) {
					String result = register(firstName, lastName, socialInsuranceNumber, login, testpass);
					endString += "Registration for " + firstName + " " + lastName + " was a ";
					if (result.contains(Constants.returnCodes.REG_SUCCESS.name()))
						endString += success;
					else
						endString += failure;
				}
				// if vote is set to true, try to login and then vote
				if (Boolean.valueOf(voterInfo[5])) {
					String result = login(login, testpass);
					endString += "Login for " + firstName + " " + lastName + " with credentials " 
								+ login + "/" + testpass + " was a ";
					if (result.contains(Constants.returnCodes.LOGIN_SUCCESS.name())) { 
						endString += success;
						
						//login succeeded, now a vote can be sent
						
						String[] resultArray = parseResponse(result);
						ArrayList<String> listOfSINs = new ArrayList<String>();
						for (int i = 2; i < resultArray.length; i += 2) { // start after returnCode
							listOfSINs.add(resultArray[i]);
						}
						
						String voteSIN = listOfSINs.get((int)(Math.random() * (listOfSINs.size() - 1)));
						result = voteFor(voteSIN);
						
						endString += "Vote sent in by " + firstName + " " + lastName 
									+ " for candidate " + voteSIN + " was a ";
						if (result.contains(Constants.returnCodes.VOTE_SUCCESS.name()))
							endString += success;
						else
							endString += failure;
					}
					else {
						endString += failure;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String[] returnArray = {Constants.TEST_COMPLETE, endString};
		updateObservers(returnArray);
		
		testActive = false;
    }

    private void updateObservers(String[] response) {
        setChanged();
        notifyObservers(response);
    }
}
