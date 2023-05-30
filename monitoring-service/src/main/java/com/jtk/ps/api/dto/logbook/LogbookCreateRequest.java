package com.jtk.ps.api.dto.logbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Logbook;
import com.jtk.ps.api.model.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LogbookCreateRequest {
    @JsonProperty("participant_id")
    private Integer participantId;
    private LocalDate date;
    @JsonProperty("project_name")
    private String projectName;
    @JsonProperty("project_manager")
    private String projectManager;
    @JsonProperty("technical_leader")
    private String technicalLeader;
    private String task;
    @JsonProperty("time_and_activity")
    private String timeAndActivity;
    private String tools;
    @JsonProperty("work_result")
    private String workResult;
    private String description;

    public LogbookCreateRequest(Integer id, Integer participantId, LocalDate date, String projectName, String projectManager, String technicalLeader, String task, String timeAndActivity, String tools, String workResult, String description, Integer grade, Status status) {
        this.participantId = participantId;
        this.date = date;
        this.projectName = projectName;
        this.projectManager = projectManager;
        this.technicalLeader = technicalLeader;
        this.task = task;
        this.timeAndActivity = timeAndActivity;
        this.tools = tools;
        this.workResult = workResult;
        this.description = description;
    }

    public LogbookCreateRequest(Logbook logbook) {
        this.participantId = logbook.getParticipantId();
        this.date = logbook.getDate();
        this.projectName = logbook.getProjectName();
        this.projectManager = logbook.getProjectManager();
        this.technicalLeader = logbook.getTechnicalLeader();
        this.task = logbook.getTask();
        this.timeAndActivity = logbook.getTimeAndActivity();
        this.tools = logbook.getTools();
        this.workResult = logbook.getWorkResult();
        this.description = logbook.getDescription();
    }
}