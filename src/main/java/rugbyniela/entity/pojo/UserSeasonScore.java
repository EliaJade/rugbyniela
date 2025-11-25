package rugbyniela.entity.pojo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSeasonScore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //why Long?//se usa long en vez de int, bbdd no sabe cuantos registros van a tener y cuanto va durar y pues el long tiene menos limite
	
	@NotNull
	@Column(nullable = false)
	private int totalPoints;
	
	@OneToMany(mappedBy = "userSeason")
	private Set<WeeklyBetTicket> tickets;//this should be a bidirectional relationship
	
	@OneToMany(mappedBy = "userSeason")
	private Set<UserMatchDayScore> matchDayScores;//this should be a bidirectional relationship
	
	@ManyToOne
	private Season season;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne
	private Coalition coalition;
	/**
	 * Method to add or subtract points
	 * @param points
	 */
	public void updatePoints(int points) {
		this.totalPoints += points;
	}
	
	/**
	 * Method to add a new weekly bet ticket to 
	 * the tickets and set the userSeason to the 
	 * weekly bet ticket (synchronized the relationship)
	 * @param ticket
	 */
	public void addTicket(WeeklyBetTicket ticket) {
		if(this.tickets == null ){
			this.tickets = new HashSet<WeeklyBetTicket>();
		}
		tickets.add(ticket);
		//in this case, when the next line is use, the relationship will be updated, because the UserSeasonScore is the owner of the relationship
		ticket.setUserSeason(this);
	}
	
	public void addMatchDayScore(UserMatchDayScore matchDayScore) {
		if(this.matchDayScores == null) {
			this.matchDayScores = new HashSet<UserMatchDayScore>();
		}
		this.matchDayScores.add(matchDayScore);
		matchDayScore.setUserSeason(this);
	}

	//TODO: ask if we need a method to delete a item from the lists, of course if the logic allows it
}
