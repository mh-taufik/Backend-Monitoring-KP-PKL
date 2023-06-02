package com.jtk.ps.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "deadline")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deadline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "day_range")
    private Integer dayRange;

    @Column(name = "start_assignment_date")
    private LocalDate startAssignmentDate;

    @Column(name = "finish_assignment_date")
    private LocalDate finishAssignmentDate;

}