package io.github.susimsek.springnextjssamples.security.xss;

import io.github.susimsek.springnextjssamples.utils.SanitizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * A wrapper for {@link HttpServletRequest} that sanitizes input to prevent Cross-Site Scripting (XSS) attacks.
 * <p>
 * This class provides methods to sanitize request parameters, headers, query strings, URIs, and form data.
 * It is designed to work with JSON and form-based data, and can selectively bypass sanitization for specified headers.
 * </p>
 */
public class XssRequestWrapper extends ContentCachingRequestWrapper {

    private final FastByteArrayOutputStream cachedContent;
    private final List<String> nonSanitizedHeaders;

    /**
     * Constructs a new {@code XssRequestWrapper} that wraps the given request and applies sanitization.
     *
     * @param request the original {@link HttpServletRequest} to be wrapped
     * @param nonSanitizedHeaders a list of headers that should bypass sanitization
     */
    public XssRequestWrapper(HttpServletRequest request, List<String> nonSanitizedHeaders) {
        super(request);
        this.nonSanitizedHeaders = nonSanitizedHeaders;
        int contentLength = request.getContentLength();
        this.cachedContent = contentLength > 0 ? new FastByteArrayOutputStream(contentLength) : new FastByteArrayOutputStream();
    }

    /**
     * Returns the sanitized value of a request parameter.
     *
     * @param name the name of the parameter
     * @return the sanitized parameter value
     */
    @Override
    public String getParameter(String name) {
        if (this.cachedContent.size() == 0 && this.isFormPost()) {
            this.writeRequestParametersToCachedContent();
        }
        return super.getParameter(name);
    }

    /**
     * Returns the sanitized values of a request parameter.
     *
     * @param name the name of the parameter
     * @return an array of sanitized parameter values
     */
    @Override
    public String[] getParameterValues(String name) {
        if (this.cachedContent.size() == 0 && this.isFormPost()) {
            this.writeRequestParametersToCachedContent();
        }
        return super.getParameterValues(name);
    }

    /**
     * Returns a sanitized map of request parameters.
     *
     * @return a map of parameter names and sanitized values
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.cachedContent.size() == 0 && this.isFormPost()) {
            this.writeRequestParametersToCachedContent();
        }
        return super.getParameterMap();
    }

    /**
     * Returns the sanitized content as a byte array.
     *
     * @return the sanitized byte array of content
     */
    @Override
    public byte[] getContentAsByteArray() {
        var content = this.cachedContent.toByteArray();
        return SanitizationUtil.sanitizeJson(content);
    }

    /**
     * Returns the sanitized content as a string.
     *
     * @return the sanitized content as a string
     */
    @Override
    public String getContentAsString() {
        var content = this.cachedContent.toString(Charset.forName(this.getCharacterEncoding()));
        return SanitizationUtil.sanitizeJsonString(content);
    }

    /**
     * Returns the sanitized value of a request header.
     *
     * @param name the name of the header
     * @return the sanitized header value
     */
    @Override
    public String getHeader(String name) {
        if (isNonSanitized(name)) {
            return super.getHeader(name);
        }
        String value = super.getHeader(name);
        return value != null ? SanitizationUtil.sanitizeInput(value) : null;
    }

    /**
     * Returns an enumeration of sanitized header values.
     *
     * @param name the name of the header
     * @return an enumeration of sanitized header values
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        if (isNonSanitized(name)) {
            return super.getHeaders(name);
        }
        return Collections.enumeration(
            Collections.list(super.getHeaders(name)).stream()
                .map(SanitizationUtil::sanitizeInput)
                .toList()
        );
    }

    /**
     * Returns the sanitized query string.
     *
     * @return the sanitized query string, or {@code null} if none exists
     */
    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        return queryString != null ? SanitizationUtil.sanitizeInput(queryString) : null;
    }

    /**
     * Returns the sanitized request URI.
     *
     * @return the sanitized URI as a string
     */
    @Override
    public String getRequestURI() {
        String uri = super.getRequestURI();
        return uri != null ? SanitizationUtil.sanitizeInput(uri) : null;
    }

    /**
     * Returns the sanitized request URL as a {@link StringBuffer}.
     *
     * @return the sanitized request URL
     */
    @Override
    public StringBuffer getRequestURL() {
        StringBuffer requestURL = super.getRequestURL();
        return requestURL != null ? new StringBuffer(SanitizationUtil.sanitizeInput(requestURL.toString())) : null;
    }

    /**
     * Returns the sanitized servlet path.
     *
     * @return the sanitized servlet path
     */
    @Override
    public String getServletPath() {
        String servletPath = super.getServletPath();
        return servletPath != null ? SanitizationUtil.sanitizeInput(servletPath) : null;
    }

    /**
     * Returns the sanitized path information.
     *
     * @return the sanitized path information
     */
    @Override
    public String getPathInfo() {
        String pathInfo = super.getPathInfo();
        return pathInfo != null ? SanitizationUtil.sanitizeInput(pathInfo) : null;
    }

    /**
     * Checks if the specified header should bypass sanitization.
     *
     * @param headerName the name of the header
     * @return {@code true} if the header should not be sanitized, {@code false} otherwise
     */
    private boolean isNonSanitized(String headerName) {
        return nonSanitizedHeaders.stream()
            .anyMatch(item -> item.equalsIgnoreCase(headerName));
    }

    /**
     * Checks if the request is a POST request with form-encoded content.
     *
     * @return {@code true} if the request is a form-encoded POST, {@code false} otherwise
     */
    private boolean isFormPost() {
        String contentType = this.getContentType();
        return contentType != null && contentType.contains("application/x-www-form-urlencoded")
            && HttpMethod.POST.matches(this.getMethod());
    }

    /**
     * Writes request parameters to cached content, applying sanitization.
     */
    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                String requestEncoding = this.getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                Iterator<String> nameIterator = form.keySet().iterator();

                while (nameIterator.hasNext()) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    Iterator<String> valueIterator = values.iterator();

                    while (valueIterator.hasNext()) {
                        String value = valueIterator.next();
                        this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            value = SanitizationUtil.sanitizeInput(value);
                            this.cachedContent.write(61); // '=' character
                            this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.cachedContent.write(38); // '&' character
                            }
                        }
                    }

                    if (nameIterator.hasNext()) {
                        this.cachedContent.write(38); // '&' character
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write request parameters to cached content", e);
        }
    }
}
