package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupervisorGradeRepository extends JpaRepository<SupervisorGrade, Integer> {
    SupervisorGrade findById(int id);
    List<SupervisorGrade> findByParticipantId(int participantId);
    Optional<SupervisorGrade> findByParticipantIdAndPhase(int participantId, int phase);
}
