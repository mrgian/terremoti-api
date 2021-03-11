package im.gian.terremoti.api;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import im.gian.terremoti.api.exception.InvalidFieldException;
import im.gian.terremoti.api.exception.InvalidFilterException;
import im.gian.terremoti.api.model.Terremoti;
import im.gian.terremoti.api.utils.FileUtils;

/**
 * Classe che contiene i test JUnit
 * 
 * @author Gianmatteo Palmieri
 */
class TerremotiAPITests {

	String twitterResponse;
	String terremotiJsonExpected;
	String statsJsonExpected;

	/**
	 * Legge i file di test da confrontare
	 */
	@BeforeEach
	void setUp() {
		twitterResponse = FileUtils.readFile("/tests/twitterResponseTest.json", getClass());
		terremotiJsonExpected = FileUtils.readFile("/tests/terremotiTest.json", getClass());
		statsJsonExpected = FileUtils.readFile("/tests/statsTest.json", getClass());
	}

	/**
	 * Effettua il test del parsing dei tweet. Effettua il parsing di una lista di
	 * tweet di esempio presente nel file twitterResponseExample.json e lo confronta
	 * con una lista di terremoti già parsati presente nel file
	 * terremotiExample.json
	 */
	@Test
	void testParsing() {
		try {
			Terremoti terremoti = new Terremoti(twitterResponse);
			String terremotiJson = new ObjectMapper().writeValueAsString(terremoti);

			JsonNode node = new ObjectMapper().readTree(terremotiJson);
			JsonNode expectedNode = new ObjectMapper().readTree(terremotiJsonExpected);

			assertEquals(expectedNode, node);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Effettua il test delle statistiche. Calcola le statistiche di una lista di
	 * terremoti presente nel file terremotiExample.json e le confronta con le
	 * statistiche presenti nel file statsExample.json
	 */
	@Test
	void testStats() {
		try {
			Terremoti terremoti = new ObjectMapper().readValue(terremotiJsonExpected, new TypeReference<Terremoti>() {
			});

			JsonNode expectedNode = new ObjectMapper().readTree(statsJsonExpected);
			JsonNode node = new ObjectMapper().readTree(terremoti.getStats("profondita"));

			assertEquals(expectedNode, node);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Effettua il test del lancio dell'eccezione InvalidFieldException passando un
	 * campo sbagliato
	 */
	@Test
	void testFieldException() {
		Terremoti terremoti = new Terremoti();
		assertThrows(InvalidFieldException.class, () -> terremoti.getStats("profondits")); // typo campo
	}

	/**
	 * Effettua il test del lancio dell'eccezione InvalidFiterException passando un
	 * json non valido
	 */
	@Test
	void testFilterException() {
		Terremoti terremoti = new Terremoti();
		assertThrows(InvalidFilterException.class,
				() -> terremoti.filter("{\"<\": [{\"var\": \"valoreMagnitudo\"}3]}")); // json non valido
	}

}
