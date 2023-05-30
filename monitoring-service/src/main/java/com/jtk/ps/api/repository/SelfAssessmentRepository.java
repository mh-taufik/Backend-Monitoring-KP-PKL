package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Rpp;
import com.jtk.ps.api.model.SelfAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SelfAssessmentRepository extends JpaRepository<SelfAssessment, Integer> {
    SelfAssessment findById(int id);
    List<SelfAssessment> findByParticipantId(int participantId);
}
