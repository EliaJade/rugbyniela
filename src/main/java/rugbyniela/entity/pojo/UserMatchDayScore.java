package rugbyniela.entity.pojo;

import java.util.Set;

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
 * This class will represent a 'UserJornadaScore'
 * I Just changed the name to be totally in English
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserMatchDayScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false)
	private int points;
	
	@ManyToOne
	@JoinColumn(name = "user_season_id", nullable = false)
	private UserSeasonScore userSeason; //bidirectional relationship
}
