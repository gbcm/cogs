package io.pivotal;

import lombok.Data;

@Data
public class ReleasesResponse {
    private ReleasesResponseInternal pagination;

    public int getReleases() {
        return this.getPagination().getItems();
    }

    @Data
    static class ReleasesResponseInternal{
        private int items;
    }
}
