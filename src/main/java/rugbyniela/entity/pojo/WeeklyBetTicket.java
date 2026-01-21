package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class WeeklyBetTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false)
	private LocalDateTime creationDate; // default LocalDateTime.now()
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_season_score_id", nullable = false)
	private UserSeasonScore userSeason;//bidirectional relationship
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "predicted_leaderboard_winner_team_id", nullable = false)
	private Team predictedLeaderBoardWinner;
	
	@OneToMany(mappedBy = "weeklyBetTicket", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)//bidirectional relationship
	private Set<Bet> bets;
	
	@OneToMany(mappedBy = "weeklyBetTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DivisionBet> divisionBets;
	
	
	/**
	 * This is to have a reference of the coalition that the user belongs
	 * in order to avoid lose points when the user changes of coalition
	 * after make a bet (in a match day, basically avoid cheating)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_coalition_id")
	private Coalition coalitionAtBetTime;
	
	public void addBet(Bet bet) {
		if(this.bets==null) {
			this.bets = new HashSet<Bet>();
		}
		this.bets.add(bet);
		bet.setWeeklyBetTicket(this);
	}
	public void addDivisionBet(DivisionBet divBet) {
        if (this.divisionBets == null) this.divisionBets = new HashSet<>();
        this.divisionBets.add(divBet);
        divBet.setWeeklyBetTicket(this);
    }
}