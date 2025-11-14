package rugbyniela.entity.pojo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Division {

	@Id
	private Long id;
	private String name;
	private String category;
	@ManyToOne
	private Season season;
	@OneToMany(mappedBy = "division")
	private Set<MatchDay> matchDays; 
	
	/**
	 * Method to add a matchDay in the list
	 * of matches day 'Jornada'
	 * @param matchDay
	 */
	public void addMatchDay(MatchDay matchDay) {
		if(this.matchDays==null) {
			this.matchDays = new HashSet<MatchDay>();
		}
		this.matchDays.add(matchDay);
		//in this case, when the next line is use, the relationship will be updated, because the MatchDay is the owner of the relationship
		matchDay.setDivision(this);
	}
}
