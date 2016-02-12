package io.pivotal;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface IFeignDiscogsService {
    @Headers("user-agent: cogs/0.1")
    @RequestLine("GET artists/{discogsId}/releases")
    ReleasesResponse getReleases(@Param("discogsId") String discogsId);
}