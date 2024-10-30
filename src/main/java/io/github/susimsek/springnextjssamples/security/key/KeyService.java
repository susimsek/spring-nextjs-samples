//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.susimsek.springnextjssamples.security.key;

import java.util.List;
import org.springframework.lang.Nullable;

public interface KeyService {

    List<Key> findAll();

    void save(Key key);

    void remove(Key key);

    @Nullable
    Key findById(String id);

    @Nullable
    Key findByKid(String kid);

    Key findByKidOrThrow(String kid);
}
