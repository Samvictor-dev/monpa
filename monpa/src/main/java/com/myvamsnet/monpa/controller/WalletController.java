package com.myvamsnet.monpa.controller;

import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.dto.wallet.DepositRequest;
import com.myvamsnet.monpa.dto.wallet.WalletResponse;
import com.myvamsnet.monpa.dto.wallet.WithdrawRequest;
import com.myvamsnet.monpa.security.CustomUserDetails;
import com.myvamsnet.monpa.service.TransactionService;
import com.myvamsnet.monpa.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    private final TransactionService transactionService;

    public WalletController(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    @GetMapping("/freeze")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<WalletResponse> freezeWallet(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                walletService.freezeWallet(authentication.getName())
        );

    }

    @GetMapping("/me")
    public ResponseEntity<WalletResponse> getMyWallet(
            Authentication authentication) {

        return ResponseEntity.ok(
                walletService.getMyWallet(authentication.getName())
        );
    }

    @GetMapping("/test")
    public ResponseEntity<WalletResponse> getWalletByUserId(
            Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        assert user != null;
        return ResponseEntity.ok(
                walletService.getWalletByUserId(user.getId())
        );
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            Authentication authentication,
            @Valid @RequestBody DepositRequest request) {

        return ResponseEntity.ok(
                walletService.deposit(
                        authentication.getName(),
                        request
                )
        );
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public WalletResponse withdraw(
            Authentication authentication,
            @Valid @RequestBody WithdrawRequest request
    ) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        assert user != null;
        return walletService.withdraw(
                user.getId(),
                request
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public WalletResponse getWalletByUserId(
            @PathVariable Long id
    ) {
        return walletService.getWalletByUserId(id);
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public List<TransactionResponse> getTransactionHistory(
            Authentication authentication
    ) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        assert user != null;
        return transactionService.getTransactionHistory(user.getId());
    }

}