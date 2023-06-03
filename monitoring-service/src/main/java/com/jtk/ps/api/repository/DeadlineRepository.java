package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Deadline;
import com.jtk.ps.api.model.Milestone;
import com.jtk.ps.api.model.Rpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    Deadline findById(int id);
    @Query(value = "select * from `monitoring-db`.deadline d  where d.name like :name", nativeQuery = true)
    Deadline findByName(@Param("name") String name);
}
