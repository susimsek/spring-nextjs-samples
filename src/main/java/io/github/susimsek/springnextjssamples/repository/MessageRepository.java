package io.github.susimsek.springnextjssamples.repository;


import io.github.susimsek.springnextjssamples.entity.MessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByLocale(String locale);

}
