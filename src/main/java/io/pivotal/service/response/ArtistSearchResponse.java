package io.pivotal.service.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ArtistSearchResponse {

    private List<ArtistSearchResult> results;

    @Data
    static class ArtistSearchResult{
        @SerializedName("title")
        private String name;

        @SerializedName("id")
        private int discogsId;
    }
}
