package it.mrgian.terremotiapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.utils.TerremotiUtils;

class TerremotiAPITests {

	@BeforeEach
	void setUp() {

	}

	@Test
	void testParsing() {
		try {
			String twitterResponse = readTestExample("twitterResponseExample");
			ArrayList<Terremoto> terremoti = TerremotiUtils.getTerremotiFromTwitterResponse(twitterResponse);
			String terremotiJson = new ObjectMapper().writeValueAsString(terremoti);
			String terremotiJsonExpected = readTestExample("terremotiExample");

			JsonNode node = new ObjectMapper().readTree(terremotiJson);
			JsonNode expectedNode = new ObjectMapper().readTree(terremotiJsonExpected);

			assertEquals(expectedNode.asText(), node.asText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readTestExample(String name) {
		String response = "";

		try {
			InputStreamReader stream = new InputStreamReader(
					getClass().getResourceAsStream("/tests/" + name + ".json"));
			BufferedReader reader = new BufferedReader(stream);

			String line;
			while ((line = reader.readLine()) != null)
				response += line + "\n";

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
