package org.intech.vehiclerental.exceptions;

public class WrongLoginEmailCredentialException extends RuntimeException{

    public WrongLoginEmailCredentialException(String message){
        super(message);
    }

}
