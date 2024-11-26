package io.github.susimsek.springnextjssamples.config;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;

class NativeConfigTest {

    @Test
    void testRegisterHints() {
        NativeConfig.AppNativeRuntimeHints hintsRegistrar = new NativeConfig.AppNativeRuntimeHints();
        RuntimeHints hints = new RuntimeHints();

        hintsRegistrar.registerHints(hints, Thread.currentThread().getContextClassLoader());

        assertFalse(hints.reflection().typeHints().toList().isEmpty(), "No hints were registered!");
    }
}
