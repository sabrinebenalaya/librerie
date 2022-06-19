package com.micro_service_librerie.librerie.Exception;

public class AlreadyExistsException extends RuntimeException {

    private String message;

    public AlreadyExistsException(String message) {
        this.message = message;
    }
    public AlreadyExistsException() {
    }
    
    public String getMessage (){
        return message;
    }
}
