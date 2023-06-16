package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    Deadline findById(int id);
//    @Query(value = "select * from deadline d  where d.name like :name", nativeQuery = true)
    Deadline findByNameLike(String name);
    @Query(value = "select * from deadline where name like '%laporan%'", nativeQuery = true)
    List<Deadline> findAllLaporan();
    @Query(value = "select count(*) from deadline d where d.name like '%laporan%'", nativeQuery = true)
    Integer countLaporanPhase();
    @Query(value = "select count(*) from deadline d where d.start_assignment_date <= :date and d.name like '%laporan%'", nativeQuery = true)
    Integer countLaporanPhaseNow(@Param("date")LocalDate date);
    @Query(value = "select * from deadline d where d.start_assignment_date <= :start and d.finish_assignment_date  >= :end and d.name like :name", nativeQuery = true)
    Deadline findDeadline(@Param("start")LocalDate start, @Param("end")LocalDate end, @Param("name")String name);
}
