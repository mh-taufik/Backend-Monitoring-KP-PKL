package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MappingResponse {
    @JsonProperty("participant_id")
    private Integer participantId;
    @JsonProperty("prodi_id")
    private Integer prodiId;
}
