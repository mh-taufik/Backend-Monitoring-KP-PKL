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
    @Query(value = "select * from Rpp where participant_id = :id", nativeQuery = true)
    List<Rpp> findByParticipantId(@Param("id") int participantId);
    Rpp findById(int id);
    @Query(value = "select count(*) from rpp l where l.participant_id = :participant_id",nativeQuery = true)
    Integer countByParticipantId(@Param("participant_id") int participantId);
    @Query(value = "select count(*) from rpp l where l.start_date >= :start and l.finish_date <= :end",nativeQuery = true)
    Integer countAllForCommittee(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
