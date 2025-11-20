package rugbyniela.entity.pojo;

import com.cmeza.sdgenerator.annotation.SDGenerate;
import com.cmeza.sdgenerator.annotation.SDGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int pointsAwarded;
	@ManyToOne
	private WeeklyBetTicket weeklyBetTicket; //bidirectional relationship
	
	@ManyToOne
	private Team team; //possible changes unidirectional relationship
//	@ManyToOne
//	private Match match; //possible changes unidirectional relationship
}
