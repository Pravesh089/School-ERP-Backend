package com.school.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a Fee Head (Category).
 * Examples: Tuition Fee, Transport Fee, Library Fee.
 */
@Entity
@Table(name = "fee_heads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeHead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
