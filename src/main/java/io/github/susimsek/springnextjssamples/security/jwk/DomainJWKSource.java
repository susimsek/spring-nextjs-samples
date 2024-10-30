package io.github.susimsek.springnextjssamples.security.jwk;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.susimsek.springnextjssamples.security.key.Key;
import io.github.susimsek.springnextjssamples.security.key.KeyService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DomainJWKSource implements JWKSource<SecurityContext> {

    private final KeyService keyService;

    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
        return this.keyService.findAll().stream()
            .map(Key::toRSAKey)
            .filter(rsaKey -> jwkSelector.getMatcher().matches(rsaKey))
            .collect(Collectors.toList());
    }
}
