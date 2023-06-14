package com.jtk.ps.api.dto.supervisor_grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorGradeUpdateRequest {
    private Integer id;
    private Integer phase;
    @JsonProperty("grade_list")
    private List<GradeUpdateRequest> gradeList;
}
