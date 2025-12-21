package com.school.dto;

import lombok.Data;

@Data
public class StudentMarksDTO {
    private Long studentId;
    private Double marksObtained;
    private String remarks;
}
