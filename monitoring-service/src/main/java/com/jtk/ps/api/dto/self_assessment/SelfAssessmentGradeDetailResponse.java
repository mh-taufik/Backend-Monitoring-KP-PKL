package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelfAssessmentGradeDetailResponse {
    @JsonProperty("aspect_id")
    private Integer aspectId;
    @JsonProperty("grade_id")
    private Integer gradeId;
    @JsonProperty("aspect_name")
    private String aspectName;
    private Integer grade;
    private String description;

    public SelfAssessmentGradeDetailResponse(Integer aspectId, Integer gradeId, String aspectName, Integer grade, String description) {
        this.aspectId = aspectId;
        this.gradeId = gradeId;
        this.aspectName = aspectName;
        this.grade = grade;
        this.description = description;
    }

    public Integer getAspectId() {
        return aspectId;
    }

    public void setAspectId(Integer aspectId) {
        this.aspectId = aspectId;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getAspectName() {
        return aspectName;
    }

    public void setAspectName(String aspectName) {
        this.aspectName = aspectName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
