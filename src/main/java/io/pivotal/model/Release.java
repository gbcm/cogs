package io.pivotal.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Release implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, unique = true)
    private String discogsId;

    protected Release() {}

    public Release(String name, String discogsId) {
        this.name = name;
        this.discogsId = discogsId;
    }
}