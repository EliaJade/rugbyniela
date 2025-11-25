package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	@Column(nullable = false)
	private LocalDateTime timeDate; 
	private int localResult;
	private int awayResult;
	private String bonus; //check type is correct
	//missing possible variable
	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address; //unidirectional relationship
	@ManyToOne
	private MatchDay matchDay; //bidirectional relationship
	@ManyToOne
	@JoinColumn(name = "local_team_id")
	private Team localTeam;
	@ManyToOne
	@JoinColumn(name = "away_team_id")
	private Team awayTeam;
	
}
