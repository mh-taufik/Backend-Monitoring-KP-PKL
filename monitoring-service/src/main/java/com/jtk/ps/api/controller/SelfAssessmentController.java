package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CheckDate;
import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.self_assessment.*;
import com.jtk.ps.api.model.ERole;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/self-assessment")
public class SelfAssessmentController {
    @Autowired
    private IMonitoringService monitoringService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveSelfAssessment(@RequestBody SelfAssessmentRequest selfAssessmentCreateRequest, HttpServletRequest request) {
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            Integer prodi = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
            CreateId id = monitoringService.createSelfAssessment(selfAssessmentCreateRequest, participantId, prodi);
            return ResponseHandler.generateResponse("Save SelfAssessment succeed", HttpStatus.OK, id);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> updateSelfAssessment(@RequestBody SelfAssessmentUpdateRequest selfAssessmentUpdateRequest, HttpServletRequest request) {
        try {
            Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            Integer role = (Integer) request.getAttribute(Constant.VerifyConstant.ID_ROLE);
            Integer prodi = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
            monitoringService.updateSelfAssessment(selfAssessmentUpdateRequest, id, role, prodi);
            return ResponseHandler.generateResponse("Update SelfAssessment succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id_self_assessment}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getSelfAssessmentDetail(@PathVariable("id_self_assessment") Integer idSelfAssessment, HttpServletRequest request) {
        try {
            SelfAssessmentDetailResponse response = monitoringService.getSelfAssessmentDetail(idSelfAssessment);
            return ResponseHandler.generateResponse("Get Self Assessment succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all/{id_participant}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getSelfAssessmentList(@PathVariable("id_participant") Integer idParticipant, HttpServletRequest request) {
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            int prodi = monitoringService.getSupervisorMappingByParticipant(cookie, idParticipant).getProdiId();
            List<SelfAssessmentResponse> response = monitoringService.getSelfAssessmentList(idParticipant, prodi);
            return ResponseHandler.generateResponse("Get All Self Assessment succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get-final-grade/{id_participant}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getFinalSelfAssessmentGrade(@PathVariable("id_participant") Integer idParticipant, HttpServletRequest request) {
        try {
            Integer prodi = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
            SelfAssessmentFinalGradeResponse response = monitoringService.getFinalSelfAssessment(idParticipant, prodi);
            return ResponseHandler.generateResponse("Get Final Self Assessment succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/aspect/create")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> createSelfAssessmentAspect(@RequestBody SelfAssessmentAspectRequest selfAssessmentAspectRequest, HttpServletRequest request) {
        try {
            Integer id = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID));
            monitoringService.createSelfAssessmentAspect(selfAssessmentAspectRequest, id);
            return ResponseHandler.generateResponse("Create Self Assessment Aspect succeed", HttpStatus.OK, id);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/aspect/update")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> updateSelfAssessmentAspect(@RequestBody SelfAssessmentAspectRequest selfAssessmentAspectRequest, HttpServletRequest request) {
        try {
            Integer id = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID));
            monitoringService.updateSelfAssessmentAspect(selfAssessmentAspectRequest, id);
            return ResponseHandler.generateResponse("Update Self Assessment Aspect succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aspect/get")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getSelfAssessmentAspect(@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        try {
            Integer prodi = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
            if(Objects.equals(type,"active")){
                List<SelfAssessmentAspectResponse> response = monitoringService.getActiveSelfAssessmentAspect(prodi);
                return ResponseHandler.generateResponse("Get Self Assessment Aspect Active succeed", HttpStatus.OK, response);
            }
            List<SelfAssessmentAspectResponse> response = monitoringService.getSelfAssessmentAspect(prodi);
            return ResponseHandler.generateResponse("Get Self Assessment Aspect succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/check")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> checkByDate(@RequestBody CheckDate date, HttpServletRequest request){
        try {
            Integer id = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID));
            return ResponseHandler.generateResponse("Check date succeed", HttpStatus.OK, monitoringService.isSelfAssessmentExist(id, date.getDate()));
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/rekap")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','SUPERVISOR')")
    public ResponseEntity<Object> getRekapSelfAssessment(HttpServletRequest request) {
        try {
            String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
            Integer role = (Integer) request.getAttribute(Constant.VerifyConstant.ID_ROLE);
            if(role == ERole.SUPERVISOR.id){
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
                List<SelfAssessmentRekapResponse> response = monitoringService.getRekapSelfAssessment(ERole.SUPERVISOR, id, cookie);
                return ResponseHandler.generateResponse("Get Rekap Self Assessment succeed", HttpStatus.OK, response);
            }
            if(role == ERole.COMMITTEE.id){
                Integer prodiId = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
                List<SelfAssessmentRekapResponse> response = monitoringService.getRekapSelfAssessment(ERole.COMMITTEE, prodiId, cookie);
                return ResponseHandler.generateResponse("Get Rekap Self Assessment succeed", HttpStatus.OK, response);
            }
            return ResponseHandler.generateResponse("Get Rekap Self Assessment Failed", HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
