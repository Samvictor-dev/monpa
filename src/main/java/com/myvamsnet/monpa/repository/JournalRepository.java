package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Long> {

    Optional<Journal> findByJournalReference(String journalReference);

}
