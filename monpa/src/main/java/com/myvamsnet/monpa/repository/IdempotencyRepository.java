package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository
        extends JpaRepository<IdempotencyRecord, Long> {

    Optional<IdempotencyRecord>
    findByIdempotencyKey(String key);

}