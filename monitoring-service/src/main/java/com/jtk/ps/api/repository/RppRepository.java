package com.jtk.ps.api.repository;

import com.jtk.ps.api.dto.rpp.RppResponse;
import com.jtk.ps.api.model.Logbook;
import com.jtk.ps.api.model.Rpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RppRepository extends JpaRepository<Rpp, Integer>{
    @Query(value = "select * from Rpp where participant_id = :id order by start_date asc ", nativeQuery = true)
    List<Rpp> findByParticipantId(@Param("id") int participantId);
    Rpp findById(int id);
    @Query(value = "select * from Rpp where participant_id = :participant_id where date between :start and :end", nativeQuery = true)
    List<Rpp> findByParticipantIdAndDateOrderByDateAsc(@Param("participant_id") int participantId, @Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query(value = "select count(*) from rpp where participant_id = :participant_id",nativeQuery = true)
    Integer countByParticipantId(@Param("participant_id") int participantId);
    @Query(value = "select count(*) from rpp where participant_id in (:participant_id) group by participant_id",nativeQuery = true)
    Integer countAllInParticipantId(@Param("participant_id") List<Integer> participantId);
}
