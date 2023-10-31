package com.week12.ecommerce.exception;

public class OrderDetailsNotFoundException extends RuntimeException{
    public OrderDetailsNotFoundException(String message){
        super(message);
    }
}
