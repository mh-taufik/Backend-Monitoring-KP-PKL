package com.jtk.ps.api.dto;

import com.jtk.ps.api.dto.logbook.LogbookDetailResponse;
import com.jtk.ps.api.dto.rpp.RppDetailResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentDetailResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedDocumentRpp {
    private RppDetailResponse rpp;
    private List<LogbookDetailResponse> logbook;
    private List<SelfAssessmentDetailResponse> selfAssessment;
}
