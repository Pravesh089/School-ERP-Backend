package com.school.service;

import com.school.dto.TimetableSlotRequest;
import com.school.model.*;
import com.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public TimetableSlot createSlot(TimetableSlotRequest request) {
        // Validate IDs
        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Overlap Checks
        validateNoOverlap(request);

        TimetableSlot slot = new TimetableSlot();
        slot.setSchoolClass(schoolClass);
        slot.setSection(section);
        slot.setSubject(subject);
        slot.setTeacher(teacher);
        slot.setDayOfWeek(request.getDayOfWeek());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());

        return timetableRepository.save(slot);
    }

    private void validateNoOverlap(TimetableSlotRequest request) {
        // 1. Check Class/Section Overlap
        List<TimetableSlot> classSlots = timetableRepository.findBySchoolClassIdAndSectionIdAndDayOfWeek(
                request.getClassId(), request.getSectionId(), request.getDayOfWeek());
        
        for (TimetableSlot slot : classSlots) {
            if (isOverlapping(request.getStartTime(), request.getEndTime(), slot.getStartTime(), slot.getEndTime())) {
                throw new RuntimeException("Class/Section already has a slot at this time on " + request.getDayOfWeek());
            }
        }

        // 2. Check Teacher Overlap
        List<TimetableSlot> teacherSlots = timetableRepository.findByTeacherIdAndDayOfWeek(
                request.getTeacherId(), request.getDayOfWeek());
        
        for (TimetableSlot slot : teacherSlots) {
            if (isOverlapping(request.getStartTime(), request.getEndTime(), slot.getStartTime(), slot.getEndTime())) {
                throw new RuntimeException("Teacher is already busy at this time on " + request.getDayOfWeek());
            }
        }
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public List<TimetableSlot> getClassTimetable(Long classId, Long sectionId) {
        return timetableRepository.findBySchoolClassIdAndSectionId(classId, sectionId);
    }

    public List<TimetableSlot> getTeacherSchedule(Long teacherId) {
        return timetableRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public void deleteSlot(Long id) {
        timetableRepository.deleteById(id);
    }
}
