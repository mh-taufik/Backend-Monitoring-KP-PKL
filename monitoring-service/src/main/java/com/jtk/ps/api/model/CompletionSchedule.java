package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "completion_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletionSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Rpp rpp;

    @Column(name = "task_name")
    @JsonProperty("task_name")
    private String taskName;

    @Column(name = "task_type")
    @JsonProperty("task_type")
    private ETaskType taskType;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    @JsonProperty("finish_date")
    private LocalDate finishDate;

    public Rpp getRpp() {
        return rpp;
    }

    public void setRpp(Rpp rpp) {
        this.rpp = rpp;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ETaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(ETaskType taskType) {
        this.taskType = taskType;
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

}