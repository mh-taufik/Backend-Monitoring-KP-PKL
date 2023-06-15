package com.jtk.ps.api.dto.laporan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Laporan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaporanResponse {
    private Integer id;
    private String uri;
    @JsonProperty("upload_date")
    private LocalDate uploadDate;
    private Integer phase;
    @JsonProperty("supervisor_grade")
    private Integer supervisorGrade;

    public LaporanResponse(Laporan laporan){
        this.id = laporan.getId();
        this.uri = laporan.getUriName();
        this.uploadDate = laporan.getUploadDate();
        this.phase = laporan.getPhase();
    }
}
