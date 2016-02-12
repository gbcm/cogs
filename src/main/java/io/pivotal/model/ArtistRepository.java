package io.pivotal.model;

import org.springframework.data.repository.CrudRepository;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
    Artist findByNameIgnoreCase(String name);
    Artist findByDiscogsIdIgnoreCase(String discogsId);
}
