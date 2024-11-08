package io.github.susimsek.springnextjssamples.web.controller.graphql

import io.github.susimsek.springnextjssamples.dto.request.LoginRequestDTO
import io.github.susimsek.springnextjssamples.dto.response.TokenDTO
import io.github.susimsek.springnextjssamples.service.AuthenticationService
import jakarta.validation.Valid
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping

@GraphQLController
class AuthGraphQLController(
    private val authenticationService: AuthenticationService
) {

    @MutationMapping
    fun authenticate(@Argument @Valid loginRequest: LoginRequestDTO): TokenDTO {
        return authenticationService.authenticate(loginRequest)
    }
}
