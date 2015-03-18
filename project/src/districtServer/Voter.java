package districtServer;

public class Voter extends Person {
	
	private String district, loginName, password;
	private boolean registered, voted;

	public Voter(String firstName, String lastName, String socialInsuranceNumber, String district){
		super(firstName, lastName, socialInsuranceNumber);
		this.district = district;
        voted = false;
        registered = false;
	}
	
	public boolean vote(){
		return false;
	}
	
	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}


	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean hasVoted() {
		return voted;
	}

	public void setVoted(boolean voted) {
		this.voted = voted;
	}
	
	@Override
	public boolean equals(Object obj){
		Voter voter;
		if(obj instanceof Voter)
			voter = (Voter)obj;
		else
			return false;

		//Compares ALL of the attributes of Voter to ensure the two are entirely equal.
		if(!(this.socialInsuranceNumber.equals(voter.getSocialInsuranceNumber())))
			return false;
		if (this.loginName != null) {
            if (voter.getLoginName() == null)
                return false;

            if(!(this.loginName.equals(voter.getLoginName())))
                return false;
        }
        if (this.password != null) {
            if (voter.getPassword() == null)
                return false;

            if(!(this.password.equals(voter.getPassword())))
                return false;
        }
		if(!(this.registered == voter.isRegistered()))
			return false;
		if(!(this.voted == voter.hasVoted()))
			return false;

		return true;
	}
}