package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "supervisor_grade_aspect")
@Data
public class SupervisorGradeAspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "edited_by")
    private Integer editedBy;

    @Column(name = "last_edit_date")
    private LocalDate lastEditDate;

    @OneToOne
    @JoinColumn(name = "status")
    private Status status;

    @OneToMany(mappedBy = "aspectGrade", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<SupervisorGradeResult> results;
}