package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Match {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Address address; //unidirectional relationship 
	
	@NotNull
	@Column(nullable = false)
	private LocalDateTime timeDate; 
	
	private int localResult;
	
	private int awayResult;
	
	@Enumerated(EnumType.STRING)
	private Bonus bonus; //check type is correct
	
	@ManyToOne
	private MatchDay matchDay; //bidirectional relationship
	@OneToMany //see about jointable
	private Set<Team> teams; //unidirectional relationship
	@OneToMany
	private Set<Bet> bets; //unidirectional relationship
	
	
	public void addTeam(Team team) {
		if(this.teams==null) {
			this.teams = new HashSet<Team>();
		}
		this.teams.add(team);
	}
	
	public void addBet (Bet bet) {
		if(this.bets == null) {
			this.bets = new HashSet<Bet>();
		}
		this.bets.add(bet);//unidirectional so it ends here
	}
}
