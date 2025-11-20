package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * This class will represent a 'CoalitionJornadaScore'
 *  I Just changed the name to be totally in English
 */
@Entity
public class CoalitionMatchDayScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int points;
	@ManyToOne
	private CoalitionSeasonScore coalitionSeason;//bidirectional relationship
}
