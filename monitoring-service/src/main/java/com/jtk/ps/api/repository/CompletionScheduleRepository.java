package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.CompletionSchedule;
import com.jtk.ps.api.model.Rpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompletionScheduleRepository extends JpaRepository<CompletionSchedule, Integer> {
    List<CompletionSchedule> findByRpp(Rpp rpp);
}
