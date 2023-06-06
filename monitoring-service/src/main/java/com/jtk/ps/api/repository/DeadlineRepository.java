package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    Deadline findById(int id);
    @Query(value = "select * from deadline d  where d.name like :name", nativeQuery = true)
    Deadline findByName(@Param("name") String name);
    @Query(value = "select * from deadline d where d.start_assignment_date <= :date and d.finish_assignment_date  >= :date and d.name like '%laporan%'", nativeQuery = true)
    Deadline findLaporanPhaseNow(@Param("date")LocalDate date);
    @Query(value = "select * from deadline d where d.start_assignment_date <= :start and d.finish_assignment_date  >= :end and d.name like :name", nativeQuery = true)
    Deadline findDeadline(@Param("start")LocalDate start, @Param("end")LocalDate end, @Param("name")String name);
}
