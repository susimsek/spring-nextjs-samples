package io.github.susimsek.springnextjssamples.exception.ratelimit;

import lombok.Getter;

/**
 * Exception thrown when a rate limit is exceeded.
 * <p>
 * This exception captures details about the rate limit that was breached, including the
 * rate limiter's name, the limit for the specified period, the remaining available permissions,
 * the reset time, and the required wait time before retrying.
 * </p>
 */
@Getter
public class RateLimitExceededException extends RuntimeException {

    /** The name of the rate limiter that triggered the exception. */
    private final String rateLimiterName;

    /** The maximum number of allowed requests for the specified period. */
    private final int limitForPeriod;

    /** The number of available permissions remaining at the time of the exception. */
    private final long availablePermissions;

    /** The time (in milliseconds) when the rate limit will reset. */
    private final long resetTime;

    /** The time (in milliseconds) the client must wait before retrying. */
    private final long waitTime;

    /**
     * Constructs a new {@code RateLimitExceededException} with the specified details.
     *
     * @param rateLimiterName the name of the rate limiter that was exceeded
     * @param message a message describing the exception
     * @param limitForPeriod the maximum number of allowed requests in the period
     * @param availablePermissions the number of available permissions remaining
     * @param resetTime the time (in milliseconds) when the rate limit will reset
     * @param waitTime the time (in milliseconds) the client must wait before retrying
     */
    public RateLimitExceededException(
        String rateLimiterName, String message,
        int limitForPeriod, long availablePermissions, long resetTime, long waitTime) {
        super(message);
        this.rateLimiterName = rateLimiterName;
        this.limitForPeriod = limitForPeriod;
        this.availablePermissions = availablePermissions;
        this.resetTime = resetTime;
        this.waitTime = waitTime;
    }

}
