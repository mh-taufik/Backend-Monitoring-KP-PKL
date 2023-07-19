package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.*;
import com.jtk.ps.api.model.ERole;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("")
public class MonitoringController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboard(@RequestParam(value = "participant_id", required = false) Integer participantId,HttpServletRequest request) {
        try {
            Integer role = (Integer) request.getAttribute(Constant.VerifyConstant.ID_ROLE);
            if(participantId != null) {
                DashboardParticipant response = monitoringService.getDashboardDataParticipant(participantId);
                return ResponseHandler.generateResponse("get data dashboard participant succeed", HttpStatus.OK, response);
            }else if(role == ERole.PARTICIPANT.id) {
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
                DashboardParticipant response = monitoringService.getDashboardDataParticipant(id);
                return ResponseHandler.generateResponse("get data dashboard participant succeed", HttpStatus.OK, response);
            }else if(role == ERole.SUPERVISOR.id){
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
                DashboardLecturer response = monitoringService.getDashboardDataLecturer(id);
                return ResponseHandler.generateResponse("get data dashboard supervisor succeed", HttpStatus.OK, response);
            }else if(role == ERole.COMMITTEE.id){
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
                String cookie = request.getHeader(Constant.PayloadResponseConstant.COOKIE);
                DashboardCommittee response = monitoringService.getDashboardDataCommittee(id, cookie);
                return ResponseHandler.generateResponse("get data dashboard committee succeed", HttpStatus.OK, response);
            }
            return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.I_AM_A_TEAPOT);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/associated/rpp")
    public ResponseEntity<Object> getAssociatedRpp(@RequestParam("rpp_id") Integer rpp, @RequestParam("participant_id") Integer participant, HttpServletRequest request) {
        try {
            AssociatedDocumentRpp response = monitoringService.getAssociatedRpp(participant, rpp);
            return ResponseHandler.generateResponse("get associated data succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/associated/logbook")
    public ResponseEntity<Object> getAssociatedLogbook(@RequestParam("logbook_id") Integer logbook, @RequestParam("participant_id") Integer participant, HttpServletRequest request) {
        try {
            AssociatedDocumentLogbook response = monitoringService.getAssociatedLogbook(participant, logbook);
            return ResponseHandler.generateResponse("get associated data succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/associated/self-assessment")
    public ResponseEntity<Object> getAssociatedSelfAssessment(@RequestParam("self_assessment_id") Integer selfAssessment, @RequestParam("participant_id") Integer participant, HttpServletRequest request) {
        try {
            AssociatedDocumentSelfAssessment response = monitoringService.getAssociatedSelfAssessment(participant, selfAssessment);
            return ResponseHandler.generateResponse("get associated data succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-hari-libur")
    public ResponseEntity<Object> getHariLiburInMonth(@RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date, HttpServletRequest request) {
        try {
            HashMap<LocalDate, String> response = monitoringService.getHariLiburFromDate(date);
            return ResponseHandler.generateResponse("get succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/document-grade")
    public ResponseEntity<Object> getDocumentGradeStats(@RequestParam("participant_id") Integer participant, HttpServletRequest request) {
        try {
            DocumentGradeStat response = monitoringService.getDocumentGradeStat(participant);
            return ResponseHandler.generateResponse("get associated data succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex) {
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}