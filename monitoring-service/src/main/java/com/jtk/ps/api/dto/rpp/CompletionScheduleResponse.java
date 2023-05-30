package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Rpp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletionScheduleResponse {
    private Integer id;
    private String taskName;
    private String taskType;
    private LocalDate startDate;
    private LocalDate finishDate;
}