package com.jtk.ps.api.dto.logbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Logbook;
import com.jtk.ps.api.model.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LogbookDetailResponse {
    private Integer id;
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
    private Integer grade;
    private Status status;

    public LogbookDetailResponse(Integer id, Integer participantId, LocalDate date, String projectName, String projectManager, String technicalLeader, String task, String timeAndActivity, String tools, String workResult, String description, Integer grade, Status status) {
        this.id = id;
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
        this.grade = grade;
        this.status = status;
    }

    public LogbookDetailResponse(Logbook logbook) {
        this.id = logbook.getId();
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
        this.grade = logbook.getGrade();
        this.status = logbook.getStatus();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getTechnicalLeader() {
        return technicalLeader;
    }

    public void setTechnicalLeader(String technicalLeader) {
        this.technicalLeader = technicalLeader;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTimeAndActivity() {
        return timeAndActivity;
    }

    public void setTimeAndActivity(String timeAndActivity) {
        this.timeAndActivity = timeAndActivity;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getWorkResult() {
        return workResult;
    }

    public void setWorkResult(String workResult) {
        this.workResult = workResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}