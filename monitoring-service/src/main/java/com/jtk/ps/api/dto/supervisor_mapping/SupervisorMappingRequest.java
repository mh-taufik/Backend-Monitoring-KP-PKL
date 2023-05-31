package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMappingRequest {
    @JsonProperty("participant_id")
    private Integer participantId;
    @JsonProperty("company_id")
    private Integer companyId;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    @JsonProperty("prodi_id")
    private Integer prodiId;
    @JsonProperty("created_by")
    private Integer createdBy;
}