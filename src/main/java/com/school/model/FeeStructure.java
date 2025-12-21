package com.school.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing the Fee Structure for a specific Class and Academic Year.
 * Defines how much amount is to be paid for a specific Fee Head.
 */
@Entity
@Table(name = "fee_structures", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_id", "fee_head_id", "academic_year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @ManyToOne
    @JoinColumn(name = "fee_head_id", nullable = false)
    private FeeHead feeHead;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "academic_year", nullable = false)
    private String academicYear; // e.g., "2024-25"

    @Column(nullable = false)
    private LocalDate dueDate;
}
