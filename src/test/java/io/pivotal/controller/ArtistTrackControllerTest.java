package io.pivotal.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import io.pivotal.service.response.ArtistSearchResponse;
import io.pivotal.service.IFeignDiscogsService;
import io.pivotal.config.Config;
import org.junit.Test;

/**
 * Created by pivotal on 2/12/16.
 */
public class ArtistTrackControllerTest {

    @Test
    public void testDoTheThing() throws Exception {
        IFeignDiscogsService discogsService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");

        ArtistSearchResponse asr = discogsService.searchArtists("Nirvana", Config.getDiscogsToken());
        assert(asr.getResults().size() == 50);
    }

    @Test
    public void testAddNewPost() throws Exception {

    }
}