package io.pivotal.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.pivotal.service.response.ArtistSearchResponse;
import io.pivotal.service.response.ReleasesResponse;

public interface IFeignDiscogsService {
    @Headers("user-agent: cogs/0.1")
    @RequestLine("GET artists/{discogsId}/releases")
    ReleasesResponse getReleases(@Param("discogsId") String discogsId);

    @Headers("user-agent: cogs/0.1")
    @RequestLine("GET database/search?q={artistName}&type=artist&token={token}")
    ArtistSearchResponse searchArtists(@Param("artistName") String artistName,
                                       @Param("token") String token);
}