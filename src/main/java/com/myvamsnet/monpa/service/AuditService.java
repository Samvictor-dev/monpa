package com.myvamsnet.monpa.service;

import com.myvamsnet.monpa.model.AuditAction;
import com.myvamsnet.monpa.model.AuditLog;
import com.myvamsnet.monpa.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;

    public void log(

            String actor,

            String target,

            AuditAction action,

            String description

    ) {

        AuditLog audit = AuditLog.builder()

                .actorEmail(actor)

                .targetEmail(target)

                .action(action)

                .description(description)

                .build();

        repository.save(audit);

    }

}