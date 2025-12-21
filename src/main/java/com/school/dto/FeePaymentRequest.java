package com.school.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class FeePaymentRequest {
    @NotNull(message = "Student Fee ID is required")
    private Long studentFeeId;

    @NotNull(message = "Paid Amount is required")
    private BigDecimal paidAmount;
    
    private String paymentMode; // CASH, ONLINE
}
