package io.github.susimsek.springnextjssamples.security.key;

import static io.github.susimsek.springnextjssamples.security.EncryptionConstants.PRIVATE_KEY_FOOTER;
import static io.github.susimsek.springnextjssamples.security.EncryptionConstants.PRIVATE_KEY_HEADER;
import static io.github.susimsek.springnextjssamples.security.EncryptionConstants.PUBLIC_KEY_FOOTER;
import static io.github.susimsek.springnextjssamples.security.EncryptionConstants.PUBLIC_KEY_HEADER;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.Requirement;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.converter.RsaKeyConverters;

/**
 * Represents a cryptographic key used within the application, encapsulating both public and private key information.
 * This class supports creating instances through a builder pattern, and provides methods for converting keys
 * to the JWK (JSON Web Key) format for secure token signing and verification.
 *
 * <p>This class includes fields for an identifier, type, algorithm, public and private key, key usage, activation status,
 * and a key ID for unique identification. Instances of {@code Key} are immutable after creation.</p>
 *
 * <p>The {@code Builder} inner class supports the chaining of methods to set key properties such as id, type,
 * algorithm, public and private keys (from PEM-formatted strings), and other attributes. The {@link #toRSAKey()} method
 * is available for converting the key to an {@link RSAKey}.</p>
 *
 * <p>Example usage:
 * <pre>
 *     Key key = Key.builder()
 *                  .id("1234")
 *                  .type("RSA")
 *                  .algorithm("RS256")
 *                  .publicKey(publicKeyString)
 *                  .privateKey(privateKeyString)
 *                  .active(true)
 *                  .kid("kid-1234")
 *                  .use("sig")
 *                  .build();
 * </pre>
 * </p>
 *
 * @see RSAKey
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Key implements Serializable {

    /**
     * Unique identifier for the key.
     */
    private String id;

    /**
     * Type of the key, typically representing the algorithm type (e.g., RSA).
     */
    private String type;

    /**
     * Algorithm used by the key (e.g., RS256).
     */
    private Algorithm algorithm;

    /**
     * Public key component of this cryptographic key.
     */
    private PublicKey publicKey;

    /**
     * Private key component of this cryptographic key.
     */
    private PrivateKey privateKey;

    /**
     * Indicates whether this key is active or not.
     */
    private boolean active;

    /**
     * Key ID for identifying the key in a unique manner.
     */
    private String kid;

    /**
     * Key usage, typically defining whether it is used for signing or encryption.
     */
    private KeyUse use;

    /**
     * Private constructor to create an instance using the {@link Builder}.
     *
     * @param builder the {@link Builder} instance containing key properties.
     */
    private Key(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.algorithm = builder.algorithm;
        this.publicKey = builder.publicKey;
        this.privateKey = builder.privateKey;
        this.active = builder.active;
        this.kid = builder.kid;
        this.use = builder.use;
    }

    /**
     * Returns a new {@link Builder} instance to facilitate construction of a {@code Key}.
     *
     * @return a new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Converts this {@code Key} instance to an {@link RSAKey}, suitable for use in JWK contexts.
     * This is useful for applications requiring JWK-compliant representation of RSA keys.
     *
     * @return the converted RSAKey instance.
     */
    public RSAKey toRSAKey() {
        return new RSAKey.Builder((RSAPublicKey) publicKey)
            .privateKey(privateKey)
            .keyUse(use)
            .algorithm(algorithm)
            .keyID(kid)
            .build();
    }

    /**
     * Builder class for constructing instances of {@link Key}. Supports a fluent API to set various
     * properties such as ID, type, algorithm, and keys (in PEM format), and enables the creation of
     * {@link Key} instances with specific configurations.
     */
    public static class Builder {
        private String id;
        private String type;
        private Algorithm algorithm;
        private PublicKey publicKey;
        private PrivateKey privateKey;
        private boolean active;
        private String kid;
        private KeyUse use;

        /**
         * Sets the identifier of the key.
         *
         * @param id the unique identifier.
         * @return the Builder instance.
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the type of the key, such as "RSA".
         *
         * @param type the key type.
         * @return the Builder instance.
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the algorithm used by the key, converting the algorithm name to an {@link Algorithm} instance.
         *
         * @param algorithm the algorithm name.
         * @return the Builder instance.
         */
        public Builder algorithm(String algorithm) {
            this.algorithm = new Algorithm(algorithm, Requirement.OPTIONAL);
            return this;
        }

        /**
         * Loads and sets the public key from a PEM-formatted string.
         *
         * @param publicKey the public key in PEM format.
         * @return the Builder instance.
         */
        public Builder publicKey(String publicKey) {
            String formattedPublicKey = PUBLIC_KEY_HEADER + publicKey + PUBLIC_KEY_FOOTER;
            this.publicKey = RsaKeyConverters.x509().convert(new ByteArrayInputStream(
                formattedPublicKey.getBytes(StandardCharsets.UTF_8)));
            return this;
        }

        /**
         * Loads and sets the private key from a PEM-formatted string.
         *
         * @param privateKey the private key in PEM format.
         * @return the Builder instance.
         */
        public Builder privateKey(String privateKey) {
            String formattedPrivateKey = PRIVATE_KEY_HEADER + privateKey + PRIVATE_KEY_FOOTER;
            this.privateKey = RsaKeyConverters.pkcs8().convert(new ByteArrayInputStream(
                formattedPrivateKey.getBytes(StandardCharsets.UTF_8)));
            return this;
        }

        /**
         * Sets the activation status of the key, determining whether the key is currently in use.
         *
         * @param active the active status.
         * @return the Builder instance.
         */
        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        /**
         * Sets the Key ID, a unique identifier for this key.
         *
         * @param kid the key ID.
         * @return the Builder instance.
         */
        public Builder kid(String kid) {
            this.kid = kid;
            return this;
        }

        /**
         * Sets the key usage, defining the purpose of the key (e.g., signing or encryption).
         *
         * @param use the key use purpose.
         * @return the Builder instance.
         */
        public Builder use(String use) {
            this.use = new KeyUse(use);
            return this;
        }

        /**
         * Builds and returns a {@link Key} instance based on the configured properties.
         *
         * @return a new Key instance with the specified configurations.
         */
        public Key build() {
            return new Key(this);
        }
    }
}
