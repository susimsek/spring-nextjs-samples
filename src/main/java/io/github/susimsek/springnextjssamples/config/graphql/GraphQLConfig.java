package io.github.susimsek.springnextjssamples.config.graphql;

import graphql.scalars.ExtendedScalars;
import io.github.susimsek.springnextjssamples.config.graphql.scalars.DateTimeScalar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration(proxyBeanMethods = false)
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
            .scalar(DateTimeScalar.DATE_TIME)
            .scalar(ExtendedScalars.Locale);
    }
}
