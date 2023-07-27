package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SelfAssessment;
import com.jtk.ps.api.model.SelfAssessmentAspect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelfAssessmentAspectRepository extends JpaRepository<SelfAssessmentAspect, Integer> {
    SelfAssessmentAspect findById(int id);

    @Query(value = "select * from self_assessment_aspect where status = 6 and prodi_id = :prodi",nativeQuery = true)
    List<SelfAssessmentAspect> findAllActiveAspect(@Param("prodi") Integer prodiId);

    List<SelfAssessmentAspect> findByProdiId(int prodiId);
}
