package it.mrgian.terremotiapi.exception;

/**
 * Eccezione lanciata quando manca un campo da parsare in un tweet
 * 
 * @author Gianmatteo Palmieri
 */
public class MissingTweetFieldException extends Exception {
    private static final long serialVersionUID = 1L;

    public MissingTweetFieldException(String s) {
        super(s);
    }
}