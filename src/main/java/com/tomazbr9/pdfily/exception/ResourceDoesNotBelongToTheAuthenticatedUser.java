package com.tomazbr9.pdfily.exception;

public class ResourceDoesNotBelongToTheAuthenticatedUser extends RuntimeException{

    public ResourceDoesNotBelongToTheAuthenticatedUser(String message){
        super(message);
    }
}
