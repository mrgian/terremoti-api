package it.mrgian.terremotiapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe che contiente il metodi statici per operazioni sui file
 * 
 * @author Gianmatteo Palmieri
 */
public class FileUtils {
    /**
     * Legge il contenuto di un file di testo presente nella cartella "resources"
     * 
     * @param name Nome del file
     * @return Contenuto del file
     */
    public static String readFile(String path, @SuppressWarnings("rawtypes") Class theclass) {
        String content = "";

        try {
            InputStreamReader stream = new InputStreamReader(theclass.getResourceAsStream(path));
            BufferedReader reader = new BufferedReader(stream);

            String line;
            while ((line = reader.readLine()) != null)
                content += line + "\n";

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}