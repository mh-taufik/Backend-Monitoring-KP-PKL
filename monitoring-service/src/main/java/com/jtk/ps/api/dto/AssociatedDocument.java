package com.jtk.ps.api.dto;

import com.jtk.ps.api.dto.logbook.LogbookDetailResponse;
import com.jtk.ps.api.dto.rpp.RppDetailResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedDocument {
    private List<RppDetailResponse> rpp;
    private List<LogbookDetailResponse> logbook;
    private List<SelfAssessmentDetailResponse> selfAssessment;
}
