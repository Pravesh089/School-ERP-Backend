package com.school.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MarkAttendanceRequest {
    private Long classId;
    private Long sectionId;
    private LocalDate date;
    private List<StudentAttendanceDTO> attendanceList;
}
