package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CoalitionSeasonScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int totalPoints;
	@ManyToOne
	private Season season;//bidirectional relationship
	@ManyToOne
	private Coalition coalition;//bidirectional relationship
	@OneToMany(mappedBy = "coalitionSeason")
	private Set<CoalitionMatchDayScore> coalitionMatchDays; //bidirectional relationship
	
}
