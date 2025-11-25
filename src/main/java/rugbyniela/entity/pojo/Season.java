package rugbyniela.entity.pojo;

import java.time.LocalDate;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Season {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false, length = 100)
	private String name;
	
	@NotNull
	@Column(nullable = false)
	private LocalDate start_season;//here we use the LocalDate in order to get the just the date not time (recommended)
	
	@NotNull
	@Column(nullable = false)
	private LocalDate end_season;//here we use the LocalDate in order to get the just the date not time (recommended)
	
	@OneToMany(mappedBy = "season")
	private Set<UserSeasonScore> seasonParticipants;
	
	@OneToMany(mappedBy = "season")
	private Set<CoalitionSeasonScore> coalitionScore;
	
	@OneToMany(mappedBy = "season")
	private Set<Division> divisions;
	
	/**
	 * Method to add a participant to the list of
	 * userSeasonScore
	 * @param participan
	 */
	public void addParticipant(UserSeasonScore participan) {
		if(this.seasonParticipants==null) {
			this.seasonParticipants = new HashSet<UserSeasonScore>();
		}
		this.seasonParticipants.add(participan);
		//in this case, when the next line is use, the relationship will be updated, because the UserSeasonScore is the owner of the relationship
		participan.setSeason(this);
	}
	
	/**
	 * Method to add a coalitionScore object to the list coalitionScore
	 * @param coaltion
	 */
	public void addCoalition(CoalitionSeasonScore coaltion) {
		if(this.coalitionScore==null) {
			this.coalitionScore = new HashSet<CoalitionSeasonScore>();
		}
		this.coalitionScore.add(coaltion);
		//in this case, when the next line is use, the relationship will be updated, because the coalitionScore is the owner of the relationship
		coaltion.setSeason(this);
	}
	
	/**
	 * Method to add a coalitionScore object to the list coalitionScore
	 * @param division
	 */
	public void addDivision(Division division) {
		if(this.divisions==null) {
			this.divisions = new HashSet<Division>();
		}
		this.divisions.add(division);
		//in this case, when the next line is use, the relationship will be updated, because the Division is the owner of the relationship
		division.setSeason(this);
	}

	//TODO: ask if we need a method to delete a item from the lists, of course if the logic allows it
}
