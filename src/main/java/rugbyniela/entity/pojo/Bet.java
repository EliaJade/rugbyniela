package rugbyniela.entity.pojo;

import com.cmeza.sdgenerator.annotation.SDGenerate;
import com.cmeza.sdgenerator.annotation.SDGenerator;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@JoinColumn(name = "team_id")
	private Team predictedWinner; //possible changes unidirectional relationship
	@ManyToOne
	@JoinColumn(name = "match_id")//unidirectional relationship
	private Match match;

}
