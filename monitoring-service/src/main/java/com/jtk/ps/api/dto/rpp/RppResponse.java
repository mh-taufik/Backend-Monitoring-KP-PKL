package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RppResponse {
    @JsonProperty("rpp_id")
    private int rppId;

    @JsonProperty("work_title")
    private String workTitle;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("finish_date")
    private LocalDate finishDate;

    @JsonProperty("status")
    private String status;
}
