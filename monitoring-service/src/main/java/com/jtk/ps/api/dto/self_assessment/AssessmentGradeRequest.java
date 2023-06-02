package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentGradeRequest {
    @JsonProperty("grade_id")
    private Integer gradeId;
    @JsonProperty("aspect_id")
    private Integer aspectId;
    private Integer grade;
    private String description;
}
