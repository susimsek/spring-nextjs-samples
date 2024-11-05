package io.github.susimsek.springnextjssamples.config;

import io.github.susimsek.springnextjssamples.web.exception.Violation;
import io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable;
import java.util.Collections;
import org.hibernate.cache.jcache.internal.JCacheRegionFactory;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeHint;

public class NativeConfig {

    public static class AppNativeRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("config/liquibase/*");
            hints.resources().registerPattern("org/aspectj/weaver/weaver-messages.properties");
            hints.reflection().registerType(Violation.class, TypeHint.Builder::withMembers);
            hints.reflection().registerType(Loggable.class, TypeHint.Builder::withMembers);
            hints
                .reflection()
                .registerType(liquibase.ui.LoggerUIService.class, hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
            hints
                .reflection()
                .registerType(liquibase.database.LiquibaseTableNamesFactory.class, hint ->
                    hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
                );
            hints
                .reflection()
                .registerType(liquibase.report.ShowSummaryGeneratorFactory.class, hint ->
                    hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
                );
            hints
                .reflection()
                .registerType(org.hibernate.binder.internal.BatchSizeBinder.class, hint ->
                    hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
                );
            hints.reflection().registerType(JCacheRegionFactory.class, type ->
                type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
            // jhipster-needle-add-native-hints - JHipster will add native hints here
        }
    }
}
