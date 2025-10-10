package rugbyniela.entities;

public class UserSystem extends Person {

	private String username;
	private String password;
	private Role role;
	
	public UserSystem(long id, String name, String surname, String phoneNumber, String email, Address address,
			String username, String password, Role role) {
		super(id, name, surname, phoneNumber, email, address);
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
	
	
	

}
