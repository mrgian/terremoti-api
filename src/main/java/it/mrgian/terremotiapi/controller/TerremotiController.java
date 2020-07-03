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

import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.webclient.TwitterWebClient;
import it.mrgian.terremotiapi.webclient.config.TwitterWebClientConfig;

/**
 * Controller che gestisce le richieste alla API.
 * 
 * @author Gianmatteo Palmier
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
     * JSON le informazioni sui terremoti degli ultimi 7 giorni.
     * 
     * @return Informazioni sui terremoti in formato JSON
     */
    @RequestMapping(value = "/terremoti", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getTerremoti() {
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste POST alla rotta /terremoti. Restituisce in formato JSON
     * le informazioni sui terremoti filtrati in base al filtro passato nel body
     * della richiesta. Se il body non è presente viene la lista dei terremoti non
     * filtrata
     * 
     * @param filter filtro in formato JSON
     * @return Lista dei terremoti filtrata
     */
    @RequestMapping(value = "/terremoti", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> getFilteredTerremoti(@RequestBody(required = false) String filter) {
        if (filter != null)
            return new ResponseEntity<>(twitterWebClient.getLatestFilteredTerremoti(filter), HttpStatus.OK);
        else
            return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste GET alla rotta "/terremoti/stats". Restituisce in
     * formato JSON le statistiche sui terremoti degli ultimi 7 giorni.
     * 
     * @return Statistiche sui terremoti in formato JSON
     */
    @RequestMapping(value = "/terremoti/stats", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getStatsTerremoti(@RequestParam(required = false) String field) {
        if (field == null)
            return new ResponseEntity<>(twitterWebClient.getStatsLatestTerremoti(), HttpStatus.OK);
        else
            return new ResponseEntity<>(twitterWebClient.getStatsLatestTerremoti(field), HttpStatus.OK);
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

}