package io.github.susimsek.springnextjssamples.repository;

import static io.github.susimsek.springnextjssamples.config.cache.CacheName.USER_ENTITY_BY_EMAIL_CACHE;
import static io.github.susimsek.springnextjssamples.config.cache.CacheName.USER_ENTITY_BY_USERNAME_CACHE;

import io.github.susimsek.springnextjssamples.entity.UserEntity;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @EntityGraph(attributePaths = "roles")
    @Cacheable(cacheNames = USER_ENTITY_BY_USERNAME_CACHE)
    Optional<UserEntity> findByUsername(String username);

    @Cacheable(cacheNames = USER_ENTITY_BY_EMAIL_CACHE)
    Optional<UserEntity> findByEmail(String email);
}
