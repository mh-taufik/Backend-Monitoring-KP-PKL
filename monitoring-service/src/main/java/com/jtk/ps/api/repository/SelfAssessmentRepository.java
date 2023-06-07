package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SelfAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SelfAssessmentRepository extends JpaRepository<SelfAssessment, Integer> {
    SelfAssessment findById(int id);
    List<SelfAssessment> findByParticipantIdOrderByStartDateAsc(int participantId);
    @Query(value = "select if(count(*)>0, 'true', 'false') from self_assessment where participant_id = :participant_id and start_date = :date", nativeQuery = true)
    Boolean isExist(@Param("participant_id") int participantId, @Param("date") LocalDate date);
    @Query(value = "select count(*) from self_assessment where participant_id = :participant_id",nativeQuery = true)
    Integer countByParticipantId(@Param("participant_id") int participantId);
    @Query(value = "select count(*) from self_assessment where participant_id in (:participant)",nativeQuery = true)
    Integer countAllInParticipantId(@Param("participant") List<Integer> participant);
}
