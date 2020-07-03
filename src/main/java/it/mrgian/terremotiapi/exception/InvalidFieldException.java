package it.mrgian.terremotiapi.exception;

public class InvalidFieldException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidFieldException(String message) {
        super(message);
    }
}