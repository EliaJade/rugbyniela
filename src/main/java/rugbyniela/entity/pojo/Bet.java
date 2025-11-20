package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Bet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int pointsAwarded;
	@ManyToOne
	private WeeklyBetTicket weeklyBetTicket; //bidirectional relationship
	
//	@ManyToOne
//	private Team team; //possible changes unidirectional relationship
//	@ManyToOne
//	private Match match; //possible changes unidirectional relationship
}
