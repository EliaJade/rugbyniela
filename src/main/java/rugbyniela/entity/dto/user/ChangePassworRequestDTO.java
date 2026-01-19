package rugbyniela.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePassworRequestDTO(
		@NotBlank(message = "La contraseña actual es obligatoria")
	    String currentPassword,

	    @NotBlank(message = "La nueva contraseña es obligatoria")
	    @Size(min = 8, max=200, message = "La contraseña debe tener entre 8 y 200 caracteres")
	    @Pattern(
	        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
	        message = "La contraseña debe tener al menos 1 mayuscula, 1 minuscula, 1 numero y 1 caracter especial (@$!%*?&)"
	        		+ " y debe tener 8-200 caracteres"
	    )
	    String newPassword,
	    
	    @NotBlank(message = "La confirmación de contraseña es obligatoria")
	    String confirmationPassword
		) {
}
