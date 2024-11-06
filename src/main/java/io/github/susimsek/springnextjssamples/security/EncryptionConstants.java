package io.github.susimsek.springnextjssamples.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for encryption and decryption configurations.
 * <p>
 * Provides constant values used for RSA encryption, key formatting, and charset encoding.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptionConstants {

    /**
     * The RSA algorithm name for cryptographic operations.
     */
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * The RSA transformation string specifying the encryption mode and padding.
     */
    public static final String RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    /**
     * Header for the PEM-formatted public key.
     */
    public static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----\n";

    /**
     * Footer for the PEM-formatted public key.
     */
    public static final String PUBLIC_KEY_FOOTER = "\n-----END PUBLIC KEY-----";

    /**
     * Header for the PEM-formatted private key.
     */
    public static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----\n";

    /**
     * Footer for the PEM-formatted private key.
     */
    public static final String PRIVATE_KEY_FOOTER = "\n-----END PRIVATE KEY-----";

    /**
     * The character encoding used for encryption-related operations.
     */
    public static final String CHARSET = "UTF-8";

    /**
     * The claim name used to store encrypted data in the JWT.
     */
    public static final String CLAIM_NAME = "data";
}
