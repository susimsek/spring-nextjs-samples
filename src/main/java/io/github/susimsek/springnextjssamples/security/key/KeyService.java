package io.github.susimsek.springnextjssamples.security.key;

import java.util.List;
import org.springframework.lang.Nullable;

/**
 * {@code KeyService} defines the contract for managing cryptographic keys within the application.
 * This interface provides methods for retrieving, saving, and deleting keys, as well as methods
 * for finding keys by their unique identifiers or key IDs (kid).
 *
 * <p>Implementations of this interface are expected to handle the storage and retrieval of keys,
 * allowing for operations necessary in security contexts, such as JWT signing and verification.</p>
 *
 * <p>Example usage:
 * <pre>
 *     KeyService keyService = new KeyServiceImpl();
 *     List&lt;Key&gt; allKeys = keyService.findAll();
 *     Key key = keyService.findById("1234");
 * </pre>
 * </p>
 *
 * @see Key
 */
public interface KeyService {

    /**
     * Retrieves a list of all available {@link Key} instances.
     *
     * @return a list of all stored keys.
     */
    List<Key> findAll();

    /**
     * Saves the given {@link Key} instance, adding it to the storage or updating it if it already exists.
     *
     * @param key the key to be saved.
     */
    void save(Key key);

    /**
     * Removes the specified {@link Key} from storage.
     *
     * @param key the key to be removed.
     */
    void remove(Key key);

    /**
     * Finds a {@link Key} by its unique identifier.
     *
     * @param id the unique identifier of the key.
     * @return the found key, or {@code null} if no key with the specified ID exists.
     */
    @Nullable
    Key findById(String id);

    /**
     * Finds a {@link Key} by its key ID (kid).
     *
     * @param kid the key ID.
     * @return the found key, or {@code null} if no key with the specified kid exists.
     */
    @Nullable
    Key findByKid(String kid);

    /**
     * Finds a {@link Key} by its key ID (kid).
     *
     * @param kid the key ID.
     * @return the found key if it exists.
     */
    Key findByKidOrThrow(String kid);
}
