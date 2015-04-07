package centralServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import constants.Constants;

public class CentralServer extends Observable implements Runnable{

	private Map<String,Map<String,Integer>> votesMap; // <District, <Candidate, Vote>>
	private int centralServerPort;
	private CentralServerConnection connection;

	public CentralServer(int centralServerPort){
		this.centralServerPort = centralServerPort;
		//initialize the voting maps
		votesMap = new HashMap<String,Map<String,Integer>>();
		CentralServerConnection connection = new CentralServerConnection();
		connection.initializeCentralServerPort(centralServerPort);
	}
	
	public CentralServer(){
		//initialize the voting maps
		votesMap = new HashMap<String,Map<String,Integer>>();
		CentralServerConnection connection = new CentralServerConnection();
		connection.initializeCentralServerPort();
	}

	public String[] setElectionResults(byte[] candidateInfo) {
		String candidateInfoString = new String(candidateInfo);
		String[] info = candidateInfoString.split(Constants.PACKET_DELIMITER);

		String districtName = info[0];
		System.out.println("SET: " + Arrays.toString(info) + " " + districtName);
		for (int i = 1; i < info.length; i+=2) {
			String name = info[i];
			int voteCount = Integer.parseInt(info[i+1]);

			Map<String,Integer> temp;
			if (votesMap.get(districtName) != null)
				temp = votesMap.get(districtName);
			else
				temp = new HashMap<String, Integer>();

			temp.put(name, voteCount);
			votesMap.put(districtName, temp);
		}
		
		this.setChanged();
		this.notifyObservers(votesMap.keySet().toArray(new String[0]));
		return votesMap.keySet().toArray(new String[0]);
	}

	public Map<String,Map<String,Integer>> districtRequest(String district) {
		Map<String,Integer> candidateMap = votesMap.get(district);
		Map<String,Map<String,Integer>> responseMap = new HashMap<>();
		responseMap.put(district, candidateMap);
		this.setChanged();
		this.notifyObservers(responseMap);
		return responseMap;
	}

	public void setCentralServerView(CentralServerView centralServerView){
		this.addObserver(centralServerView);
	}

	@Override
	public void run() {
		runServerRun();
	}

	public void runServerRun(){
		
		// handle the polling of results until null is received
		while(true) {
			byte[] electionResults = null;
			if (centralServerPort != 0){
				System.out.println("FUCK"+centralServerPort);
				electionResults = connection.receiveCandidateVotes(centralServerPort);
			}
			if (electionResults == null) {
				// Election is over (or assumed so after X time where no new packets have been sent).
				break;
			}
			setElectionResults(electionResults);
		}
	}
}