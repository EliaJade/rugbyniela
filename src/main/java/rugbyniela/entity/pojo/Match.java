package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Address address; //if not just string location
	private LocalDateTime timeDate; //check it's localdatetime
	private int localResult;
	private int awayResult;
	private String bonus; //check type is correct
	//missing possible variable
	@ManyToOne
	private MatchDay matchday; //bidirectional relationship
	@OneToMany
	private Set<Team> team; //unidirectional relationship
	@OneToMany
	private Set<Bet> bets; //unidirectional relationship
	
}
