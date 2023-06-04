package com.jtk.ps.api.dto.laporan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaporanCreateRequest {
    private String uri;
    private Integer phase;
}
