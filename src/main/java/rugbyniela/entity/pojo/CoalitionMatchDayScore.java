package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class will represent a 'CoalitionJornadaScore'
 *  I Just changed the name to be totally in English
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoalitionMatchDayScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int points;
	@ManyToOne
	private CoalitionSeasonScore coalitionSeason;//bidirectional relationship
}
