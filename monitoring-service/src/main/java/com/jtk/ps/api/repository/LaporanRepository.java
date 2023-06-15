package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Laporan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LaporanRepository extends JpaRepository<Laporan, Integer>{
    Laporan findById(int id);
    List<Laporan> findByParticipantIdAndPhaseOrderByPhaseAsc(int id, int phase);

    List<Laporan> findByParticipantId(int participantId);
    @Query(value = "select if(count(*)>0, 'true', 'false') from laporan where participant_id = :participant_id and phase = :phase", nativeQuery = true)
    Boolean isExist(@Param("participant_id") int participantId, @Param("phase")  int phase);
    @Query(value = "select count(*) from laporan l where l.participant_id = :participant_id",nativeQuery = true)
    Integer countByParticipantId(@Param("participant_id") int participantId);
    @Query(value = "select count(*) from laporan l where l.participant_id = :date",nativeQuery = true)
    Integer countByDate(@Param("date") int participantId);
    @Query(value = "select count(*) from laporan l where l.upload_date >= :start and l.upload_date <= :end",nativeQuery = true)
    Integer countAllForCommittee(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query(value = "select count(*) from laporan l where l.participant_id in :participant_id",nativeQuery = true)
    Integer countAllInParticipantId(@Param("participant_id") List<Integer> participantId);

}
