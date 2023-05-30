package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.*;
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
    @JsonProperty("work_title")
    private String workTitle;
    @JsonProperty("group_role")
    private String groupRole;
    @JsonProperty("task_description")
    private String taskDescription;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    private LocalDate finishDate;
    private Status status;
    private List<Milestone> milestones;
    private List<Deliverable> deliverables;
    @JsonProperty("completion_schedules")
    private List<CompletionSchedule> completionSchedules;
    @JsonProperty("weekly_achievement_plans")
    private List<WeeklyAchievementPlan> weeklyAchievementPlans;

}
