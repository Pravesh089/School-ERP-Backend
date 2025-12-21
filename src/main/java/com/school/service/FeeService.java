package com.school.service;

import com.school.dto.FeePaymentRequest;
import com.school.dto.FeeStructureRequest;
import com.school.model.*;
import com.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FeeService {

    @Autowired
    private FeeHeadRepository feeHeadRepository;

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Creates a new Fee Structure and assigns it to all students in the class.
     */
    @Transactional
    public FeeStructure createFeeStructure(FeeStructureRequest request) {
        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        FeeHead feeHead = feeHeadRepository.findById(request.getFeeHeadId())
                .orElseThrow(() -> new RuntimeException("Fee Head not found"));

        // 1. Create Structure
        FeeStructure structure = new FeeStructure();
        structure.setSchoolClass(schoolClass);
        structure.setFeeHead(feeHead);
        structure.setAmount(request.getAmount());
        structure.setAcademicYear(request.getAcademicYear());
        structure.setDueDate(request.getDueDate());
        
        structure = feeStructureRepository.save(structure);

        // 2. Assign to all existing students in that class
        // Fetch students by classId. Wait, StudentRepository needs a method for this? 
        // We have findBySchoolClassId (or similar) let's check repo. 
        // StudentRepository has findBySchoolClassIdAndSectionId, but maybe not just ClassId. 
        // Let's assume we fetch all via iteration or add a method. 
        // Ideally we should add findBySchoolClassId to StudentRepository.
        // For now, I will add logic to fetch via existing or add new repo method.
        
        // Assuming findBySchoolClassId logic or fetching all and filtering (bad for perf).
        // Let's rely on adding the method to repository in next step if needed.
        // Actually, current StudentRepository has: findBySchoolClassIdAndSectionId. 
        // I'll add findBySchoolClassId to repo now.
        
        List<Student> students = studentRepository.findBySchoolClassId(schoolClass.getId());
        
        for (Student s : students) {
            StudentFee sf = new StudentFee();
            sf.setStudent(s);
            sf.setFeeStructure(structure);
            sf.setStatus(FeeStatus.PENDING);
            studentFeeRepository.save(sf);
        }

        return structure;
    }

    /**
     * Records a fee payment.
     */
    @Transactional
    public StudentFee payFee(FeePaymentRequest request) {
        StudentFee fee = studentFeeRepository.findById(request.getStudentFeeId())
                .orElseThrow(() -> new RuntimeException("Fee record not found"));

        if (fee.getStatus() == FeeStatus.PAID) {
            throw new RuntimeException("Fee already paid!");
        }

        fee.setPaidAmount(request.getPaidAmount());
        fee.setPaidDate(LocalDate.now());
        fee.setStatus(FeeStatus.PAID);
        fee.setTransactionRef(UUID.randomUUID().toString()); // Simple ref generation

        return studentFeeRepository.save(fee);
    }
}
