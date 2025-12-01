package rugbyniela.entity.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false) 
	private int pointsAwarded; //default 0
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "weekly_bet_ticket_id", nullable = false)
	private WeeklyBetTicket weeklyBetTicket; //bidirectional relationship
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private Team predictedWinner; //unidirectional relationship
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "match_id", nullable = false)//unidirectional relationship
	private Match match;

}
