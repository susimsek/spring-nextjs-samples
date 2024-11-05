package io.github.susimsek.springnextjssamples.web.controller;

import io.github.susimsek.springnextjssamples.service.dto.LoginRequestDTO;
import io.github.susimsek.springnextjssamples.service.dto.TokenDTO;
import io.github.susimsek.springnextjssamples.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "auth", description = "Authentication APIs")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate user",
        description = "Authenticate user and return access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TokenDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "Invalid login credentials",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "429", description = "Too Many Requests",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/token")
    public ResponseEntity<TokenDTO> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login request",
            required = true)
        @Valid @RequestBody LoginRequestDTO loginRequest) {

        TokenDTO tokenResponse = authenticationService.authenticate(loginRequest);

        return ResponseEntity.ok(tokenResponse);
    }
}
