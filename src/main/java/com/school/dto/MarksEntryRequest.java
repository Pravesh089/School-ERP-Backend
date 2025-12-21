package com.school.dto;

import lombok.Data;
import java.util.List;

@Data
public class MarksEntryRequest {
    private Long examId;
    private Long subjectId;
    private Long classId;
    private List<StudentMarksDTO> studentMarks;
}
