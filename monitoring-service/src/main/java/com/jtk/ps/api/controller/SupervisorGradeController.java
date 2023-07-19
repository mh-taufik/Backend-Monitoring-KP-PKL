package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.supervisor_grade.*;
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
@RequestMapping("/supervisor-grade")
public class SupervisorGradeController {
    @Autowired
    private IMonitoringService monitoringService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public ResponseEntity<Object> saveSupervisorGrade(@RequestBody SupervisorGradeCreateRequest supervisorGradeCreateRequest, HttpServletRequest request) {
        try {
//            Integer supervisorId = Integer.parseInt(String.valueOf(request.getAttribute(Constant.VerifyConstant.SUB)));
            CreateId id = monitoringService.createSupervisorGrade(supervisorGradeCreateRequest);
            return ResponseHandler.generateResponse("Save SupervisorGrade succeed", HttpStatus.OK, id);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public ResponseEntity<Object> updateSupervisorGrade(@RequestBody SupervisorGradeUpdateRequest supervisorGradeUpdateRequest, HttpServletRequest request) {
        try {
            Integer supervisorId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.updateSupervisorGrade(supervisorGradeUpdateRequest, supervisorId);
            return ResponseHandler.generateResponse("Update SupervisorGrade succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id_supervisor_grade}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getSupervisorGrade(@PathVariable("id_supervisor_grade") Integer idSupervisorGrade, HttpServletRequest request) {
        try {
            SupervisorGradeDetailResponse response = monitoringService.getSupervisorGradeDetail(idSupervisorGrade);
            return ResponseHandler.generateResponse("Get Supervisor Grade succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all/{id_participant}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getSupervisorGradeList(@PathVariable("id_participant") Integer idParticipant, HttpServletRequest request) {
        try {
            List<SupervisorGradeResponse> response = monitoringService.getSupervisorGradeList(idParticipant);
            return ResponseHandler.generateResponse("Get Supervisor Grade List succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistic/{id_participant}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> get(@PathVariable("id_participant") Integer idParticipant, HttpServletRequest request) {
        try {
            StatisticResponse response = monitoringService.getMonitoringStatistic(idParticipant);
            return ResponseHandler.generateResponse("Get statistic succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/aspect/create")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> saveSupervisorGradeAspect(@RequestBody SupervisorGradeAspectRequest supervisorGradeAspectRequest, HttpServletRequest request) {
        try {
            Integer id = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID));
            monitoringService.createSupervisorGradeAspect(supervisorGradeAspectRequest, id);
            return ResponseHandler.generateResponse("Save Supervisor grade aspect succeed", HttpStatus.OK, id);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/aspect/update")
    @PreAuthorize("hasAnyAuthority('COMMITTEE')")
    public ResponseEntity<Object> updateSupervisorGradeAspect(@RequestBody SupervisorGradeAspectRequest supervisorGradeAspectRequest, HttpServletRequest request) {
        try {
            Integer id = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID));
            monitoringService.updateSupervisorGradeAspect(supervisorGradeAspectRequest, id);
            return ResponseHandler.generateResponse("Update Supervisor grade aspect succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aspect/get")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getSupervisorGradeAspect(HttpServletRequest request) {
        try {
            List<SupervisorGradeAspectResponse> response = monitoringService.getListSupervisorGradeAspect();
            return ResponseHandler.generateResponse("Get Supervisor Grade succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
