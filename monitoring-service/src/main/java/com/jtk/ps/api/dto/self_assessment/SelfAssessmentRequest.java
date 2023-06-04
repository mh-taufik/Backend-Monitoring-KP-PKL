package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfAssessmentRequest {
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("finish_date")
    private LocalDate finishDate;

    private List<AssessmentGradeRequest> grade;
}
