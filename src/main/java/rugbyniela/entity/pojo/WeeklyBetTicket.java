package rugbyniela.entity.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private LocalDate creationDate = LocalDate.now(); //maybe it's worth putting it as LocalDateTime so we know time of bet made ? to make sure it in range
	@ManyToOne()
	@JoinColumn(name = "user_season_score_id")
	private UserSeasonScore userSeason;//bidirectional relationship
	
	@OneToMany(mappedBy = "weeklyBetTicket")//bidirectional relationship
	private List<Bet> bets;
	
	public void addBet(Bet bet) {
		if(this.bets==null) {
			this.bets = new CopyOnWriteArrayList<Bet>();
		}
		this.bets.add(bet);
		bet.setWeeklyBetTicket(this);
	}
}