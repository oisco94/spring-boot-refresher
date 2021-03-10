package com.example.demo.util.errors;

public class UsernameTakenException extends Exception{
    public UsernameTakenException(String message, Throwable cause){
        super(message, cause);
    }
}
