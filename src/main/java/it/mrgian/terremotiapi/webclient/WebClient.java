package it.mrgian.terremotiapi.webclient;

import java.util.List;

import it.mrgian.terremotiapi.model.Terremoto;
import it.mrgian.terremotiapi.webclient.config.TwitterWebClientConfig;

public interface WebClient {
    public abstract List<Terremoto> getLatestTerremoti();

    public abstract TwitterWebClientConfig getConfig();

    public abstract void setConfig(TwitterWebClientConfig config);
}