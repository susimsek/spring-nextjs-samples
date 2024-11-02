package io.github.susimsek.springnextjssamples.security;

import io.github.susimsek.springnextjssamples.constant.Constants;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin()
            .orElse(Constants.SYSTEM));
    }
}