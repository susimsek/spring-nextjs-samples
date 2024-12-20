package io.github.susimsek.springnextjssamples.service;

import io.github.susimsek.springnextjssamples.config.cache.CacheName;
import io.github.susimsek.springnextjssamples.entity.MessageEntity;
import io.github.susimsek.springnextjssamples.repository.MessageRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Cacheable(value = CacheName.MESSAGES_CACHE, key = "#locale")
    public Map<String, String> getMessages(String locale) {
        return loadMessagesFromDatabase(locale);
    }

    private Map<String, String> loadMessagesFromDatabase(String locale) {
        List<MessageEntity> messages = messageRepository.findByLocale(locale);
        return messages.stream()
            .collect(Collectors.toMap(MessageEntity::getCode, MessageEntity::getContent));
    }
}
