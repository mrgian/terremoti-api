package it.mrgian.terremotiapi;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.webclient.TwitterWebClient;
import it.mrgian.terremotiapi.webclient.config.TwitterWebClientConfig;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
