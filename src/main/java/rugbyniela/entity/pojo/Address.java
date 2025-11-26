package rugbyniela.entity.pojo;

import com.cmeza.sdgenerator.annotation.SDGenerate;
import com.cmeza.sdgenerator.annotation.SDGenerator;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
//	@NotNull
//	@Column(nullable = false, length = 100) //see if its a good idea or not
	@Column(length = 100)
	private String street;
	
	@NotNull
	@Column(nullable = false, length = 50) //see if its a good idea or not
	private String city;
	
	@NotNull
	@Column(nullable = false, length = 30)
	private String postalCode;
	
	@Column(length = 100)
	private String descripcion;
	
}
