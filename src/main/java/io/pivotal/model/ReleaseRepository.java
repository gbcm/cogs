package io.pivotal.model;

import org.springframework.data.repository.CrudRepository;

public interface ReleaseRepository extends CrudRepository<Release, Long> {
    Release findByDiscogsIdIgnoreCase(String discogsId);
}
