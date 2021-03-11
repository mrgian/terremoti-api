package im.gian.terremoti.api.webclient;

import java.util.List;

import im.gian.terremoti.api.model.Terremoto;
import im.gian.terremoti.api.webclient.config.TwitterWebClientConfig;

/**
 * Interfaccia che contiene i metodi astratti del web client
 * 
 * @author Gianmatteo Palmieri
 */
public interface WebClient {
    public abstract List<Terremoto> getLatestTerremoti();

    public abstract TwitterWebClientConfig getConfig();
}