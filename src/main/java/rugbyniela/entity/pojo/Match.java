package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
//	@Column(nullable = false)
	@ManyToOne
	@JoinColumn()
	private Address address; //unidirectional relationship
	private LocalDateTime timeDate; 
	private int localResult;
	private int awayResult;
	private String bonus; //check type is correct
	
	@ManyToOne
	private MatchDay matchDay; //bidirectional relationship
	@OneToMany
	private Set<Team> team; //unidirectional relationship
	@OneToMany
	private Set<Bet> bets; //unidirectional relationship
	
}
