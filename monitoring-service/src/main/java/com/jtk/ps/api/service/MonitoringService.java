package com.jtk.ps.api.service;

import com.jtk.ps.api.dto.*;
import com.jtk.ps.api.dto.deadline.DeadlineCreateRequest;
import com.jtk.ps.api.dto.deadline.DeadlineResponse;
import com.jtk.ps.api.dto.deadline.DeadlineUpdateRequest;
import com.jtk.ps.api.dto.self_assessment.*;
import com.jtk.ps.api.dto.supervisor_grade.*;
import com.jtk.ps.api.dto.supervisor_mapping.Participant;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingCreateRequest;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingResponse;
import com.jtk.ps.api.dto.laporan.LaporanCreateRequest;
import com.jtk.ps.api.dto.laporan.LaporanResponse;
import com.jtk.ps.api.dto.laporan.LaporanUpdateRequest;
import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.dto.rpp.*;
import com.jtk.ps.api.dto.supervisor_mapping.SupervisorMappingUpdateRequest;
import com.jtk.ps.api.model.*;
import com.jtk.ps.api.repository.*;
import com.jtk.ps.api.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.next;
import static java.time.DayOfWeek.SUNDAY;

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
        List<RppResponse> responses = new ArrayList<>();
        for(Rpp temp:rppList){
            responses.add(new RppResponse(temp.getId(), temp.getWorkTitle(), temp.getStartDate(), temp.getFinishDate(), temp.getStatus().getStatus()));
        }
        return responses;
    }

    @Override
    public RppDetailResponse getRppDetail(int id) {
        Rpp rpp = rppRepository.findById(id);
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
    public void createRpp(RppCreateRequest rpp, Integer participantId) {
        Rpp rppNew = new Rpp();
        rppNew.setParticipantId(participantId);
        rppNew.setWorkTitle(rpp.getWorkTitle());
        rppNew.setGroupRole(rpp.getGroupRole());
        rppNew.setTaskDescription(rpp.getTaskDescription());
        rppNew.setStartDate(rpp.getStartDate());
        rppNew.setFinishDate(rpp.getFinishDate());
//        rppNew.setStatus(statusRepository.findById(1));
//        TODO: Status set by date
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

    }

    @Override
    public void updateRpp(RppUpdateRequest rppUpdate) {
        //TODO: check date until this week sunday
        LocalDate sunday = LocalDate.now().with(next(SUNDAY));
        Rpp rpp = rppRepository.findById(rppUpdate.getRppId());
        rpp.setFinishDate(rppUpdate.getFinishDate());
        Rpp temp = rppRepository.save(rpp);

        for(MilestoneRequest milestone:rppUpdate.getMilestones()){
            if(milestone.getStartDate().isAfter(sunday))
                milestoneRepository.save(new Milestone(null, temp, milestone.getDescription(), milestone.getStartDate(), milestone.getFinishDate()));
        }
        for(DeliverablesRequest deliverable:rppUpdate.getDeliverables()){
            if(deliverable.getDueDate().isAfter(sunday))
                deliverablesRepository.save(new Deliverable(null, temp, deliverable.getDeliverables(), deliverable.getDueDate()));
        }
        for(CompletionScheduleRequest completionSchedule:rppUpdate.getCompletionSchedules()){
            if(completionSchedule.getFinishDate().isAfter(sunday))
                completionScheduleRepository.save(new CompletionSchedule(null, temp, completionSchedule.getTaskName(), completionSchedule.getTaskType(), completionSchedule.getStartDate(), completionSchedule.getFinishDate()));
        }
        for(WeeklyAchievementPlanRequest weeklyAchievementPlan:rppUpdate.getWeeklyAchievementPlans()){
            if(weeklyAchievementPlan.getFinishDate().isAfter(sunday))
                weeklyAchievementPlanRepository.save(new WeeklyAchievementPlan(null, temp, weeklyAchievementPlan.getAchievementPlan(), weeklyAchievementPlan.getStartDate(), weeklyAchievementPlan.getFinishDate()));
        }
    }

    @Override
    public Boolean isLogbookExist(int participantId, LocalDate date) {
        return logbookRepository.isExist(participantId, date);
    }

    @Override
    public List<LogbookResponse> getLogbookByParticipantId(int participantId) {
        List<Logbook> logbookList = logbookRepository.findByParticipantId(participantId);
        List<LogbookResponse> responses = new ArrayList<>();
        for(Logbook temp:logbookList){
            responses.add(new LogbookResponse(temp.getId(), temp.getDate(), temp.getGrade().name().replace("_", " "), temp.getStatus().getStatus()));
        }
        return responses;
    }

    @Override
    public LogbookDetailResponse getLogbookDetail(int id) {
        LogbookDetailResponse logbookResponse = new LogbookDetailResponse(logbookRepository.findById(id));
        return logbookResponse;
    }

    @Override
    public void createLogbook(LogbookCreateRequest logbook, Integer participantId) {
        if(logbookRepository.isExist(participantId, logbook.getDate())) {
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
            logbookRepository.save(logbook);
        }
    }

    @Override
    public Boolean isSelfAssessmentExist(int participantId, LocalDate date) {
        return selfAssessmentRepository.isExist(participantId, date);
    }

    @Override
    public List<SelfAssessmentAspectResponse> getSelfAssessmentAspect() {
        List<SelfAssessmentAspect> aspect = selfAssessmentAspectRepository.findAllActiveAspect();
        List<SelfAssessmentAspectResponse> response = new ArrayList<>();
        for(SelfAssessmentAspect temp:aspect){
            response.add(new SelfAssessmentAspectResponse(temp.getId(), temp.getName(), temp.getDescription()));
        }
        return response;
    }

    @Override
    public void createSelfAssessment(SelfAssessmentRequest request, Integer participantId) {
        try {
            SelfAssessment selfAssessment = new SelfAssessment();
            selfAssessment.setParticipantId(participantId);
            selfAssessment.setStartDate(request.getStartDate());
            selfAssessment.setFinishDate(request.getFinishDate());
            SelfAssessment sa = selfAssessmentRepository.save(selfAssessment);

            List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAllActiveAspect();
            List<SelfAssessmentGrade> gradeList = new ArrayList<>();
            for (SelfAssessmentAspect aspect : aspectList) {
                for(AssessmentGradeRequest grade: request.getGrade()) {
                    if (aspect.getId() == grade.getAspectId()){
                        if(grade.getGrade() == null)
                            gradeList.add(new SelfAssessmentGrade(null, sa, aspect, 0, grade.getDescription()));
                        else
                            gradeList.add(new SelfAssessmentGrade(null, sa, aspect, grade.getGrade(), grade.getDescription()));
                        break;
                    }
                }
            }
            selfAssessmentGradeRepository.saveAll(gradeList);
        } catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public SelfAssessmentDetailResponse getSelfAssessmentDetail(int id) {
        SelfAssessment selfAssessment = selfAssessmentRepository.findById(id);
        List<SelfAssessmentGrade> grades = selfAssessmentGradeRepository.findBySelfAssessmentId(id);
        List<SelfAssessmentGradeDetailResponse> aspectList = new ArrayList<>();
        for(SelfAssessmentGrade temp: grades){
            aspectList.add(new SelfAssessmentGradeDetailResponse(
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
        List<SelfAssessmentAspect> aspectList = selfAssessmentAspectRepository.findAllActiveAspect();
        List<SelfAssessmentGradeDetailResponse> grades = new ArrayList<>();
        for(SelfAssessmentAspect aspect:aspectList){
            SelfAssessmentGrade grade = selfAssessmentGradeRepository.findMaxGradeByParticipantIdAndAspectId(participantId);
            grades.add(new SelfAssessmentGradeDetailResponse(
                    grade.getSelfAssessmentAspect().getId(),
                    grade.getId(),
                    grade.getSelfAssessmentAspect().getName(),
                    grade.getGrade(),
                    grade.getDescription()));
        }
        return grades;
    }

    @Override
    public void updateSelfAssessment(SelfAssessmentUpdateRequest request) {
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
    public void createSupervisorGrade(SupervisorGradeCreateRequest request) {
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
        //get jumlah total logbook yang seharusnya dikumpulkan menggunakan tanggal di deadline
        Deadline logbookDeadline = deadlineRepository.findByName("logbook");
//        int total = null;
//        logbookRepository.
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
        //TODO: get all self assessment, make percentage
        //get jumlah total self assessment yang seharusnya dikumpulkan menggunakan tanggal di deadline
    }

    @Override
    public void createLaporan(LaporanCreateRequest laporanCreateRequest, Integer participantId) {
        Laporan laporan = new Laporan();
        laporan.setParticipant(participantId);
        laporan.setUriName(laporanCreateRequest.getUri());
        laporan.setPhase(laporanCreateRequest.getPhase());
        laporan.setUploadDate(LocalDate.now());

        if(laporanRepository.findByParticipantIdAndPhase(participantId, laporanCreateRequest.getPhase()) == null){
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
    public void createSupervisorMapping(List<SupervisorMappingCreateRequest> supervisorMapping, String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.PayloadResponseConstant.COOKIE, cookie);
        HttpEntity<String> req = new HttpEntity<>(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Get is final mapping (Mapping)
        ResponseEntity<Response<Integer>> isFinalMappingD3Request =
                restTemplate.exchange(
                        "http://mapping-service/mapping/get-is-final/1",
                        HttpMethod.GET,
                        req,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        });

        if (isFinalMappingD3Request.getStatusCode().is4xxClientError() || isFinalMappingD3Request.getStatusCode().is5xxServerError()) {
            throw new IllegalStateException("Error when getting is final mapping D3");
        }

//        Integer isFinalMappingD3 = Objects.requireNonNull(isFinalMappingD3Request.getBody()).();

        ResponseEntity<Response<Integer>> isFinalMappingD4Request =
                restTemplate.exchange(
                        "http://mapping-service/mapping/get-is-final/2",
                        HttpMethod.GET,
                        req,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        });

        if (isFinalMappingD4Request.getStatusCode().is4xxClientError() || isFinalMappingD4Request.getStatusCode().is5xxServerError()) {
            throw new IllegalStateException("Error when getting is final mapping D4");
        }

//        Integer isFinalMappingD4 = Objects.requireNonNull(isFinalMappingD4Request.getBody()).getData();

    }

    @Override
    public void updateSupervisorMapping(List<SupervisorMappingUpdateRequest> supervisorMapping) {

    }

    @Override
    public List<HashMap<Integer, String>> getUserList(String cookie){
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.PayloadResponseConstant.COOKIE, cookie);
        HttpEntity<String> req = new HttpEntity<>(headers);
        List<HashMap<Integer, String>> user = new ArrayList<>();

       //get participant
        HashMap<Integer, String> participantList = new HashMap<>();
        ResponseEntity<ResponseList<ParticipantResponse>> participantRes = restTemplate.exchange("http://participant-service/participant/get-all?type=dropdown",
                HttpMethod.GET, req, new ParameterizedTypeReference<>() {
                });
        List<ParticipantResponse> participantResponseList = Objects.requireNonNull(participantRes.getBody()).getData();
        for (ParticipantResponse participant : participantResponseList) {
            participantList.put(participant.getIdParticipant(), participant.getName());
        }
        user.add(participantList);

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
        ResponseEntity<ResponseList<CommitteeResponse>> committeeRes = restTemplate.exchange("http://account-service/get-supervisor",
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
        List<SupervisorMapping> mapping = supervisorMappingRepository.findAllGroupByCompanyId();
        List<HashMap<Integer, String>> user = getUserList(cookie);
        HashMap<Integer, String> participant = user.get(0);
        HashMap<Integer, String> company = user.get(1);
        HashMap<Integer, String> lecturer = user.get(2);
        List<SupervisorMappingResponse> response = new ArrayList<>();
        for(SupervisorMapping map:mapping){
            List<SupervisorMapping> temp = supervisorMappingRepository.findByCompanyId(map.getCompanyId());
            List<Participant> participantList = new ArrayList<>();
            for(SupervisorMapping temp2 : temp){
                participantList.add(new Participant(temp2.getParticipantId(), participant.get(temp2.getParticipantId())));
            }
            response.add(new SupervisorMappingResponse(
                    map.getCompanyId(), company.get(map.getCompanyId()),
                    map.getLecturerId(), lecturer.get(map.getLecturerId()),
                    map.getProdiId(), map.getDate(), participantList)
            );
        }

        return response;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByLecturer(String cookie, int lecturerId) {
//        List<SupervisorMapping> supervisorMapping = supervisorMappingRepository.findByLecturerId(lecturerId);
//        List<SupervisorMappingResponse> response = new ArrayList<>();
//        for(SupervisorMapping temp:supervisorMapping){
//            SupervisorMappingResponse mapping = new SupervisorMappingResponse();
//            mapping.setParticipantId(temp.getParticipantId());
//            mapping.setLecturerId(temp.getLecturerId());
//            mapping.setCompanyId(temp.getCompanyId());
//            mapping.setProdiId(temp.getProdiId());
//            ResponseEntity<ResponseList<ParticipantResponse>> pResponse = restTemplate.exchange(
//                    "http://participant-service/participant/get-all?year=" + currentYear,
//                    HttpMethod.GET,
//                    req,
//                    new ParameterizedTypeReference<>() {
//                    });


//            response.add(mapping);
//        }
        return null;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByCompany(String cookie, int companyId) {
        return null;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByProdi(String cookie, int prodiId) {
        return null;
    }

    @Override
    public List<SupervisorMappingResponse> getSupervisorMappingByYear(String cookie, int year) {
        return null;
    }

    @Override
    public SupervisorMappingResponse getSupervisorMappingByParticipant(int participantId) {
        return null;
    }

    @Override
    public void createDeadline(DeadlineCreateRequest request) {
        deadlineRepository.save(new Deadline(null, request.getName(), request.getDayRange(), request.getStartAssignmentDate(), request.getFinishAssignmentDate()));
    }

    @Override
    public void updateDeadline(DeadlineUpdateRequest request) {
        deadlineRepository.save(new Deadline(request.getId(), request.getName(), request.getDayRange(), request.getStartAssignmentDate(), request.getFinishAssignmentDate()));
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
