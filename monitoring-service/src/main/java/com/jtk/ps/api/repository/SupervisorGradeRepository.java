package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupervisorGradeRepository extends JpaRepository<SupervisorGrade, Integer> {
    SupervisorGrade findById(int id);
    List<SupervisorGrade> findByParticipantId(int participantId);
}
