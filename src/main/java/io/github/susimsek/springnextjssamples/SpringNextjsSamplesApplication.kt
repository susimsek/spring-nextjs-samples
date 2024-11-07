package io.github.susimsek.springnextjssamples

import io.github.susimsek.springnextjssamples.config.NativeConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints

@SpringBootApplication
@ImportRuntimeHints(NativeConfig.AppNativeRuntimeHints::class)
class SpringNextjsSamplesApplication

fun main(args: Array<String>) {
    runApplication<SpringNextjsSamplesApplication>(*args)
}
