package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * This class will represent a 'UserJornadaScore'
 * I Just changed the name to be totally in English
 */
@Entity
public class UserMatchDayScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int points;
	@ManyToOne()
	@JoinColumn(name = "user_season_id")
	private UserSeasonScore userSeason; //bidirectional relationship
}
