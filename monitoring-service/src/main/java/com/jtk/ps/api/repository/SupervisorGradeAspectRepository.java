package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGrade;
import com.jtk.ps.api.model.SupervisorGradeAspect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupervisorGradeAspectRepository extends JpaRepository<SupervisorGradeAspect, Integer> {
    SupervisorGradeAspect findById(int id);
}
