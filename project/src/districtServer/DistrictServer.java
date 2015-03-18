package districtServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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

		if(isValidDistrictName(commandLineArguments[0])){this.districtName = commandLineArguments[0];}	
		else{System.out.println("Invalid District Name"); System.exit(1);}
		
		voters = new ArrayList<Voter>();
		candidates = new ArrayList<Candidate>();
		
		

	}
	
	private void buildVoterList(){
		String csvFile = System.getProperty("user.dir") + "\\voters.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String firstName, lastName, socialInsuranceNumber, district;
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] voterInfo = line.split(cvsSplitBy);
				firstName = voterInfo[0].trim();
				lastName = voterInfo[1].trim();
				socialInsuranceNumber = voterInfo[2].trim();
				district = voterInfo[3].trim();
				voters.add(new Voter(firstName, lastName, socialInsuranceNumber, district));
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
	}
	
	private ArrayList<Voter> buildCandidateList(){
		return null;
	}

	/**
	 * @param districtName string used to check is districtName is valid
	 * @return true if the given distractName is equal to a real Canadian district name
	 */
	private boolean isValidDistrictName(String districtName){
		//TODO this should consult 
		String csvFile = System.getProperty("user.dir") + "\\canadianElectoralDistricts.csv";
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				line = line.trim();
				System.out.println(line);
				if(line.equals(districtName))
					return true;

				try{}
				catch(ArrayIndexOutOfBoundsException e){

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

		return false;
	}
}
