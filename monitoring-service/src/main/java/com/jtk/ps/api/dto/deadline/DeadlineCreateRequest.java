package com.jtk.ps.api.dto.deadline;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeadlineCreateRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("day_range")
    private Integer dayRange;

    @JsonProperty("start_assignment_date")
    private LocalDate startAssignmentDate;

    @JsonProperty("finish_assignment_date")
    private LocalDate finishAssignmentDate;
}
