package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingLecturerResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.dto.rpp.RppResponse;
import com.jtk.ps.api.dto.rpp.RppCreateRequest;
import com.jtk.ps.api.dto.rpp.RppUpdateRequest;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentDetailResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeDetailResponse;
import com.jtk.ps.api.dto.rpp.RppDetailResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeResponse;
import com.jtk.ps.api.model.SupervisorMapping;

import java.util.List;

public interface IMonitoringService {
    //RPP
    List<RppResponse> getRppList(int participantId);

    RppDetailResponse getRppDetail(int id);
    void createRpp(RppCreateRequest rpp);
    void updateRpp(RppUpdateRequest rpp);

    //Logbook
    List<LogbookResponse> getLogbookByParticipantId(int participantId);
    LogbookDetailResponse getLogbookDetail(int id);
    void createLogbook(LogbookCreateRequest logbook);
    void updateLogbook(LogbookUpdateRequest logbook);
    void gradeLogbook(LogbookGradeRequest gradeRequest);

    //Self Assessment
    void createSelfAssessment();
    SelfAssessmentDetailResponse getSelfAssessmentDetail(int id);
    List<SelfAssessmentResponse> getSelfAssessmentList(int idParticipant);
    void updateSelfAssessment(int participantId);

    //Supervisor Grade
    void createSupervisorGrade(int participantId);
    SupervisorGradeDetailResponse getSupervisorGradeDetail(int id);
    List<SupervisorGradeResponse> getSupervisorGradeList(int participantId);
    void getMonitoringStatistic(int participantId);

    //Laporan KP PKL
    void createLaporan(LaporanCreateRequest laporanCreateRequest);
    void updateLaporan(LaporanUpdateRequest laporanUpdateRequest);
    LaporanResponse getLaporan(Integer id);
    List<LaporanResponse> getListLaporan(Integer participantId);


    //Supervisor mapping
    void setSupervisorMapping(SupervisorMapping supervisorMapping);
    List<SupervisorMappingResponse> getSupervisorMappingByLecturer(int lecturerId);
    List<SupervisorMappingResponse> getSupervisorMappingByCompany(int companyId);
    List<SupervisorMappingResponse> getSupervisorMappingByProdi(int prodiId);
    List<SupervisorMappingResponse> getSupervisorMappingByParticipant(int participantId);

    //Reminder
    void sendReminderParticipantLogbook();
    void sendReminderParticipantRpp();
    void sendReminderParticipantSelfAssessment();
    void sendReminderSupervisorGrade();

    //Deadline

}
