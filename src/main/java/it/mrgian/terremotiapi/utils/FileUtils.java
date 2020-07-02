package it.mrgian.terremotiapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    /**
     * Legge il contenuto di un file di testo presente nella cartella "resources"
     * 
     * @param name
     * @return il contenuto del file
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