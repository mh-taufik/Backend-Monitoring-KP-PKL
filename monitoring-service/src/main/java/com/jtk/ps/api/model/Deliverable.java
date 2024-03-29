package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "deliverables")
public class Deliverable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    private Rpp rpp;

    @Column(name = "deliverables")
    private String deliverables;

    @Column(name = "due_date")
    private LocalDate dueDate;

    public Rpp getRpp() {
        return rpp;
    }

    public void setRpp(Rpp rpp) {
        this.rpp = rpp;
    }

    public String getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}