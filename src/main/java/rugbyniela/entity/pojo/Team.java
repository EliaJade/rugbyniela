package rugbyniela.entity.pojo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(length = 300)
	private String url;
	
	@OneToMany(mappedBy = "team") //unidirectional relationship
	private Set<Bet> bets;
	
	
	public void addBet(Bet bet) {
		if(this.bets==null) {
			this.bets = new HashSet<Bet>();
		}
		this.bets.add(bet);
	}
}
