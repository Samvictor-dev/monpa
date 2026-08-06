package com.myvamsnet.monpa.transfer.controller;

import com.myvamsnet.monpa.application.transfer.TransferUseCase;
import com.myvamsnet.monpa.dto.transaction.TransactionResponse;
import com.myvamsnet.monpa.transfer.dto.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferUseCase transferUseCase;

    @PostMapping
    public ResponseEntity<TransactionResponse> transfer(
            Authentication authentication,
            @RequestBody TransferRequest request
    ) {

        String email = authentication.getName();

        TransactionResponse response =
                transferUseCase.transfer(email, request);

        return ResponseEntity.ok(response);
    }
}


//    @PostMapping("/transfer")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<TransactionResponse> transfer(
//            Authentication authentication,
//            @Valid @RequestBody TransferRequest request) {
//
//        return ResponseEntity.ok(
//
//                transferService.transfer(
//                        authentication.getName(),
//                        request
//                )
//
//        );
//    }

//    @PostMapping("/transfer")
//    public ResponseEntity<TransactionResponse> transfer(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @Valid @RequestBody TransferRequest request) {
//
//        TransactionResponse response =
//                walletService.transfer(
//                        userDetails.getUsername(),
//                        request
//                );
//
//        return ResponseEntity.ok(response);
//    }


