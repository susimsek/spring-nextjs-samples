package io.github.susimsek.springnextjssamples.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.susimsek.springnextjssamples.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import java.io.Serializable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * Represents a validation violation, encapsulating details about a constraint violation.
 * <p>
 * This record is used to provide information on failed validation constraints, including
 * the object name, field, rejected value, and an error message. This structure is often used
 * in API responses to give feedback on specific validation errors.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Violation(
    @Schema(description = "Code.", example = "not_blank")
    @JsonProperty("code")
    String code,

    @Schema(description = "Object name", example = "chatRequest")
    @JsonProperty("object")
    String objectName,

    @Schema(description = "Field name", example = "prompt")
    @JsonProperty
    String field,

    @Schema(description = "Violation rejected value", example = "a")
    @JsonProperty
    Object rejectedValue,

    @Schema(description = "Error Message", example = "size must be between 4 and 50")
    @JsonProperty
    String message
) implements Serializable {

    /**
     * Constructs a simple {@code Violation} with only the object name and message.
     *
     * @param objectName the name of the object where the violation occurred
     * @param message the error message describing the violation
     */
    public Violation(String objectName, String message) {
        this(null, objectName, null, null, message);
    }

    /**
     * Constructs a {@code Violation} from a {@link FieldError} instance.
     *
     * @param error the field error containing details about the validation violation
     */
    public Violation(FieldError error) {
        this(
            getCode(error.getCode()),
            error.getObjectName().replaceFirst("DTO$", ""),
            error.getField(),
            error.getRejectedValue(),
            error.getDefaultMessage()
        );
    }

    /**
     * Constructs a {@code Violation} from an {@link ObjectError} instance.
     *
     * @param error the object error containing details about the validation violation
     */
    public Violation(ObjectError error) {
        this(
            getCode(error.getCode()),
            null,
            error.getObjectName().replaceFirst("DTO$", ""),
            null,
            error.getDefaultMessage()
        );
    }

    /**
     * Constructs a {@code Violation} from a {@link ConstraintViolation} instance.
     *
     * @param violation the constraint violation containing details about the validation error
     */
    public Violation(ConstraintViolation<?> violation) {
        this(
            getCode(violation.getConstraintDescriptor()
                .getAnnotation().annotationType().getSimpleName()),
            null,
            getField(violation.getPropertyPath()),
            violation.getInvalidValue(),
            violation.getMessage()
        );
    }

    /**
     * Extracts the field name from the provided property path.
     *
     * @param propertyPath the path representing the property where the violation occurred
     * @return the name of the field in violation
     */
    private static String getField(Path propertyPath) {
        String fieldName = null;
        for (Path.Node node : propertyPath) {
            fieldName = node.getName();
        }
        return fieldName;
    }

    /**
     * Converts the constraint annotation name to a snake_case format.
     *
     * @param annotationName the name of the annotation related to the constraint violation
     * @return the code for the violation in snake_case
     */
    private static String getCode(String annotationName) {
        return StringUtils.toSnakeCase(annotationName);
    }
}
