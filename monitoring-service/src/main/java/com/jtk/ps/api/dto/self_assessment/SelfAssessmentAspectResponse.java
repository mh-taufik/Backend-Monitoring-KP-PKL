package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelfAssessmentAspectResponse {
    @JsonProperty("aspect_id")
    private Integer aspectId;
    @JsonProperty("aspect_name")
    private String aspectName;
    @JsonProperty("start_assessment_date")
    private LocalDate startAssessmentDate;
    private String description;
    private String status;
    @JsonProperty("day_range")
    private int dayRange;
}
