package com.micro_service_librerie.librerie.Exception;



public class NotFoundExeption extends RuntimeException{

    public NotFoundExeption(){

    }
    
    public NotFoundExeption(String message){
        super(message);
    }
}
