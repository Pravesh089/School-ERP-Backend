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
    private StudentRepository studentRepository;
    
    @Autowired
    private UserRepository userRepository;

    // --- Admin Endpoints ---

    @PostMapping("/admin/fees/heads")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFeeHead(@RequestBody FeeHead feeHead) {
        return ResponseEntity.ok(feeHeadRepository.save(feeHead));
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
