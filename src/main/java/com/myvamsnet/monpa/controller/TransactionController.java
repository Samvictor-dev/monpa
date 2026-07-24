package com.myvamsnet.monpa.controller;

import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.dto.transaction.TransactionHistoryResponse;
import com.myvamsnet.monpa.model.TransactionStatus;
import com.myvamsnet.monpa.model.TransactionType;
import com.myvamsnet.monpa.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<PagedResponse<TransactionHistoryResponse>> getTransactionHistory(

            Authentication authentication,

            @RequestParam(required = false)
            TransactionType type,

            @RequestParam(required = false)
            TransactionStatus status,

            @RequestParam(required = false)
            String reference,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {

        return ResponseEntity.ok(

                transactionService.getTransactionHistory(

                        authentication.getName(),
                        type,
                        status,
                        reference,
                        from,
                        to,
                        page,
                        size

                )

        );

    }

//    Cleaner Version

//    #public ResponseEntity<PagedResponse<TransactionHistoryResponse>> getTransactionHistory(
//            Authentication authentication,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//
//        return ResponseEntity.ok(
//                transactionService.getTransactionHistory(
//                        authentication.getName(),
//                        page,
//                        size
//                )
//        );
//    }
}