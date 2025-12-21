package com.school.repository;

import com.school.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Attendance entity.
 * Provides custom queries to fetch attendance by student/date or class/date.
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
    List<Attendance> findBySchoolClassIdAndSectionIdAndDate(Long classId, Long sectionId, LocalDate date);
    List<Attendance> findByStudentId(Long studentId);
}
