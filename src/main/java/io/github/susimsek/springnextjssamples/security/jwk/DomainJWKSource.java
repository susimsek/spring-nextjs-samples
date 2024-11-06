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

/**
 * DomainJWKSource is an implementation of {@link JWKSource} that fetches JSON Web Keys (JWK) from a custom {@link KeyService}.
 * This service provides the necessary public keys to be used for verifying JSON Web Tokens (JWT) in a domain-specific context.
 *
 * <p>This class interacts with {@link KeyService} to retrieve all available keys, filters them to match the selector criteria,
 * and then returns a list of JWKs that satisfy the requirements specified by the {@link JWKSelector}.
 * </p>
 *
 * <p>Note: This class is annotated with {@link RequiredArgsConstructor} to enable constructor injection of {@link KeyService}.
 * </p>
 *
 * <p>Usage example:
 * <pre>
 *     DomainJWKSource jwkSource = new DomainJWKSource(keyService);
 *     List&lt;JWK&gt; jwks = jwkSource.get(jwkSelector, securityContext);
 * </pre>
 * </p>
 *
 * @see JWKSource
 * @see JWKSelector
 * @see KeyService
 */
@RequiredArgsConstructor
public class DomainJWKSource implements JWKSource<SecurityContext> {

    /**
     * The KeyService instance used to retrieve keys.
     */
    private final KeyService keyService;

    /**
     * Retrieves a list of {@link JWK} that match the given {@link JWKSelector}.
     *
     * <p>This method uses {@link KeyService} to fetch all available keys, maps each key to a {@link JWK},
     * filters the JWKs based on the criteria provided by {@link JWKSelector}, and returns the filtered list.
     * </p>
     *
     * @param jwkSelector the selector used to specify the matching criteria for the JWKs
     * @param context the security context for this key selection (can be null)
     * @return a list of matching {@link JWK} instances, filtered by the selector criteria
     * @throws KeySourceException if an error occurs while retrieving the keys
     */
    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
        return this.keyService.findAll().stream()
            .map(Key::toRSAKey)
            .filter(rsaKey -> jwkSelector.getMatcher().matches(rsaKey))
            .collect(Collectors.toList());
    }
}
