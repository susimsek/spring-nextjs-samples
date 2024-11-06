package io.github.susimsek.springnextjssamples.security;

import io.github.susimsek.springnextjssamples.constant.Constants;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 * <p>
 * Provides the current auditor, typically the username of the authenticated user.
 * If no user is authenticated, it defaults to the system account.
 * </p>
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    /**
     * Retrieves the current auditor, which is the login of the authenticated user.
     * <p>
     * If the user is not authenticated, it returns a default system account identifier.
     * </p>
     *
     * @return an {@link Optional} containing the current auditor's username, or the system account if unauthenticated
     */
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin()
            .orElse(Constants.SYSTEM));
    }
}
