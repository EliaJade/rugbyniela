package rugbyniela.entity.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String surname;
	private int age;
	private String phoneNumber;
	private String email;
	private String password;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="user_id")
	private List<Address> addresses;//unidirectional relationship
	@OneToMany(mappedBy = "user")
	private List<UserSeasonScore> seasonScores;//unidirectional relationship
	
	/**
	 * Method to add a new userSeasonScore to the user
	 * @param userSeasonScore
	 */
	public void addUserSeasonScore(UserSeasonScore userSeasonScore) {
		if(this.seasonScores != null ){
			this.seasonScores = new ArrayList<>();
		}
		seasonScores.add(userSeasonScore);
		//in this case as the relationship is not bidirectional only to save in here the relationship will be updated/saved successfully 
	}
	
	//TODO: ask if we need a method to delete a item from the lists, of course if the logic allows it

}
