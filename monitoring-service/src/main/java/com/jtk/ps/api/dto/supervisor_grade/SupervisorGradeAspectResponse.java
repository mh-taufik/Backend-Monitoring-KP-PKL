package com.jtk.ps.api.dto.supervisor_grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorGradeAspectResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("max_grade")
    private Integer maxGrade;

    @JsonProperty("edited_by")
    private Integer editedBy;

    @JsonProperty("last_edit_date")
    private LocalDate lastEditDate;

    @JsonProperty("name")
    private String name;
}
