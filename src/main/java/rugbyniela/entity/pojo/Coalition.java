package rugbyniela.entity.pojo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Coalition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@NotBlank
	@Size(max=50)
	@Column(nullable = false, length = 50)
	private String name;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "capitan_id",unique = true,nullable = false)
	private User capitan;
	

	@OneToMany(mappedBy = "coalition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<CoalitionSeasonScore> coalitionSeasonScores; //bidirectional relationship
	
	
	@OneToMany(mappedBy = "coalition",fetch = FetchType.LAZY)
	private Set<UserSeasonScore> userSeasonScores; //bidirectional relationship
	
	@OneToMany(
	        mappedBy = "coalition", // Apunta al nombre del campo en la otra clase
	        fetch = FetchType.LAZY, // IMPORTANTE: No cargues las peticiones si no es necesario
	        cascade = CascadeType.ALL, // Si borras la coalición, se borran las peticiones
	        orphanRemoval = true // Si quitas una petición de la lista, se borra de la BD
	    )
	    private Set<CoalitionRequest> requests;

	    // Helper method para mantener la coherencia
	    public void addRequest(CoalitionRequest request) {
	        requests.add(request);
	        request.setCoalition(this);
	    }
	
	public void addCoalitionSeasonScore(CoalitionSeasonScore coalSeasonScore) {
		if(this.coalitionSeasonScores==null) {
			this.coalitionSeasonScores = new HashSet<CoalitionSeasonScore>();
		}
		this.coalitionSeasonScores.add(coalSeasonScore);
		coalSeasonScore.setCoalition(this);
		
	}
	public void addUserSeasonScore(UserSeasonScore uSeasonScore) {
		if(this.userSeasonScores==null) {
			this.userSeasonScores = new HashSet<UserSeasonScore>();
		}
		this.userSeasonScores.add(uSeasonScore);
		uSeasonScore.setCoalition(this);
		
	}
}
