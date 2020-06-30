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
import org.springframework.web.bind.annotation.RestController;

import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.webclient.TwitterWebClient;
import it.mrgian.terremotiapi.webclient.config.TwitterWebClientConfig;

@RestController
public class TerremotiController {
    @Autowired
    TwitterWebClient twitterWebClient;

    @PostConstruct
    public void init() {
        TwitterWebClientConfig config = new TwitterWebClientConfig();
        twitterWebClient = new TwitterWebClient(config);
    }

    @RequestMapping(value = "/terremoti", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getTerremoti() {
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

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