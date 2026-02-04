package rugbyniela.entity.pojo;

import java.time.LocalDate;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchDay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//TODO: maybe remove both dates
	@Column(nullable = false)
	private LocalDate dateBegin;
	
	
	@Column(nullable = false)
	private LocalDate dateEnd;
	

	@Column(nullable = false, length = 100)
	private String name; //jornada 1, jornada 2....
	
	Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "division_id", nullable = true)
	private Division division;//bidirectional relationship
	
	@OneToMany(mappedBy = "matchDay", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	private Set<Match> matches; //bidirectional relationship
	
	@Column
	private Boolean arePointsCalculated;
	
	public void addMatch (Match match) {
		if(this.matches==null) {
			this.matches = new HashSet<Match>(); //TODO: delete this because mapping does it already
		}
		this.matches.add(match);
		match.setMatchDay(this);
	}
}
