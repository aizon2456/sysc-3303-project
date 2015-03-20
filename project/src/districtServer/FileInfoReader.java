package districtServer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileInfoReader {
	
	public FileInfoReader(){
		
	}
	
	/**
	 * Builds a list of voter objects by taking the specific voter information from a
	 * voter file of voters that belong to the given district.
	 * @param districtName string used to get certain voter
	 * @return an arrayList of all voters of the district.
	 */
	public ArrayList<Voter> buildVoterList(String districtName){
		ArrayList<Voter> voters = new ArrayList<Voter>();
		String csvFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "voters.csv";
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
				if(district.equals(districtName))
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
		return voters;
	}
	
	/**
	 * Builds a list of candidate objects by taking the specific candidate information from a
	 * candidate file of candidates that belong to the given district.
	 * @param districtName string used to get certain candidates
	 * @return an arrayList of all candidates of the district.
	 */
	public ArrayList<Candidate> buildCandidateList(String districtName){
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		String csvFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "candidates.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String firstName, lastName, socialInsuranceNumber, district;
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] candidateInfo = line.split(cvsSplitBy);
				firstName = candidateInfo[0].trim();
				lastName = candidateInfo[1].trim();
				socialInsuranceNumber = candidateInfo[2].trim();
				district = candidateInfo[3].trim();
				if(district.equals(districtName))
					candidates.add(new Candidate(firstName, lastName, socialInsuranceNumber));
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
		return candidates;
	}

	/**
	 * Compares the given district name to a list of legal district names derived from a file.
	 * @param districtName string used to check is districtName is valid
	 * @return true if the given distractName is equal to a real Canadian district name
	 */
	public boolean isValidDistrictName(String districtName){
		//TODO this should consult 
		String csvFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "canadianElectoralDistricts.csv";
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				line = line.trim();
				if(line.equals(districtName))
					return true;
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
