package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CoalitionSeasonScore {

	@Id
	private Long id;
	@ManyToOne
	private Season season;
}
