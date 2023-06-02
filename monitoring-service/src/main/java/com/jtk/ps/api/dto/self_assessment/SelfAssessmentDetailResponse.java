package com.jtk.ps.api.dto.self_assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class SelfAssessmentDetailResponse {
    @JsonProperty("participant_id")
    private Integer participantId;
    @JsonProperty("self_assessment_id")
    private Integer selfAssessmentId;
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonProperty("finish_date")
    private LocalDate finishDate;
    @JsonProperty("aspect_list")
    private List<SelfAssessmentGradeDetailResponse> aspectList;

    public SelfAssessmentDetailResponse(Integer participantId, Integer selfAssessmentId, LocalDate startDate, LocalDate finishDate, List<SelfAssessmentGradeDetailResponse> aspectList) {
        this.participantId = participantId;
        this.selfAssessmentId = selfAssessmentId;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.aspectList = aspectList;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Integer getSelfAssessmentId() {
        return selfAssessmentId;
    }

    public void setSelfAssessmentId(Integer selfAssessmentId) {
        this.selfAssessmentId = selfAssessmentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public List<SelfAssessmentGradeDetailResponse> getAspectList() {
        return aspectList;
    }

    public void setAspectList(List<SelfAssessmentGradeDetailResponse> aspectList) {
        this.aspectList = aspectList;
    }
}
