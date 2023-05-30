package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SelfAssessment;
import com.jtk.ps.api.model.SelfAssessmentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelfAssessmentGradeRepository extends JpaRepository<SelfAssessmentGrade, Integer> {
    SelfAssessmentGrade findById(int id);
    @Query(value = "SELECT * FROM self_assessment_grade WHERE self_assessment_id = :id", nativeQuery = true)
    List<SelfAssessmentGrade> findBySelfAssessmentId(@Param("id") int id);
    List<SelfAssessmentGrade> findBySelfAssessment(SelfAssessment selfAssessment);
}
