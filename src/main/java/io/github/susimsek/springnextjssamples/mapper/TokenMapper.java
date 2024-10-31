package io.github.susimsek.springnextjssamples.mapper;

import io.github.susimsek.springnextjssamples.dto.TokenDTO;
import java.time.Duration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.oauth2.jwt.Jwt;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    @Mapping(target = "accessToken", source = "tokenValue")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "accessTokenExpiresIn", source = "jwt", qualifiedByName = "calculateAccessTokenExpiresIn")
    TokenDTO toTokenDTO(Jwt jwt);

    @Named("calculateAccessTokenExpiresIn")
    default long calculateAccessTokenExpiresIn(Jwt jwt) {
        if (jwt.getIssuedAt() != null && jwt.getExpiresAt() != null) {
            return Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()).getSeconds();
        }
        return 0L;
    }
}
