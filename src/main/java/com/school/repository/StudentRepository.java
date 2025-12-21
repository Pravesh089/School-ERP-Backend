package com.school.repository;

import com.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByAdmissionNo(String admissionNo);
    java.util.List<Student> findBySchoolClassIdAndSectionId(Long classId, Long sectionId);
    java.util.List<Student> findBySchoolClassId(Long classId);
    java.util.Optional<Student> findByUserId(Long userId);
}
