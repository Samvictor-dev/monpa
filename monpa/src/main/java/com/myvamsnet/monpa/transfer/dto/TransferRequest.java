package com.myvamsnet.monpa.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class TransferRequest {

    // Input Fields

    // 1) Enter Account Number:
    @NotBlank(message = "Destination account number is required")
    private String destinationAccountNumber;


    // 2) Enter Account Amount:
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;


    // 3) Enter Description:
    @NotBlank(message = "Description is required")
    private String description;


}
