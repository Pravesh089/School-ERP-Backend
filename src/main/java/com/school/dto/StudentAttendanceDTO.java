package com.school.dto;

import lombok.Data;

@Data
public class StudentAttendanceDTO {
    private Long studentId;
    private String status; // PRESENT, ABSENT, LEAVE
}
