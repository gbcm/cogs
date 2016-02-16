package io.pivotal.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class CogsUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany
    private List<Artist> trackedArtists;

    @OneToMany
    private List<Release> hiddenReleases;

    protected CogsUser() {}

    public CogsUser(String name) {
        this.name = name;
        trackedArtists = new ArrayList<>();
        hiddenReleases = new ArrayList<>();
    }
}