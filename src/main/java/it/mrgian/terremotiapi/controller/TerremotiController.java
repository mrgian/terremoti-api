package it.mrgian.terremotiapi.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.mrgian.terremotiapi.exception.InvalidFieldException;
import it.mrgian.terremotiapi.exception.InvalidFilterException;
import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.webclient.TwitterWebClient;
import it.mrgian.terremotiapi.webclient.config.TwitterWebClientConfig;

/**
 * Controller che gestisce le richieste alla API.
 * 
 * @author Gianmatteo Palmieri
 */
@RestController
public class TerremotiController {
    @Autowired
    TwitterWebClient twitterWebClient;

    /**
     * Inizializza il web client usato per ricevere i dati.
     */
    @PostConstruct
    public void init() {
        TwitterWebClientConfig config = new TwitterWebClientConfig();
        twitterWebClient = new TwitterWebClient(config);
    }

    /**
     * Gestisce le richieste GET alla rotta "/terremoti". Restituisce in formato
     * JSON le informazioni sui terremoti.
     * 
     * @return Informazioni sui terremoti in formato JSON
     */
    @RequestMapping(value = "/terremoti", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getTerremoti() {
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste GET alla rotta "/terremoti/stats". Restituisce in
     * formato JSON le statistiche sui terremoti.
     * 
     * @return Statistiche sui terremoti in formato JSON
     */
    @RequestMapping(value = "/terremoti/stats", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getStatsTerremoti(@RequestParam(required = false) String field) {
        if (field == null) // campo assente
            return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().getStats(), HttpStatus.OK);
        else {
            try {
                return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().getStats(field), HttpStatus.OK);
            } catch (InvalidFieldException e) {
                return new ResponseEntity<>(e.getJsonMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Gestisce le richieste GET alla rotta "/terremoti/metadata"
     * 
     * @return metadata dell'oggetto Terremoto in formato JSON
     */
    @RequestMapping(value = "/terremoti/metadata", produces = "application/json")
    public ResponseEntity<Object> getMetadata() {
        return new ResponseEntity<Object>(Terremoto.getMetadata(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste POST alla rotta /terremoti. Restituisce in formato JSON
     * le informazioni sui terremoti filtrati in base al filtro passato nel body
     * della richiesta. Se il body non è presente viene la lista dei terremoti non
     * filtrata.
     * 
     * @param filter filtro in formato JSON
     * @return Lista dei terremoti filtrata
     */
    @RequestMapping(value = "/terremoti", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> getFilteredTerremoti(@RequestBody(required = false) String filter) {
        if (filter != null) {
            try {
                return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().filter(filter), HttpStatus.OK);
            } catch (InvalidFilterException e) {
                return new ResponseEntity<>(e.getJsonMessage(), HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste POST alla rotta "/terremoti/stats". Restituisce in
     * formato JSON le statistiche del campo sui terremoti filtrati in base al
     * filtro passato nel body della richiesta.
     * 
     * @return Statistiche sui terremoti in formato JSON
     */
    @RequestMapping(value = "/terremoti/stats", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> getStatsFilteredTerremoti(@RequestParam(required = false) String field,
            @RequestParam(required = false) String filter) {
        if (field != null && filter == null) { // campo assente ma filtro presente
            try {
                return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().getStats(field), HttpStatus.OK);
            } catch (InvalidFieldException e) {
                return new ResponseEntity<>(e.getJsonMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (field == null && filter != null) { // campo presente ma filtro assente
            try {
                return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().filter(filter).getStats(),
                        HttpStatus.OK);
            } catch (InvalidFilterException e) {
                return new ResponseEntity<>(e.getJsonMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (field != null && filter != null) { // campo e filtro presenti
            try {
                return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().filter(filter).getStats(field),
                        HttpStatus.OK);
            } catch (InvalidFilterException e) {
                return new ResponseEntity<>(e.getJsonMessage(), HttpStatus.BAD_REQUEST);
            } catch (InvalidFieldException e) {
                return new ResponseEntity<>(e.getJsonMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        // campo e filtro assenti
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().getStats(), HttpStatus.OK);

    }

}