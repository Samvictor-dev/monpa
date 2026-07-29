package com.myvamsnet.monpa.accounting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class ReferenceGenerator {

    private ReferenceGenerator() {
    }

    public static String generateJournalReference() {

        return "JRN-"
                + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

    }

    public static String generateLedgerEntryReference() {

        return "LED-"
                + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

    }

}
