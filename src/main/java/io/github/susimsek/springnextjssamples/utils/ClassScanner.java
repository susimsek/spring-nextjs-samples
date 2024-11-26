package io.github.susimsek.springnextjssamples.utils;

import java.util.HashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

@UtilityClass
public class ClassScanner {

    /**
     * Finds all classes in the specified package that implement the given interface or extend the given base class.
     *
     * @param basePackage The package to scan.
     * @param targetType  The interface or base class to match.
     * @return A set of classes implementing the specified interface or extending the base class.
     */
    public Set<Class<?>> scanForImplementations(String basePackage, Class<?> targetType) {
        Set<Class<?>> classes = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(targetType));

        scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
            try {
                classes.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + beanDefinition.getBeanClassName(), e);
            }
        });

        return classes;
    }

    /**
     * Finds all classes in the specified package that are annotated with the given annotation.
     *
     * @param basePackage The package to scan.
     * @param annotation  The annotation to match.
     * @return A set of classes annotated with the specified annotation.
     */
    public Set<Class<?>> scanForAnnotatedClasses(String basePackage, Class<?> annotation) {
        Set<Class<?>> classes = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter((Class) annotation));

        scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
            try {
                classes.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + beanDefinition.getBeanClassName(), e);
            }
        });

        return classes;
    }
}
