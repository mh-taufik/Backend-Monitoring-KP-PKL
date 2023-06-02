package com.jtk.ps.api.dto.logbook;

import com.jtk.ps.api.model.ENilai;
import com.jtk.ps.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LogbookResponse {
    private Integer id;

    private LocalDate date;

    private String grade;

    private String status;

    public LogbookResponse(Integer id, LocalDate date, String grade, String status) {
        this.id = id;
        this.date = date;
        this.grade = grade;
        this.status = status;
    }
}