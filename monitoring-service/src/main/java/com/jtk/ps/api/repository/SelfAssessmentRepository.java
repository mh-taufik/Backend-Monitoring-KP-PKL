package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Rpp;
import com.jtk.ps.api.model.SelfAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SelfAssessmentRepository extends JpaRepository<SelfAssessment, Integer> {
    SelfAssessment findById(int id);
    List<SelfAssessment> findByParticipantId(int participantId);
    @Query(value = "select if(count(*)>0, 'true', 'false') from SelfAssessment where participant_id = :participant_id and start_date = :date", nativeQuery = true)
    Boolean isExist(@Param("participant_id") int participantId, @Param("date") LocalDate date);
    @Query(value = "select count(*) from SelfAssessment l where l.participant_id = :participant_id",nativeQuery = true)
    Integer countByParticipantId(@Param("participant_id") int participantId);
}
