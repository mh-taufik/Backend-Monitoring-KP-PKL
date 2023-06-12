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
public class SupervisorGradeCreateRequest {
    private Integer phase;
    @JsonProperty("participant_id")
    private Integer participantId;
    @JsonProperty("grade_list")
    private List<GradeRequest> gradeList;
}
