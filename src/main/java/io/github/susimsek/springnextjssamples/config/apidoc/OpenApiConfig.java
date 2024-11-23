package io.github.susimsek.springnextjssamples.config.apidoc;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.method.HandlerMethod;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .description(
                            "JWT Authorization header using the Bearer scheme. "
                                + "Example: \"Authorization: Bearer {token}\"")
                ))
            .info(new Info()
                .title("Spring Boot Next.js Samples REST API")
                .description("Spring Boot Next.js Samples  REST API Documentation")
                .version("v1.0")
                .contact(new Contact()
                    .name("Şuayb Şimşek")
                    .url("https://github.com/susimsek")
                    .email("contact@susimsek.dev"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addParametersItem(new HeaderParameter()
                .schema(new StringSchema()._enum(Arrays.asList("en", "tr")))
                .name("Accept-Language")
                .description("Language preference")
                .required(false)
                .example("tr"));

            return operation;
        };
    }

    @Bean
    public OpenApiCustomizer errorResponsesCustomizer() {
        return openApi -> {
            Components components = openApi.getComponents();
            openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    addErrorToApi(operation, components);
                    if (operation.getSecurity() != null && !operation.getSecurity().isEmpty()) {
                        addSecurityResponses(operation, components);
                    }
                })
            );
        };
    }

    private void addErrorToApi(Operation operation, Components components) {
        io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType()
            .schema(AnnotationsUtils.resolveSchemaFromType(ProblemDetail.class, components, null));

        // 500 Internal Server Error
        operation.getResponses().addApiResponse("500", new ApiResponse()
            .description("Internal Server Error")
            .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, mediaType)));
    }

    private void addSecurityResponses(Operation operation, Components components) {
        io.swagger.v3.oas.models.media.MediaType mediaType = new io.swagger.v3.oas.models.media.MediaType()
            .schema(AnnotationsUtils.resolveSchemaFromType(ProblemDetail.class, components, null));

        // 401 Unauthorized
        operation.getResponses().addApiResponse("401", new ApiResponse()
            .description("Unauthorized")
            .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, mediaType)));

        // 403 Forbidden
        operation.getResponses().addApiResponse("403", new ApiResponse()
            .description("Forbidden")
            .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE, mediaType)));
    }
}
