package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "self_assessment_grade")
@Data
public class SelfAssessmentGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private SelfAssessment selfAssessment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private SelfAssessmentAspect selfAssessmentAspect;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Lob
    @Column(name = "description")
    private String description;
}