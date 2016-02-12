package io.pivotal.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
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
    private String discogsId;

    @Column(nullable = false)
    private int releaseCount;

    protected Artist() {}

    public Artist(String name, String discogsId, int releaseCount) {
        this.name = name;
        this.discogsId = discogsId;
        this.releaseCount = releaseCount;
    }
}