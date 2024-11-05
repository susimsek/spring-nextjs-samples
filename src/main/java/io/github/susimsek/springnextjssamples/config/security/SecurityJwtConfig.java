package io.github.susimsek.springnextjssamples.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import io.github.susimsek.springnextjssamples.service.mapper.KeyMapper;
import io.github.susimsek.springnextjssamples.repository.KeyRepository;
import io.github.susimsek.springnextjssamples.security.AuthoritiesConstants;
import io.github.susimsek.springnextjssamples.security.SecurityProperties;
import io.github.susimsek.springnextjssamples.security.jwk.DomainJWKSource;
import io.github.susimsek.springnextjssamples.security.key.KeyService;
import io.github.susimsek.springnextjssamples.security.token.DomainTokenEncoder;
import io.github.susimsek.springnextjssamples.security.token.TokenDecoder;
import io.github.susimsek.springnextjssamples.security.token.TokenEncoder;
import io.github.susimsek.springnextjssamples.security.token.TokenGenerator;
import io.github.susimsek.springnextjssamples.service.DomainKeyService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityJwtConfig {

    @Bean
    public KeyService oAuth2KeyService(
        KeyRepository keyRepository,
        KeyMapper keyMapper) {
        return new DomainKeyService(keyRepository, keyMapper);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public JWKSource<SecurityContext> jwkSource(KeyService keyService) {
        return new DomainJWKSource(keyService);
    }

    @Bean
    public TokenEncoder tokenEncoder(JWKSource<SecurityContext> jwkSource) {
        return new DomainTokenEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(
        KeyService keyService,
        JWKSource<SecurityContext> jwkSource) {
        Set<JWSAlgorithm> jwsAlgs = new HashSet<>();
        jwsAlgs.addAll(JWSAlgorithm.Family.RSA);
        jwsAlgs.addAll(JWSAlgorithm.Family.EC);
        jwsAlgs.addAll(JWSAlgorithm.Family.HMAC_SHA);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgs, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
        });
        return new TokenDecoder(keyService, jwtProcessor);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName(AuthoritiesConstants.CLAIM_NAME);
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public TokenGenerator tokenGenerator(TokenEncoder tokenEncoder,
                                         KeyService oAuth2KeyService,
                                         SecurityProperties securityProperties) {
        return new TokenGenerator(tokenEncoder, oAuth2KeyService, securityProperties);
    }

}
