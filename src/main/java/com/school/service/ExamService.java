package com.school.service;

import com.school.dto.MarksEntryRequest;
import com.school.dto.StudentMarksDTO;
import com.school.model.*;
import com.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Service class for managing Examination logic.
 */
@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Enters marks for a list of students for a specific exam schedule.
     * 
     * @param request The request containing exam, class, subject details, and the marks list.
     * @throws IllegalArgumentException If marks exceed max marks or schedule is not found.
     */
    @Transactional
    public void enterMarks(MarksEntryRequest request) {
        // 1. Find the Exam Schedule matching Exam + Class + Subject
        // (Assuming a unique schedule exists for this combination)
        ExamSchedule schedule = examScheduleRepository.findAll().stream()
                .filter(es -> es.getExam().getId().equals(request.getExamId()) &&
                              es.getSchoolClass().getId().equals(request.getClassId()) &&
                              es.getSubject().getId().equals(request.getSubjectId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Exam Schedule not found for given details"));

        // 2. Iterate through students and save marks
        for (StudentMarksDTO dto : request.getStudentMarks()) {
            // Validation: Ensure marks do not exceed the maximum allowed for this exam
            if (dto.getMarksObtained() > schedule.getMaxMarks()) {
                throw new IllegalArgumentException("Marks obtained cannot exceed max marks: " + schedule.getMaxMarks());
            }

            Student student = studentRepository.findById(dto.getStudentId()).orElseThrow();

            // Handle Updates: Check if marks already entered for this student
            Optional<Marks> existing = marksRepository.findByExamScheduleIdAndStudentId(schedule.getId(), student.getId());
            Marks marks = existing.orElse(new Marks());
            
            marks.setExamSchedule(schedule);
            marks.setStudent(student);
            marks.setMarksObtained(dto.getMarksObtained());
            marks.setRemarks(dto.getRemarks());
            
            marksRepository.save(marks);
        }
    }
}
