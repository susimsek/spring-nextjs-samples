package io.github.susimsek.springnextjssamples.security;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Utility class for Spring Security-related operations.
 * <p>
 * Provides methods to retrieve the current user's login, check authentication status,
 * verify roles, and determine if the current authentication is JWT-based.
 * </p>
 */
@UtilityClass
public class SecurityUtils {

    /**
     * Retrieves the username of the currently authenticated user, if available.
     *
     * @return an {@link Optional} containing the current user's username, or empty if
     *         the user is not authenticated or is anonymous
     */
    public Optional<String> getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            username = jwtAuthenticationToken.getName();
        } else if (authentication.getPrincipal() instanceof String principal) {
            username = principal;
        }

        return Optional.ofNullable(username);
    }

    /**
     * Checks if the current user is authenticated.
     *
     * @return {@code true} if the user is authenticated and the authentication is not JWT-based,
     *         {@code false} otherwise
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
            && authentication.isAuthenticated()
            && !(authentication instanceof JwtAuthenticationToken);
    }

    /**
     * Checks if the currently authenticated user has the specified role.
     *
     * @param role the role to check, such as "ROLE_USER" or "ROLE_ADMIN"
     * @return {@code true} if the current user has the specified role, {@code false} otherwise
     */
    public boolean isCurrentUserInRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    /**
     * Determines if the current authentication method is JWT-based.
     *
     * @return {@code true} if the current authentication is a {@link JwtAuthenticationToken},
     *         {@code false} otherwise
     */
    public boolean isJwtAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof JwtAuthenticationToken;
    }
}
