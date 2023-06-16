package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SelfAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SelfAssessmentRepository extends JpaRepository<SelfAssessment, Integer> {
    SelfAssessment findById(int id);
    List<SelfAssessment> findByParticipantIdOrderByStartDateAsc(int participantId);
    @Query(value = "select id from self_assessment where participant_id = :participant_id and start_date between :start and :end and finish_date between :start and :end order by start_date asc", nativeQuery = true)
    List<Integer> findIdByParticipanAndDate(@Param("participant_id") int participantId, @Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query(value = "select id from self_assessment where participant_id = :participant_id and :date between start_date and finish_date", nativeQuery = true)
    Optional<Integer> findIdByParticipanAndDateOne(@Param("participant_id") int participantId, @Param("date") LocalDate date);
    @Query(value = "select if(count(*)>0, 'true', 'false') from self_assessment where participant_id = :participant_id and start_date = :date", nativeQuery = true)
    Boolean isExist(@Param("participant_id") int participantId, @Param("date") LocalDate date);
    @Query(value = "select count(*) from self_assessment where participant_id = :participant_id",nativeQuery = true)
    Integer countByParticipantId(@Param("participant_id") int participantId);
    @Query(value = "select count(*) from self_assessment where participant_id in (:participant)",nativeQuery = true)
    Integer countAllInParticipantId(@Param("participant") List<Integer> participant);
    @Query(value = "select count(*) from self_assessment sa join self_assessment_grade sag on sa.id = sag.self_assessment_id join self_assessment_aspect saa on saa.id = sag.self_assessment_aspect_id where sa.participant_id = :participant and (saa.name like '%apresiasi%' and sag.grade > 0)",nativeQuery = true)
    Integer countApresiasiPerusahaanNotNull(@Param("participant") int participant);
}
