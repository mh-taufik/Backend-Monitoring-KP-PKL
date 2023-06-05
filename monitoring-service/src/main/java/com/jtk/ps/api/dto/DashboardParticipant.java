package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardParticipant {
    @JsonProperty("rpp_submitted")
    private Integer rppSubmitted;
    @JsonProperty("logbook_submitted")
    private Integer logbookSubmitted;
    @JsonProperty("logbook_missing")
    private Integer logbookMissing;
    @JsonProperty("self_assessment_submitted")
    private Integer selfAssessmentSubmitted;
    @JsonProperty("self_assessment_missing")
    private Integer selfAssessmentMissing;
    @JsonProperty("laporan_submitted")
    private Integer laporanSubmitted;
    @JsonProperty("laporan_missing")
    private Integer laporanMissing;
}
