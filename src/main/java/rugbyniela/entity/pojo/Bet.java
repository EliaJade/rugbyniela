package rugbyniela.entity.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import rugbyniela.enums.BetResult;
import rugbyniela.enums.Bonus;

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
	private int pointsAwarded; //default 0 //represents bonus
	
	@Column
	private Boolean betCorrect;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "weekly_bet_ticket_id", nullable = false)
	private WeeklyBetTicket weeklyBetTicket; //bidirectional relationship
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id", nullable = false)
	private Team predictedWinner; //unidirectional relationship
	
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	//TODO: validate if it matches with the match
	private Bonus bonus;
	
	@Enumerated(EnumType.STRING)
	@Column
	private BetResult betResult;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_id", nullable = false)//unidirectional relationship
	private Match match;

	

}
