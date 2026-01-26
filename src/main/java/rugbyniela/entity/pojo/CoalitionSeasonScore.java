package rugbyniela.entity.pojo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	
	
	@Column(nullable = false)
	private int totalPoints = 0; //default 0
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="season_id", nullable = false)
	private Season season;//bidirectional relationship
	
	Boolean isActive;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="coalition_id", nullable = false)
	private Coalition coalition;//bidirectional relationship
	
	@OneToMany(mappedBy = "coalitionSeason", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	private Set<CoalitionMatchDayScore> coalitionMatchDays; //bidirectional relationship
	
	
	public void addCoalitionMatchDay(CoalitionMatchDayScore coalMatchDay) {
		if(this.coalitionMatchDays==null) {
			this.coalitionMatchDays = new HashSet<CoalitionMatchDayScore>();
		}
		this.coalitionMatchDays.add(coalMatchDay);
		coalMatchDay.setCoalitionSeason(this); //maintains bidirectional relationship connecting parent to child this being matchdays --> coaltionSeason
	}
}
