package io.pivotal.model;

import org.springframework.data.repository.CrudRepository;

public interface CogsUserRepository extends CrudRepository<CogsUser, Long> {
    CogsUser findByNameIgnoreCase(String name);
}
