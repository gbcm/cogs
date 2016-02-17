package io.pivotal.view.form;

import lombok.Data;

import java.util.List;

@Data
public class ArtistReleasePresenter {
    private String name;
    private List<String> releases;

    public ArtistReleasePresenter(String name, List<String> releases) {
        this.name = name;
        this.releases = releases;
    }
}
