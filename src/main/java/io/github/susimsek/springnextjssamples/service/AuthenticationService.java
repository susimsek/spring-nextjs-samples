package io.github.susimsek.springnextjssamples.service;


import io.github.susimsek.springnextjssamples.dto.LoginRequestDTO;
import io.github.susimsek.springnextjssamples.dto.TokenDTO;
import io.github.susimsek.springnextjssamples.mapper.TokenMapper;
import io.github.susimsek.springnextjssamples.security.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenGenerator tokenGenerator;
    private final TokenMapper tokenMapper;

    public TokenDTO authenticate(LoginRequestDTO loginRequest) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginRequest.username(),
            loginRequest.password()
        );

        // Authenticate the user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        Jwt jwt = tokenGenerator.generate(authentication);

        // Map JWT to TokenDTO using MapStruct
        return tokenMapper.toTokenDTO(jwt);
    }
}