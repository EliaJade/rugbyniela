package rugbyniela.entity.pojo;

import java.time.LocalDate;
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
public class MatchDay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate dateBegin;
	private LocalDate dateEnd;

	@ManyToOne
	private Division division;//bidirectional relationship
	@OneToMany(mappedBy = "matchDay")
	private Set<Match> matches; //bidirectional relationship
}
