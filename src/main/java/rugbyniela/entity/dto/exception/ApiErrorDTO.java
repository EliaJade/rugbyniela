package rugbyniela.entity.dto.exception;

import java.time.LocalDateTime;

public record ApiErrorDTO(
		 LocalDateTime timestamp,
	     int status,
	     String error,    // "Bad Request", "Not Found", etc.
	     String message,  // "El email ya existe", etc.
	     String action,   // "REGISTER", "LOGIN", "BETTING" (Lo que pediste)
	     String path){    // "/api/auth/login"

}
