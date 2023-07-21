package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardParticipant {
    @JsonProperty("rpp_submitted")
    private Integer rppSubmitted;
    @JsonProperty("logbook_submitted")
    private Integer logbookSubmitted;
    @JsonProperty("logbook_total")
    private Integer logbookTotal;
    @JsonProperty("logbook_missing")
    private List<LocalDate> logbookMissing;
    @JsonProperty("self_assessment_submitted")
    private Integer selfAssessmentSubmitted;
    @JsonProperty("self_assessment_total")
    private Integer selfAssessmentTotal;
    @JsonProperty("laporan_submitted")
    private Integer laporanSubmitted;
    @JsonProperty("laporan_total")
    private Integer laporanTotal;
}
