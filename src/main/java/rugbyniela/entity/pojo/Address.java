package rugbyniela.entity.pojo;

import java.text.Normalizer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rugbyniela.utils.StringUtils;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = {
				"street",
				"city",
				"description",
				"postal_code"
		}))
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 100)
	private String street;
	
	@Column(nullable = false, columnDefinition = "citext") //case sensitive MUST ADD IN SUPABASE: CREATE EXTENSIONS IF NOT EXISTS citext
	private String city;
	
	@Column(name = "postal_code", nullable = false, length = 30)
	private String postalCode;
	
	@Column(length = 100)
	private String description;
	
	@PrePersist
	@PreUpdate
	private void normalize() {
		this.street = StringUtils.normalize(street);
		this.city = StringUtils.normalize(city);
		this.description = StringUtils.normalize(description);
		this.postalCode = StringUtils.normalize(postalCode);
	}
	
	
	
}
