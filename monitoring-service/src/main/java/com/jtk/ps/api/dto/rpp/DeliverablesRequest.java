package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliverablesRequest {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("deliverables")
    private String deliverables;

    @JsonProperty("due_date")
    private LocalDate dueDate;
}
