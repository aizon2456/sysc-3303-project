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
}
