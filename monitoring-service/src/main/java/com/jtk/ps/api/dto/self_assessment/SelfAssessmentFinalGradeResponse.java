package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfAssessmentFinalGradeResponse {
    @JsonProperty("best_grade")
    private List<SelfAssessmentGradeDetailResponse> bestGrade;
    @JsonProperty("average_grade")
    private List<SelfAssessmentGradeDetailResponse> avgGrade;
    @JsonProperty("final_grade")
    private List<SelfAssessmentGradeDetailResponse> finalGrade;
}
