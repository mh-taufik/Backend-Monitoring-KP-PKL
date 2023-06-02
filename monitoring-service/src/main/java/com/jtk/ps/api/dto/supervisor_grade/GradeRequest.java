package com.jtk.ps.api.dto.supervisor_grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeRequest {
    @JsonProperty("aspect_id")
    private Integer aspectId;
    private Integer grade;
}
