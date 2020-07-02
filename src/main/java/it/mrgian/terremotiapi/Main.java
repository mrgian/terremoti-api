package it.mrgian.terremotiapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale che contiene il metodo main
 * 
 * @author Gianmatteo Palmieri
 */
@SpringBootApplication
public class Main {
	/**
	 * Entry point del programma. Avvia l'applicazione Spring Boot.
	 * 
	 * @param args array di argomenti passati a riga di comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
