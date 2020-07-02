package it.mrgian.terremotiapi.controller;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.webclient.TwitterWebClient;
import it.mrgian.terremotiapi.webclient.config.TwitterWebClientConfig;

/**
 * Controller che gestisce le richieste alla API.
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
     * JSON le informazioni sui terremoti degli ultimi 7 giorni. E' possibile
     * specificare il parametro "data" quando si effuttua la chiamata per ricevere
     * solo le informazioni sui terremoti di una specifica data
     * 
     * @param data Data dei terremoti in formato "yyyy-MM-dd"
     * @return Informazioni sui terremoti in formato JSON
     */
    @RequestMapping(value = "/terremoti", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getTerremoti() {
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

    /**
     * Gestisce le richieste GET alla rotta "/terremoti/metadata"
     * 
     * @return metadata dell'oggetto {@link it.mrgian.terremotiapi.model.Terremoto}
     *         in formato JSON
     */
    @RequestMapping(value = "/terremoti/metadata", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getMetadata() {
        String metadata = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(objectMapper);
            JsonSchema schema = schemaGenerator.generateSchema(Terremoto.class);

            metadata = objectMapper.writeValueAsString(schema);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Object>(metadata, HttpStatus.OK);
    }

}