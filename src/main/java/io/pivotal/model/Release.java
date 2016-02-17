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

    public Release(String name, String discogsId, int year) {
        this.name = name;
        this.discogsId = discogsId;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Release release = (Release) o;
        return discogsId != null ? discogsId.equals(release.discogsId) : release.discogsId == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + (discogsId != null ? discogsId.hashCode() : 0);
        return result;
    }
}