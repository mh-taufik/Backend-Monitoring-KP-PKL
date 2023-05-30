package com.jtk.ps.api.repository;

import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.model.Laporan;
import com.jtk.ps.api.model.Rpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LaporanRepository extends JpaRepository<Laporan, Integer>{
    Laporan findById(int id);
    List<Laporan> findByParticipantIdAndPhase(int id, int phase);

    @Query(value = "select id, uri_name, upload_date, phase from Laporan where participant_id = :id", nativeQuery = true)
    List<Laporan> findByParticipantId(@Param("id") int participantId);
}
