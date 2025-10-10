package rugbyniela.entities;

public class Address {
	
	private long id;
	private String street;
	private String doorNumber;
	private String town;
	private String postCode;
	private String country;
	
	public Address(long id, String street, String doorNumber, String town, String postCode, String country) {
		super();
		this.id = id;
		this.street = street;
		this.doorNumber = doorNumber;
		this.town = town;
		this.postCode = postCode;
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	

}
