package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String url;
//	@ManyToOne
//	private Match match; //unidirectional
	@OneToMany(mappedBy = "team") //unidirectional relationship
	private Set<Bet> bets;
}
