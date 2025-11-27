package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@ManyToOne
	@JoinColumn(name = "user_season_score_id", nullable = false)
	private UserSeasonScore userSeason;//bidirectional relationship
	
	
	@OneToMany(mappedBy = "weeklyBetTicket", cascade = CascadeType.ALL, orphanRemoval = true)//bidirectional relationship
	private Set<Bet> bets;
	
	public void addBet(Bet bet) {
		if(this.bets==null) {
			this.bets = new HashSet<Bet>();
		}
		this.bets.add(bet);
		bet.setWeeklyBetTicket(this);
	}
}