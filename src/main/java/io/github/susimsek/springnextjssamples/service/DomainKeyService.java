package io.github.susimsek.springnextjssamples.service;

import com.nimbusds.jose.jwk.KeyUse;
import io.github.susimsek.springnextjssamples.entity.KeyEntity;
import io.github.susimsek.springnextjssamples.mapper.KeyMapper;
import io.github.susimsek.springnextjssamples.repository.KeyRepository;
import io.github.susimsek.springnextjssamples.security.key.Key;
import io.github.susimsek.springnextjssamples.security.key.KeyService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
public class DomainKeyService implements KeyService {

    private final KeyRepository keyRepository;
    private final KeyMapper authorizationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Key> findAll() {
        var entities = keyRepository.findByActiveAndUse(
            true, KeyUse.SIGNATURE.identifier());
        return authorizationMapper.toModelList(entities);
    }

    @Override
    @Transactional
    public void save(Key key) {
        Assert.notNull(key, "key cannot be null");
        Optional<KeyEntity> existingKey = keyRepository.findById(key.getId());
        if (existingKey.isPresent()) {
            updateKey(key, existingKey.get());
        } else {
            insertKey(key);
        }
    }

    private void updateKey(Key key, KeyEntity existingKey) {
        KeyEntity entity = authorizationMapper.toEntity(key);
        entity.setId(existingKey.getId());
        keyRepository.save(entity);
    }

    private void insertKey(Key key) {
        KeyEntity entity = authorizationMapper.toEntity(key);
        keyRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Key findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return keyRepository.findById(id)
            .map(authorizationMapper::toModel)
            .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Key findByKid(String kid) {
        Assert.hasText(kid, "kid cannot be empty");
        return keyRepository.findById(kid)
            .map(authorizationMapper::toModel)
            .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Key findByKidOrThrow(String kid) {
        Assert.hasText(kid, "kid cannot be empty");
        return keyRepository.findById(kid)
            .filter(KeyEntity::isActive)
            .map(authorizationMapper::toModel)
            .orElseThrow(() -> new DataRetrievalFailureException("The Key with kid '"
                + kid + "' not found or not active in the KeyRepository."));
    }

    @Override
    @Transactional
    public void remove(Key key) {
        Assert.notNull(key, "key cannot be null");
        keyRepository.deleteById(key.getId());
    }
}
