package centralServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import constants.Constants;

public class CentralServer extends Observable{
	
	private Map<String,Map<String,Integer>> votesMap;
	
	public CentralServer(int centralServerPort){
		
		//initialize the voting maps
		votesMap = new HashMap<String,Map<String,Integer>>();
		
		CentralServerConnection connection = new CentralServerConnection();
		connection.initializeCentralServerPort(centralServerPort);
		
		// handle the polling of results until null is received
		while(true) {
			String electionResults = connection.receiveCandidateVotes(centralServerPort);
			
			if (electionResults == null) {
				// Election is over (or assumed so after X time where no new packets have been sent).
				break;
			}
			
			// if the election isn't done, parse through the return and set the Map with the appropriate values
			updateVotes(electionResults);
		}
	}
	
	public void setCentralServerView(CentralServerView centralServerView){
		this.addObserver(centralServerView);
	}
	
	public void updateCentralServerView(Object updateObject){
		this.setChanged();
		this.notifyObservers(updateObject);
	}
	
	/**
	 * Updates the votesMap object with the correct number of votes
	 * @param input The string directly pulled from the data packet
	 */
	private void updateVotes(String input) {
		//parse the data packet for relevant informations
		String districtName, firstName, lastName = "";
		int voteCount = 0;
		String[] result = input.split(Constants.PACKET_DELIMITER);
		Map<String,Integer> candidatesMap = new HashMap<String,Integer>();
		
		if (Constants.packetType.UPDATE.name().equals(result[0]) && result.length == 5) {
			// proper operation, now parse remaining parts
			districtName = result[1];
			firstName = result[2];
			lastName = result[3];
			voteCount = Integer.parseInt(result[4]);
			
			// if map exists for current district, get it, otherwise make new Map
			if (votesMap.containsKey(districtName)) {
				candidatesMap = votesMap.get(districtName);
			}
			else {
				candidatesMap = new HashMap<String,Integer>();
			}
			
			//put the updated map back/newly into the main map
			candidatesMap.put((firstName + " " + lastName), voteCount);
			votesMap.put(districtName, candidatesMap);
		}
	}
}