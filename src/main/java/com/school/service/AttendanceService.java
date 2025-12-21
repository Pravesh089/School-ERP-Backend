package com.school.service;

import com.school.dto.MarkAttendanceRequest;
import com.school.dto.StudentAttendanceDTO;
import com.school.model.*;
import com.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service class for managing Attendance logic.
 */
@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Marks attendance for a list of students on a specific date.
     * 
     * @param request The attendance request containing class, section, date, and student statuses.
     * @param username The username of the teacher marking the attendance.
     * @throws IllegalArgumentException if the date is in the future or the user is not a teacher.
     */
    @Transactional
    public void markAttendance(MarkAttendanceRequest request, String username) {
        // 1. Validate Date
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot mark attendance for future date.");
        }

        // 2. Identify the Teacher from the username
        User user = userRepository.findByUsername(username).orElseThrow();
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not a teacher"));

        // 3. Resolve Class and Section
        SchoolClass schoolClass = classRepository.findById(request.getClassId()).orElseThrow();
        Section section = sectionRepository.findById(request.getSectionId()).orElseThrow();

        // 4. Iterate and Save Attendance
        for (StudentAttendanceDTO dto : request.getAttendanceList()) {
            Student student = studentRepository.findById(dto.getStudentId()).orElseThrow();
            
            // Handle Updates: Check if attendance record already exists for this day key
            Optional<Attendance> existing = attendanceRepository.findByStudentIdAndDate(student.getId(), request.getDate());
            
            Attendance attendance = existing.orElse(new Attendance());
            attendance.setStudent(student);
            attendance.setSchoolClass(schoolClass);
            attendance.setSection(section);
            attendance.setDate(request.getDate());
            attendance.setStatus(AttendanceStatus.valueOf(dto.getStatus()));
            attendance.setMarkedBy(teacher);
            
            attendanceRepository.save(attendance);
        }
    }
}
