package com.myvamsnet.monpa.accounting;

import java.time.LocalDate;
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

    public static String generateTransactionReference() {
        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }

    public static String generateTransferReference() {
        return "TRF-" +
                LocalDate.now()
                        .format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

}
