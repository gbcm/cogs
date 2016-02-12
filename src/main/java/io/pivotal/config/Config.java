package io.pivotal.config;

/**
 * Created by pivotal on 2/12/16.
 */
public class Config {

    public static String getDiscogsToken(){
        return System.getenv("DISCOGS_TOKEN");
    }
}
