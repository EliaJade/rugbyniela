package rugbyniela.entities;

import java.util.ArrayList;
import java.util.List;

public class User extends UserSystem{
	
	private List<UserSeasonDivision> userSeasonDivision;

	public User(long id, String name, String surname, String phoneNumber, String email, Address address,
			String username, String password, Role role) {
		super(id, name, surname, phoneNumber, email, address, username, password, role);
		userSeasonDivision= new ArrayList();
	}

	public List<UserSeasonDivision> getUserSeasonDivision() {
		return userSeasonDivision;
	}

	public void setUserSeasonDivision(List<UserSeasonDivision> userSeasonDivision) {
		this.userSeasonDivision = userSeasonDivision;
	}
	
	

	
	
	
}
