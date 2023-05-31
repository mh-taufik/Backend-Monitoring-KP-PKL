package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.ParticipantResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingLecturerResponse;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.dto.rpp.*;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentAspectDetailResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentDetailResponse;
import com.jtk.ps.api.dto.self_assessment.SelfAssessmentResponse;
import com.jtk.ps.api.dto.supervisor_grade.Grade;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeDetailResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeResponse;
import com.jtk.ps.api.model.*;
import com.jtk.ps.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringService implements IMonitoringService {

    @Autowired
    private LogbookRepository logbookRepository;
    @Autowired
    private RppRepository rppRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private DeliverablesRepository deliverablesRepository;
    @Autowired
    private WeeklyAchievementPlanRepository weeklyAchievementPlanRepository;
    @Autowired
    private CompletionScheduleRepository completionScheduleRepository;
    @Autowired
    private SelfAssessmentRepository selfAssessmentRepository;
    @Autowired
    private SelfAssessmentAspectRepository selfAssessmentAspectRepository;
    @Autowired
    private SelfAssessmentGradeRepository selfAssessmentGradeRepository;
    @Autowired
    private SupervisorGradeRepository supervisorGradeRepository;
    @Autowired
    private SupervisorGradeResultRepository supervisorGradeResultRepository;
    @Autowired
    private SupervisorMappingRepository supervisorMappingRepository;
    @Autowired
    private LaporanRepository laporanRepository;
    @Autowired
    private StatusRepository statusRepository;


    @Override
    public List<RppResponse> getRppList(int participantId) {
        List<Rpp> rppList = rppRepository.findByParticipantId(participantId);
        List<RppResponse> responses = new ArrayList<>();
        for(Rpp temp:rppList){
            responses.add(new RppResponse(temp.getId(), temp.getWorkTitle(), temp.getStartDate(), temp.getFinishDate(), temp.getStatus().getStatus()));
        }
        return responses;
    }

    @Override
    public RppDetailResponse getRppDetail(int id) {
        Rpp rpp = rppRepository.findById(id);
        List<CompletionSchedule> completionScheduleList = completionScheduleRepository.findByRpp(rpp);
        List<CompletionScheduleResponse> completionScheduleResponse = new ArrayList<>();
        for(CompletionSchedule temp:completionScheduleList){
            completionScheduleResponse.add(new CompletionScheduleResponse(temp.getId(), temp.getTaskName(), temp.getTaskType().name(), temp.getStartDate(), temp.getFinishDate()));
        }

        RppDetailResponse rppDetail = new RppDetailResponse(
                rpp,
                milestoneRepository.findByRpp(rpp),
                deliverablesRepository.findByRpp(rpp),
                completionScheduleResponse,
                weeklyAchievementPlanRepository.findByRpp(rpp)
                );
        return rppDetail;
    }

    @Override
    public void createRpp(RppCreateRequest rpp) {
        Rpp rppNew = new Rpp();
        rppNew.setParticipantId(rpp.getParticipantId());
        rppNew.setWorkTitle(rpp.getWorkTitle());
        rppNew.setGroupRole(rpp.getGroupRole());
        rppNew.setTaskDescription(rpp.getTaskDescription());
        rppNew.setStartDate(rpp.getStartDate());
        rppNew.setFinishDate(rpp.getFinishDate());
        rppNew.setStatus(statusRepository.findById(1));
        rppNew.setMilestones(rpp.getMilestones());
        rppNew.setDeliverables(rpp.getDeliverables());
        rppNew.setCompletionSchedules(rpp.getCompletionSchedules());
        rppNew.setWeeklyAchievementPlans(rpp.getWeeklyAchievementPlans());

        rppRepository.save(rppNew);
    }

    @Override
    public void updateRpp(RppUpdateRequest rppUpdate) {
//        RppDetailResponse rppOld = getRppDetail(id);
        //check date
        try {
            Rpp rpp = new Rpp(
                    rppUpdate.getRppId(),
null,
//                    rppUpdate.getParticipantId(),
                    rppUpdate.getGroupRole(),
                    rppUpdate.getWorkTitle(),
                    rppUpdate.getTaskDescription(),
                    rppUpdate.getStartDate(),
                    rppUpdate.getFinishDate(),
                    rppUpdate.getStatus(),
                    rppUpdate.getMilestones(),
                    rppUpdate.getDeliverables(),
                    rppUpdate.getCompletionSchedules(),
                    rppUpdate.getWeeklyAchievementPlans()
            );
            rppRepository.save(rpp);
//            if(rppUpdate.getMilestones() != null){
//                milestoneRepository.deleteAllByRppId(id);
//                milestoneRepository.saveAll(rppUpdate.getMilestones());
//            }
//            if(rppUpdate.getDeliverables() != null){
//                deliverablesRepository.deleteAllByRppId(id);
//                deliverablesRepository.saveAll(rppUpdate.getDeliverables());
//            }
//            if(rppUpdate.getCompletionSchedules() != null){
//                completionScheduleRepository.deleteAllByRppId(id);
//                completionScheduleRepository.saveAll(rppUpdate.getCompletionSchedules());
//            }
//            if(rppUpdate.getWeeklyAchievementPlans() != null){
//                weeklyAchievementPlanRepository.deleteAllByRppId(id);
//                weeklyAchievementPlanRepository.saveAll(rppUpdate.getWeeklyAchievementPlans());
//            }
        } catch (Exception e){

        }
    }

    @Override
    public List<LogbookResponse> getLogbookByParticipantId(int participantId) {
        List<Logbook> logbookList = logbookRepository.findByParticipantId(participantId);
        List<LogbookResponse> responses = new ArrayList<>();
        for(Logbook temp:logbookList){
            responses.add(new LogbookResponse(temp.getId(), temp.getDate(), temp.getGrade(), temp.getStatus().getStatus()));
        }
        return responses;
    }

    @Override
    public LogbookDetailResponse getLogbookDetail(int id) {
        LogbookDetailResponse logbookResponse = new LogbookDetailResponse(logbookRepository.findById(id));
        return logbookResponse;
    }

    @Override
    public void createLogbook(LogbookCreateRequest logbook) {
        if(logbookRepository.logbookExist(logbook.getParticipantId(), logbook.getDate())) {
            throw new IllegalStateException("Logbook already created on this date, please update it instead");
        }
        Logbook newLogbook = new Logbook(logbook);
        if(LocalDate.now().isAfter(newLogbook.getDate())){
            newLogbook.setStatus(statusRepository.findById(4));
        }else{
            newLogbook.setStatus(statusRepository.findById(5));
        }
        logbookRepository.save(newLogbook);
    }

    @Override
    public void updateLogbook(LogbookUpdateRequest logbook) {
        if(logbookRepository.isChecked(logbook.getId())) {
            throw new IllegalStateException("Logbook already been graded, cant be edit anymore");
        }
        Logbook newLogbook = new Logbook(logbook);
        logbookRepository.save(newLogbook);
    }

    @Override
    public void gradeLogbook(LogbookGradeRequest gradeRequest) {
        Logbook logbook = logbookRepository.findById(gradeRequest.getId());
        if(logbook.getId() != null && logbook.getGrade() == null){
            logbook.setGrade(gradeRequest.getGrade());
            logbook.setStatus(statusRepository.findById(2));
            logbookRepository.save(logbook);
        }
    }

    @Override
    public void createSelfAssessment() {
//        List<SelfAssessment> assessmentList = ;
        List<SelfAssessmentAspect> aspectLists = selfAssessmentAspectRepository.findAll();
        for (SelfAssessmentAspect i:aspectLists){

        }
    }

    @Override
    public SelfAssessmentDetailResponse getSelfAssessmentDetail(int id) {
        SelfAssessment selfAssessment = selfAssessmentRepository.findById(id);
        List<SelfAssessmentGrade> grades = selfAssessmentGradeRepository.findBySelfAssessmentId(id);
        List<SelfAssessmentAspectDetailResponse> aspectList = new ArrayList<>();
        for(SelfAssessmentGrade temp: grades){
            aspectList.add(new SelfAssessmentAspectDetailResponse(
                    temp.getSelfAssessmentAspect().getId(),
                    temp.getId(),
                    temp.getSelfAssessmentAspect().getName(),
                    temp.getGrade(),
                    temp.getDescription())
            );
        }
        SelfAssessmentDetailResponse selfAssessmentDetailResponse = new SelfAssessmentDetailResponse(
            selfAssessment.getParticipantId(),
            selfAssessment.getId(),
            selfAssessment.getStartDate(),
            selfAssessment.getFinishDate(),
            aspectList
        );
        return selfAssessmentDetailResponse;
    }

    @Override
    public List<SelfAssessmentResponse> getSelfAssessmentList(int idParticipant) {
        List<SelfAssessment> selfAssessments = selfAssessmentRepository.findByParticipantId(idParticipant);
        List<SelfAssessmentResponse> responses = new ArrayList<>();
        for(SelfAssessment temp:selfAssessments){
            responses.add(new SelfAssessmentResponse(temp.getParticipantId(), temp.getId(), temp.getStartDate(), temp.getFinishDate()));
        }
        return responses;
    }

    @Override
    public void updateSelfAssessment(int participantId) {

    }

    @Override
    public void createSupervisorGrade(int participantId) {
        
    }

    @Override
    public SupervisorGradeDetailResponse getSupervisorGradeDetail(int id) {
        SupervisorGrade supervisorGrade = supervisorGradeRepository.findById(id);
        List<SupervisorGradeResult> result = supervisorGradeResultRepository.findBySupervisorGradeId(id);
        List<Grade> grades = new ArrayList<>();
        for(SupervisorGradeResult temp: result){
            grades.add(new Grade(temp.getId(), temp.getAspectGrade().getDescription(), temp.getGrade()));
        }
        SupervisorGradeDetailResponse response = new SupervisorGradeDetailResponse(
                supervisorGrade.getId(),
                supervisorGrade.getDate(),
                supervisorGrade.getPhase(),
                supervisorGrade.getParticipantId(),
                grades
        );
        return response;
    }

    @Override
    public List<SupervisorGradeResponse> getSupervisorGradeList(int participantId) {
        List<SupervisorGrade> supervisorGrade = supervisorGradeRepository.findByParticipantId(participantId);
        List<SupervisorGradeResponse> response = new ArrayList<>();
        for(SupervisorGrade temp:supervisorGrade){
            response.add(new SupervisorGradeResponse(temp.getId(), temp.getDate(), temp.getPhase()));
        }
        return response;
    }

    @Override
    public void getMonitoringStatistic(int participantId) {
        //TODO: get all logbook, make percentage
        //TODO: get all self assessment, make percentage
    }

    @Override
    public void createLaporan(LaporanCreateRequest laporanCreateRequest) {
        Laporan laporan = new Laporan();
        laporan.setParticipant(laporanCreateRequest.getParticipantId());
        laporan.setUriName(laporanCreateRequest.getUri());
        laporan.setPhase(laporanCreateRequest.getPhase());
        laporan.setUploadDate(LocalDate.now());

        if(laporanRepository.findByParticipantIdAndPhase(laporanCreateRequest.getParticipantId(), laporanCreateRequest.getPhase()) == null){
            laporan.setId(null);
        }
        laporanRepository.save(laporan);
    }

    @Override
    public void updateLaporan(LaporanUpdateRequest laporanUpdateRequest) {
        if(laporanUpdateRequest.getId() == null || laporanUpdateRequest.getId() == 0){
            throw new IllegalStateException("cant edit, id cant be null or 0");
        }
        Laporan laporan = laporanRepository.findById((int)laporanUpdateRequest.getId());
        if(laporan == null){
            throw new IllegalStateException("cant edit, id cant be null or 0");
        }

        laporan.setUriName(laporanUpdateRequest.getUri());
        laporan.setPhase(laporanUpdateRequest.getPhase());
        laporan.setUploadDate(LocalDate.now());
        laporanRepository.save(laporan);
    }

    @Override
    public LaporanResponse getLaporan(Integer id) {
        Optional<Laporan> laporan = laporanRepository.findById(id);
        if(laporan.isPresent()){
            return new LaporanResponse(laporan.get());
        }else{
            return null;
        }
    }

    @Override
    public List<LaporanResponse> getListLaporan(Integer participantId){
        List<Laporan> laporanList = laporanRepository.findByParticipantId(participantId);
        List<LaporanResponse> responses = new ArrayList<>();
        for(Laporan temp:laporanList) {
            responses.add(new LaporanResponse(temp.getId(), temp.getUriName(), temp.getUploadDate(), temp.getPhase()));
        }
        return responses;
    }

    @Override
    public void setSupervisorMapping(SupervisorMapping supervisorMapping) {

    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByLecturer(int lecturerId) {
        List<SupervisorMapping> supervisorMapping = supervisorMappingRepository.findByLecturerId(lecturerId);
        List<SupervisorMappingResponse> response = new ArrayList<>();
        for(SupervisorMapping temp:supervisorMapping){
            SupervisorMappingResponse mapping = new SupervisorMappingResponse();
            mapping.setParticipantId(temp.getParticipantId());
            mapping.setLecturerId(temp.getLecturerId());
            mapping.setCompanyId(temp.getCompanyId());
            mapping.setProdiId(temp.getProdiId());
//            ResponseEntity<ResponseList<ParticipantResponse>> pResponse = restTemplate.exchange(
//                    "http://participant-service/participant/get-all?year=" + currentYear,
//                    HttpMethod.GET,
//                    req,
//                    new ParameterizedTypeReference<>() {
//                    });


            response.add(mapping);
        }
        return response;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByCompany(int companyId) {
        return null;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByProdi(int prodiId) {
        return null;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByParticipant(int participantId) {
        return null;
    }

    @Override
    public void sendReminderParticipantLogbook() {

    }

    @Override
    public void sendReminderParticipantRpp() {

    }

    @Override
    public void sendReminderParticipantSelfAssessment() {

    }

    @Override
    public void sendReminderSupervisorGrade() {

    }

//    public

}
