package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentGradeStat {
    @JsonProperty("logbook_graded")
    private Integer logbookGraded;
    @JsonProperty("logbook_ungraded")
    private Integer logbookUngraded;
    @JsonProperty("laporan_graded")
    private Integer laporanGraded;
    @JsonProperty("laporan_ungraded")
    private Integer laporanUngraded;
}
