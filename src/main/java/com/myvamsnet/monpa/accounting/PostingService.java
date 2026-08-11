package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.accounting.validator.JournalValidator;
import com.myvamsnet.monpa.model.Journal;
import com.myvamsnet.monpa.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class PostingService {

    private final JournalRepository journalRepository;

    private final JournalValidator journalValidator;

    @Transactional
    public Journal post (Journal journal) {

        journalValidator.validate(journal);

        journal.post();

        return journalRepository.save(journal);
    }

}