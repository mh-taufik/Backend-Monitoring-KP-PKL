package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CheckDate;
import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.self_assessment.*;
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
            CreateId id = monitoringService.createSelfAssessment(selfAssessmentCreateRequest, participantId);
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
            monitoringService.updateSelfAssessment(selfAssessmentUpdateRequest, id, role);
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
            List<SelfAssessmentResponse> response = monitoringService.getSelfAssessmentList(idParticipant);
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
            SelfAssessmentFinalGradeResponse response = monitoringService.getFinalSelfAssessment(idParticipant);
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
            if(Objects.equals(type,"active")){
                List<SelfAssessmentAspectResponse> response = monitoringService.getActiveSelfAssessmentAspect();
                return ResponseHandler.generateResponse("Get Self Assessment Aspect Active succeed", HttpStatus.OK, response);
            }
            List<SelfAssessmentAspectResponse> response = monitoringService.getSelfAssessmentAspect();
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
}
