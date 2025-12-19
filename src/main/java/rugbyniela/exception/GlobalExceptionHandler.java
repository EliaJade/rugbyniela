package rugbyniela.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import rugbyniela.entity.dto.exception.ApiErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RugbyException.class)
    public ResponseEntity<ApiErrorDTO> handleRugbyException(
            RugbyException ex, 
            HttpServletRequest request
    ) {
        ApiErrorDTO errorDto = 
        		new ApiErrorDTO(
        				LocalDateTime.now(),
        				ex.getStatus().value(),
        				ex.getStatus().getReasonPhrase(),
        				ex.getMessage(),
        				ex.getAction().name(),
        				request.getRequestURI()
        				);

        return new ResponseEntity<>(errorDto, ex.getStatus());
    }

    // 2. Manejar validaciones de @Valid (ej: @NotBlank, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationException(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request
    ) {
        // Obtenemos el primer error de validación para mostrarlo
        String errorMessage = ex.getBindingResult().getFieldError() != null 
                ? ex.getBindingResult().getFieldError().getDefaultMessage() 
                : "Error de validación";

        ApiErrorDTO errorDto =
        		new ApiErrorDTO(
        				LocalDateTime.now(),
        				HttpStatus.BAD_REQUEST.value(),
        				"Validation Error",
        				errorMessage,
        				"VALIDATION",
        				request.getRequestURI()
        				);

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(
            Exception ex, 
            HttpServletRequest request
    ) {
        ApiErrorDTO errorDto = 
        		new ApiErrorDTO(
        				LocalDateTime.now(),
        				HttpStatus.INTERNAL_SERVER_ERROR.value(),
        				"Internal Server Error",
        				"Ocurrió un error inesperado: " + ex.getMessage(),
        				"INTERNAL_ERROR",
        				request.getRequestURI()
        				);

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
