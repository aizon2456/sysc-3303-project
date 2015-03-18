package districtServer;

public class Candidate extends Person{

	private int numVotes;

	public Candidate (String firstName, String lastName, String socialInsuranceNumber){
		super(firstName, lastName, socialInsuranceNumber);
		numVotes = 0;
	}
	
	public int getNumVotes() {
		return numVotes;
	}
	
	public void setNumVotes(int numVotes){
		this.numVotes = numVotes;
	}
	
	public void incrementNumVotes(){
		this.numVotes ++;
	}
	
	public boolean equals (Object obj){
		Candidate candidate = null;
		if(obj instanceof Candidate){
			candidate = (Candidate)obj;
		}else return false;

		if(!(this.socialInsuranceNumber.equals(candidate.getSocialInsuranceNumber())))
			return false;
		
		return true;
	}
}
