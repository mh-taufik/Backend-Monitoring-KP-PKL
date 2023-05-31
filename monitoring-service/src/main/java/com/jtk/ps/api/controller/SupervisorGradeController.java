package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeDetailResponse;
import com.jtk.ps.api.dto.supervisor_grade.SupervisorGradeResponse;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
    @RequestMapping("/supervisor_grade")
public class SupervisorGradeController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/detail/{id_supervisor_grade}")
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

    @GetMapping("/list/{id_participant}")
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
