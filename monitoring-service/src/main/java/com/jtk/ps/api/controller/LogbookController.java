package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.logbook.*;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/logbook")
public class LogbookController {
    @Autowired
    private IMonitoringService monitoringService;

    @PostMapping("/create")
    public ResponseEntity<Object> saveLogbook(@RequestBody LogbookCreateRequest logbookCreateRequest, HttpServletRequest request) {
        try {
            monitoringService.createLogbook(logbookCreateRequest);
            return ResponseHandler.generateResponse("Save Logbook succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateLogbook(@RequestBody LogbookUpdateRequest logbookUpdateRequest, HttpServletRequest request) {
        try {
            monitoringService.updateLogbook(logbookUpdateRequest);
            return ResponseHandler.generateResponse("Update Logbook succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{participant_id}")
    public ResponseEntity<Object> getRppList(@PathVariable("participant_id") Integer participantId, HttpServletRequest request) {
        try {
            List<LogbookResponse> logbookList = monitoringService.getLogbookByParticipantId(participantId);
            return ResponseHandler.generateResponse("Get Logbook list succeed", HttpStatus.OK, logbookList);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{id_logbook}")
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

    @PostMapping("/grade")
    public ResponseEntity<Object> setLogbookGrade(@RequestBody LogbookGradeRequest gradeRequest, HttpServletRequest request){
        try {
            monitoringService.gradeLogbook(gradeRequest);
            return ResponseHandler.generateResponse("Grade Logbook save succeed", HttpStatus.OK);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
