package com.myvamsnet.monpa.accounting;

import com.myvamsnet.monpa.model.Journal;
import com.myvamsnet.monpa.model.JournalStatus;
import com.myvamsnet.monpa.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final JournalRepository journalRepository;

    public Journal post (Journal journal) {

        journal.setStatus(JournalStatus.POSTED);
        journal.setPostedAt(LocalDateTime.now());

        return journalRepository.save(journal);

    }

}