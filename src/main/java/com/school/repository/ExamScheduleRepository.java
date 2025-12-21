package com.school.repository;

import com.school.model.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
}
