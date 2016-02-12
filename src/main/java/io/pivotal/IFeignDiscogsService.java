package io.pivotal;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface IFeignDiscogsService {
    @RequestMapping(method = RequestMethod.GET, value = "artists/{discogsId}/releases", consumes = "application/json")

    @RequestLine("GET artists/{discogsId}/releases")
    ReleasesResponse getReleases(@Param("discogsId") String discogsId);
}