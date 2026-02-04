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
import lombok.extern.slf4j.Slf4j;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
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
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "predicted_leaderboard_winner_team_id", nullable = false)
//	private Team predictedLeaderBoardWinner;
	
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
	
	@Column(nullable = false)
	private Integer weeklyPoints;
	
	public void addBet(Bet bet) {
		
		bets.add(bet);
		bet.setWeeklyBetTicket(this);
	}

	public void removeBet(Bet bet) {
		bets.remove(bet);
		bet.setWeeklyBetTicket(null);
	}
	
	public void updateBets(Set<Bet> newBets) {
		if (this.bets == null) {
	        this.bets = new HashSet<>();
	    }
		bets.removeIf(existing -> newBets.stream().anyMatch(newBet -> newBet.getMatch().equals(existing.getMatch())));
		newBets.forEach(this::addBet);
		log.debug("Updated ticket");
	}
	
	public void addDivisionBet(DivisionBet bet) {
	    divisionBets.add(bet);
	    bet.setWeeklyBetTicket(this);
	}

	public void removeDivisionBet(DivisionBet bet) {
	    divisionBets.remove(bet);
	    bet.setWeeklyBetTicket(null);
	}

	public void updateDivisionBets(Set<DivisionBet> newBets) {
		if (this.divisionBets == null) {
	        this.divisionBets = new HashSet<>();
	    }
		divisionBets.removeIf(existing ->
	        newBets.stream().anyMatch(nb ->
	            nb.getDivision().equals(existing.getDivision())
	        )
	    );

	    newBets.forEach(this::addDivisionBet);
	}
//	
}