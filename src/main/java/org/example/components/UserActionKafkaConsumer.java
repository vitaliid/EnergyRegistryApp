package org.example.components;

import lombok.RequiredArgsConstructor;
import org.example.entity.UserActionLog;
import org.example.domain.UserActionEvent;
import org.example.repository.UserActionLogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserActionKafkaConsumer {

    private final UserActionLogRepository repository;

    @KafkaListener(
            topics = "${app.kafka.topics.user-actions}",
            groupId = "homeenergyregistry-user-action-log-writer"
    )
    @Transactional(transactionManager = "transactionManager")
    public void consume(UserActionEvent event) {
        repository.save(UserActionLog.from(event));
    }
}