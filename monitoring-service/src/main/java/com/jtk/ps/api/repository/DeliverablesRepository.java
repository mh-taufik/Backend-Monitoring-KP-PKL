package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Deliverable;
import com.jtk.ps.api.model.Milestone;
import com.jtk.ps.api.model.Rpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliverablesRepository extends JpaRepository<Deliverable, Integer> {
    List<Deliverable> findByRpp(Rpp rpp);
}
