package com.jtk.ps.api.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "laporan")
public class Laporan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "participant_id")
    private Integer participantId;

    @Column(name = "uri_name")
    private String uriName;

    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @Column(name = "phase")
    private Integer phase;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParticipant() {
        return participantId;
    }

    public void setParticipant(Integer participantId) {
        this.participantId = participantId;
    }

    public String getUriName() {
        return uriName;
    }

    public void setUriName(String uriName) {
        this.uriName = uriName;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

}