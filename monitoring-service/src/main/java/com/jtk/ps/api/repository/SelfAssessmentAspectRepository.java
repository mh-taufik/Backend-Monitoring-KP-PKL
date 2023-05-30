package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SelfAssessment;
import com.jtk.ps.api.model.SelfAssessmentAspect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SelfAssessmentAspectRepository extends JpaRepository<SelfAssessmentAspect, Integer> {
    SelfAssessmentAspect findById(int id);
}
