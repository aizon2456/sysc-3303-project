package centralServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import constants.Constants;

public class CentralServer extends Observable{
	
	private Map<String,Map<String,Integer>> votesMap; // <District, <Candidate, Vote>>
	
	public CentralServer(int centralServerPort){
		
		//initialize the voting maps
		votesMap = new HashMap<String,Map<String,Integer>>();
		
		CentralServerConnection connection = new CentralServerConnection();
		connection.initializeCentralServerPort(centralServerPort);
		
		// handle the polling of results until null is received
		while(true) {
			byte[] electionResults = connection.receiveCandidateVotes(centralServerPort);
			
			if (electionResults == null) {
				// Election is over (or assumed so after X time where no new packets have been sent).
				break;
			}
			
			setElectionResults(electionResults);
		}
	}
	
	public void setElectionResults(byte[] candidateInfo) {
		String[] info = (new String(candidateInfo)).split(Constants.PACKET_DELIMITER);
		
		String districtName = info[0];

		for (int i = 1; i < info.length; i+=2) {
			String name = info[i];
			int voteCount = Integer.parseInt(info[i+1]);
			
			Map<String,Integer> temp;
			if (votesMap.get(districtName) != null)
				temp = votesMap.get(districtName);
			else
				temp = new HashMap<>();
				
			temp.put(name, voteCount);
			votesMap.put(districtName, temp);
		}
		
		this.setChanged();
		this.notifyObservers((String[])votesMap.keySet().toArray());
	}
	
	public void districtRequest(String district) {
		this.setChanged();
		Map<String,Integer> candidateMap = votesMap.get(district);
		Map<String,Map<String,Integer>> responseMap = new HashMap<>();
		responseMap.put(district, candidateMap);
		this.notifyObservers(responseMap);
	}
	
	public void setCentralServerView(CentralServerView centralServerView){
		this.addObserver(centralServerView);
	}
	
	public void updateCentralServerView(Object updateObject){
		this.setChanged();
		this.notifyObservers(updateObject);
	}
}