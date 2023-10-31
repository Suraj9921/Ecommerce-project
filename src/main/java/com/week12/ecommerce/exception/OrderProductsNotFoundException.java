package com.week12.ecommerce.exception;

public class OrderProductsNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public OrderProductsNotFoundException(String message){
        super(message);
    }
}
