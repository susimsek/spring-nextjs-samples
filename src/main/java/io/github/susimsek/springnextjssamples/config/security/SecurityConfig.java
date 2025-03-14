package io.github.susimsek.springnextjssamples.config.security;

import static io.github.susimsek.springnextjssamples.constant.Constants.SPRING_PROFILE_DEVELOPMENT;
import static io.github.susimsek.springnextjssamples.security.AuthoritiesConstants.ADMIN;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import io.github.susimsek.springnextjssamples.config.RequestMatcherConfig;
import io.github.susimsek.springnextjssamples.exception.security.SecurityProblemSupport;
import io.github.susimsek.springnextjssamples.repository.UserRepository;
import io.github.susimsek.springnextjssamples.security.SecurityProperties;
import io.github.susimsek.springnextjssamples.service.DomainUserDetailsService;
import io.github.susimsek.springnextjssamples.service.mapper.UserMapper;
import io.github.susimsek.springnextjssamples.web.filter.SpaWebFilter;
import io.github.susimsek.springnextjssamples.web.filter.XssFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final Environment env;

    @Bean
    @Order(org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain defaultSecurityFilterChain(
        HttpSecurity http,
        RequestMatcherConfig requestMatcherConfig,
        SecurityProblemSupport problemSupport) throws Exception {
        if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_DEVELOPMENT))) {
            http.authorizeHttpRequests(authz -> authz.requestMatchers(antMatcher("/h2-console/**")).permitAll());
        }
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .cors(withDefaults())
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                .contentSecurityPolicy(csp -> csp.policyDirectives(securityProperties.getContentSecurityPolicy()))
                .referrerPolicy(
                    referrer -> referrer.policy(
                        ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                ))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport))
            .authorizeHttpRequests(authz ->
                authz
                    .requestMatchers(requestMatcherConfig.staticResources()).permitAll()
                    .requestMatchers(requestMatcherConfig.swaggerPaths()).permitAll()
                    .requestMatchers(requestMatcherConfig.actuatorPaths()).permitAll()
                    .requestMatchers(requestMatcherConfig.graphqlPath()).permitAll()
                    .requestMatchers(requestMatcherConfig.graphiqlPath()).permitAll()
                    .requestMatchers(requestMatcherConfig.subscriptionPath()).permitAll()
                    .requestMatchers(requestMatcherConfig.tokenPath()).permitAll()
                    .requestMatchers(requestMatcherConfig.helloApiPaths()).hasAuthority(ADMIN)
                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .jwt(withDefaults()))
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(xssFilter(requestMatcherConfig), BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    RequestMatcherConfig requestMatcherConfig(MvcRequestMatcher.Builder mvc) {
        return new RequestMatcherConfig(mvc);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository,
                                                 UserMapper userMapper) {
        return new DomainUserDetailsService(userRepository, userMapper);
    }

    public XssFilter xssFilter(
        RequestMatcherConfig requestMatcherConfig) {
        return XssFilter.builder()
            .requestMatchers(requestMatcherConfig.staticResources()).permitAll()
            .requestMatchers(requestMatcherConfig.swaggerPaths()).permitAll()
            .requestMatchers(requestMatcherConfig.actuatorPaths()).permitAll()
            .anyRequest().sanitized()
            .nonSanitizedHeaders(
                HttpHeaders.CONTENT_ENCODING, HttpHeaders.CACHE_CONTROL,
                HttpHeaders.CONTENT_TYPE, HttpHeaders.CONTENT_LENGTH, HttpHeaders.AUTHORIZATION,
                HttpHeaders.COOKIE, HttpHeaders.HOST, HttpHeaders.USER_AGENT,
                HttpHeaders.REFERER, HttpHeaders.ACCEPT,
                "sec-ch-ua",
                "sec-ch-ua-mobile",
                "sec-ch-ua-platform",
                "sec-fetch-site",
                "sec-fetch-mode",
                "sec-fetch-user",
                "sec-fetch-dest")
            .build();
    }
}
