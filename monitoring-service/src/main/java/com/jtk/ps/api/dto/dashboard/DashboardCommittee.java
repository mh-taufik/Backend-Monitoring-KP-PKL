package com.jtk.ps.api.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCommittee {
    @JsonProperty("all")
    private DashboardItem all;
    @JsonProperty("weekly")
    private DashboardItem weekly;
    @JsonProperty("supervisor_mapping_done")
    private Integer supervisorMappingDone;
    @JsonProperty("supervisor_mapping_undone")
    private Integer supervisorMappingUndone;
}
