package org.intech.vehiclerental.exceptions;

public class WrongLoginPasswordCredentialException extends RuntimeException{
    public WrongLoginPasswordCredentialException(String message){
        super(message);
    }
}
