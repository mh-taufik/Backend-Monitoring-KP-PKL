package com.jtk.ps.api.dto.supervisor_mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMappingLecturerResponse {
    @JsonProperty("company_id")
    private Integer companyId;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("prodi_id")
    private Integer prodiId;
    private LocalDate date;
    private List<Participant> participant;
}