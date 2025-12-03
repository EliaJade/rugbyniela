package rugbyniela.entity.pojo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@JoinColumn(name = "address_id", nullable = false)
	private Address location; //unidirectional relationship 
	
	private String name; //team away vs team local
	
	
	@Column(nullable = false)
	private LocalDateTime timeMatchStart; 
	
	@Column
	private Integer localResult; //null is notPlayedYet

	@Column
	private Integer awayResult; //null is notPlayedYet
	
	
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private Bonus bonus; 
	
	@Enumerated(EnumType.STRING)
	private MatchStatus status;

	@ManyToOne
	@JoinColumn(name="match_day_id", nullable = false)
	private MatchDay matchDay; //bidirectional relationship

	@ManyToOne
	@JoinColumn(name = "local_team_id", nullable = false)
	private Team localTeam;
	@ManyToOne
	@JoinColumn(name = "away_team_id", nullable = false)
	private Team awayTeam;

}
