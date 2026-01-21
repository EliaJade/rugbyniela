package rugbyniela.entity.pojo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rugbyniela.enums.Gender;
import rugbyniela.enums.Role;

@Entity
@Table(name = "Usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@Column(nullable = false, length = 50)
	private String name;
	

	@Column(length = 50)
	private String surname;
	

	@Column(length = 50, unique = true)
	private String nickname;
	
	
	@Column(nullable = false)
	private int age;
	

	@Column(length = 50, unique = true)
	private String phoneNumber;
	

	@Column(nullable = false, length = 200, unique = true)
	private String email;
	

	@Column(nullable = false, length = 200)
	private String password;
	
	@Column(nullable = false)
	private boolean isActive; //default true
	

	@Column(length = 80,unique = true)
	private String instagram;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;
	
	@Column(length = 500)
	private String profilePictureUrl;
	
	@NotNull
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	@JoinColumn(name="address_id")
	Address address;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="current_coalition_id")
	private Coalition currentCoalition;
	
	@Column(name = "coalition_joined_at")
	private LocalDateTime coalitionJoinedAt;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Set<UserSeasonScore> seasonScores;//unidirectional relationship
	
	/**
	 * Method to add a new userSeasonScore to the user
	 * @param userSeasonScore
	 */
	public void addUserSeasonScore(UserSeasonScore userSeasonScore) {
		if(this.seasonScores == null ){
			this.seasonScores = new HashSet<>();
		}
		seasonScores.add(userSeasonScore);
		//in this case as the relationship is not bidirectional only to save in here the relationship will be updated/saved successfully 
	}
	
	
	
	//TODO: ask if we need a method to delete a item from the lists, of course if the logic allows it

}
