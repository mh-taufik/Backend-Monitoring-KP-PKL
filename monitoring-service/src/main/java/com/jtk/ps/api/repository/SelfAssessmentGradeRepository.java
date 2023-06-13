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
    @Query(value = "select a.id, a.self_assessment_id, a.self_assessment_aspect_id, max(a.grade) as grade, a.description from self_assessment_grade a join self_assessment b on a.self_assessment_id = b.id where b.participant_id = :participant_id and a.self_assessment_aspect_id = :aspect_id group by a.self_assessment_aspect_id", nativeQuery = true)
    SelfAssessmentGrade findMaxGradeByParticipantIdAndAspectId(@Param("participant_id") int participantId, @Param("aspect_id") int aspectId);
    @Query(value = "select a.id, a.self_assessment_id, a.self_assessment_aspect_id, avg(a.grade) as grade, a.description from self_assessment_grade a join self_assessment b on a.self_assessment_id = b.id where b.participant_id = :participant_id and a.self_assessment_aspect_id = :aspect_id group by a.self_assessment_aspect_id", nativeQuery = true)
    SelfAssessmentGrade findAvgGradeByParticipantIdAndAspectId(@Param("participant_id") int participantId, @Param("aspect_id") int aspectId);
}
