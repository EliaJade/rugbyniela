package rugbyniela.entity.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeeklyBetTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime creationDate; //used to be called creation_at:
	@ManyToOne()
	@JoinColumn(name = "user_season_score_id")
	private UserSeasonScore userSeason;//bidirectional relationship
	
	@OneToMany(mappedBy = "weeklyBetTicket")//bidirectional relationship
	private List<Bet> bets;
}