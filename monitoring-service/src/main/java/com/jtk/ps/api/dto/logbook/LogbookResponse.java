package com.jtk.ps.api.dto.logbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.ENilai;
import com.jtk.ps.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogbookResponse {
    private Integer id;

    private LocalDate date;

    private String grade;

    private String status;
    @JsonProperty("project_name")
    private String projectName;
}