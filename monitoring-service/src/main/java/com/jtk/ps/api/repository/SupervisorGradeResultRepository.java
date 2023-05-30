package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGradeResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupervisorGradeResultRepository extends JpaRepository<SupervisorGradeResult, Integer> {
    List<SupervisorGradeResult> findBySupervisorGradeId(int id);
//    List<SupervisorGradeResult> findBySupervisorGradeResult(SupervisorGradeResult supervisorGradeResult);
}
