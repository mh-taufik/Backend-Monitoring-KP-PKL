package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RppSimpleCreateRequest {
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
}
