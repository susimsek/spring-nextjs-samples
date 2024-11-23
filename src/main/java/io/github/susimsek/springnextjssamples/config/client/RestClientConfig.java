package io.github.susimsek.springnextjssamples.config.client;


import io.github.susimsek.springnextjssamples.client.TodoClient;
import io.github.susimsek.springnextjssamples.config.logging.wrapper.HttpLoggingWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(RestClientProperties.class)
@RequiredArgsConstructor
public class RestClientConfig {

    private final RestClientProperties restClientProperties;

    @Bean
    @Scope("prototype")
    RestClient.Builder restClientBuilder(
        RestClientBuilderConfigurer restClientBuilderConfigurer,
        ObjectProvider<HttpLoggingWrapper> httpLoggingWrapperProvider) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(restClientProperties.getConnectTimeout())
            .withReadTimeout(restClientProperties.getReadTimeout());
        RestClient.Builder builder = RestClient.builder()
            .requestFactory(ClientHttpRequestFactories.get(settings));
        HttpLoggingWrapper httpLoggingWrapper = httpLoggingWrapperProvider.getIfAvailable();
        if (httpLoggingWrapper != null) {
            builder = builder.requestInterceptor(httpLoggingWrapper.createRestClientInterceptor());
        }
        return restClientBuilderConfigurer.configure(builder);
    }

    @Bean
    public TodoClient todoClient(RestClient.Builder builder) {
        String todoClientUrl = restClientProperties.getClients()
            .get("todoClient")
            .getUrl();
        RestClient restClient = builder.baseUrl(todoClientUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(TodoClient.class);
    }
}
