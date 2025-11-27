package rugbyniela.entity.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size( max = 100)
	@Column(length = 100)
	private String street;
	
	@NotBlank
	@Size( max = 50)
//	@Column(nullable = false, length = 50)
	@Column(nullable = false, columnDefinition = "citext") //case sensitive MUST ADD IN SUPABASE: CREATE EXTENSIONS IF NOT EXISTS citext
	private String city;
	
	@NotBlank
	@Size( max = 30)
	@Column(nullable = false, length = 30)
	private String postalCode;
	
	@Size( max = 100)
	@Column(length = 100)
	private String description;
	
}
