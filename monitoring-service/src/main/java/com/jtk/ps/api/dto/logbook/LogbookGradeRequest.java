package com.jtk.ps.api.dto.logbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.ENilai;
import com.jtk.ps.api.model.Logbook;
import com.jtk.ps.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogbookGradeRequest {
    @JsonProperty(required = true)
    private int id;
    @JsonProperty(required = true)
    private ENilai grade;
}