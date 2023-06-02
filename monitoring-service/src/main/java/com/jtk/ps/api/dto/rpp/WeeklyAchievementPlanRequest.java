package com.jtk.ps.api.dto.rpp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jtk.ps.api.model.Rpp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyAchievementPlanRequest {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("achievement_plan")
    private String achievementPlan;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("finish_date")
    private LocalDate finishDate;
}
