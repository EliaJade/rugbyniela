package rugbyniela.entity.pojo;

import java.time.LocalDate;
import java.util.List;
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
public class WeeklyBetTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false)
	private LocalDate creationDate = LocalDate.now(); //maybe it's worth putting it as LocalDateTime so we know time of bet made ? to make sure it in range
	
	@ManyToOne()
	private UserSeasonScore userSeason;//bidirectional relationship
	
	@OneToMany(mappedBy = "weeklyBetTicket")//bidirectional relationship
	private List<Bet> bets;
}