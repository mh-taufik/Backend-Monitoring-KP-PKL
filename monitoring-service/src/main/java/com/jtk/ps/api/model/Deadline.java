package com.jtk.ps.api.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "deadline")
public class Deadline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "day_range")
    private Integer dayRange;

    @Column(name = "start_assignment_date")
    private LocalDate startAssignmentDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDayRange() {
        return dayRange;
    }

    public void setDayRange(Integer dayRange) {
        this.dayRange = dayRange;
    }

    public LocalDate getStartAssignmentDate() {
        return startAssignmentDate;
    }

    public void setStartAssignmentDate(LocalDate startAssignmentDate) {
        this.startAssignmentDate = startAssignmentDate;
    }

}