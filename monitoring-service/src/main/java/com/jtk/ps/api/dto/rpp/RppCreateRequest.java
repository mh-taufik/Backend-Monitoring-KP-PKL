package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class RppCreateRequest {
    @JsonProperty("participant_id")
    private int participantId;
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
    private List<MilestoneRequest> milestones;
    private List<DeliverablesRequest> deliverables;
    @JsonProperty("completion_schedules")
    private List<CompletionScheduleRequest> completionSchedules;
    @JsonProperty("weekly_achievement_plans")
    private List<WeeklyAchievementPlanRequest> weeklyAchievementPlans;
}
