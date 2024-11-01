package io.github.susimsek.springnextjssamples.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TCKNValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TCKN {
    String message() default "{validation.field.tckn}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
