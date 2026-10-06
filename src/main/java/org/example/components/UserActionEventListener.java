package org.example.components;

import lombok.RequiredArgsConstructor;
import org.example.entity.UserActionLog;
import org.example.domain.UserActionEvent;
import org.example.repository.UserActionLogRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserActionEventListener {

    private final UserActionLogRepository repository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void store(UserActionEvent event) {
        repository.save(UserActionLog.from(event));
    }
}