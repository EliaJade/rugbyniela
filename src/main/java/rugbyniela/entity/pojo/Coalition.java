package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Coalition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@OneToMany(mappedBy = "coalition")
	private Set<CoalitionSeasonScore> coalitionSeasons; //bidirectional relationship
	@OneToMany(mappedBy = "coalition") 
	private Set<UserSeasonScore> userSeasons; //bidirectional relationship
	
}
