package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "self_assessment")
@Data
public class SelfAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "participant_id", nullable = false)
    private Integer participantId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "finish_date", nullable = false)
    private LocalDate finishDate;

    @OneToMany(mappedBy = "selfAssessment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SelfAssessmentGrade> selfAssessmentGrades;
}