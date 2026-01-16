package rugbyniela.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.address.AddressRequestDTO;

public record UserRequestDTO(
		@NotBlank(message = "El nombre no debe estar vacia")
		@Size(max=50,message = "El tamaño del nombre debe estar entre 0-50 caracteres")
		String name,
		@Size(max = 50,message = "El tamaño del apellido debe estar entre 0-50 caracteres")
		String surname,
		@Size(max=50,message = "El tamaño del apodo debe estar entre 0-50 caracteres")
		String nickname,
		int age, 
		@Size(max=20,message = "El tamaño del telefono debe estar entre 0-20 caracteres")
		String phoneNumber,
		@NotBlank(message = "El email no debe estar vacio")
		@Size(max=150,message = "El tamaño del email debe estar entre 0-150 caracteres")
		String email,
		@NotBlank(message = "La contraseña no debe estar vacia")
		@Size(min = 8, max=200, message = "La contraseña debe tener entre 8 y 200 caracteres")
		@Pattern(
		        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
		        message = "La contraseña debe tener al menos 1 mayuscula, 1 minuscula, 1 numero y 1 caracter especial (@$!%*?&)"
		        		+ " y debe tener 8-200 caracteres"
		    )
		String password,
		@Size(max=80,message = "El tamaño del instagram debe estar entre 0-50 caracteres")
		String instagram,
		String gender,
		String role,
		AddressRequestDTO address) {

}
