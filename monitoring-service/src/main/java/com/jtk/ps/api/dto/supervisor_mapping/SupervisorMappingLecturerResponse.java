package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMappingLecturerResponse {
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    @JsonProperty("lecturer_name")
    private String lecturerName;
    @JsonProperty("prodi_id")
    private Integer prodiId;
    @JsonProperty("participant_company")
    private List<ParticipantCompany> participantCompanyList;
}
