package com.myvamsnet.monpa.util;

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

}
