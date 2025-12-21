package com.school.controller;

import com.school.dto.FeePaymentRequest;
import com.school.dto.FeeStructureRequest;
import com.school.model.*;
import com.school.repository.*;
import com.school.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeHeadRepository feeHeadRepository;

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserRepository userRepository;

    // --- Admin Endpoints ---

    @PostMapping("/admin/fees/heads")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFeeHead(@RequestBody FeeHead feeHead) {
        return ResponseEntity.ok(feeHeadRepository.save(feeHead));
    }

    @GetMapping("/admin/fees/heads")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FeeHead>> getAllFeeHeads() {
        return ResponseEntity.ok(feeHeadRepository.findAll());
    }

    @GetMapping("/admin/fees/structure")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FeeStructure>> getAllFeeStructures() {
        return ResponseEntity.ok(feeStructureRepository.findAll());
    }

    @GetMapping("/admin/fees/status/class/{classId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentFee>> getFeesByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(studentFeeRepository.findByStudentSchoolClassId(classId));
    }

    @PostMapping("/admin/fees/structure")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFeeStructure(@jakarta.validation.Valid @RequestBody FeeStructureRequest request) {
        return ResponseEntity.ok(feeService.createFeeStructure(request));
    }

    @PostMapping("/admin/fees/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> payFee(@jakarta.validation.Valid @RequestBody FeePaymentRequest request) {
        return ResponseEntity.ok(feeService.payFee(request));
    }

    // --- Student Endpoints ---

    @GetMapping("/student/fees")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<StudentFee>> getMyFees() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Student student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
                
        return ResponseEntity.ok(studentFeeRepository.findByStudentId(student.getId()));
    }
}
