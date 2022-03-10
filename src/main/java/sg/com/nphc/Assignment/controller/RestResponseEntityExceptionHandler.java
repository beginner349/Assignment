package sg.com.nphc.Assignment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sg.com.nphc.Assignment.exception.BadInputException;
import sg.com.nphc.Assignment.util.Helper;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadInputException.class)
    public ResponseEntity<Object> handleBadInputException(BadInputException ex) {
        return Helper.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
