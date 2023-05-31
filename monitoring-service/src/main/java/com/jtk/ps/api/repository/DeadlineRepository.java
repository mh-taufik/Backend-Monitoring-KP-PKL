package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Deadline;
import com.jtk.ps.api.model.Milestone;
import com.jtk.ps.api.model.Rpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    Deadline findById(int id);
}
