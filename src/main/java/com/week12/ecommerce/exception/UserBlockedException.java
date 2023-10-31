package com.week12.ecommerce.exception;

public class UserBlockedException extends RuntimeException{
    public UserBlockedException(String message){
        super(message);
    }
}
