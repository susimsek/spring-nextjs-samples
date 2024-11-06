package io.github.susimsek.springnextjssamples.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for Spring Security authority roles.
 * <p>
 * Provides predefined roles for user access control, including administrator, regular user, and anonymous user roles.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthoritiesConstants {

    /**
     * Role assigned to administrators with elevated permissions.
     */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Role assigned to regular users with standard permissions.
     */
    public static final String USER = "ROLE_USER";

    /**
     * Role assigned to anonymous or unauthenticated users.
     */
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * The claim name used to store authorities in JWTs or other security contexts.
     */
    public static final String CLAIM_NAME = "authorities";
}
