package com.school.controller;

import com.school.dto.TimetableSlotRequest;
import com.school.model.TimetableSlot;
import com.school.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    // --- Admin Endpoints ---

    @PostMapping("/admin/timetable/slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimetableSlot> createSlot(@Valid @RequestBody TimetableSlotRequest request) {
        return ResponseEntity.ok(timetableService.createSlot(request));
    }

    @DeleteMapping("/admin/timetable/slots/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSlot(@PathVariable Long id) {
        timetableService.deleteSlot(id);
        return ResponseEntity.ok("Slot deleted successfully");
    }

    // --- General Viewing Endpoints ---

    @GetMapping("/timetable/class/{classId}/section/{sectionId}")
    public ResponseEntity<List<TimetableSlot>> getClassTimetable(
            @PathVariable Long classId, @PathVariable Long sectionId) {
        return ResponseEntity.ok(timetableService.getClassTimetable(classId, sectionId));
    }

    @GetMapping("/timetable/teacher/{teacherId}")
    public ResponseEntity<List<TimetableSlot>> getTeacherSchedule(@PathVariable Long teacherId) {
        return ResponseEntity.ok(timetableService.getTeacherSchedule(teacherId));
    }
}
