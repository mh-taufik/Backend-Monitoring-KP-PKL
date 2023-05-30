package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rpp")
@Data
@NoArgsConstructor
public class Rpp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "participant_id")
    private Integer participantId;

    @Lob
    @Column(name = "work_title")
    private String workTitle;

    @Lob
    @Column(name = "group_role")
    private String groupRole;

    @Lob
    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @OneToOne
    @JoinColumn(name = "status")
    private Status status;

    @OneToMany(mappedBy = "rpp", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Milestone> milestones;

    @OneToMany(mappedBy = "rpp", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Deliverable> deliverables;

    @OneToMany(mappedBy = "rpp", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CompletionSchedule> completionSchedules;

    @OneToMany(mappedBy = "rpp", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<WeeklyAchievementPlan> weeklyAchievementPlans;

    public Rpp(Integer id, Integer participantId, String workTitle, String groupRole, String taskDescription, LocalDate startDate, LocalDate finishDate, Status status) {
        this.id = id;
        this.participantId = participantId;
        this.workTitle = workTitle;
        this.groupRole = groupRole;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
    }

    public Rpp(Integer id, Integer participantId, String workTitle, String groupRole, String taskDescription, LocalDate startDate, LocalDate finishDate, Status status, List<Milestone> milestones, List<Deliverable> deliverables, List<CompletionSchedule> completionSchedules, List<WeeklyAchievementPlan> weeklyAchievementPlans) {
        this.id = id;
        this.participantId = participantId;
        this.workTitle = workTitle;
        this.groupRole = groupRole;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.milestones = milestones;
        this.deliverables = deliverables;
        this.completionSchedules = completionSchedules;
        this.weeklyAchievementPlans = weeklyAchievementPlans;
    }
}