package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Bet {

	@Id
	private Long id;
	@ManyToOne
	private WeeklyBetTicket weeklyBetTicket;
}
