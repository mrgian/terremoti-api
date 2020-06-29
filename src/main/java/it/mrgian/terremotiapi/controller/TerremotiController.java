package it.mrgian.terremotiapi.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/terremoti")
    public ResponseEntity<Object> getTerremoti() {
        return new ResponseEntity<>(twitterWebClient.getLatestTerremoti(), HttpStatus.OK);
    }

}