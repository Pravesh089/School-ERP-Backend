package com.school.repository;

import com.school.model.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
    List<FeeStructure> findBySchoolClassId(Long classId);
}
