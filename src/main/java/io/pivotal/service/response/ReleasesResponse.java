package io.pivotal.service.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
public class ReleasesResponse {
    private List<Release> releases;

    public List<Release> getRelevantReleases(){
        List<Release> temp = getReleases().stream()
                .filter(r -> (r.getRole().equals("Main") &&
                        r.getType().equals("master")))
                .sorted((r1,r2) -> Integer.compare(r1.getYear(), r2.getYear()))
                .collect(Collectors.toList());
        return temp;
    }

    @Data
    static class Release{
        private String title;
        private String role;
        private int year;
        private String type;
        @SerializedName("id")
        private int discogsId;
    }
}
