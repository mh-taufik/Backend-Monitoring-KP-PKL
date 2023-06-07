package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfAssessmentAspectRequest {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("start_assessment_date")
    private LocalDate startAssessmentDate;

    @JsonProperty("status")
    private Integer status;
}
