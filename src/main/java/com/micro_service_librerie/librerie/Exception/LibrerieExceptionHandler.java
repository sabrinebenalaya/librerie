package com.micro_service_librerie.librerie.Exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.micro_service_librerie.librerie.Model.ErreurMessage;


@RestControllerAdvice
public class LibrerieExceptionHandler {

    @ExceptionHandler(value={NotFoundExeption.class})
    public ResponseEntity<Object> entityNotFoundException(NotFoundExeption exeption){
        ErreurMessage erreurMessage = ErreurMessage.builder()
        .errors(exeption.getMessage())
        .time(new Date())
        .code(404)
        .build();
        return new ResponseEntity<>(erreurMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={AlreadyExistsException.class})
    public ResponseEntity<Object> entityAlreadyExistsException(AlreadyExistsException exeption){
        ErreurMessage erreurMessage = ErreurMessage.builder()
        .errors(exeption.getMessage())
        .time(new Date())
        .code(409)
        .build();
        return new ResponseEntity<>(erreurMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value={FieldNotFound.class})
    public ResponseEntity<Object> FieldNotFound(FieldNotFound exeption){
        ErreurMessage erreurMessage = ErreurMessage.builder()
        .errors(exeption.getMessage())
        .time(new Date())
        .code(408)
        .build();
        return new ResponseEntity<>(erreurMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={ConstraintViolationException.class})
    public ResponseEntity<Object>
        handleConstraintViolation(ConstraintViolationException ex) {
          
            List<String> errors = new ArrayList<String>();
            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + " : "
                        + violation.getMessage());
            }
            ErreurMessage erreurMessage = ErreurMessage.builder()
            .errors(errors)
            .time(new Date())
            .code(402)
            .build();
		return new ResponseEntity<>(erreurMessage, HttpStatus.BAD_REQUEST);
     
    }
    @ExceptionHandler(value={MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object>
        handleArgumentMismatchException(MethodArgumentTypeMismatchException ex) {
             ErreurMessage erreurMessage = ErreurMessage.builder()
            .errors("la valeur de "+ex.getName()+" : '"+ex.getValue()+"' doit étre de type "+ex.getRequiredType())
            .time(new Date())
            .code(400)
            .build();
		return new ResponseEntity<>(erreurMessage, HttpStatus.BAD_REQUEST);
     
    }

    @ExceptionHandler(value={IllegalArgumentException.class})
    public ResponseEntity<Object>
        handleIllegalArgumentException(IllegalArgumentException ex) {
             ErreurMessage erreurMessage = ErreurMessage.builder()
            .errors(ex.getMessage())
            .time(new Date())
            .code(401)
            .build();
		return new ResponseEntity<>(erreurMessage, HttpStatus.BAD_REQUEST);
     
    }
    
}