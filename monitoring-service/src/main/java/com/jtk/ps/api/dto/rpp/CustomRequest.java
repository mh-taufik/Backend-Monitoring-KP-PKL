package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRequest {
    @JsonProperty("rpp_id")
    private Integer rppId;
    @JsonProperty("milestone")
    private List<MilestoneRequest> milestone;
    @JsonProperty("deliverables")
    private List<DeliverablesRequest> deliverables;
    @JsonProperty("completion_schedule")
    private List<CompletionScheduleRequest> completionSchedule;
    @JsonProperty("weekly_achievement_plan")
    private List<WeeklyAchievementPlanRequest> weeklyAchievementPlan;
}