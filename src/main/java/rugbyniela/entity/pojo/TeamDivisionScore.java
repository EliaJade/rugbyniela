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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamDivisionScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="season_id", nullable = false)
	private Season season;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="division_id", nullable = false)
	private Division division;
	
	@Column(nullable = false)
	private int totalPoints;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="team_id", nullable = false)
	private Team team;
}
