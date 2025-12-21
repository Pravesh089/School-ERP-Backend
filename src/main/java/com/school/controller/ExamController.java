package com.school.controller;

import com.school.dto.MarksEntryRequest;
import com.school.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST Controller for Examination management.
 * Handles the entry of marks for students.
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    /**
     * Enters or Updates marks for students for a specific Exam combination (Class + Subject).
     * 
     * @param request The request containing Exam ID, Subject ID, Class ID, and a list of student marks.
     * @return Success message.
     */
    @PostMapping("/marks/entry")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> enterMarks(@RequestBody MarksEntryRequest request) {
        examService.enterMarks(request);
        return ResponseEntity.ok("Marks entered successfully.");
    }
}
