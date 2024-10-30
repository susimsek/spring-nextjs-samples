package io.github.susimsek.springnextjssamples.security.token;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import io.github.susimsek.springnextjssamples.security.SecurityProperties;
import io.github.susimsek.springnextjssamples.security.key.Key;
import io.github.susimsek.springnextjssamples.security.key.KeyService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public final class TokenGenerator {
    private final TokenEncoder tokenEncoder;
    private final KeyService keyService;
    private final SecurityProperties securityProperties;


    public Jwt generate(Authentication authentication) {
        var token = securityProperties.getToken();
        String issuer = token.getIssuer();

        Instant issuedAt = Instant.now();
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;
        JWEAlgorithm jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
        EncryptionMethod encryptionMethod = EncryptionMethod.A256GCM;
        Instant expiresAt = issuedAt.plus(token.getAccessTokenTimeToLive());;

        String jweKeyId = token.getKeyId();


        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }

        claimsBuilder.subject(authentication.getName())
            .issuedAt(issuedAt)
            .expiresAt(expiresAt).id(UUID.randomUUID().toString())
            .notBefore(issuedAt);

        var authorizedScopes = authentication.getAuthorities().stream()
            .map(Object::toString)
            .toList();
        if (!CollectionUtils.isEmpty(authorizedScopes)) {
            claimsBuilder.claim("scope", authorizedScopes);
        }

        JwsHeader.Builder jwsHeaderBuilder = JwsHeader.with(jwsAlgorithm);

        JwsHeader jwsHeader = jwsHeaderBuilder.build();
        JWEHeader.Builder jweHeaderBuilder = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
            .contentType("JWT");
        if (jweKeyId != null) {
            jweHeaderBuilder.keyID(jweKeyId);
        }
        JwtClaimsSet claims = claimsBuilder.build();
        Boolean jweEnabled = token.isJweEnabled();
        if (Boolean.FALSE.equals(jweEnabled)) {
            return tokenEncoder.encode(TokenEncoderParameters.from(jwsHeader, null, claims),
                null);
        }
        Key key = keyService.findByKidOrThrow(jweKeyId);
        return tokenEncoder.encode(
            TokenEncoderParameters.from(jwsHeader, jweHeaderBuilder.build(), claims),
            key.toRSAKey());
    }
}
