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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Collaborator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 80)
	@NotBlank
	@Column(unique = true, length = 80, nullable = false)
	private String name;
	@Size(max = 300)
	@Column(length = 300)
	private String url;
	@Size(max = 500)
	@Column(length = 500)
	private String pictureUrl;
}
