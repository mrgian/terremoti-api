package im.gian.terremoti.api.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import im.gian.terremoti.api.exception.InvalidFieldException;
import im.gian.terremoti.api.exception.InvalidFilterException;
import im.gian.terremoti.api.model.Terremoto;
import im.gian.terremoti.api.webclient.TwitterWebClient;
import im.gian.terremoti.api.webclient.config.TwitterWebClientConfig;

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
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getTerremoti() {
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste GET alla rotta "/terremoti/stats". Restituisce in
     * formato JSON le statistiche di un determinato campo. Se il parametro non
     * viene passato restituisce le media di terremoti avvenuti in un giorno.
     * 
     * @param field Eventuale campo su cui effettuare le statitistiche
     * @return Statistiche in formato JSON
     */
    @RequestMapping(value = "/stats", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getStatsTerremoti(@RequestParam(required = false) String field) {
        if (field == null) // parametro field assente
            return new ResponseEntity<>(twitterWebClient.getLatestTerremoti().getStats(), HttpStatus.OK);
        else { // prametro field presente
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
     * @return Metadata dell'oggetto Terremoto in formato JSON
     */
    @RequestMapping(value = "/metadata", produces = "application/json")
    public ResponseEntity<Object> getMetadata() {
        return new ResponseEntity<Object>(Terremoto.getMetadata(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste POST alla rotta /terremoti. Restituisce in formato JSON
     * le informazioni sui terremoti filtrati in base al filtro passato nel body
     * della richiesta. Se il body non è presente viene restituita la lista dei
     * terremoti non filtrata.
     * 
     * @param filter Filtro in formato JSON
     * @return Lista dei terremoti filtrata
     */
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
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
     * Se sia il campo che il filtro sono specificati restituisce le statistiche del
     * campo specificato calcolate dai terremoti filtrati. Se è specificato solo il
     * filtro restituisce la media di terremoti in un giorno calcolata dai terremoti
     * filtrati. Se è specificato solo il campo restiuisce le statistiche del campo
     * specificato calcolate dai terremoti non filtrati. Se nè il campo che il
     * filtro sono specificati restituisce la media dei terremoti in un giorno
     * calcolata dai terremoti non filtrati.
     * 
     * @param field  Campo su cui effettuare le statistiche
     * @param filter Body in cui viene specificato il filtro
     * @return Statistiche sui terremoti in formato JSON
     */
    @RequestMapping(value = "/stats", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> getStatsFilteredTerremoti(@RequestParam(required = false) String field,
            @RequestBody(required = false) String filter) {
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

        if (field != null && filter != "") { // campo e filtro presenti
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