package com.jtk.ps.api.dto.logbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Logbook;
import com.jtk.ps.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonProperty("encountered_problem")
    private String encounteredProblem;
    private String grade;
    private Status status;
}