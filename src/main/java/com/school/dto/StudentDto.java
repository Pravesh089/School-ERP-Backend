package com.school.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class StudentDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    private String lastName;
    
    @NotBlank(message = "Admission number is required")
    private String admissionNo;
    
    @NotNull(message = "Roll number is required")
    private Integer rollNo;
    
    @NotNull(message = "Class ID is required")
    private Long classId;
    
    @NotNull(message = "Section ID is required")
    private Long sectionId;
    
    private LocalDate dob;
    
    @NotBlank(message = "Gender is required (M, F, O)")
    private String gender; // M, F, O
    
    private String parentContact;
    
    private String password; // Optional
}
