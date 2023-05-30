package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMappingSupervisorResponse {
    @JsonProperty("participant_id")
    private Integer participantId;
    @JsonProperty("participant_name")
    private String participantName;
    @JsonProperty("company_id")
    private Integer companyId;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    @JsonProperty("lecturer_name")
    private String lecturerName;
    @JsonProperty("prodi_id")
    private Integer prodiId;
}