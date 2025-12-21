package com.school.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FeeStructureRequest {
    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Fee Head ID is required")
    private Long feeHeadId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Academic Year is required")
    private String academicYear;

    @NotNull(message = "Due Date is required")
    private LocalDate dueDate;
}
