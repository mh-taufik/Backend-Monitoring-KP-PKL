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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

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
        if(!rpp.getParticipantId().equals(participantId)){
            throw new IllegalStateException("Rpp tidak dapat diedit");
        }
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        if(rppUpdate.getFinishDate().isAfter(sunday))
            rpp.setFinishDate(rppUpdate.getFinishDate());
        else
            throw new IllegalStateException("tidak bisa mengedit rpp, tanggal harus lebih dari minggu ini");

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

    @Override
    public void updateRpp(RppSimpleUpdateRequest rppUpdate, Integer participantId) {
        Optional<Rpp> rpp = rppRepository.findById((Integer)rppUpdate.getRppId());
        if(rpp.isEmpty())
            throw new IllegalStateException("Rpp tidak ditemukan");
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        if(!rpp.get().getParticipantId().equals(participantId)){
            throw new IllegalStateException("Rpp tidak dapat diedit");
        }
        if(rppUpdate.getFinishDate().isAfter(sunday))
            rpp.get().setFinishDate(rppUpdate.getFinishDate());
        else
            throw new IllegalStateException("tidak bisa mengedit rpp, tanggal harus lebih dari minggu ini");

        rppRepository.save(rpp.get());
    }

    @Override
    public CreateId createRpp(RppSimpleCreateRequest rpp, Integer participantId) {
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

        return new CreateId(temp.getId());
    }

    @Override
    public void createMilestone(List<MilestoneRequest> request, int rppId) {
        Rpp rpp = rppRepository.findById(rppId);
        for(MilestoneRequest milestone:request){
            milestoneRepository.save(new Milestone(null, rpp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
        }
    }

    @Override
    public void updateMilestone(List<MilestoneRequest> request, int rppId) {
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        Rpp rpp = rppRepository.findById(rppId);
        for(MilestoneRequest milestone: request){
            if(milestone.getStartDate().isAfter(sunday) &&  milestone.getId() != null)
                milestoneRepository.save(new Milestone(milestone.getId(), rpp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
        }
    }

    @Override
    public void createDeliverables(List<DeliverablesRequest> request, int rppId) {
        Rpp rpp = rppRepository.findById(rppId);
        for(DeliverablesRequest deliverable:request){
            deliverablesRepository.save(new Deliverable(null, rpp, deliverable.getDeliverables(), deliverable.getDueDate()));
        }
    }

    @Override
    public void updateDeliverables(List<DeliverablesRequest> request, int rppId) {
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        Rpp rpp = rppRepository.findById(rppId);
        for(DeliverablesRequest deliverable:request){
            if(deliverable.getDueDate().isAfter(sunday) && deliverable.getId() != null)
                deliverablesRepository.save(new Deliverable(deliverable.getId(), rpp, deliverable.getDeliverables(), deliverable.getDueDate()));
        }
    }

    @Override
    public void createCompletionSchedule(List<CompletionScheduleRequest> request, int rppId) {
        Rpp rpp = rppRepository.findById(rppId);
        for(CompletionScheduleRequest completionSchedule:request){
            completionScheduleRepository.save(new CompletionSchedule(null, rpp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
        }
    }

    @Override
    public void updateCompletionSchedule(List<CompletionScheduleRequest> request, int rppId) {
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        Rpp rpp = rppRepository.findById(rppId);
        for(CompletionScheduleRequest completionSchedule:request){
            if(completionSchedule.getStartDate().isAfter(sunday) && completionSchedule.getId() != null)
                completionScheduleRepository.save(new CompletionSchedule(completionSchedule.getId(), rpp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
        }
    }

    @Override
    public void createWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId) {
        Rpp rpp = rppRepository.findById(rppId);
        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:request){
            weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(null, rpp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
        }
    }

    @Override
    public void updateWeeklyAchievementPlan(List<WeeklyAchievementPlanRequest> request, int rppId) {
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        Rpp rpp = rppRepository.findById(rppId);
        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:request){
            if(weeklyAchievementPlan.getStartDate().isAfter(sunday) && weeklyAchievementPlan.getId() != null)
                weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(weeklyAchievementPlan.getId(), rpp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
        }
    }

    @Override
    public Boolean isLogbookExist(int participantId, LocalDate date) {
        HashMap<LocalDate, String> hariLibur = getHariLiburFromDate(LocalDate.now());
        if(hariLibur.containsKey(date)) {
            throw new IllegalStateException("Hari ini merupakan " + hariLibur.get(date));
        }
        if(date.getDayOfWeek().name() == SUNDAY.name() || date.getDayOfWeek().name() == SATURDAY.name()) {
            throw new IllegalStateException("Hari ini termasuk weekend, tidak menerima logbook");
        }
        if(logbookRepository.isExist(participantId, date))
            throw new IllegalStateException("logbook sudah ada pada tanggal ini, mohon pilih update");
        return true;
    }

    @Override
    public List<LogbookResponse> getLogbookByParticipantId(int participantId) {
        List<Logbook> logbookList = logbookRepository.findByParticipantIdOrderByDateAsc(participantId);
        if(logbookList.size()==0)
            throw new IllegalStateException("Logbook tidak ditemukan");

        final Set<DayOfWeek> businessDays = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
        Deadline logbook = deadlineRepository.findByNameLike("logbook");
        long totalLogbook = 0;
        List<LocalDate> dateList = new ArrayList<>();
        if(LocalDate.now().isAfter(logbook.getFinishAssignmentDate())){
            totalLogbook = logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
            dateList = logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).collect(Collectors.toList());
        }else{
            totalLogbook = logbook.getStartAssignmentDate().datesUntil(LocalDate.now().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
            dateList = logbook.getStartAssignmentDate().datesUntil(LocalDate.now().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).collect(Collectors.toList());
        }

        List<LogbookResponse> responses = new ArrayList<>();
        for (Logbook temp : logbookList) {
            if (temp.getGrade() != null) {
                responses.add(new LogbookResponse(temp.getId(), temp.getDate(), temp.getGrade().name().replace("_", " "), temp.getStatus().getStatus(), temp.getProjectName()));
                dateList.remove(temp.getDate());
            }else{
                responses.add(new LogbookResponse(temp.getId(), temp.getDate(), "BELUM DINILAI", temp.getStatus().getStatus(), temp.getProjectName()));
                dateList.remove(temp.getDate());
            }
        }

        if(logbookList.size() != totalLogbook){
            Status status = statusRepository.findById(12);
            for(LocalDate date: dateList){
                responses.add(new LogbookResponse(null, date, "BELUM DINILAI", status.getStatus(), null));
            }
        }

        Collections.sort(responses, new Comparator<LogbookResponse>() {
            @Override
            public int compare(LogbookResponse o1, LogbookResponse o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

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
            throw new IllegalStateException("Logbook pada tanggal ini sudah ada, lakukan update");
        }
        if(logbook.getDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Pengumpulan belum dibuka logbook untuk tanggal "+logbook.getDate());
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
            throw new IllegalStateException("Logbook sudah dinilai, tidak dapat diedit");
        }
        Logbook newLogbook = logbookRepository.findById((int)logbook.getId());
        if(!newLogbook.getParticipantId().equals(participantId))
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
        Optional<Integer> supervisor = supervisorMappingRepository.findLecturerId(logbook.getParticipantId());
        if(supervisor.isPresent() && supervisor.get() != lecturer)
            throw new IllegalStateException("Logbook tidak dapat diakses");
        if(logbook.getId() != null){
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
        if(selfAssessmentRepository.isExist(participantId, request.getStartDate()))
            throw new IllegalStateException("Self Assessment sudah terbuat, silahkan update");
        SelfAssessment selfAssessment = new SelfAssessment();
        selfAssessment.setParticipantId(participantId);
        selfAssessment.setStartDate(request.getStartDate());
        selfAssessment.setFinishDate(request.getFinishDate());
        SelfAssessment sa = selfAssessmentRepository.save(selfAssessment);

        List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAll();
        List<SelfAssessmentGrade> gradeList = new ArrayList<>();
        for (SelfAssessmentAspect aspect : aspectList) {
            boolean find = false;
            for(AssessmentGradeRequest grade: request.getGrade()) {
                if (aspect.getId() == grade.getAspectId() && grade != null){
                    find = true;
                    if(grade.getGrade() == null)
                        gradeList.add(new SelfAssessmentGrade(null, sa, aspect, 0, grade.getDescription()));
                    else
                        gradeList.add(new SelfAssessmentGrade(null, sa, aspect, grade.getGrade(), grade.getDescription()));
                    break;
                }
            }
            if(!find)
                gradeList.add(new SelfAssessmentGrade(null, sa, aspect, 0, "-"));
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
        List<LocalDate> dateList = new ArrayList<>();

        final Set<DayOfWeek> businessDays = Set.of(MONDAY);
        Deadline selfAssessment = deadlineRepository.findByNameLike("self assessment");
        int totalSelfAssessment = 0;
        if(LocalDate.now().isAfter(selfAssessment.getFinishAssignmentDate())){
            totalSelfAssessment = selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            dateList = selfAssessment.getStartAssignmentDate().datesUntil(selfAssessment.getFinishAssignmentDate()).filter((t -> businessDays.contains(t.getDayOfWeek()))).collect(Collectors.toList());
        }else{
            totalSelfAssessment = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            dateList = selfAssessment.getStartAssignmentDate().datesUntil(LocalDate.now()).filter((t -> businessDays.contains(t.getDayOfWeek()))).collect(Collectors.toList());
        }

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
            Collections.sort(responses, new Comparator<SelfAssessmentResponse>() {
                @Override
                public int compare(SelfAssessmentResponse o1, SelfAssessmentResponse o2) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                }
            });
            responses.add(new SelfAssessmentResponse(temp.getParticipantId(), temp.getId(), temp.getStartDate(), temp.getFinishDate(), grade));
            dateList.remove(temp.getStartDate());
        }

        if(totalSelfAssessment != selfAssessments.size()){
            for(LocalDate date: dateList){
                responses.add(new SelfAssessmentResponse(idParticipant, null, date, date.plusDays(4), null));
            }
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
            }else{
                grades.add(new SelfAssessmentGradeDetailResponse(
                        aspect.getId(),
                        null,
                        aspect.getName(),
                        0,
                        "-"));
            }
        }
        Collections.sort(grades, new Comparator<SelfAssessmentGradeDetailResponse>() {
            @Override
            public int compare(SelfAssessmentGradeDetailResponse o1, SelfAssessmentGradeDetailResponse o2) {
                return o1.getAspectId().compareTo(o2.getAspectId());
            }
        });
        return grades;
    }

    @Override
    public List<SelfAssessmentGradeDetailResponse> getAverage(int participantId) {
        List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAll();
        List<SelfAssessmentGradeDetailResponse> grades = new ArrayList<>();
        for(SelfAssessmentAspect aspect:aspectList){
            SelfAssessmentGrade grade = selfAssessmentGradeRepository.findAvgGradeByParticipantIdAndAspectId(participantId, aspect.getId());
            if(grade != null){
                grades.add(new SelfAssessmentGradeDetailResponse(
                        grade.getSelfAssessmentAspect().getId(),
                        grade.getId(),
                        grade.getSelfAssessmentAspect().getName(),
                        Math.round(grade.getGrade()),
                        grade.getDescription()));
            }else{
                grades.add(new SelfAssessmentGradeDetailResponse(
                        aspect.getId(),
                        null,
                        aspect.getName(),
                        0,
                        "-"));
            }
        }

        Collections.sort(grades, new Comparator<SelfAssessmentGradeDetailResponse>() {
            @Override
            public int compare(SelfAssessmentGradeDetailResponse o1, SelfAssessmentGradeDetailResponse o2) {
                return o1.getAspectId().compareTo(o2.getAspectId());
            }
        });
        return grades;
    }

    @Override
    public SelfAssessmentFinalGradeResponse getFinalSelfAssessment(int participantId) {
        List<SelfAssessmentGradeDetailResponse> maxGrade = getBestPerformance(participantId);
        List<SelfAssessmentGradeDetailResponse> avgGrade = getAverage(participantId);
        List<SelfAssessmentGradeDetailResponse> finalGrade = new ArrayList<>();
        for(SelfAssessmentGradeDetailResponse temp:maxGrade){
            for(SelfAssessmentGradeDetailResponse temp2:avgGrade){
                if(temp.getAspectId().equals(temp2.getAspectId())){
                    finalGrade.add(new SelfAssessmentGradeDetailResponse(temp2.getAspectId(), null, null, Math.round((temp.getGrade()*60f/100f)+(temp2.getGrade()*40f/100f)), null));
                    break;
                }
            }
        }

        Collections.sort(finalGrade, new Comparator<SelfAssessmentGradeDetailResponse>() {
            @Override
            public int compare(SelfAssessmentGradeDetailResponse o1, SelfAssessmentGradeDetailResponse o2) {
                return o1.getAspectId().compareTo(o2.getAspectId());
            }
        });
        SelfAssessmentFinalGradeResponse response = new SelfAssessmentFinalGradeResponse(maxGrade, avgGrade, finalGrade);
        return response;
    }

    @Override
    public void updateSelfAssessment(SelfAssessmentUpdateRequest request, Integer participantId, Integer role) {
        SelfAssessment selfAssessment = selfAssessmentRepository.findById((int)request.getId());
        if(role == ERole.PARTICIPANT.id){
            if (selfAssessment.getParticipantId() != participantId)
                throw new IllegalStateException("Self Assessment tidak dapat diakses");
            if (selfAssessment.getFinishDate().isAfter(selfAssessment.getStartDate().with(TemporalAdjusters.next(DayOfWeek.SUNDAY))))
                throw new IllegalStateException("Self Assessment melebihi batas deadline, tidak dapat diedit");

            selfAssessment.setId(request.getId());
            selfAssessment.setParticipantId(request.getParticipantId());
            selfAssessment.setStartDate(request.getStartDate());
            selfAssessment.setFinishDate(request.getFinishDate());
            SelfAssessment sa = selfAssessmentRepository.save(selfAssessment);

            List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAll();
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
        }else if(role == ERole.SUPERVISOR.id){
            List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAll();
            List<SelfAssessmentGrade> gradeList = new ArrayList<>();
            for (SelfAssessmentAspect aspect : aspectList) {
                for(AssessmentGradeRequest grade: request.getGrade()) {
                    if (aspect.getId() == grade.getAspectId()){
                        if(grade.getGrade() == null)
                            gradeList.add(new SelfAssessmentGrade(grade.getGradeId(), selfAssessment, aspect, 0, grade.getDescription()));
                        else
                            gradeList.add(new SelfAssessmentGrade(grade.getGradeId(), selfAssessment, aspect, grade.getGrade(), grade.getDescription()));
                        break;
                    }
                }
            }
            selfAssessmentGradeRepository.saveAll(gradeList);
        }
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
        if(!laporanRepository.isExist(request.getParticipantId(), request.getPhase()))
            throw new IllegalStateException("Mahasiswa belum mengumpulkan laporan pada tahap ini!");
        SupervisorGrade supervisorGrade = new SupervisorGrade();
        supervisorGrade.setId(null);
        supervisorGrade.setSupervisorId(0);
        supervisorGrade.setParticipantId(request.getParticipantId());
        supervisorGrade.setDate(LocalDate.now());
        supervisorGrade.setPhase(request.getPhase());
        SupervisorGrade temp = supervisorGradeRepository.save(supervisorGrade);
        for(GradeRequest grade:request.getGradeList()){
            SupervisorGradeAspect aspect = supervisorGradeAspectRepository.findById((int)grade.getAspectId());
            supervisorGradeResultRepository.save(new SupervisorGradeResult(null, temp, aspect, grade.getGrade(), aspect.getMaxGrade()));
        }
        return new CreateId(temp.getId());
    }

    @Override
    public void updateSupervisorGrade(SupervisorGradeUpdateRequest request, int supervisorId) {
        SupervisorGrade supervisorGrade = supervisorGradeRepository.findById((int)request.getId());
        if(supervisorGrade != null){
            supervisorGrade.setDate(LocalDate.now());
            supervisorGrade.setPhase(request.getPhase());
            SupervisorGrade temp = supervisorGradeRepository.save(supervisorGrade);
            for (GradeUpdateRequest grade : request.getGradeList()) {
                Optional<SupervisorGradeResult> result = supervisorGradeResultRepository.findById(grade.getGradeId());
                if(result.isPresent()){
                    result.get().setGrade(grade.getGrade());
                    supervisorGradeResultRepository.save(result.get());
                }
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
            grades.add(new Grade(temp.getAspectGrade().getId(), temp.getAspectGrade().getDescription(), temp.getId(), temp.getGrade()));
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
            List<SupervisorGradeResult> result = supervisorGradeResultRepository.findBySupervisorGradeId(temp.getId());
            List<Grade> grades = new ArrayList<>();
            for(SupervisorGradeResult grade: result){
                grades.add(new Grade(grade.getAspectGrade().getId(), grade.getAspectGrade().getDescription(), grade.getId(), grade.getGrade()));
            }
            response.add(new SupervisorGradeResponse(temp.getId(), temp.getDate(), temp.getPhase(), grades));
        }
        return response;
    }

    @Override
    public StatisticResponse getMonitoringStatistic(int participantId) {
        //TODO: get all logbook, make percentage
        final Set<DayOfWeek> businessDays = Set.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);

        //get jumlah total logbook yang seharusnya dikumpulkan menggunakan tanggal di deadline
        Deadline logbook = deadlineRepository.findByNameLike("logbook");
        int totalLogbook = 0;
        if(LocalDate.now().isAfter(logbook.getFinishAssignmentDate())){
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
        }else{
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(LocalDate.now().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count();
        }
        int submittedLogbook = logbookRepository.countByParticipantId(participantId);
        int missingLogbook = (totalLogbook - submittedLogbook);

        //Nilai Logbook
        HashMap<ENilai, Integer> nilai = new HashMap<>();
        nilai.put(ENilai.SANGAT_BAIK, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.SANGAT_BAIK.id));
        nilai.put(ENilai.BAIK, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.BAIK.id));
        nilai.put(ENilai.CUKUP, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.CUKUP.id));
        nilai.put(ENilai.KURANG, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.KURANG.id));
        nilai.put(ENilai.BELUM_DINILAI, logbookRepository.countByParticipantIdAndGrade(participantId, ENilai.BELUM_DINILAI.id));

        //Kedisiplinan Logbook
        Integer onTime = logbookRepository.countStatusOnTime(participantId);
        Integer late = logbookRepository.countStatusLate(participantId);
        Integer match = logbookRepository.countEncounteredProblemNull(participantId);
        Integer notMatch = logbookRepository.countEncounteredProblemNotNull(participantId);

        //get jumlah total self assessment yang seharusnya dikumpulkan menggunakan tanggal di deadline
        Deadline selfAssessment = deadlineRepository.findByNameLike("self assessment");
        int totalSelfAssessment = 0;
        if(LocalDate.now().isAfter(selfAssessment.getFinishAssignmentDate())){
            totalSelfAssessment = (selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        }else{
            totalSelfAssessment = (LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        }
        int submittedSelfAssessment = selfAssessmentRepository.countByParticipantId(participantId);
        int missingSelfAssessment = totalSelfAssessment - submittedSelfAssessment;
        int apresiasiPerusahaanSelfAssessment = selfAssessmentRepository.countApresiasiPerusahaanNotNull(participantId);

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
            getPercentage(nilai.get(ENilai.BELUM_DINILAI) + missingLogbook, totalLogbook),
            getPercentage(submittedSelfAssessment, totalSelfAssessment),
            getPercentage(missingSelfAssessment, totalSelfAssessment),
            getPercentage(apresiasiPerusahaanSelfAssessment, totalSelfAssessment)
        );
        return response;
    }

    public Percentage getPercentage(int count, int total){
        float percent = (count * 100f) / total;
        return new Percentage(count, Math.round(percent));
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
    public CreateId uploadLaporan(Integer phase, MultipartFile file, Integer participantId) {
        Laporan laporan = new Laporan();
        laporan.setParticipant(participantId);
//        laporan.setUriName(laporanCreateRequest.getUri());
//        laporan.setPhase(laporanCreateRequest.getPhase());
        laporan.setUploadDate(LocalDate.now());

//        if(laporanRepository.findByParticipantIdAndPhaseOrderByPhaseAsc(participantId, laporanCreateRequest.getPhase()) == null){
//            laporan.setId(null);
//        }

//        Laporan temp = laporanRepository.save(laporan);

//        List<SupervisorGradeAspect> aspectList = supervisorGradeAspectRepository.findAll();
//        List<GradeRequest> requests = new ArrayList<>();
//        for(SupervisorGradeAspect aspect:aspectList){
//            requests.add(new GradeRequest(aspect.getId(), 0));
//        }
//        createSupervisorGrade(new SupervisorGradeCreateRequest(temp.getPhase(), temp.getParticipant(), requests));
//
//        return new CreateId(temp.getId());
        return null;
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

        List<SupervisorGradeAspect> aspectList = supervisorGradeAspectRepository.findAll();
        List<GradeRequest> requests = new ArrayList<>();
        for(SupervisorGradeAspect aspect:aspectList){
            requests.add(new GradeRequest(aspect.getId(), 0));
        }
        createSupervisorGrade(new SupervisorGradeCreateRequest(temp.getPhase(), temp.getParticipant(), requests));

        return new CreateId(temp.getId());
    }

    @Override
    public void updateLaporan(LaporanUpdateRequest laporanUpdateRequest, Integer participantId) {
        Laporan laporan = laporanRepository.findById((int)laporanUpdateRequest.getId());
        if(laporanUpdateRequest.getId() == null || laporanUpdateRequest.getId() == 0){
            throw new IllegalStateException("laporan tidak dapat diedit, id harus terisi");
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
            LaporanResponse response = new LaporanResponse(laporan.get());
            response.setSupervisorGrade(supervisorGradeRepository.findByParticipantIdAndPhase(laporan.get().getParticipant(), laporan.get().getPhase()).get().getId());
            return response;
        }else{
            throw new IllegalStateException("laporan tidak ditemukan");
        }
    }

    @Override
    public List<LaporanResponse> getListLaporan(Integer participantId){
        List<Laporan> laporanList = laporanRepository.findByParticipantId(participantId);
        List<LaporanResponse> responses = new ArrayList<>();
        for(Laporan temp:laporanList) {
            Optional<SupervisorGrade> supervisorGrade = supervisorGradeRepository.findByParticipantIdAndPhase(temp.getParticipant(), temp.getPhase());
            responses.add(new LaporanResponse(temp.getId(), temp.getUriName(), temp.getUploadDate(), temp.getPhase(), supervisorGrade.get().getId()));
        }
        return responses;
    }

    @Override
    public Integer getPhase() {
        return deadlineRepository.countLaporanPhaseNow(LocalDate.now());
    }

    @Override
    public Boolean isFinalPhase() {
        Integer phase = deadlineRepository.countLaporanPhaseNow(LocalDate.now());
        if(deadlineRepository.countLaporanPhase() == phase)
            return true;
        return false;
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
        ResponseEntity<ResponseList<CompanyResponse>> companyRes = restTemplate.exchange("http://company-service/company/get-all?type=dropdown",
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                });
        List<CompanyResponse> companyResponses = Objects.requireNonNull(companyRes.getBody()).getData();
        for (CompanyResponse company : companyResponses) {
            companyList.put(company.getId(), company.getName());
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
            companyList.remove(map.getCompanyId());
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
            companyList.remove(map.getCompanyId());
        }

        if(!companyList.isEmpty()){
            for(Integer key: companyList.keySet()){
                response.add(new SupervisorMappingResponse(
                        key, companyList.get(key),
                        null, null, null, null, null)
                );
            }
        }

        return response;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingLecturer(String cookie, int lecturerId) {
        List<SupervisorMapping> mapping = supervisorMappingRepository.findByLecturerIdGroupByCompanyId(lecturerId);
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
    public List<DeadlineResponse> getDeadlineLaporan() {
        List<Deadline> deadline = deadlineRepository.findAllLaporan();
        List<DeadlineResponse> response = new ArrayList<>();
        for(Deadline temp:deadline){
            response.add(new DeadlineResponse(temp.getId(), temp.getName(), temp.getDayRange(), temp.getStartAssignmentDate(), temp.getFinishAssignmentDate()));
        }
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
        response.setLaporanMissing(totalLaporan - response.getLaporanSubmitted());
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
            totalSelfAssessment = (selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR)) * participants.size();
        }else{
            totalSelfAssessment = (LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR)) * participants.size();
        }
        response.setSelfAssessmentSubmitted(selfAssessmentRepository.countAllInParticipantId(participants));
        response.setSelfAssessmentMissing(totalSelfAssessment - response.getSelfAssessmentSubmitted());

        int totalLaporan = deadlineRepository.countLaporanPhaseNow(LocalDate.now()) * participants.size();
        response.setLaporanSubmitted(laporanRepository.countAllInParticipantId(participants));
        response.setLaporanMissing(totalLaporan - response.getLaporanSubmitted());
        response.setRppSubmitted(rppRepository.countAllInParticipantId(participants));
        response.setRppMissing(participants.size() - response.getRppSubmitted());

        return response;
    }

    @Override
    public DashboardCommittee getDashboardDataCommittee(int prodiId, String cookie) {
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
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(logbook.getFinishAssignmentDate().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count() * participants.size();
        }else{
            totalLogbook = (int) logbook.getStartAssignmentDate().datesUntil(LocalDate.now().plusDays(1)).filter((t -> businessDays.contains(t.getDayOfWeek()))).count() * participants.size();
        }
        response.setLogbookSubmitted(logbookRepository.countAllInParticipantId(participants));
        response.setLogbookMissing(totalLogbook- response.getLogbookSubmitted());

        Deadline selfAssessment = deadlineRepository.findByNameLike("self assessment");
        int totalSelfAssessment = 0;
        if(LocalDate.now().isAfter(selfAssessment.getFinishAssignmentDate())){
            totalSelfAssessment = (selfAssessment.getFinishAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR)) * participants.size();
        }else{
            totalSelfAssessment = (LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).get(ChronoField.ALIGNED_WEEK_OF_YEAR) - selfAssessment.getStartAssignmentDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR)) * participants.size();
        }
        response.setSelfAssessmentSubmitted(selfAssessmentRepository.countAllInParticipantId(participants));
        response.setSelfAssessmentMissing(totalSelfAssessment - response.getSelfAssessmentSubmitted());

        int totalLaporan = deadlineRepository.countLaporanPhaseNow(LocalDate.now()) * participants.size();
        response.setLaporanSubmitted(laporanRepository.countAllInParticipantId(participants));
        response.setLaporanMissing(totalLaporan - response.getLaporanSubmitted());
        response.setRppSubmitted(rppRepository.countAllInParticipantId(participants));
        response.setRppMissing(participants.size() - response.getRppSubmitted());

        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.PayloadResponseConstant.COOKIE, cookie);
        HttpEntity<String> req = new HttpEntity<>(headers);
        ResponseEntity<ResponseList<ParticipantDropdownResponse>> participantRes = restTemplate.exchange("http://participant-service/participant/get-all?type=dropdown",
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                });
        Integer totalParticipant = Objects.requireNonNull(participantRes.getBody()).getData().size();
        response.setSupervisorMappingDone(participants.size());
        response.setSupervisorMappingUndone(totalParticipant - participants.size());

        return response;
    }

    @Override
    public AssociatedDocumentRpp getAssociatedRpp(int participantId, int rppId) {
        AssociatedDocumentRpp response = new AssociatedDocumentRpp();
        response.setRpp(getRppDetail(rppId));
        LocalDate start = response.getRpp().getStartDate();
        LocalDate finish = response.getRpp().getFinishDate();

        List<Integer> logbook = logbookRepository.findByParticipantIdAndDateOrderByDateAsc(participantId, start, finish);
        if(logbook.size() != 0){
            List<LogbookDetailResponse> newLogbook = new ArrayList<>();
            for (Integer temp : logbook) {
                newLogbook.add(getLogbookDetail(temp));
            }
            response.setLogbook(newLogbook);
        }

        List<Integer> assessments = selfAssessmentRepository.findIdByParticipanAndDate(participantId, start, finish);
        if(assessments.size() != 0){
            List<SelfAssessmentDetailResponse> newSelfAssessment = new ArrayList<>();
            for (Integer temp2 : assessments) {
                newSelfAssessment.add(getSelfAssessmentDetail(temp2));
            }
            response.setSelfAssessment(newSelfAssessment);
        }

        return response;
    }

    @Override
    public AssociatedDocumentLogbook getAssociatedLogbook(int participantId, int logbookId) {
        AssociatedDocumentLogbook response = new AssociatedDocumentLogbook();
        response.setLogbook(getLogbookDetail(logbookId));

        Optional<Integer> rpp = rppRepository.findByParticipantIdAndDateOrderByDateAsc(participantId, response.getLogbook().getDate());
        if(rpp.isPresent()) {
            response.setRpp(getRppDetail(rpp.get()));
        }

        Optional<Integer> assessment = selfAssessmentRepository.findIdByParticipanAndDateOne(participantId, response.getLogbook().getDate());
        if(assessment.isPresent())
            response.setSelfAssessment(getSelfAssessmentDetail(assessment.get()));

        return response;
    }

    @Override
    public AssociatedDocumentSelfAssessment getAssociatedSelfAssessment(int participantId, int selfAsssessmentId) {
        AssociatedDocumentSelfAssessment response = new AssociatedDocumentSelfAssessment();
        response.setSelfAssessment(getSelfAssessmentDetail(selfAsssessmentId));

        Optional<Integer> rpp = rppRepository.findByParticipantIdAndDateOrderByDateAsc(participantId, response.getSelfAssessment().getStartDate());
        if(rpp.isPresent()) {
            response.setRpp(getRppDetail(rpp.get()));
        }

        List<Integer> logbook = logbookRepository.findByParticipantIdAndDateOrderByDateAsc(participantId, response.getSelfAssessment().getStartDate(), response.getSelfAssessment().getFinishDate());
        List<LogbookDetailResponse> newLogbook = new ArrayList<>();
        for(Integer temp:logbook){
            newLogbook.add(getLogbookDetail(temp));
        }
        response.setLogbook(newLogbook);

        return response;
    }

    @Override
    public DocumentGradeStat getDocumentGradeStat(int participantId) {
        DocumentGradeStat response = new DocumentGradeStat();
        response.setLogbookGraded(logbookRepository.countGradeNotNull(participantId));
        response.setLogbookUngraded(logbookRepository.countGradeNull(participantId));
        if(supervisorGradeResultRepository.countTotalGraded(participantId) != null)
            response.setLaporanGraded(supervisorGradeResultRepository.countTotalGraded(participantId));
        else
            response.setLaporanGraded(0);
        response.setLaporanUngraded(getPhase() - response.getLaporanGraded());
        return response;
    }

    @Override
    public HashMap<LocalDate, String> getHariLiburFromDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        try {
            Document doc = Jsoup.connect("http://kalenderbali.com/hari-penting/?bl="+month+"&th="+year).get();
            Element hariLibur =  doc.select("div#right-sidebar").select("div.foresmall").first();
            String[] split = hariLibur.toString().split("\\n <br>-");
            HashMap<LocalDate, String> result = new HashMap<>();
            Boolean finish = false;
            for(int i=1;i<split.length;i++){
                String[] temp = split[i].split("[ .]");
                LocalDate dateRes = LocalDate.of(Integer.valueOf(temp[3]), EMonth.valueOf(temp[2]).id, Integer.valueOf(temp[1]));
                String descRes = "";
                for(int j=5;j<temp.length;j++){
                    if(temp[j].contains("\n")){
                        descRes += temp[j].replace("\n","");
                        finish = true;
                        break;
                    }
                    if(j == temp.length - 1)
                        descRes += temp[j];
                    else
                        descRes += temp[j] + " ";
                }
                result.put(dateRes, descRes);
                if(finish)
                    break;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}