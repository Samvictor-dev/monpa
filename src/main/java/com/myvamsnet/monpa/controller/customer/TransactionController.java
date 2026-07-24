package com.myvamsnet.monpa.controller.customer;

import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.dto.transaction.TransactionFilter;
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

            @ModelAttribute TransactionFilter filter,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {

        return ResponseEntity.ok(

                transactionService.getTransactionHistory(

                        authentication.getName(),
                        filter,
                        page,
                        size

                )

        );

    }

//    Starting point Version

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