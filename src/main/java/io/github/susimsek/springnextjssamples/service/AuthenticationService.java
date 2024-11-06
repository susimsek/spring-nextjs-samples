package io.github.susimsek.springnextjssamples.service;


import io.github.susimsek.springnextjssamples.dto.request.LoginRequestDTO;
import io.github.susimsek.springnextjssamples.dto.response.TokenDTO;
import io.github.susimsek.springnextjssamples.exception.InvalidCredentialsException;
import io.github.susimsek.springnextjssamples.service.mapper.TokenMapper;
import io.github.susimsek.springnextjssamples.security.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
        try {
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

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }
    }
}
