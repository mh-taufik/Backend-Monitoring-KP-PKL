package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Milestone;
import com.jtk.ps.api.model.Rpp;
import com.jtk.ps.api.model.WeeklyAchievementPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeeklyAchievementPlanRepository extends JpaRepository<WeeklyAchievementPlan, Integer> {
    List<WeeklyAchievementPlan> findByRpp(Rpp rpp);
}
