package rugbyniela.entity.pojo;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Bet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false) //see if its a good idea or not. it's according to the first esquema we did
	private int pointsAwarded = 0; //default 0
	
	@ManyToOne
	private WeeklyBetTicket weeklyBetTicket; //bidirectional relationship
	
	@ManyToOne
	private Team team; //possible changes unidirectional relationship //check this again

	
	
	
//	@ManyToOne
//	private Match match; //possible changes unidirectional relationship
}
