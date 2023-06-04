package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.deadline.DeadlineCreateRequest;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.deadline.DeadlineUpdateRequest;
import com.jtk.ps.api.dto.self_assessment.*;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeCreateRequest;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeUpdateRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingCreateRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.dto.rpp.RppResponse;
import com.jtk.ps.api.dto.rpp.RppCreateRequest;
import com.jtk.ps.api.dto.rpp.RppUpdateRequest;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeDetailResponse;
import com.jtk.ps.api.dto.rpp.RppDetailResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingUpdateRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface IMonitoringService {
    //RPP
    List<RppResponse> getRppList(int participantId);

    RppDetailResponse getRppDetail(int id);
    void createRpp(RppCreateRequest rpp, Integer participantId);
    void updateRpp(RppUpdateRequest rpp);

    //Logbook
    Boolean isLogbookExist(int participantId, LocalDate date);
    List<LogbookResponse> getLogbookByParticipantId(int participantId);
    LogbookDetailResponse getLogbookDetail(int id);
    void createLogbook(LogbookCreateRequest logbook, Integer participantId);
    void updateLogbook(LogbookUpdateRequest logbook);
    void gradeLogbook(LogbookGradeRequest gradeRequest);

    //Self Assessment
    Boolean isSelfAssessmentExist(int participantId, LocalDate date);
    List<SelfAssessmentAspectResponse> getSelfAssessmentAspect();
    void createSelfAssessment(SelfAssessmentRequest request, Integer participantId);
    SelfAssessmentDetailResponse getSelfAssessmentDetail(int id);
    List<SelfAssessmentResponse> getSelfAssessmentList(int idParticipant);
    List<SelfAssessmentGradeDetailResponse> getBestPerformance(int participantId);
    void updateSelfAssessment(SelfAssessmentUpdateRequest request);

    //Supervisor Grade
    void createSupervisorGrade(SupervisorGradeCreateRequest request);
    void updateSupervisorGrade(SupervisorGradeUpdateRequest request);
    SupervisorGradeDetailResponse getSupervisorGradeDetail(int id);
    List<SupervisorGradeResponse> getSupervisorGradeList(int participantId);
    void getMonitoringStatistic(int participantId);

    //Laporan KP PKL
    void createLaporan(LaporanCreateRequest laporanCreateRequest, Integer participantId);
    void updateLaporan(LaporanUpdateRequest laporanUpdateRequest);
    LaporanResponse getLaporan(Integer id);
    List<LaporanResponse> getListLaporan(Integer participantId);

    //Supervisor mapping
    void createSupervisorMapping(List<SupervisorMappingCreateRequest> supervisorMappingCreateRequest, String cookie);
    void updateSupervisorMapping(List<SupervisorMappingUpdateRequest> supervisorMapping);
    List<HashMap<Integer, String>> getUserList(String token);
    List<SupervisorMappingResponse> getSupervisorMapping(String accessToken);
    List<SupervisorMappingResponse> getSupervisorMappingByLecturer(String accessToken, int lecturerId);
    List<SupervisorMappingResponse> getSupervisorMappingByCompany(String accessToken, int companyId);
    List<SupervisorMappingResponse> getSupervisorMappingByProdi(String accessToken, int prodiId);
    List<SupervisorMappingResponse> getSupervisorMappingByYear(String accessToken, int year);
    SupervisorMappingResponse getSupervisorMappingByParticipant(int participantId);

    //Deadline
    void createDeadline(DeadlineCreateRequest request);
    void updateDeadline(DeadlineUpdateRequest request);
    DeadlineResponse getDeadline(int id);
    List<DeadlineResponse> getDeadline();

    //Reminder
    void sendReminderParticipantLogbook();
    void sendReminderParticipantRpp();
    void sendReminderParticipantSelfAssessment();
    void sendReminderSupervisorGrade();
}
