package com.jtk.ps.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {
    private String name;
    private Integer total;
    private Integer submitted;
    private Integer missing;
}
