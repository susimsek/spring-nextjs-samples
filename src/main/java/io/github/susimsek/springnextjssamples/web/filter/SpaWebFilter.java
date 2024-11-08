package io.github.susimsek.springnextjssamples.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.filter.OncePerRequestFilter;

public class SpaWebFilter extends OncePerRequestFilter {


    private final List<String> supportedLanguages = List.of("tr", "en");

    private final Pattern languagePattern = Pattern.compile("^/([a-z]{2})(/.*)?$");

    /**
     * Forwards any unmapped paths to the client language-specific HTML or default {@code index.html}.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // Request URI includes the contextPath if any, remove it.
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (!path.startsWith("/api") &&
            !path.startsWith("/graphql") &&
            !path.startsWith("/graphiql") &&
            !path.startsWith("/actuator") &&
            !path.startsWith("/v3/api-docs") &&
            !path.startsWith("/h2-console") &&
            !path.contains(".") &&
            path.matches("/(.*)")
        ) {

            Matcher matcher = languagePattern.matcher(path);
            if (matcher.matches()) {
                String language = matcher.group(1);
                if (supportedLanguages.contains(language)) {
                    request.getRequestDispatcher("/" + language + ".html").forward(request, response);
                    return;
                }
            }

            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
