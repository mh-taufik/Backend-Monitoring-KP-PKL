package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RppSimpleUpdateRequest {
    @JsonProperty("rpp_id")
    private int rppId;
    @JsonProperty("finish_date")
    private LocalDate finishDate;
}
