package com.school.controller;

import com.school.model.*;
import com.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST Controller for Student Portal operations.
 * Allows students to view their own profile, attendance, and marks.
 */
@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository; // Needed if we want to show exam details with marks, but marks are linked to schedule. 
    // Actually Marks entity has ExamSchedule, so we can fetch Marks and it will contain the schedule info? 
    // Yes, if we didn't use DTOs that hide it. But we should probably return a DTO or the entity (ignoring recursion).
    // Let's stick to Entities for MVP simplicity, relying on Jackson to handle serialization (careful with recursion).
    // Student -> User (OneToOne) OK.
    // Marks -> Student (Backref?)
    // Marks -> ExamSchedule -> Exam.
    
    private Student getLoggedStudent() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found for user: " + username));
    }

    /**
     * Retrieves the profile details of the currently logged-in student.
     */
    @GetMapping("/profile")
    public ResponseEntity<Student> getProfile() {
        return ResponseEntity.ok(getLoggedStudent());
    }

    /**
     * Retrieves the attendance history for the logged-in student.
     */
    @GetMapping("/attendance")
    public ResponseEntity<List<Attendance>> getMyAttendance() {
        Student student = getLoggedStudent();
        // We only have repository method findByStudentIdAndDate (single).
        // need findByStudentId (list).
        // I need to check AttendanceRepository if it has findByStudentId. 
        // LLD said findByStudentIdAndDate. Inspecting repo previously showed findByStudentIdAndDate and findByClass...
        // I will need to add findByStudentId to repository.
        // For now, I'll assume I'll add it.
        return ResponseEntity.ok(attendanceRepository.findByStudentId(student.getId()));
    }

    /**
     * Retrieves all marks for the logged-in student.
     */
    @GetMapping("/marks")
    public ResponseEntity<List<Marks>> getMyMarks() {
        Student student = getLoggedStudent();
        // Similarly, need findByStudentId in MarksRepository.
        return ResponseEntity.ok(marksRepository.findByStudentId(student.getId()));
    }
}
