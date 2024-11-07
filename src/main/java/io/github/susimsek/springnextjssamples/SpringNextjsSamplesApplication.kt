package io.github.susimsek.springnextjssamples;

import io.github.susimsek.springnextjssamples.config.NativeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(NativeConfig.AppNativeRuntimeHints.class)
public class SpringNextjsSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNextjsSamplesApplication.class, args);
    }

}
