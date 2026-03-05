package org.intech.vehiclerental.exceptions;

public class VehicleAccessDeniedException extends RuntimeException {
    public VehicleAccessDeniedException(String s) {
        super(s);
    }
}
