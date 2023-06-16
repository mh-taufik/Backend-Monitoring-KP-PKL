package com.jtk.ps.api.dto.supervisor_grade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponse {
    @JsonProperty("logbook_submitted")
    private Percentage logbookSubmitted;
    @JsonProperty("logbook_missing")
    private Percentage logbookMissing;
    @JsonProperty("logbook_on_time")
    private Percentage logbookOnTime;
    @JsonProperty("logbook_late")
    private Percentage logbookLate;
    @JsonProperty("logbook_match")
    private Percentage logbookMatch;
    @JsonProperty("logbook_not_match")
    private Percentage logbookNotMatch;
    @JsonProperty("logbook_nilai_sangat_baik")
    private Percentage logbookNilaiSangatBaik;
    @JsonProperty("logbook_nilai_baik")
    private Percentage logbookNilaiBaik;
    @JsonProperty("logbook_nilai_cukup")
    private Percentage logbookNilaiCukup;
    @JsonProperty("logbook_nilai_kurang")
    private Percentage logbookNilaiKurang;
    @JsonProperty("logbook_nilai_belum_dinilai")
    private Percentage logbookNilaiBelumDinilai;
    @JsonProperty("self_assessment_submitted")
    private Percentage selfAssessmentSubmitted;
    @JsonProperty("self_assessment_missing")
    private Percentage selfAssessmentMissing;
    @JsonProperty("self_assessment_apresiasi_perusahaan")
    private Percentage selfAssessmentApresiasiPerusahaan;
}
