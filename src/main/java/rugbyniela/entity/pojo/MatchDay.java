package rugbyniela.entity.pojo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	
	
	@Column(nullable = false)
	private LocalDate dateBegin;
	
	
	@Column(nullable = false)
	private LocalDate dateEnd;
	
	@NotBlank
	@Size(max=100)
	@Column(nullable = false, length = 100)
	private String name; //jornada 1, jornada 2....

	@ManyToOne
	@JoinColumn(name = "division_id", nullable = false)
	private Division division;//bidirectional relationship
	
	@OneToMany(mappedBy = "matchDay", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Match> matches; //bidirectional relationship
	
	private boolean arePointsCalculated;
	
	public void addMatch (Match match) {
		if(this.matches==null) {
			this.matches = new HashSet<Match>();
		}
		this.matches.add(match);
		match.setMatchDay(this);
	}
}
