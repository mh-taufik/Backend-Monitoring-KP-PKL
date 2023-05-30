package com.jtk.ps.api.dto.supervisor_grade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private Integer id;
    private String aspect;
    private Integer grade;
}
