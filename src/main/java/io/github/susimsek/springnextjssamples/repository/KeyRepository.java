package io.github.susimsek.springnextjssamples.repository;

import static io.github.susimsek.springnextjssamples.config.cache.CacheName.KEY_ENTITY_BY_KID_CACHE;
import static io.github.susimsek.springnextjssamples.config.cache.CacheName.KEY_ENTITY_BY_USE_CACHE;
import io.github.susimsek.springnextjssamples.entity.KeyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyRepository extends JpaRepository<KeyEntity, String> {
    @Cacheable(cacheNames = KEY_ENTITY_BY_KID_CACHE)
    Optional<KeyEntity> findByKid(String kid);

    @Cacheable(cacheNames = KEY_ENTITY_BY_USE_CACHE)
    List<KeyEntity> findByActiveAndUse(boolean active, String use);
}
