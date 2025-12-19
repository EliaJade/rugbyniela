package rugbyniela.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import rugbyniela.enums.ActionType;

@Getter
public class RugbyException extends RuntimeException {

	//we extends from RuntimeException because are unchecked
	
	private final HttpStatus status;
    private final ActionType action;

    public RugbyException(String message, HttpStatus status, ActionType action) {
        super(message);
        this.status = status;
        this.action = action;
    }
}
