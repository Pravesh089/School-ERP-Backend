package com.school.repository;

import com.school.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Marks entity.
 * Provides lookup for existing marks for a specific student and exam schedule.
 */
public interface MarksRepository extends JpaRepository<Marks, Long> {
    Optional<Marks> findByExamScheduleIdAndStudentId(Long examScheduleId, Long studentId);
    java.util.List<Marks> findByStudentId(Long studentId);
}
