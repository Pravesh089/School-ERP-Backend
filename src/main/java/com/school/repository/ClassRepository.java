package com.school.repository;

import com.school.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByClassNumber(Integer classNumber);
}
