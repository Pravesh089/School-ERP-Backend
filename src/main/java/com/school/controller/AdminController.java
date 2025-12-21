package com.school.controller;

import com.school.dto.StudentDto;
import com.school.dto.TeacherDto;
import com.school.model.*;
import com.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST Controller for Admin operations.
 * Manages administrative tasks such as creating Classes, Sections, Subjects, and Students.
 * This controller is typically secured to allow only users with ROLE_ADMIN (though current SecurityConfig allows all authenticated users for simplicity in MVP).
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Classes ---

    /**
     * Creates a new School Class (e.g., Grade 10, Grade 5).
     * 
     * @param schoolClass The school class object containing classNumber and academicYear.
     * @return The saved SchoolClass entity.
     */
    @PostMapping("/classes")
    public ResponseEntity<?> createClass(@RequestBody SchoolClass schoolClass) {
        return ResponseEntity.ok(classRepository.save(schoolClass));
    }

    /**
     * Retrieves all existing School Classes.
     * 
     * @return A list of all SchoolClass entities.
     */
    @GetMapping("/classes")
    public List<SchoolClass> getAllClasses() {
        return classRepository.findAll();
    }

    // --- Sections ---

    /**
     * Adds a new Section (e.g., 'A', 'B') to a specific School Class.
     * 
     * @param classId The ID of the School Class to add the section to.
     * @param section The section object to create.
     * @return The saved Section entity linked to the class.
     */
    @PostMapping("/classes/{classId}/sections")
    public ResponseEntity<?> addSection(@PathVariable Long classId, @RequestBody Section section) {
        SchoolClass schoolClass = classRepository.findById(classId).orElseThrow();
        section.setSchoolClass(schoolClass);
        return ResponseEntity.ok(sectionRepository.save(section));
    }

    // --- Subjects ---

    /**
     * Creates a new Subject (e.g., Mathematics, Science).
     * 
     * @param subject The subject entity to create.
     * @return The saved Subject entity.
     */
    @PostMapping("/subjects")
    public ResponseEntity<?> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectRepository.save(subject));
    }
    
    // --- Students ---

    /**
     * Registers a new Student in the system.
     * This method performs a transactional-like operation:
     * 1. Creates a User entity for the student (for login).
     * 2. Creates a Student entity linked to that User, assigned to a Class and Section.
     * 
     * @param dto The Data Transfer Object containing student details and class/section IDs.
     * @return The saved Student entity.
     */
    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@jakarta.validation.Valid @RequestBody StudentDto dto) {
        // 1. Create User
        User user = new User();
        user.setUsername(dto.getAdmissionNo()); // Username is Admission No
        user.setPassword(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : "123456"));
        user.setRole(Role.ROLE_STUDENT);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        // 2. Create Student
        Student student = new Student();
        student.setUser(user);
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setAdmissionNo(dto.getAdmissionNo());
        student.setRollNo(dto.getRollNo());
        student.setDob(dto.getDob());
        student.setParentContact(dto.getParentContact());
        
        // Gender Enum
        if (dto.getGender() != null) {
             student.setGender(Gender.valueOf(dto.getGender())); 
        }
        
        SchoolClass schoolClass = classRepository.findById(dto.getClassId()).orElseThrow();
        Section section = sectionRepository.findById(dto.getSectionId()).orElseThrow();
        
        student.setSchoolClass(schoolClass);
        student.setSection(section);
        
        return ResponseEntity.ok(studentRepository.save(student));
    }

    // --- Teachers ---

    /**
     * Registers a new Teacher in the system.
     * 1. Creates User (ROLE_TEACHER).
     * 2. Creates Teacher entity linked to User.
     * 
     * @param dto The teacher details.
     * @return The saved Teacher entity.
     */
    @PostMapping("/teachers")
    public ResponseEntity<?> createTeacher(@jakarta.validation.Valid @RequestBody TeacherDto dto) {
        // 1. Create User
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_TEACHER);
        user.setActive(true);
        user.setCreatedAt(java.time.LocalDateTime.now());
        user = userRepository.save(user);

        // 2. Create Teacher
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setName(dto.getName());
        teacher.setQualification(dto.getQualification());
        teacher.setEmail(dto.getEmail());
        
        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    // --- Exams ---

    @PostMapping("/exams")
    public ResponseEntity<?> createExam(@RequestBody Exam exam) {
        return ResponseEntity.ok(examRepository.save(exam));
    }

    @PostMapping("/exam-schedules")
    public ResponseEntity<?> createExamSchedule(@RequestBody ExamSchedule schedule) {
        // Simple implementation: Ensure Exam/Class/Subject objects are provided or handle lookup
        return ResponseEntity.ok(examScheduleRepository.save(schedule));
    }
}
