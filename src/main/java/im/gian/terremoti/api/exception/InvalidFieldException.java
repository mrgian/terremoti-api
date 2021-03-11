package im.gian.terremoti.api.exception;

import java.util.HashMap;

import im.gian.terremoti.api.utils.JsonUtils;

/**
 * Eccezione generata quando il parametro passato quando si esegue una richiesta
 * GET o POST non è valido.
 * 
 * @author Gianmatteo Palmieri
 */
public class InvalidFieldException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidFieldException(String message) {
        super(message);
    }

    /**
     * Metodo usato per generare un messaggio di errore da restituire quando viene
     * fatta una richiesta che genera una eccezione.
     * 
     * @return Messaggio di errore in formato JSON
     */
    public String getJsonMessage() {
        HashMap<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("errore", getMessage());
        return JsonUtils.mapToJson(errorMap);
    }
}