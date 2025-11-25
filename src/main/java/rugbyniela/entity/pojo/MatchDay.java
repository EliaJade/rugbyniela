package rugbyniela.entity.pojo;

import java.time.LocalDate;
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
public class MatchDay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false)
	private LocalDate dateBegin;
	
	@NotNull
	@Column(nullable = false)
	private LocalDate dateEnd;
	
	@NotNull
	@Column(nullable = false, length = 100)
	private String name; //jornada 1, jornada 2....

	@ManyToOne
	private Division division;//bidirectional relationship
	
	@OneToMany(mappedBy = "matchDay")
	private Set<Match> matches; //bidirectional relationship
}
