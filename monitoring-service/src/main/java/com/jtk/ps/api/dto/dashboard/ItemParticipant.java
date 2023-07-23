package com.jtk.ps.api.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemParticipant {
    @JsonProperty("nim")
    private Integer nim;
    @JsonProperty("name")
    private String name;
    @JsonProperty("company")
    private String company;
}
