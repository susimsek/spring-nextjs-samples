package io.github.susimsek.springnextjssamples.mapper;

import com.nimbusds.jose.jwk.KeyUse;
import io.github.susimsek.springnextjssamples.entity.KeyEntity;
import io.github.susimsek.springnextjssamples.security.EncryptionConstants;
import io.github.susimsek.springnextjssamples.security.key.Key;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeyMapper {

    default KeyEntity toEntity(Key model) {
        if (model == null) {
            return null;
        }

        KeyEntity entity = new KeyEntity();
        entity.setId(model.getId());
        entity.setType(model.getType());
        entity.setAlgorithm(model.getAlgorithm() != null ? model.getAlgorithm().getName() : null);
        entity.setPublicKey(publicKeyToString(model.getPublicKey()));
        entity.setPrivateKey(privateKeyToString(model.getPrivateKey()));
        entity.setActive(model.isActive());
        entity.setKid(model.getKid());
        entity.setUse(keyUseToString(model.getUse()));

        return entity;
    }


    default Key toModel(KeyEntity entity) {
        if (entity == null) {
            return null;
        }

        return Key.builder()
            .id(entity.getId())
            .type(entity.getType())
            .algorithm(entity.getAlgorithm())
            .publicKey(entity.getPublicKey())
            .privateKey(entity.getPrivateKey())
            .active(entity.isActive())
            .kid(entity.getKid())
            .use(entity.getUse())
            .build();
    }

    List<Key> toModelList(List<KeyEntity> entities);

    static String publicKeyToString(PublicKey publicKey) {
        if (publicKey == null) {
            return null;
        }
        String encodedKey = new String(publicKey.getEncoded(), StandardCharsets.UTF_8);
        return encodedKey.replace(EncryptionConstants.PUBLIC_KEY_HEADER, "")
            .replace(EncryptionConstants.PUBLIC_KEY_FOOTER, "");
    }

    static String privateKeyToString(PrivateKey privateKey) {
        if (privateKey == null) {
            return null;
        }
        String encodedKey = new String(privateKey.getEncoded(), StandardCharsets.UTF_8);
        return encodedKey.replace(EncryptionConstants.PRIVATE_KEY_HEADER, "")
            .replace(EncryptionConstants.PRIVATE_KEY_FOOTER, "");
    }

    static String keyUseToString(KeyUse keyUse) {
        return keyUse == null ? null : keyUse.identifier();
    }
}
