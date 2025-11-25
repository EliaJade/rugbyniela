package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class CoalitionSeasonScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false)
	private int totalPoints = 0; //default 0
	@ManyToOne
	private Season season;//bidirectional relationship
	@ManyToOne
	private Coalition coalition;//bidirectional relationship
	@OneToMany(mappedBy = "coalitionSeason")
	private Set<CoalitionMatchDayScore> coalitionMatchDays; //bidirectional relationship
	
}
