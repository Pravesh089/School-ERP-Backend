package com.school.repository;

import com.school.model.TimetableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;

public interface TimetableRepository extends JpaRepository<TimetableSlot, Long> {
    List<TimetableSlot> findBySchoolClassIdAndSectionId(Long classId, Long sectionId);
    List<TimetableSlot> findByTeacherId(Long teacherId);
    List<TimetableSlot> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<TimetableSlot> findBySchoolClassIdAndSectionIdAndDayOfWeek(Long classId, Long sectionId, DayOfWeek dayOfWeek);
    List<TimetableSlot> findByTeacherIdAndDayOfWeek(Long teacherId, DayOfWeek dayOfWeek);
}
