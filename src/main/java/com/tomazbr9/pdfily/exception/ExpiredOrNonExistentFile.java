package com.tomazbr9.pdfily.exception;

public class ExpiredOrNonExistentFile extends RuntimeException{

    public ExpiredOrNonExistentFile(String message){
        super(message);
    }
}
