package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "supervisor_grade_result")
@Data
public class SupervisorGradeResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private SupervisorGrade supervisorGrade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private SupervisorGradeAspect aspectGrade;

    @Column(name = "grade", nullable = false)
    private Integer grade;
}