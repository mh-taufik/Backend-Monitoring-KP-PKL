package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.DashboardCommittee;
import com.jtk.ps.api.dto.DashboardLecturer;
import com.jtk.ps.api.dto.DashboardParticipant;
import com.jtk.ps.api.dto.deadline.DeadlineCreateRequest;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.deadline.DeadlineUpdateRequest;
import com.jtk.ps.api.dto.rpp.*;
import com.jtk.ps.api.dto.self_assessment.*;
import com.jtk.ps.api.dto.supervisor_grade.*;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingLecturerResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface IMonitoringService {
    //RPP
    List<RppResponse> getRppList(int participantId);
    RppDetailResponse getRppDetail(int id);
    CreateId createRpp(RppCreateRequest rpp, Integer participantId);
    void updateRpp(RppUpdateRequest rpp);
    void createMilestone(List<MilestoneRequest> request, int rppId);
    void updateMilestone(List<MilestoneRequest> request, int rppId);
    void createDeliverables(List<DeliverablesRequest> request, int rppId);
    void updateDeliverables(List<DeliverablesRequest> request, int rppId);
    void createCompletionSchedule(List<CompletionScheduleRequest> request, int rppId);
    void updateCompletionSchedule(List<CompletionScheduleRequest> request, int rppId);
    void createWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId);
    void updateWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId);

    //Logbook
    Boolean isLogbookExist(int participantId, LocalDate date);
    List<LogbookResponse> getLogbookByParticipantId(int participantId);
    LogbookDetailResponse getLogbookDetail(int id);
    CreateId createLogbook(LogbookCreateRequest logbook, Integer participantId);
    void updateLogbook(LogbookUpdateRequest logbook);
    void gradeLogbook(LogbookGradeRequest gradeRequest);

    //Self Assessment
    Boolean isSelfAssessmentExist(int participantId, LocalDate date);
    CreateId createSelfAssessment(SelfAssessmentRequest request, Integer participantId);
    SelfAssessmentDetailResponse getSelfAssessmentDetail(int id);
    List<SelfAssessmentResponse> getSelfAssessmentList(int idParticipant);
    List<SelfAssessmentGradeDetailResponse> getBestPerformance(int participantId);
    void updateSelfAssessment(SelfAssessmentUpdateRequest request);
    void createSelfAssessmentAspect(SelfAssessmentAspectRequest request, int creator);
    void updateSelfAssessmentAspect(SelfAssessmentAspectRequest request, int creator);
    List<SelfAssessmentAspectResponse> getSelfAssessmentAspect();

    //Supervisor Grade
    CreateId createSupervisorGrade(SupervisorGradeCreateRequest request);
    void updateSupervisorGrade(SupervisorGradeUpdateRequest request);
    SupervisorGradeDetailResponse getSupervisorGradeDetail(int id);
    List<SupervisorGradeResponse> getSupervisorGradeList(int participantId);
    StatisticResponse getMonitoringStatistic(int participantId);
    void createSupervisorGradeAspect(SupervisorGradeAspectRequest request, int creator);
    void updateSupervisorGradeAspect(SupervisorGradeAspectRequest request, int creator);
    List<SupervisorGradeAspectResponse> getListSupervisorGradeAspect();

    //Laporan KP PKL
    CreateId createLaporan(LaporanCreateRequest laporanCreateRequest, Integer participantId);
    void updateLaporan(LaporanUpdateRequest laporanUpdateRequest);
    LaporanResponse getLaporan(Integer id);
    List<LaporanResponse> getListLaporan(Integer participantId);
    Integer getPhase();
    Boolean isLaporanExist(int participantId, int phase);

    //Supervisor mapping
    void createSupervisorMapping(List<SupervisorMappingRequest> supervisorMappingRequest, String cookie, int creatorId);
    void updateSupervisorMapping(List<SupervisorMappingRequest> supervisorMappingRequest, String cookie, int creatorId);
    List<HashMap<Integer, String>> getUserList(String token);
    List<SupervisorMappingResponse> getSupervisorMapping(String accessToken, int prodi);
    List<SupervisorMappingLecturerResponse> getSupervisorMappingByLecturer(String accessToken, int lecturerId);
    SupervisorMappingResponse getSupervisorMappingByParticipant(String accessToken, int participantId);

    //Deadline
    void createDeadline(DeadlineCreateRequest request);
    void updateDeadline(DeadlineUpdateRequest request);
    DeadlineResponse getDeadline(int id);
    List<DeadlineResponse> getDeadline();

    //Monitoring
    DashboardParticipant getDashboardDataParticipant(int participantId);
    DashboardLecturer getDashboardDataLecturer(int lecturerId);
    DashboardCommittee getDashboardDataCommittee(int committeeId);

    //Reminder
    void sendReminderParticipantLogbook();
    void sendReminderParticipantRpp();
    void sendReminderParticipantSelfAssessment();
    void sendReminderSupervisorGrade();
}
