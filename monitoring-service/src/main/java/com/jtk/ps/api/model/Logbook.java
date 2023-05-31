package com.jtk.ps.api.model;

import com.jtk.ps.api.dto.logbook.LogbookGradeRequest;
import com.jtk.ps.api.dto.logbook.LogbookCreateRequest;
import com.jtk.ps.api.dto.logbook.LogbookUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "logbook")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "participant_id")
    private Integer participantId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_manager")
    private String projectManager;

    @Column(name = "technical_leader")
    private String technicalLeader;

    @Lob
    @Column(name = "task")
    private String task;

    @Lob
    @Column(name = "time_and_activity")
    private String timeAndActivity;

    @Lob
    @Column(name = "tools")
    private String tools;

    @Lob
    @Column(name = "work_result")
    private String workResult;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "grade")
    private ENilai grade;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    public Logbook(LogbookCreateRequest logbook) {
        this.participantId = logbook.getParticipantId();
        this.date = logbook.getDate();
        this.projectName = logbook.getProjectName();
        this.projectManager = logbook.getProjectManager();
        this.technicalLeader = logbook.getTechnicalLeader();
        this.task = logbook.getTask();
        this.timeAndActivity = logbook.getTimeAndActivity();
        this.workResult = logbook.getWorkResult();
        this.tools = logbook.getTools();
        this.description = logbook.getDescription();
    }

    public Logbook(LogbookUpdateRequest logbook) {
        this.participantId = logbook.getParticipantId();
        this.date = logbook.getDate();
        this.projectName = logbook.getProjectName();
        this.projectManager = logbook.getProjectManager();
        this.technicalLeader = logbook.getTechnicalLeader();
        this.task = logbook.getTask();
        this.timeAndActivity = logbook.getTimeAndActivity();
        this.tools = logbook.getTools();
        this.description = logbook.getDescription();
    }
}