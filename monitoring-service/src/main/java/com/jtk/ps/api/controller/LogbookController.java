package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.CheckDate;
import com.jtk.ps.api.dto.CreateId;
import com.jtk.ps.api.dto.logbook.*;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/logbook")
public class LogbookController {
    @Autowired
    private IMonitoringService monitoringService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> saveLogbook(@RequestBody LogbookCreateRequest logbookCreateRequest, HttpServletRequest request) {
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            CreateId id = monitoringService.createLogbook(logbookCreateRequest, participantId);
            return ResponseHandler.generateResponse("Save Logbook succeed", HttpStatus.OK, id);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('PARTICIPANT')")
    public ResponseEntity<Object> updateLogbook(@RequestBody LogbookUpdateRequest logbookUpdateRequest, HttpServletRequest request) {
        try {
            Integer participantId = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.updateLogbook(logbookUpdateRequest, participantId);
            return ResponseHandler.generateResponse("Update Logbook succeed", HttpStatus.OK, new CreateId());
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/grade")
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public ResponseEntity<Object> gradeLogbook(@RequestBody LogbookGradeRequest logbookGradeRequest, HttpServletRequest request) {
        try {
            Integer lecturer = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
            monitoringService.gradeLogbook(logbookGradeRequest, lecturer);
            return ResponseHandler.generateResponse("Save Grade Logbook succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all/{participant_id}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getLogbookList(@PathVariable("participant_id") Integer participantId, HttpServletRequest request) {
        try {
            List<LogbookResponse> logbookList = monitoringService.getLogbookByParticipantId(participantId);
            return ResponseHandler.generateResponse("Get Logbook list succeed", HttpStatus.OK, logbookList);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id_logbook}")
    @PreAuthorize("hasAnyAuthority('COMMITTEE','PARTICIPANT','SUPERVISOR')")
    public ResponseEntity<Object> getLogbookDetail(@PathVariable("id_logbook") Integer logbookId, HttpServletRequest request) {
        try {
            LogbookDetailResponse logbook = monitoringService.getLogbookDetail(logbookId);
            return ResponseHandler.generateResponse("Get Logbook succeed", HttpStatus.OK, logbook);
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
            return ResponseHandler.generateResponse("Check date succeed", HttpStatus.OK, monitoringService.isLogbookExist(id, date.getDate()));
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
