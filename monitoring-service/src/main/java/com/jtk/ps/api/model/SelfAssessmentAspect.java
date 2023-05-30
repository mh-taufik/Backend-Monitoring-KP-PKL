package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "self_assessment_aspect")
@Data
public class SelfAssessmentAspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_assessment_date", nullable = false)
    private LocalDate startAssessmentDate;

    @Column(name = "edited_by", nullable = false)
    private Integer editedBy;

    @Column(name = "last_edited_date", nullable = false)
    private LocalDate lastEditedDate;

    @OneToOne
    @JoinColumn(name = "status")
    private Status status;

    @OneToMany(mappedBy = "selfAssessmentAspect")
    @JsonManagedReference
    private List<SelfAssessmentGrade> selfAssessmentGrades;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartAssessmentDate() {
        return startAssessmentDate;
    }

    public void setStartAssessmentDate(LocalDate startAssessmentDate) {
        this.startAssessmentDate = startAssessmentDate;
    }

    public Integer getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(Integer editedBy) {
        this.editedBy = editedBy;
    }

    public LocalDate getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(LocalDate lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<SelfAssessmentGrade> getSelfAssessmentGrades() {
        return selfAssessmentGrades;
    }

    public void setSelfAssessmentGrades(List<SelfAssessmentGrade> selfAssessmentGrades) {
        this.selfAssessmentGrades = selfAssessmentGrades;
    }
}