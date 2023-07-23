package com.jtk.ps.api.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardLecturer {
    @JsonProperty("all")
    private DashboardItem all;
    @JsonProperty("weekly")
    private DashboardItem weekly;
}
