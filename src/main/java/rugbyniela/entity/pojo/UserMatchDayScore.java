package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * This class will represent a 'UserJornadaScore'
 * I Just changed the name to be totally in English
 */
@Entity
public class UserMatchDayScore {

	@Id
	private Long id;
	@ManyToOne()
	private UserSeasonScore userSeason;
}
