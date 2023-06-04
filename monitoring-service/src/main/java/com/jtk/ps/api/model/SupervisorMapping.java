package com.jtk.ps.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "supervisor_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @Column(name = "participant_id", nullable = false)
    private Integer participantId;

    @Column(name = "lecturer_id", nullable = false)
    private Integer lecturerId;

    @Column(name = "prodi_id", nullable = false)
    private Integer prodiId;

    @Column(name = "create_by", nullable = false)
    private Integer createBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Integer getProdiId() {
        return prodiId;
    }

    public void setProdiId(Integer prodiId) {
        this.prodiId = prodiId;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

}