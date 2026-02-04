package rugbyniela.entity.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable =false)
	private MatchDay matchDay;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_season_id", nullable = false)
	private UserSeasonScore userSeason; //bidirectional relationship
	
	/**
	 * Who did you play for? when you earned these points?
	 * 
	 * This attribute is to know if the user belongs to a coalition
	 * and if it contribute to the total points of the coalition.
	 * So in the case this user wants to swicth of coalition
	 * the points added to the previous one be always there.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "historical_coalition_id")
	private Coalition historicalCoalition;
}
