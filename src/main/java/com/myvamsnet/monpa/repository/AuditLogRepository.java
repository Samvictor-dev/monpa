package com.myvamsnet.monpa.repository;

import com.myvamsnet.monpa.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

}
