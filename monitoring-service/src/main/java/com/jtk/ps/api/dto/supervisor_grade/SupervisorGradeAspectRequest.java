package com.jtk.ps.api.dto.supervisor_grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorGradeAspectRequest {
    private int id;
    private String description;
    @JsonProperty("max_grade")
    private int maxGrade;
    @JsonProperty("name")
    private String name;
}
