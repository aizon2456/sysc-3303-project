package districtServer;

public abstract class Person {
	
	protected String firstName, lastName, socialInsuranceNumber;

	public Person(String firstName, String lastName, String socialInsuranceNumber){
		this.firstName = firstName;
		this.lastName = lastName;
		this.socialInsuranceNumber = socialInsuranceNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getSocialInsuranceNumber() {
		return socialInsuranceNumber;
	}

	public void setSocialInsuranceNumber(String socialInsuranceNumber) {
		this.socialInsuranceNumber = socialInsuranceNumber;
	}
	
}
