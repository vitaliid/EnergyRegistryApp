package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.AdministrationUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CacheTestService {

    private final AdministrationUnitRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadFirst(Integer id) {
        repository.findById(id).orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadSecond(Integer id) {
        repository.findById(id).orElseThrow();
    }
}
