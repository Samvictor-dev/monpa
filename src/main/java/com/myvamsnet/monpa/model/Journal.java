package com.myvamsnet.monpa.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, unique = true)
    private String journalReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalStatus status;

    @Column(nullable = false)
    private String narration;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime postedAt;

}