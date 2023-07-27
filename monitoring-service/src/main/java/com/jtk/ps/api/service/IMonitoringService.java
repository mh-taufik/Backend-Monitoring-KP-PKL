package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.*;
import com.jtk.ps.api.dto.dashboard.DashboardCommittee;
import com.jtk.ps.api.dto.dashboard.DashboardLecturer;
import com.jtk.ps.api.dto.dashboard.DashboardParticipant;
import com.jtk.ps.api.dto.deadline.DeadlineCreateRequest;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.deadline.DeadlineUpdateRequest;
import com.jtk.ps.api.dto.laporan.LaporanRekapResponse;
import com.jtk.ps.api.dto.rpp.*;
import com.jtk.ps.api.dto.self_assessment.*;
import com.jtk.ps.api.dto.supervisor_grade.*;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.model.ERole;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface IMonitoringService {
    //RPP
    List<RppResponse> getRppList(int participantId);
    RppDetailResponse getRppDetail(int id);
    CreateId createRpp(RppCreateRequest rpp, Integer participantId);
    void updateRpp(RppUpdateRequest rpp, Integer participantId);;
    CreateId createRpp(RppSimpleCreateRequest rpp, Integer participantId);
    void updateRpp(RppSimpleUpdateRequest rpp, Integer participantId);
    void createMilestone(List<MilestoneRequest> request, int rppId);
    void updateMilestone(List<MilestoneRequest> request, int rppId);
    void createDeliverables(List<DeliverablesRequest> request, int rppId);
    void updateDeliverables(List<DeliverablesRequest> request, int rppId);
    void createCompletionSchedule(List<CompletionScheduleRequest> request, int rppId);
    void updateCompletionSchedule(List<CompletionScheduleRequest> request, int rppId);
    void createWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId);
    void updateWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId);
    List<RppRekapResponse> getRekapRpp(ERole role, int id, String cookie);

    //Logbook
    Boolean isLogbookExist(int participantId, LocalDate date);
    List<LogbookResponse> getLogbookByParticipantId(int participantId);
    LogbookDetailResponse getLogbookDetail(int id);
    CreateId createLogbook(LogbookCreateRequest logbook, Integer participantId);
    void updateLogbook(LogbookUpdateRequest logbook, Integer participantId);
    void gradeLogbook(LogbookGradeRequest gradeRequest, int lecturer);
    List<LogbookRekapResponse> getRekapLogbook(ERole role, int id, String cookie);

    //Self Assessment
    Boolean isSelfAssessmentExist(int participantId, LocalDate date);
    CreateId createSelfAssessment(SelfAssessmentRequest request, Integer participantId, Integer prodi);
    SelfAssessmentDetailResponse getSelfAssessmentDetail(int id);
    List<SelfAssessmentResponse> getSelfAssessmentList(int idParticipant, int prodi);
    List<SelfAssessmentGradeDetailResponse> getBestPerformance(int participantId, int prodi);
    List<SelfAssessmentGradeDetailResponse> getAverage(int participantId, int prodi);
    SelfAssessmentFinalGradeResponse getFinalSelfAssessment(int participantId, int prodi);
    void updateSelfAssessment(SelfAssessmentUpdateRequest request, Integer participantId, Integer role, Integer prodi);
    void createSelfAssessmentAspect(SelfAssessmentAspectRequest request, int creator);
    void updateSelfAssessmentAspect(SelfAssessmentAspectRequest request, int creator);
    List<SelfAssessmentAspectResponse> getActiveSelfAssessmentAspect(int prodi);
    List<SelfAssessmentAspectResponse> getSelfAssessmentAspect(int prodi);
    List<SelfAssessmentRekapResponse> getRekapSelfAssessment(ERole role, int id, String cookie);
    void createSelfAssessmentMissingAfterDeadline();

    //Supervisor Grade
    CreateId createSupervisorGrade(SupervisorGradeCreateRequest request);
    void updateSupervisorGrade(SupervisorGradeUpdateRequest request, int supervisorId);
    SupervisorGradeDetailResponse getSupervisorGradeDetail(int id);
    List<SupervisorGradeResponse> getSupervisorGradeList(int participantId);
    StatisticResponse getMonitoringStatistic(int participantId);
    void createSupervisorGradeAspect(SupervisorGradeAspectRequest request, int creator, int prodi);
    void updateSupervisorGradeAspect(SupervisorGradeAspectRequest request, int creator, int prodi);
    List<SupervisorGradeAspectResponse> getListSupervisorGradeAspect(int prodi);

    //Laporan KP PKL
    CreateId createLaporan(LaporanCreateRequest laporanCreateRequest, Integer participantId);
    void updateLaporan(LaporanUpdateRequest laporanUpdateRequest, Integer participantId);
    LaporanResponse getLaporan(Integer id);
    List<LaporanResponse> getListLaporan(Integer participantId);
    Integer getPhase();
    Boolean isFinalPhase();
    Boolean isLaporanExist(int participantId, int phase);
    List<LaporanRekapResponse> getRekapLaporan(ERole role, int id, String cookie);

    //Supervisor mapping
    void createSupervisorMapping(List<SupervisorMappingRequest> supervisorMappingRequest, String cookie, int creatorId);
    void updateSupervisorMapping(List<SupervisorMappingRequest> supervisorMappingRequest, String cookie, int creatorId);
    List<HashMap<Integer, String>> getUserList(String cookie, Integer year, String type);
    List<SupervisorMappingResponse> getSupervisorMapping(String cookie);
    List<SupervisorMappingResponse> getSupervisorMappingByYear(String cookie, int year);
    List<SupervisorMappingResponse> getSupervisorMappingCommittee(String cookie, int prodi);
    List<SupervisorMappingResponse> getSupervisorMappingLecturer(String cookie, int lecturerId);
    SupervisorMappingResponse getSupervisorMappingByParticipant(String cookie, int participantId);
    Boolean isFinalSupervisorMapping(String cookie, int prodi);

    //Deadline
    void createDeadline(DeadlineCreateRequest request);
    void updateDeadline(DeadlineUpdateRequest request);
    DeadlineResponse getDeadline(int id);
    List<DeadlineResponse> getDeadlineLaporan();
    List<DeadlineResponse> getDeadline();

    //Monitoring
    DashboardParticipant getDashboardDataParticipant(int participantId);
    DashboardLecturer getDashboardDataLecturer(int lecturerId, String cookie);
    DashboardCommittee getDashboardDataCommittee(int prodiId, String cookie);
    AssociatedDocumentRpp getAssociatedRpp(int participantId, int rppId);
    AssociatedDocumentLogbook getAssociatedLogbook(int participantId, int logbookId);
    AssociatedDocumentSelfAssessment getAssociatedSelfAssessment(int participantId, int selfAsssessmentId);
    DocumentGradeStat getDocumentGradeStat(int participantId);
    HashMap<LocalDate, String> getHariLiburFromDate(LocalDate date);
}
