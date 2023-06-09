package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.*;
import com.jtk.ps.api.dto.deadline.DeadlineCreateRequest;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.deadline.DeadlineUpdateRequest;
import com.jtk.ps.api.dto.self_assessment.*;
import com.jtk.ps.api.dto.supervisor_grade.*;
import com.jtk.ps.api.dto.supervisor_mapping.*;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.dto.rpp.*;
import com.jtk.ps.api.model.*;
import com.jtk.ps.api.repository.*;
import com.jtk.ps.api.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.next;

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
    private SupervisorGradeAspectRepository supervisorGradeAspectRepository;
    @Autowired
    private SupervisorGradeResultRepository supervisorGradeResultRepository;
    @Autowired
    private SupervisorMappingRepository supervisorMappingRepository;
    @Autowired
    private LaporanRepository laporanRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private DeadlineRepository deadlineRepository;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<RppResponse> getRppList(int participantId) {
        List<Rpp> rppList = rppRepository.findByParticipantId(participantId);
        if(rppList.size()==0)
            throw new IllegalStateException("Rpp tidak ditemukan");
        List<RppResponse> responses = new ArrayList<>();
        for(Rpp temp:rppList){
            Rpp rpp = rppRepository.findById((int)temp.getId());
            if(rpp.getStartDate().isBefore(LocalDate.now()) && rpp.getFinishDate().isAfter(LocalDate.now())){
                rpp.setStatus(statusRepository.findById(9));
                rppRepository.save(rpp);
                responses.add(new RppResponse(temp.getId(), temp.getWorkTitle(), temp.getStartDate(), temp.getFinishDate(), rpp.getStatus().getStatus()));
            }
            if(rpp.getFinishDate().isBefore(LocalDate.now())){
                rpp.setStatus(statusRepository.findById(10));
                rppRepository.save(rpp);
                responses.add(new RppResponse(temp.getId(), temp.getWorkTitle(), temp.getStartDate(), temp.getFinishDate(), rpp.getStatus().getStatus()));
            }
            if(rpp.getStartDate().isAfter(LocalDate.now())){
                rpp.setStatus(statusRepository.findById(11));
                rppRepository.save(rpp);
                responses.add(new RppResponse(temp.getId(), temp.getWorkTitle(), temp.getStartDate(), temp.getFinishDate(), rpp.getStatus().getStatus()));
            }
        }
        return responses;
    }

    @Override
    public RppDetailResponse getRppDetail(int id) {
        Rpp rpp = rppRepository.findById(id);
        if(rpp == null){
            throw new IllegalStateException("Rpp tidak ditemukan");
        }
        RppDetailResponse rppDetail = new RppDetailResponse(
                rpp,
                milestoneRepository.findByRpp(rpp),
                deliverablesRepository.findByRpp(rpp),
                completionScheduleRepository.findByRpp(rpp),
                weeklyAchievementPlanRepository.findByRpp(rpp)
                );
        return rppDetail;
    }

    @Override
    public CreateId createRpp(RppCreateRequest rpp, Integer participantId) {
        Rpp rppNew = new Rpp();
        rppNew.setParticipantId(participantId);
        rppNew.setWorkTitle(rpp.getWorkTitle());
        rppNew.setGroupRole(rpp.getGroupRole());
        rppNew.setTaskDescription(rpp.getTaskDescription());
        rppNew.setStartDate(rpp.getStartDate());
        rppNew.setFinishDate(rpp.getFinishDate());
        if((rpp.getStartDate().isBefore(LocalDate.now()) || rpp.getStartDate().isEqual(LocalDate.now())) && (rpp.getFinishDate().isAfter(LocalDate.now()) || rpp.getFinishDate().isEqual(LocalDate.now()))) {
            rppNew.setStatus(statusRepository.findById(9));
        }
        if(rpp.getFinishDate().isBefore(LocalDate.now())){
            rppNew.setStatus(statusRepository.findById(10));
        }
        if(rpp.getStartDate().isAfter(LocalDate.now())){
            rppNew.setStatus(statusRepository.findById(11));
        }
        Rpp temp = rppRepository.save(rppNew);

        for(MilestoneRequest milestone:rpp.getMilestones()){
            milestoneRepository.save(new Milestone(null, temp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
        }
        for(DeliverablesRequest deliverable:rpp.getDeliverables()){
            deliverablesRepository.save(new Deliverable(null, temp, deliverable.getDeliverables(), deliverable.getDueDate()));
        }
        for(CompletionScheduleRequest completionSchedule:rpp.getCompletionSchedules()){
            completionScheduleRepository.save(new CompletionSchedule(null, temp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
        }
        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:rpp.getWeeklyAchievementPlans()){
            weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(null, temp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
        }

        return new CreateId(temp.getId());
    }

    @Override
    public void updateRpp(RppUpdateRequest rppUpdate, Integer participantId) {
        Rpp rpp = rppRepository.findById(rppUpdate.getRppId());
        if(rpp == null){
            throw new IllegalStateException("Rpp tidak ditemukan");
        }
        if(rpp.getParticipantId() != participantId){
            throw new IllegalStateException("Rpp tidak dapat diedit");
        }
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        if(rppUpdate.getFinishDate().isAfter(sunday))
            rpp.setFinishDate(rppUpdate.getFinishDate());
        else
            throw new IllegalStateException("cant update rpp, date must be after this week");

        Rpp temp = rppRepository.save(rpp);

        for(MilestoneRequest milestone:rppUpdate.getMilestones()){
            if(milestone.getStartDate().isAfter(sunday))
                milestoneRepository.save(new Milestone(milestone.getId(), temp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
        }
        for(DeliverablesRequest deliverable:rppUpdate.getDeliverables()){
            if(deliverable.getDueDate().isAfter(sunday))
                deliverablesRepository.save(new Deliverable(deliverable.getId(), temp, deliverable.getDeliverables(), deliverable.getDueDate()));
        }
        for(CompletionScheduleRequest completionSchedule:rppUpdate.getCompletionSchedules()){
            if(completionSchedule.getFinishDate().isAfter(sunday))
                completionScheduleRepository.save(new CompletionSchedule(completionSchedule.getId(), temp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
        }
        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:rppUpdate.getWeeklyAchievementPlans()){
            if(weeklyAchievementPlan.getFinishDate().isAfter(sunday))
                weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(weeklyAchievementPlan.getId(), temp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
        }
    }

//    @Override
//    public void updateRpp(RppSimpleUpdateRequest rppUpdate) {
//        Rpp rpp = rppRepository.findById(rppUpdate.getRppId());
//        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
//        if(rppUpdate.getFinishDate().isAfter(sunday))
//            rpp.setFinishDate(rppUpdate.getFinishDate());
//        else
//            throw new IllegalStateException("cant update rpp, date must be after this week");
//    }

//    @Override
//    public CreateId createRpp(RppSimpleCreateRequest rpp, Integer participantId) {
//        Rpp rppNew = new Rpp();
//        rppNew.setParticipantId(participantId);
//        rppNew.setWorkTitle(rpp.getWorkTitle());
//        rppNew.setGroupRole(rpp.getGroupRole());
//        rppNew.setTaskDescription(rpp.getTaskDescription());
//        rppNew.setStartDate(rpp.getStartDate());
//        rppNew.setFinishDate(rpp.getFinishDate());
//        if((rpp.getStartDate().isBefore(LocalDate.now()) || rpp.getStartDate().isEqual(LocalDate.now())) && (rpp.getFinishDate().isAfter(LocalDate.now()) || rpp.getFinishDate().isEqual(LocalDate.now()))) {
//            rppNew.setStatus(statusRepository.findById(9));
//        }
//        if(rpp.getFinishDate().isBefore(LocalDate.now())){
//            rppNew.setStatus(statusRepository.findById(10));
//        }
//        if(rpp.getStartDate().isAfter(LocalDate.now())){
//            rppNew.setStatus(statusRepository.findById(11));
//        }
//        Rpp temp = rppRepository.save(rppNew);
//
//        return new CreateId(temp.getId());
//    }

//    @Override
//    public void createMilestone(List<MilestoneRequest> request, int rppId) {
//        Rpp rpp = rppRepository.findById(rppId);
//        for(MilestoneRequest milestone:request){
//            milestoneRepository.save(new Milestone(null, rpp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
//        }
//    }
//
//    @Override
//    public void updateMilestone(List<MilestoneRequest> request, int rppId) {
//        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
//        Rpp rpp = rppRepository.findById(rppId);
//        for(MilestoneRequest milestone: request){
//            if(milestone.getStartDate().isAfter(sunday))
//                milestoneRepository.save(new Milestone(null, rpp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
//        }
//    }
//
//    @Override
//    public void createDeliverables(List<DeliverablesRequest> request, int rppId) {
//        Rpp rpp = rppRepository.findById(rppId);
//        for(DeliverablesRequest deliverable:request){
//            deliverablesRepository.save(new Deliverable(null, rpp, deliverable.getDeliverables(), deliverable.getDueDate()));
//        }
//    }
//
//    @Override
//    public void updateDeliverables(List<DeliverablesRequest> request, int rppId) {
//        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
//        Rpp rpp = rppRepository.findById(rppId);
//        for(DeliverablesRequest deliverable:request){
//            if(deliverable.getDueDate().isAfter(sunday))
//                deliverablesRepository.save(new Deliverable(deliverable.getId(), rpp, deliverable.getDeliverables(), deliverable.getDueDate()));
//        }
//    }
//
//    @Override
//    public void createCompletionSchedule(List<CompletionScheduleRequest> request, int rppId) {
//        Rpp rpp = rppRepository.findById(rppId);
//        for(CompletionScheduleRequest completionSchedule:request){
//            completionScheduleRepository.save(new CompletionSchedule(null, rpp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
//        }
//    }
//
//    @Override
//    public void updateCompletionSchedule(List<CompletionScheduleRequest> request, int rppId) {
//        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
//        Rpp rpp = rppRepository.findById(rppId);
//        for(CompletionScheduleRequest completionSchedule:request){
//            if(completionSchedule.getStartDate().isAfter(sunday))
//                completionScheduleRepository.save(new CompletionSchedule(completionSchedule.getId(), rpp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
//        }
//    }
//
//    @Override
//    public void createWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId) {
//        Rpp rpp = rppRepository.findById(rppId);
//        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:request){
//            weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(null, rpp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
//        }
//    }
//
//    @Override
//    public void updateWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId) {
//        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
//        Rpp rpp = rppRepository.findById(rppId);
//        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:request){
//            if(weeklyAchievementPlan.getStartDate().isAfter(sunday))
//                weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(weeklyAchievementPlan.getId(), rpp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
//        }
//    }

    @Override
    public Boolean isLogbookExist(int participantId, LocalDate date) {
        HttpEntity<String> req = new HttpEntity<>(new HttpHeaders());
        RestTemplate template = new RestTemplate();
        ResponseEntity<List<LiburNasionalResponse>> response = template.exchange(
                "https://api-harilibur.vercel.app/api?year=" + date.getYear() + "&month=" + date.getMonth(),
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {});
        List<LiburNasionalResponse> hariLibur = Objects.requireNonNull(response.getBody());
        for(LiburNasionalResponse temp:hariLibur){
            if(date == temp.getHolidayDate() && temp.getIsNationalHoliday())
                throw new IllegalStateException("Hari ini hari libur "+temp.getHolidayName());
        }
        if(date.getDayOfWeek().name() == SUNDAY.name() || date.getDayOfWeek().name() == SATURDAY.name())
            throw new IllegalStateException("Hari ini termasuk weekend, tidak menerima logbook");

        return logbookRepository.isExist(participantId, date);
    }

    @Override
    public List<LogbookResponse> getLogbookByParticipantId(int participantId) {
        List<Logbook> logbookList = logbookRepository.findByParticipantIdOrderByDateAsc(participantId);
        if(logbookList.size()==0)
            throw new IllegalStateException("Logbook tidak ditemukan");
        List<LogbookResponse> responses = new ArrayList<>();
        for(Logbook temp:logbookList){
            if(temp.getGrade() != null)
                responses.add(new LogbookResponse(temp.getId(), temp.getDate(), temp.getGrade().name().replace("_", " "), temp.getStatus().getStatus(), temp.getProjectName()));
            else
                responses.add(new LogbookResponse(temp.getId(), temp.getDate(), "BELUM DINILAI", temp.getStatus().getStatus(), temp.getProjectName()));
        }
        return responses;
    }

    @Override
    public LogbookDetailResponse getLogbookDetail(int id) {
        LogbookDetailResponse logbookResponse = new LogbookDetailResponse();
        Logbook logbook = logbookRepository.findById(id);
        logbookResponse.setId(logbook.getId());
        logbookResponse.setParticipantId(logbook.getParticipantId());
        logbookResponse.setDate(logbook.getDate());
        logbookResponse.setProjectName(logbook.getProjectName());
        logbookResponse.setProjectManager(logbook.getProjectManager());
        logbookResponse.setTechnicalLeader(logbook.getTechnicalLeader());
        logbookResponse.setTask(logbook.getTask());
        logbookResponse.setTimeAndActivity(logbook.getTimeAndActivity());
        logbookResponse.setTools(logbook.getTools());
        logbookResponse.setWorkResult(logbook.getWorkResult());
        logbookResponse.setDescription(logbook.getDescription());
        logbookResponse.setStatus(logbook.getStatus());
        if(logbook.getEncounteredProblem() == null)
            logbookResponse.setEncounteredProblem("-");
        else
            logbookResponse.setEncounteredProblem(logbook.getEncounteredProblem());
        if(logbook.getGrade() == null)
            logbookResponse.setGrade("BELUM DINILAI");
        else
            logbookResponse.setGrade(logbook.getGrade().name());
        return logbookResponse;
    }

    @Override
    public CreateId createLogbook(LogbookCreateRequest logbook, Integer participantId) {
        if(logbookRepository.isExist(participantId, logbook.getDate())) {
            throw new IllegalStateException("Logbook already created on this date, please update it instead");
        }
        if(logbook.getDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("pengumpulan belum dibuka logbook untuk tanggal "+logbook.getDate());
        }
        Logbook newLogbook = new Logbook(
                null,
                participantId,
                logbook.getDate(),
                logbook.getProjectName(),
                logbook.getProjectManager(),
                logbook.getTechnicalLeader(),
                logbook.getTask(),
                logbook.getTimeAndActivity(),
                logbook.getTools(),
                logbook.getWorkResult(),
                logbook.getDescription(),
                ENilai.BELUM_DINILAI,
                logbook.getEncounteredProblem(),
                null
        );

        if(LocalDate.now().isAfter(newLogbook.getDate())){
            newLogbook.setStatus(statusRepository.findById(4));
        }else{
            newLogbook.setStatus(statusRepository.findById(5));
        }
        Logbook temp = logbookRepository.save(newLogbook);
        return new CreateId(temp.getId());
    }

    @Override
    public void updateLogbook(LogbookUpdateRequest logbook, Integer participantId) {
        if(logbookRepository.isChecked(logbook.getId())) {
            throw new IllegalStateException("Logbook already been graded, cant be edit anymore");
        }
        Logbook newLogbook = logbookRepository.findById((int)logbook.getId());
        if(newLogbook.getParticipantId() != participantId)
            throw new IllegalStateException("Logbook tidak dapat diakses");
        if(!logbook.getDescription().isEmpty())
            newLogbook.setDescription(logbook.getDescription());
        if(!logbook.getProjectName().isEmpty())
            newLogbook.setProjectName(logbook.getProjectName());
        if(!logbook.getProjectManager().isEmpty())
            newLogbook.setProjectManager(logbook.getProjectManager());
        if(!logbook.getTechnicalLeader().isEmpty())
            newLogbook.setTechnicalLeader(logbook.getTechnicalLeader());
        if(!logbook.getTask().isEmpty())
            newLogbook.setTask(logbook.getTask());
        if(!logbook.getTimeAndActivity().isEmpty())
            newLogbook.setTimeAndActivity(logbook.getTimeAndActivity());
        if(!logbook.getTools().isEmpty())
            newLogbook.setTools(logbook.getTools());
        if(!logbook.getWorkResult().isEmpty())
            newLogbook.setWorkResult(logbook.getWorkResult());
        if(!logbook.getEncounteredProblem().isEmpty())
            newLogbook.setEncounteredProblem(logbook.getEncounteredProblem());

        if(LocalDate.now().isAfter(newLogbook.getDate()))
            newLogbook.setStatus(statusRepository.findById(4));
        else
            newLogbook.setStatus(statusRepository.findById(5));

        logbookRepository.save(newLogbook);
    }

    @Override
    public void gradeLogbook(LogbookGradeRequest gradeRequest, int lecturer) {
        Logbook logbook = logbookRepository.findById(gradeRequest.getId());
        if(logbook == null)
            throw new IllegalStateException("logbook tidak ditemukan");
        if(supervisorMappingRepository.findLecturerId(logbook.getParticipantId()) != lecturer)
            throw new IllegalStateException("Logbook tidak dapat diakses");
        if(logbook.getId() != null && logbook.getGrade() == ENilai.BELUM_DINILAI){
            logbook.setGrade(gradeRequest.getGrade());
            logbookRepository.save(logbook);
        }
    }

    @Override
    public Boolean isSelfAssessmentExist(int participantId, LocalDate date) {
        return selfAssessmentRepository.isExist(participantId, date);
    }

    @Override
    public List<SelfAssessmentAspectResponse> getActiveSelfAssessmentAspect() {
        List<SelfAssessmentAspect> aspect = selfAssessmentAspectRepository.findAllActiveAspect();
        Deadline deadline = deadlineRepository.findByNameLike("self assessment");
        List<SelfAssessmentAspectResponse> response = new ArrayList<>();
        for(SelfAssessmentAspect temp:aspect){
            response.add(new SelfAssessmentAspectResponse(temp.getId(), temp.getName(), temp.getStartAssessmentDate(), temp.getDescription(), temp.getStatus().getStatus(), deadline.getDayRange()));
        }
        return response;
    }

    @Override
    public List<SelfAssessmentAspectResponse> getSelfAssessmentAspect() {
        List<SelfAssessmentAspect> aspect = selfAssessmentAspectRepository.findAll();
        Deadline deadline = deadlineRepository.findByNameLike("self assessment");
        List<SelfAssessmentAspectResponse> response = new ArrayList<>();
        for(SelfAssessmentAspect temp:aspect){
            response.add(new SelfAssessmentAspectResponse(temp.getId(), temp.getName(), temp.getStartAssessmentDate(), temp.getDescription(), temp.getStatus().getStatus(), deadline.getDayRange()));
        }
        return response;
    }

    @Override
    public CreateId createSelfAssessment(SelfAssessmentRequest request, Integer participantId) {
        SelfAssessment selfAssessment = new SelfAssessment();
        selfAssessment.setParticipantId(participantId);
        selfAssessment.setStartDate(request.getStartDate());
        selfAssessment.setFinishDate(request.getFinishDate());
        SelfAssessment sa = selfAssessmentRepository.save(selfAssessment);

        List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAllActiveAspect();
        List<SelfAssessmentGrade> gradeList = new ArrayList<>();
        for (SelfAssessmentAspect aspect : aspectList) {
            for(AssessmentGradeRequest grade: request.getGrade()) {
                if (aspect.getId() == grade.getAspectId() && grade != null){
                    if(grade.getGrade() == null)
                        gradeList.add(new SelfAssessmentGrade(null, sa, aspect, 0, grade.getDescription()));
                    else
                        gradeList.add(new SelfAssessmentGrade(null, sa, aspect, grade.getGrade(), grade.getDescription()));
                    break;
                }
            }
        }
        selfAssessmentGradeRepository.saveAll(gradeList);
        return new CreateId(sa.getId());
    }

    @Override
    public SelfAssessmentDetailResponse getSelfAssessmentDetail(int id) {
        SelfAssessment selfAssessment = selfAssessmentRepository.findById(id);
        if(selfAssessment == null)
            throw new IllegalStateException("Self Assessment tidak ditemukan");

        List<SelfAssessmentGrade> grades = selfAssessmentGradeRepository.findBySelfAssessmentId(id);
        List<SelfAssessmentGradeDetailResponse> aspectList = new ArrayList<>();
        for(SelfAssessmentGrade temp: grades){
            if(temp.getGrade()!=null) {
                aspectList.add(new SelfAssessmentGradeDetailResponse(
                        temp.getSelfAssessmentAspect().getId(),
                        temp.getId(),
                        temp.getSelfAssessmentAspect().getName(),
                        temp.getGrade(),
                        temp.getDescription())
                );
            }else{
                aspectList.add(new SelfAssessmentGradeDetailResponse(
                        temp.getSelfAssessmentAspect().getId(),
                        temp.getId(),
                        temp.getSelfAssessmentAspect().getName(),
                        0,
                        temp.getDescription())
                );
            }
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
        List<SelfAssessment> selfAssessments = selfAssessmentRepository.findByParticipantIdOrderByStartDateAsc(idParticipant);
        if(selfAssessments.size() == 0)
            throw new IllegalStateException("Self Assessment belum dibuat");
        List<SelfAssessmentResponse> responses = new ArrayList<>();
        for(SelfAssessment temp:selfAssessments){
            List<SelfAssessmentGrade> grades = selfAssessmentGradeRepository.findBySelfAssessmentId(temp.getId());
            List<SelfAssessmentGradeDetailResponse> grade = new ArrayList<>();
            for(SelfAssessmentGrade temp2: grades){
                grade.add(new SelfAssessmentGradeDetailResponse(
                        temp2.getSelfAssessmentAspect().getId(),
                        temp2.getId(),
                        temp2.getSelfAssessmentAspect().getName(),
                        temp2.getGrade(),
                        temp2.getDescription())
                );
            }
            responses.add(new SelfAssessmentResponse(temp.getParticipantId(), temp.getId(), temp.getStartDate(), temp.getFinishDate(), grade));
        }
        responses.add(new SelfAssessmentResponse(idParticipant, null, null, null, getBestPerformance(idParticipant)));
        return responses;
    }

    @Override
    public List<SelfAssessmentGradeDetailResponse> getBestPerformance(int participantId) {
        List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAll();
        List<SelfAssessmentGradeDetailResponse> grades = new ArrayList<>();
        for(SelfAssessmentAspect aspect:aspectList){
            SelfAssessmentGrade grade = selfAssessmentGradeRepository.findMaxGradeByParticipantIdAndAspectId(participantId, aspect.getId());
            if(grade != null){
                grades.add(new SelfAssessmentGradeDetailResponse(
                        grade.getSelfAssessmentAspect().getId(),
                        grade.getId(),
                        grade.getSelfAssessmentAspect().getName(),
                        grade.getGrade(),
                        grade.getDescription()));
                break;
            }
        }
        return grades;
    }

    @Override
    public void updateSelfAssessment(SelfAssessmentUpdateRequest request, Integer participantId) {
        SelfAssessment temp = selfAssessmentRepository.findById((int)request.getId());
        if(temp.getParticipantId() != participantId)
            throw new IllegalStateException("Self Assessment tidak dapat diakses");

        SelfAssessment selfAssessment = new SelfAssessment();
        selfAssessment.setId(request.getId());
        selfAssessment.setParticipantId(request.getParticipantId());
        selfAssessment.setStartDate(request.getStartDate());
        selfAssessment.setFinishDate(request.getFinishDate());
        SelfAssessment sa = selfAssessmentRepository.save(selfAssessment);

        List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAllActiveAspect();
        List<SelfAssessmentGrade> gradeList = new ArrayList<>();
        for (SelfAssessmentAspect aspect : aspectList) {
            for(AssessmentGradeRequest grade: request.getGrade()) {
                if (aspect.getId() == grade.getAspectId()){
                    if(grade.getGrade() == null)
                        gradeList.add(new SelfAssessmentGrade(grade.getGradeId(), sa, aspect, 0, grade.getDescription()));
                    else
                        gradeList.add(new SelfAssessmentGrade(grade.getGradeId(), sa, aspect, grade.getGrade(), grade.getDescription()));
                    break;
                }
            }
        }
        selfAssessmentGradeRepository.saveAll(gradeList);
    }

    @Override
    public void createSelfAssessmentAspect(SelfAssessmentAspectRequest request, int creator) {
        SelfAssessmentAspect aspect = new SelfAssessmentAspect();
        aspect.setId(null);
        aspect.setName(request.getName());
        aspect.setDescription(request.getDescription());
        aspect.setStartAssessmentDate(request.getStartAssessmentDate());
        aspect.setEditedBy(creator);
        aspect.setLastEditedDate(LocalDate.now());
        aspect.setStatus(statusRepository.findById((int)request.getStatus()));
        selfAssessmentAspectRepository.save(aspect);
    }

    @Override
    public void updateSelfAssessmentAspect(SelfAssessmentAspectRequest request, int creator) {
        SelfAssessmentAspect aspect = new SelfAssessmentAspect();
        aspect.setId(request.getId());
        aspect.setName(request.getName());
        aspect.setDescription(request.getDescription());
        aspect.setStartAssessmentDate(request.getStartAssessmentDate());
        aspect.setEditedBy(creator);
        aspect.setLastEditedDate(LocalDate.now());
        aspect.setStatus(statusRepository.findById((int)request.getStatus()));
        selfAssessmentAspectRepository.save(aspect);
    }

    @Override
    public CreateId createSupervisorGrade(SupervisorGradeCreateRequest request) {
        SupervisorGrade supervisorGrade = new SupervisorGrade();
        supervisorGrade.setSupervisorId(request.getSupervisorId());
        supervisorGrade.setParticipantId(request.getParticipantId());
        supervisorGrade.setDate(request.getDate());
        supervisorGrade.setPhase(request.getPhase());
        SupervisorGrade temp = supervisorGradeRepository.save(supervisorGrade);
        for(GradeRequest grade:request.getGradeList()){
            SupervisorGradeAspect aspect = supervisorGradeAspectRepository.findById((int)grade.getAspectId());
            supervisorGradeResultRepository.save(new SupervisorGradeResult(null, temp, aspect, grade.getGrade(), aspect.getMaxGrade()));
        }
        return new CreateId(temp.getId());
    }

    @Override
    public void updateSupervisorGrade(SupervisorGradeUpdateRequest request) {
        SupervisorGrade supervisorGrade = supervisorGradeRepository.findById((int)request.getId());
        if(supervisorGrade != null){
            supervisorGrade.setDate(request.getDate());
            supervisorGrade.setPhase(request.getPhase());
            SupervisorGrade temp = supervisorGradeRepository.save(supervisorGrade);
            for (GradeRequest grade : request.getGradeList()) {
                SupervisorGradeAspect aspect = supervisorGradeAspectRepository.findById((int) grade.getAspectId());
                supervisorGradeResultRepository.save(new SupervisorGradeResult(null, temp, aspect, grade.getGrade(), aspect.getMaxGrade()));
            }
        }
    }

    @Override
    public SupervisorGradeDetailResponse getSupervisorGradeDetail(int id) {
        SupervisorGrade supervisorGrade = supervisorGradeRepository.findById(id);
        if(supervisorGrade == null){
            throw new IllegalStateException("Penilaian tidak ditemukan");
        }
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
    public StatisticResponse getMonitoringStatistic(int participantId) {
        //TODO: get all logbook, make percentage
        final Set<DayOfWeek> businessDays = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);

        //get jumlah total logbook yang seharusnya dikumpulkan menggunakan tanggal di deadline
        Deadline logbookDeadline = deadlineRepository.findByNameLike("'%logbook%'");
        int totalLogbook = (int) logbookDeadline.getStartAssignmentDate().datesUntil(logbookDeadline.getFinishAssignmentDate()).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
        int submittedLogbook = logbookRepository.countByParticipantId(participantId);
        int missingLogbook = totalLogbook - submittedLogbook;

        //Nilai Logbook
        HashMap<ENilai, Integer> nilai = new HashMap<>();
        nilai.put(ENilai.SANGAT_BAIK, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.SANGAT_BAIK.id));
        nilai.put(ENilai.BAIK, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.BAIK.id));
        nilai.put(ENilai.CUKUP, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.CUKUP.id));
        nilai.put(ENilai.KURANG, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.KURANG.id));

        //Kedisiplinan Logbook
        Integer onTime = logbookRepository.countStatusOnTime(participantId);
        Integer late = logbookRepository.countStatusLate(participantId);
        Integer match = logbookRepository.countEncounteredProblemNull(participantId);
        Integer notMatch = logbookRepository.countEncounteredProblemNotNull(participantId);

        //get jumlah total self assessment yang seharusnya dikumpulkan menggunakan tanggal di deadline
        Deadline selfAssessmentDeadline = deadlineRepository.findByNameLike("'%self assessment%'");
        int totalSelfAssessment = (int) selfAssessmentDeadline.getStartAssignmentDate().datesUntil(selfAssessmentDeadline.getFinishAssignmentDate()).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
        int submittedSelfAssessment = selfAssessmentRepository.countByParticipantId(participantId);
        int missingSelfAssessment = totalSelfAssessment - submittedSelfAssessment;

        StatisticResponse response = new StatisticResponse(
            getPercentage(submittedLogbook, totalLogbook),
            getPercentage(missingLogbook, totalLogbook),
            getPercentage(onTime, totalLogbook),
            getPercentage(late, totalLogbook),
            getPercentage(match, totalLogbook),
            getPercentage(notMatch, totalLogbook),
            getPercentage(nilai.get(ENilai.SANGAT_BAIK), totalLogbook),
            getPercentage(nilai.get(ENilai.BAIK), totalLogbook),
            getPercentage(nilai.get(ENilai.CUKUP), totalLogbook),
            getPercentage(nilai.get(ENilai.KURANG), totalLogbook),
            getPercentage(submittedSelfAssessment, totalSelfAssessment),
            getPercentage(missingSelfAssessment, totalSelfAssessment)
        );
        return response;
    }

    public Percentage getPercentage(int count, int total){
        float percent = (count/total)*100;
        return new Percentage(count, percent + "%");
    }

    @Override
    public void createSupervisorGradeAspect(SupervisorGradeAspectRequest request, int creator) {
        SupervisorGradeAspect aspect = new SupervisorGradeAspect();
        aspect.setId(null);
        aspect.setMaxGrade(request.getMaxGrade());
        aspect.setDescription(request.getDescription());
        aspect.setEditedBy(creator);
        aspect.setLastEditDate(LocalDate.now());
        aspect.setName(request.getName());
        supervisorGradeAspectRepository.save(aspect);
    }

    @Override
    public void updateSupervisorGradeAspect(SupervisorGradeAspectRequest request, int creator) {
        SupervisorGradeAspect aspect = new SupervisorGradeAspect();
        if(request != null){
            aspect.setId(request.getId());
            aspect.setMaxGrade(request.getMaxGrade());
            aspect.setDescription(request.getDescription());
            aspect.setEditedBy(creator);
            aspect.setLastEditDate(LocalDate.now());
            aspect.setName(request.getName());
            supervisorGradeAspectRepository.save(aspect);
        }
    }

    @Override
    public List<SupervisorGradeAspectResponse> getListSupervisorGradeAspect() {
        List<SupervisorGradeAspect> aspectList = supervisorGradeAspectRepository.findAll();
        List<SupervisorGradeAspectResponse> response = new ArrayList<>();
        for (SupervisorGradeAspect aspect:aspectList){
            response.add(new SupervisorGradeAspectResponse(aspect.getId(), aspect.getDescription(), aspect.getMaxGrade(), aspect.getEditedBy(), aspect.getLastEditDate(), aspect.getName()));
        }
        return response;
    }

    @Override
    public CreateId createLaporan(LaporanCreateRequest laporanCreateRequest, Integer participantId) {
        Laporan laporan = new Laporan();
        laporan.setParticipant(participantId);
        laporan.setUriName(laporanCreateRequest.getUri());
        laporan.setPhase(laporanCreateRequest.getPhase());
        laporan.setUploadDate(LocalDate.now());

        if(laporanRepository.findByParticipantIdAndPhaseOrderByPhaseAsc(participantId, laporanCreateRequest.getPhase()) == null){
            laporan.setId(null);
        }

        Laporan temp = laporanRepository.save(laporan);
        return new CreateId(temp.getId());
    }

    @Override
    public void updateLaporan(LaporanUpdateRequest laporanUpdateRequest, Integer participantId) {
        Laporan laporan = laporanRepository.findById((int)laporanUpdateRequest.getId());
        if(laporanUpdateRequest.getId() == null || laporanUpdateRequest.getId() == 0){
            throw new IllegalStateException("cant edit, id cant be null or 0");
        }
        if(laporan == null){
            throw new IllegalStateException("laporan tidak ditemukan");
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
            throw new IllegalStateException("laporan tidak ditemukan");
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
    public Integer getPhase() {
//        Deadline deadline = deadlineRepository.countLaporanPhaseNow(LocalDate.now());
//        return Integer.valueOf(deadline.getName().charAt(deadline.getName().length() - 1));
        return 0;
    }

    @Override
    public Boolean isLaporanExist(int participantId, int phase) {
        return laporanRepository.isExist(participantId, phase);
    }

    @Override
    public void createSupervisorMapping(List<SupervisorMappingRequest> supervisorMappingRequest, String cookie, int creatorId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.PayloadResponseConstant.COOKIE, cookie);
        HttpEntity<String> req = new HttpEntity<>(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);

        for(SupervisorMappingRequest request:supervisorMappingRequest){
            List<SupervisorMapping> map = new ArrayList<>();
            ResponseEntity<ResponseList<MappingResponse>> mappingRes = restTemplate.exchange("http://mapping-service/mapping/final/company/"+request.getCompanyId(),
                    HttpMethod.GET, req, new ParameterizedTypeReference<>() {});
            List<MappingResponse> mappingResponses = Objects.requireNonNull(mappingRes.getBody()).getData();
            for (MappingResponse mapping : mappingResponses) {
                map.add(new SupervisorMapping(null, LocalDate.now(), request.getCompanyId(), mapping.getParticipantId(), request.getLecturerId(), mapping.getProdiId(), creatorId));
            }
            supervisorMappingRepository.saveAll(map);
        }
    }

    @Override
    public void updateSupervisorMapping(List<SupervisorMappingRequest> supervisorMappingRequest, String cookie, int creatorId) {
        for(SupervisorMappingRequest request:supervisorMappingRequest) {
            List<SupervisorMapping> mapping = supervisorMappingRepository.findByCompanyId(request.getCompanyId());
            List<SupervisorMapping> map = new ArrayList<>();
            for(SupervisorMapping temp:mapping){
                temp.setDate(LocalDate.now());
                temp.setCreateBy(creatorId);
                temp.setLecturerId(request.getLecturerId());
                map.add(temp);
            }
            supervisorMappingRepository.saveAll(map);
        }
    }

    @Override
    public List<HashMap<Integer, String>> getUserList(String cookie, Integer year, String type){
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.PayloadResponseConstant.COOKIE, cookie);
        HttpEntity<String> req = new HttpEntity<>(headers);
        List<HashMap<Integer, String>> user = new ArrayList<>();

       //get participant
        HashMap<Integer, String> participantList = new HashMap<>();

        if(year != null){
            ResponseEntity<ResponseList<ParticipantResponse>> participantRes = restTemplate.exchange("http://participant-service/participant/get-all?year="+year,
                    HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                    });
            List<ParticipantResponse> participantResponseList = Objects.requireNonNull(participantRes.getBody()).getData();
            for (ParticipantResponse participant : participantResponseList) {
                participantList.put(participant.getIdParticipant(), participant.getName());
            }
            user.add(participantList);
        } else if(type.equals("simple") && year == null){
            ResponseEntity<ResponseList<ParticipantDropdownResponse>> participantRes = restTemplate.exchange("http://participant-service/participant/get-all?type=dropdown",
                    HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                    });
            List<ParticipantDropdownResponse> participantResponseList = Objects.requireNonNull(participantRes.getBody()).getData();
            for (ParticipantDropdownResponse participant : participantResponseList) {
                participantList.put(participant.getId(), participant.getName());
            }
            user.add(participantList);
        } else if(type.equals("full") && year == null){
            ResponseEntity<ResponseList<ParticipantResponse>> participantRes = restTemplate.exchange("http://participant-service/participant/get-all",
                    HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                    });
            List<ParticipantResponse> participantResponseList = Objects.requireNonNull(participantRes.getBody()).getData();
            for (ParticipantResponse participant : participantResponseList) {
                participantList.put(participant.getIdParticipant(), participant.getName());
            }
            user.add(participantList);
        }

        //get company
        HashMap<Integer, String> companyList = new HashMap<>();
        ResponseEntity<ResponseList<CompanyResponse>> companyRes = restTemplate.exchange("http://company-service/company/get-all",
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                });
        List<CompanyResponse> companyResponses = Objects.requireNonNull(companyRes.getBody()).getData();
        for (CompanyResponse company : companyResponses) {
            companyList.put(company.getIdCompany(), company.getCompanyName());
        }
        user.add(companyList);

        //get Supervisor
        HashMap<Integer, String> lecturerList = new HashMap<>();
        ResponseEntity<ResponseList<CommitteeResponse>> committeeRes = restTemplate.exchange("http://account-service/account/get-supervisor",
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                });
        List<CommitteeResponse> committeeResponses = Objects.requireNonNull(committeeRes.getBody()).getData();
        for (CommitteeResponse committee : committeeResponses) {
            lecturerList.put(committee.getIdLecturer(), committee.getName());
        }
        user.add(lecturerList);

        return user;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMapping(String cookie) {
        List<SupervisorMapping> mapping = supervisorMappingRepository.findAll();
        List<HashMap<Integer, String>> user = getUserList(cookie, null, "full");
        HashMap<Integer, String> participantList = user.get(0);
        HashMap<Integer, String> companyList = user.get(1);
        HashMap<Integer, String> lecturerList = user.get(2);

        List<SupervisorMappingResponse> response = new ArrayList<>();
        for(SupervisorMapping map:mapping){
            List<SupervisorMapping> temp = supervisorMappingRepository.findByCompanyId(map.getCompanyId());
            List<Participant> participants = new ArrayList<>();
            for(SupervisorMapping temp2 : temp){
                participants.add(new Participant(temp2.getParticipantId(), participantList.get(temp2.getParticipantId())));
            }
            response.add(new SupervisorMappingResponse(
                    map.getCompanyId(), companyList.get(map.getCompanyId()),
                    map.getLecturerId(), lecturerList.get(map.getLecturerId()),
                    map.getProdiId(), map.getDate(), participants)
            );
        }
        return response;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByYear(String cookie, int year) {
        List<SupervisorMapping> mapping = supervisorMappingRepository.findByYear(year);
        List<HashMap<Integer, String>> user = getUserList(cookie, year, "full");
        HashMap<Integer, String> participantList = user.get(0);
        HashMap<Integer, String> companyList = user.get(1);
        HashMap<Integer, String> lecturerList = user.get(2);

        List<SupervisorMappingResponse> response = new ArrayList<>();
        for(SupervisorMapping map:mapping){
            List<SupervisorMapping> temp = supervisorMappingRepository.findByCompanyId(map.getCompanyId());
            List<Participant> participants = new ArrayList<>();
            for(SupervisorMapping temp2 : temp){
                participants.add(new Participant(temp2.getParticipantId(), participantList.get(temp2.getParticipantId())));
            }
            response.add(new SupervisorMappingResponse(
                    map.getCompanyId(), companyList.get(map.getCompanyId()),
                    map.getLecturerId(), lecturerList.get(map.getLecturerId()),
                    map.getProdiId(), map.getDate(), participants)
            );
        }
        return response;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingCommittee(String cookie, int prodi) {
        List<SupervisorMapping> mapping = supervisorMappingRepository.findByProdiGroupByCompanyId(prodi);
        List<HashMap<Integer, String>> user = getUserList(cookie, null, "simple");
        HashMap<Integer, String> participantList = user.get(0);
        HashMap<Integer, String> companyList = user.get(1);
        HashMap<Integer, String> lecturerList = user.get(2);

        List<SupervisorMappingResponse> response = new ArrayList<>();
        for(SupervisorMapping map:mapping){
            List<SupervisorMapping> temp = supervisorMappingRepository.findByCompanyId(map.getCompanyId());
            List<Participant> participants = new ArrayList<>();
            for(SupervisorMapping temp2 : temp){
                participants.add(new Participant(temp2.getParticipantId(), participantList.get(temp2.getParticipantId())));
            }
            response.add(new SupervisorMappingResponse(
                    map.getCompanyId(), companyList.get(map.getCompanyId()),
                    map.getLecturerId(), lecturerList.get(map.getLecturerId()),
                    map.getProdiId(), map.getDate(), participants)
            );
        }
        return response;
    }

    @Override
    public List<SupervisorMappingLecturerResponse> getSupervisorMappingLecturer(String cookie, int lecturerId) {
        List<SupervisorMapping> mapping = supervisorMappingRepository.findByLecturerIdGroupByCompanyId(lecturerId);
        List<HashMap<Integer, String>> user = getUserList(cookie, null, "simple");
        HashMap<Integer, String> participantList = user.get(0);
        HashMap<Integer, String> companyList = user.get(1);
        HashMap<Integer, String> lecturerList = user.get(2);

        List<SupervisorMappingLecturerResponse> response = new ArrayList<>();
        for(SupervisorMapping map:mapping){
            List<SupervisorMapping> temp = supervisorMappingRepository.findByCompanyId(map.getCompanyId());
            List<Participant> participants = new ArrayList<>();
            for(SupervisorMapping temp2 : temp){
                participants.add(new Participant(temp2.getParticipantId(), participantList.get(temp2.getParticipantId())));
            }
            response.add(new SupervisorMappingLecturerResponse(
                    map.getCompanyId(), companyList.get(map.getCompanyId()),
                    map.getProdiId(), map.getDate(), participants)
            );
        }

        return response;
    }

    @Override
    public SupervisorMappingResponse getSupervisorMappingByParticipant(String cookie, int participantId) {
        SupervisorMapping mapping = supervisorMappingRepository.findByParticipantId(participantId);
        List<HashMap<Integer, String>> user = getUserList(cookie, null, "simple");
        HashMap<Integer, String> participantList = user.get(0);
        HashMap<Integer, String> companyList = user.get(1);
        HashMap<Integer, String> lecturerList = user.get(2);

        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(mapping.getParticipantId(), participantList.get(mapping.getParticipantId())));
        SupervisorMappingResponse response = new SupervisorMappingResponse(
                mapping.getCompanyId(), companyList.get(mapping.getCompanyId()),
                mapping.getLecturerId(), lecturerList.get(mapping.getLecturerId()),
                mapping.getProdiId(), mapping.getDate(), participants
        );

        return response;
    }

    @Override
    public Boolean isFinalSupervisorMapping(String cookie, int prodi) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.PayloadResponseConstant.COOKIE, cookie);
        HttpEntity<String> req = new HttpEntity<>(headers);

        ResponseEntity<ResponseList<ParticipantDropdownResponse>> participantRes = restTemplate.exchange("http://participant-service/participant/get-all?type=dropdown",
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                });
        List<ParticipantDropdownResponse> participantResponseList = Objects.requireNonNull(participantRes.getBody()).getData();

        if(participantResponseList.size() == supervisorMappingRepository.countByYear(LocalDate.now().getYear(), prodi))
            return true;
        else
            return false;
    }

    @Override
    public void createDeadline(DeadlineCreateRequest request) {
        deadlineRepository.save(new Deadline(null, request.getName(), request.getDayRange(), request.getStartAssignmentDate(), request.getFinishAssignmentDate()));
    }

    @Override
    public void updateDeadline(DeadlineUpdateRequest request) {
        Deadline deadline = deadlineRepository.findById((int)request.getId());
        if(request.getDayRange() != null)
            deadline.setDayRange(request.getDayRange());
        if(request.getStartAssignmentDate() != null)
            deadline.setStartAssignmentDate(request.getStartAssignmentDate());
        if(request.getFinishAssignmentDate() != null)
            deadline.setFinishAssignmentDate(request.getFinishAssignmentDate());
        deadlineRepository.save(deadline);
    }

    @Override
    public DeadlineResponse getDeadline(int id) {
        Deadline deadline = deadlineRepository.findById(id);
        DeadlineResponse response = new DeadlineResponse(deadline.getId(), deadline.getName(), deadline.getDayRange(), deadline.getStartAssignmentDate(), deadline.getFinishAssignmentDate());
        return response;
    }

    @Override
    public List<DeadlineResponse> getDeadline() {
        List<Deadline> deadline = deadlineRepository.findAll();
        List<DeadlineResponse> response = new ArrayList<>();
        for(Deadline temp:deadline){
            response.add(new DeadlineResponse(temp.getId(), temp.getName(), temp.getDayRange(), temp.getStartAssignmentDate(), temp.getFinishAssignmentDate()));
        }
        return response;
    }

    @Override
    public DashboardParticipant getDashboardDataParticipant(int participantId) {
        DashboardParticipant response = new DashboardParticipant();
        final Set<DayOfWeek> businessDays = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);

        Deadline logbook = deadlineRepository.findByNameLike("logbook");
        long totalLogbook = 0;
        if(LocalDate.now().isAfter(logbook.getFinishAssignmentDate())){
            totalLogbook = logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
        }else{
            totalLogbook = logbook.getStartAssignmentDate().datesUntil(LocalDate.now().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
        }
        response.setLogbookSubmitted(logbookRepository.countByParticipantId(participantId));
        response.setLogbookMissing((int) (totalLogbook - response.getLogbookSubmitted()));

        Deadline selfAssessment = deadlineRepository.findByNameLike("self assessment");
        int totalSelfAssessment = 0;
        if(LocalDate.now().isAfter(selfAssessment.getFinishAssignmentDate())){
            totalSelfAssessment = selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        }else{
            totalSelfAssessment = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        }
        response.setSelfAssessmentSubmitted(selfAssessmentRepository.countByParticipantId(participantId));
        response.setSelfAssessmentMissing(totalSelfAssessment - response.getSelfAssessmentSubmitted());

        int totalLaporan =  deadlineRepository.countLaporanPhaseNow(LocalDate.now());
        response.setLaporanSubmitted(laporanRepository.countByParticipantId(participantId));
        response.setLaporanMissing(totalLaporan - response.getSelfAssessmentSubmitted());
        response.setRppSubmitted(rppRepository.countByParticipantId(participantId));

        return response;
    }

    @Override
    public DashboardLecturer getDashboardDataLecturer(int lecturerId) {
        DashboardLecturer response = new DashboardLecturer();
        final Set<DayOfWeek> businessDays = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
        List<SupervisorMapping> supervisorMapping = supervisorMappingRepository.findByLecturerId(lecturerId);
        List<Integer> participants = new ArrayList<>();
        for(SupervisorMapping map:supervisorMapping){
            participants.add(map.getParticipantId());
        }

        Deadline logbook = deadlineRepository.findByNameLike("logbook");
        int totalLogbook = 0;
        if(LocalDate.now().isAfter(logbook.getFinishAssignmentDate())){
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count() * participants.size();
        }else{
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(LocalDate.now().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count() * participants.size();
        }
        response.setLogbookSubmitted(logbookRepository.countAllInParticipantId(participants));
        response.setLogbookMissing(totalLogbook - response.getLogbookSubmitted());

        Deadline selfAssessment = deadlineRepository.findByNameLike("self assessment");
        int totalSelfAssessment = 0;
        if(LocalDate.now().isAfter(selfAssessment.getFinishAssignmentDate())){
            totalSelfAssessment = selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) * participants.size();
        }else{
            totalSelfAssessment = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) * participants.size();
        }
        response.setSelfAssessmentSubmitted(selfAssessmentRepository.countAllInParticipantId(participants));
        response.setSelfAssessmentMissing(totalSelfAssessment - response.getSelfAssessmentSubmitted());

        int totalLaporan = deadlineRepository.countLaporanPhaseNow(LocalDate.now()) * participants.size();
        response.setLaporanSubmitted(laporanRepository.countAllInParticipantId(participants));
        response.setLaporanMissing(totalLaporan - response.getSelfAssessmentSubmitted());
        response.setRppSubmitted(rppRepository.countAllInParticipantId(participants));
        response.setRppMissing(participants.size() - response.getRppSubmitted());

        return response;
    }

    @Override
    public DashboardCommittee getDashboardDataCommittee(int prodiId) {
        DashboardCommittee response = new DashboardCommittee();
        final Set<DayOfWeek> businessDays = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
        List<SupervisorMapping> supervisorMapping = supervisorMappingRepository.findByProdiId(prodiId);
        List<Integer> participants = new ArrayList<>();
        for(SupervisorMapping map:supervisorMapping){
            participants.add(map.getParticipantId());
        }

        Deadline logbook = deadlineRepository.findByNameLike("logbook");
        int totalLogbook = 0;
        if(LocalDate.now().isAfter(logbook.getFinishAssignmentDate())){
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate()).filter((t -> businessDays.contains(t.getDayOfWeek()))).count() * participants.size();
        }else{
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(LocalDate.now()).filter((t -> businessDays.contains(t.getDayOfWeek()))).count() * participants.size();
        }
        response.setLogbookSubmitted(logbookRepository.countAllInParticipantId(participants));
        response.setLogbookMissing(totalLogbook- response.getLogbookSubmitted());

        Deadline selfAssessment = deadlineRepository.findByNameLike("self assessment");
        int totalSelfAssessment = 0;
        if(LocalDate.now().isAfter(selfAssessment.getFinishAssignmentDate())){
            totalSelfAssessment = selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) * participants.size();
        }else{
            totalSelfAssessment = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) * participants.size();
        }
        response.setSelfAssessmentSubmitted(selfAssessmentRepository.countAllInParticipantId(participants));
        response.setSelfAssessmentMissing(totalSelfAssessment - response.getSelfAssessmentSubmitted());

        int totalLaporan = deadlineRepository.countLaporanPhaseNow(LocalDate.now()) * participants.size();
        response.setLaporanSubmitted(laporanRepository.countAllInParticipantId(participants));
        response.setLaporanMissing(totalLaporan - response.getSelfAssessmentSubmitted());
        response.setRppSubmitted(rppRepository.countAllInParticipantId(participants));
        response.setRppMissing(participants.size() - response.getRppSubmitted());

        return response;
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

}
