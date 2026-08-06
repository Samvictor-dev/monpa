package com.myvamsnet.monpa.controller.admin;

import com.myvamsnet.monpa.application.reversal.ReverseTransactionUseCase;
import com.myvamsnet.monpa.dto.common.ApiResponse;
import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.dto.transaction.AdminTransactionFilter;
import com.myvamsnet.monpa.dto.transaction.TransactionHistoryResponse;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.service.admin.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminTransactionController {

    private final AdminTransactionService adminTransactionService;

    private final ReverseTransactionUseCase reverseTransactionUseCase;

    @GetMapping
    public PagedResponse<TransactionHistoryResponse> getAllTransactions(

            @ModelAttribute AdminTransactionFilter filter,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size

    ) {

        return adminTransactionService.getAllTransactions(
                filter,
                page,
                size
        );

    }

    @PostMapping("/{transactionReference}/reverse")
    public ResponseEntity<ApiResponse<TransactionResponse>> reverse(
            @PathVariable String transactionReference
    ) {

        TransactionResponse response =
                reverseTransactionUseCase.reverse(
                        transactionReference
                );

        return ResponseEntity.ok(

                ApiResponse.success(
                        "Transaction reversed successfully.",
                        response
                )

        );

    }



}