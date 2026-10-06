package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.UserActionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CacheTestService {

    private final UserActionLogRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadFirst(Long id) {
        repository.findById(id).orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadSecond(Long id) {
        repository.findById(id).orElseThrow();
    }
}
