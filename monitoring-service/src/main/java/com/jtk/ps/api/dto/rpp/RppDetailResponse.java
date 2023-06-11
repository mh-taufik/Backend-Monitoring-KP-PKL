package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class RppDetailResponse {
    @JsonProperty("rpp_id")
    private int rppId;
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
    private Status status;
    private List<Milestone> milestones;
    private List<Deliverable> deliverables;
    @JsonProperty("completion_schedules")
    private List<CompletionSchedule> completionSchedules;
    @JsonProperty("weekly_achievement_plans")
    private List<WeeklyAchievementPlan> weeklyAchievementPlans;

    public RppDetailResponse(Rpp rpp, List<Milestone> milestones, List<Deliverable> deliverables, List<CompletionSchedule> completionSchedules, List<WeeklyAchievementPlan> weeklyAchievementPlans) {
        this.participantId = rpp.getParticipantId();
        this.workTitle = rpp.getWorkTitle();
        this.groupRole = rpp.getGroupRole();
        this.taskDescription = rpp.getTaskDescription();
        this.startDate = rpp.getStartDate();
        this.finishDate = rpp.getFinishDate();
        this.status = rpp.getStatus();
        this.milestones = milestones;
        this.deliverables = deliverables;
        this.weeklyAchievementPlans = weeklyAchievementPlans;
        this.completionSchedules = completionSchedules;
        this.rppId = rpp.getId();
    }

    public RppDetailResponse(int participantId, int rppId, String workTitle, String groupRole, String taskDescription, LocalDate startDate, LocalDate finishDate, Status status, List<Milestone> milestones, List<Deliverable> deliverables, List<CompletionSchedule> completionSchedules, List<WeeklyAchievementPlan> weeklyAchievementPlans) {
        this.participantId = participantId;
        this.rppId = rppId;
        this.workTitle = workTitle;
        this.groupRole = groupRole;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.milestones = milestones;
        this.deliverables = deliverables;
        this.completionSchedules = completionSchedules;
        this.weeklyAchievementPlans = weeklyAchievementPlans;
    }
}
