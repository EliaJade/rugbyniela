package rugbyniela.entity.pojo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
	
	@Column(nullable = false)
	private int points = 0; //default 0
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="coal_season_score_id", nullable = false)
	private CoalitionSeasonScore coalitionSeason;//bidirectional relationship
}
