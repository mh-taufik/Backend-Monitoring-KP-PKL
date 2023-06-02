package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.ETaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletionScheduleRequest {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("task_name")
    private String taskName;
    @JsonProperty("task_type")
    private ETaskType taskType;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    private LocalDate finishDate;
}