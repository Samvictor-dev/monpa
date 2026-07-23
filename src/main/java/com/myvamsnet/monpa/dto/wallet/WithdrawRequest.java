package com.myvamsnet.monpa.dto.wallet;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


@Getter
@Setter
public class WithdrawRequest {

    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    // getters/setters
}