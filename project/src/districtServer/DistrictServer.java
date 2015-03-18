package districtServer;
import java.util.ArrayList;


/**
 * 
 * @author Nikola Neskovic (100858043), Ian Wong, Kevin Rosengren (100848909), Jonathon Penny
 * @since March 17th 2015
 */
public class DistrictServer {

	private String districtName;
	private ArrayList<Voter> voters;
	private ArrayList<Candidate> candidates;
	private DistrictServerConnection districtServerConnection;

	public DistrictServer(String[] commandLineArguments){

		FileInfoReader fileInfoReader = new FileInfoReader();
		
		if(fileInfoReader.isValidDistrictName(commandLineArguments[0])){this.districtName = commandLineArguments[0];}	
		else{System.out.println("Invalid District Name"); System.exit(1);}
		voters = fileInfoReader.buildVoterList(districtName);
		candidates = fileInfoReader.buildCandidateList(districtName);
		
		System.out.println("Number of voters: " + voters.size());
		System.out.println("Number of candidates " + candidates.size());
		
		//districtServerConnection = new districtServerConnection();

	}
	
}
