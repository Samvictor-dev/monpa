package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository
        extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findByJournalId(Long journalId);

}
