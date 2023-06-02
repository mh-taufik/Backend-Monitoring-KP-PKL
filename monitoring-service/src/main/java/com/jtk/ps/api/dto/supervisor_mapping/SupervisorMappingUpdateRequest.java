package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMappingUpdateRequest {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("company_id")
    private Integer companyId;

    @JsonProperty("participant_id")
    private Integer participantId;

    @JsonProperty("lecturer_id")
    private Integer lecturerId;

    @JsonProperty("prodi_id")
    private Integer prodiId;

    @JsonProperty("create_by")
    private Integer createBy;
}
