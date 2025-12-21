package com.school.controller;

import com.school.dto.MarkAttendanceRequest;
import com.school.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Attendance management.
 * Handles the recording of student attendance by Teachers.
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * Marks attendance for a specific class and section on a given date.
     * The authenticated user (Teacher) is automatically recorded as the one marking the attendance.
     * 
     * @param request The request body containing date, class, section, and list of students present/absent.
     * @return A success message upon completion.
     */
    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> markAttendance(@RequestBody MarkAttendanceRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        attendanceService.markAttendance(request, username);
        return ResponseEntity.ok("Attendance marked successfully.");
    }
}
