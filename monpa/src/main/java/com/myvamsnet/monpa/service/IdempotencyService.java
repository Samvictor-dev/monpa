package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.model.IdempotencyRecord;
import com.myvamsnet.monpa.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    public Optional<IdempotencyRecord>
    find(String key) {

        return idempotencyRepository.findByIdempotencyKey(key);

    }

//    public IdempotencyRecord save(...) {
//
//    ...
//    }
}