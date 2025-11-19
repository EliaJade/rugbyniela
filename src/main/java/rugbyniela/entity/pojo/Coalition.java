package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Coalition {

	@Id
	private Long id;
	@OneToMany(mappedBy = "coalition") //bidirectional
	private Set<CoalitionSeasonScore> coalitionSeasons;
	
	
}
