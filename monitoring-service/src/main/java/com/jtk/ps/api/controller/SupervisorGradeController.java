package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.rpp.RppCreateRequest;
import com.jtk.ps.api.dto.rpp.RppUpdateRequest;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeCreateRequest;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeDetailResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeUpdateRequest;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/supervisor/grade")
public class SupervisorGradeController {
    @Autowired
    private IMonitoringService monitoringService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public ResponseEntity<Object> saveSupervisorGrade(@RequestBody SupervisorGradeCreateRequest supervisorGradeCreateRequest, HttpServletRequest request) {
        try {
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
            monitoringService.updateSupervisorGrade(supervisorGradeUpdateRequest);
            return ResponseHandler.generateResponse("Update SupervisorGrade succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id_supervisor_grade}")
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

    @GetMapping("/{id_participant}")
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
}
