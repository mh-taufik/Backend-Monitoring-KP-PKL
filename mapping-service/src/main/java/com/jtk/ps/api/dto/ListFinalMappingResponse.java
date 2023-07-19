package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListFinalMappingResponse {
    @JsonProperty("final_mapping_d3")
    private FinalMappingResponse d3;

    @JsonProperty("final_mapping_d4")
    private FinalMappingResponse d4;
}
