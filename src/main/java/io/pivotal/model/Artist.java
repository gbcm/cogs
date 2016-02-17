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
public class Artist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String  discogsId;

    @OneToMany
    private List<Release> releases;

    protected Artist() {}

    public Artist(String name, String discogsId) {
        this.name = name;
        this.discogsId = discogsId;
        releases = new ArrayList<>();
    }
}