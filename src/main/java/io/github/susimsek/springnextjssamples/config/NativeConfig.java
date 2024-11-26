package io.github.susimsek.springnextjssamples.config;

import io.github.susimsek.springnextjssamples.config.logging.annotation.Loggable;
import io.github.susimsek.springnextjssamples.exception.Violation;
import io.github.susimsek.springnextjssamples.utils.ClassScanner;
import jakarta.validation.ConstraintValidator;
import java.util.Collections;
import java.util.Set;
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
            hints.resources().registerPattern("org/aspectj/weaver/weaver-messages.properties");
            hints.reflection().registerType(Violation.class, TypeHint.Builder::withMembers);
            hints.reflection().registerType(Loggable.class, TypeHint.Builder::withMembers);

            // Liquibase and Hibernate type registration
            registerLiquibaseAndHibernateHints(hints);

            // ConstraintValidator registration
            registerConstraintValidators(hints);
        }
    }

    private static void registerConstraintValidators(RuntimeHints hints) {
        Set<Class<?>> validators = ClassScanner.scanForImplementations(
            "io.github.susimsek.springnextjssamples.validation",
            ConstraintValidator.class);

        validators.forEach(validator ->
            hints.reflection().registerType(
                validator,
                builder -> builder.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
            )
        );
    }

    /**
     * Registers Liquibase and Hibernate-related hints.
     */
    private static void registerLiquibaseAndHibernateHints(RuntimeHints hints) {
        hints.resources().registerPattern("config/liquibase/*");

        hints.reflection().registerType(
            liquibase.ui.LoggerUIService.class,
            hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
        );
        hints.reflection().registerType(
            liquibase.database.LiquibaseTableNamesFactory.class,
            hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        );
        hints.reflection().registerType(
            liquibase.report.ShowSummaryGeneratorFactory.class,
            hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        );
        hints.reflection().registerType(
            org.hibernate.binder.internal.BatchSizeBinder.class,
            hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
        );
        hints.reflection().registerType(
            JCacheRegionFactory.class,
            type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE)
        );
    }
}
