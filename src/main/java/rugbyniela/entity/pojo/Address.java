package rugbyniela.entity.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
	
	@Column(length = 100)
	private String street;
	

	@Column(nullable = false, columnDefinition = "citext") //case sensitive MUST ADD IN SUPABASE: CREATE EXTENSIONS IF NOT EXISTS citext
	private String city;
	
	@Column(nullable = false, length = 30)
	private String postalCode;
	
	@Column(length = 100)
	private String description;
	
}
