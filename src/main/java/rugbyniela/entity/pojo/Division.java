package rugbyniela.entity.pojo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rugbyniela.enums.Category;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Division {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max=50, min = 1)
	@Column(nullable = false, length = 50)
	private String name; //default "Division de Honor"
	
	@Enumerated(EnumType.STRING)
	@Column(length = 30, nullable = false)
	private Category category; //enum category for A and B
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name= "season_id", nullable = false)
	private Season season;//bidirectional relationship
	
	@OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	private Set<MatchDay> matchDays; //bidirectional relationship
	
	//to know which teams we could pair with each other
	@ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(
        name = "division_teams", // Nombre de la tabla intermedia en BD
        joinColumns = @JoinColumn(name = "division_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams = new HashSet<>();
	
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
	
	//TODO: ask if we need a method to delete a item from the lists, of course if the logic allows it
}
