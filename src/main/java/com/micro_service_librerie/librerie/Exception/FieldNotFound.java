package com.micro_service_librerie.librerie.Exception;

public class FieldNotFound extends RuntimeException{
    public FieldNotFound (){

    }
    
    public FieldNotFound (String message){
        super(message);
    }
}
