package com.school.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing the ledger of fees for a student.
 * Tracks payment status of a specific fee structure assignment.
 */
@Entity
@Table(name = "student_fees", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "fee_structure_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "fee_structure_id", nullable = false)
    private FeeStructure feeStructure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeStatus status = FeeStatus.PENDING;

    private LocalDate paidDate;

    private BigDecimal paidAmount;

    private String transactionRef;
}
