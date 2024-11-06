package io.github.susimsek.springnextjssamples.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A utility class that holds constants used across the application's error handling logic.
 * <p>
 * This class is designed to be a container for error-related constants, providing a centralized
 * location for keys and other constants commonly used in exception handling and error reporting.
 * </p>
 * <p>
 * This class cannot be instantiated due to its private constructor.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorConstants {

    /** The key used for representing a list of validation violations in error responses. */
    public static final String PROBLEM_VIOLATION_KEY = "violations";
}
