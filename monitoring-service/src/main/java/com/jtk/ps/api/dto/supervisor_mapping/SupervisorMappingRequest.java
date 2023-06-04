package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMappingRequest {
    @JsonProperty("company_id")
    private Integer companyId;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
}