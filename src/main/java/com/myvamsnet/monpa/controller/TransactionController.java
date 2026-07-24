package com.myvamsnet.monpa.controller;

import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.dto.transaction.TransactionHistoryResponse;
import com.myvamsnet.monpa.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<PagedResponse<TransactionHistoryResponse>> getTransactionHistory(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        String email = authentication.getName();

        PagedResponse<TransactionHistoryResponse> response =
                transactionService.getTransactionHistory(
                        email,
                        page,
                        size
                );

        return ResponseEntity.ok(response);
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