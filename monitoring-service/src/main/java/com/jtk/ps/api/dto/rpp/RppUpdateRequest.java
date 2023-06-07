package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RppUpdateRequest {
    @JsonProperty("rpp_id")
    private int rppId;
    @JsonProperty("finish_date")
    private LocalDate finishDate;
    private List<MilestoneRequest> milestones;
    private List<DeliverablesRequest> deliverables;
    @JsonProperty("completion_schedules")
    private List<CompletionScheduleRequest> completionSchedules;
    @JsonProperty("weekly_achievement_plans")
    private List<WeeklyAchievementPlanRequest> weeklyAchievementPlans;
}
