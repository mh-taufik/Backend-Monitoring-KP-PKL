package com.jtk.ps.api.controller;

import com.jtk.ps.api.dto.AssociatedDocument;
import com.jtk.ps.api.dto.DashboardCommittee;
import com.jtk.ps.api.dto.DashboardLecturer;
import com.jtk.ps.api.dto.DashboardParticipant;
import com.jtk.ps.api.model.ERole;
import com.jtk.ps.api.service.IMonitoringService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class MonitoringController {
    @Autowired
    private IMonitoringService monitoringService;

    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboardParticipant(HttpServletRequest request) {
        try {
            Integer role = (Integer) request.getAttribute(Constant.VerifyConstant.ID_ROLE);
            if(role == ERole.PARTICIPANT.id) {
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
                DashboardParticipant response = monitoringService.getDashboardDataParticipant(id);
                return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK, response);
            }else if(role == ERole.SUPERVISOR.id){
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID);
                DashboardLecturer response = monitoringService.getDashboardDataLecturer(id);
                return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK, response);
            }else if(role == ERole.COMMITTEE.id){
                Integer id = (Integer) request.getAttribute(Constant.VerifyConstant.ID_PRODI);
                DashboardCommittee response = monitoringService.getDashboardDataCommittee(id);
                return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.OK, response);
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
            AssociatedDocument response = monitoringService.getAssociatedRpp(participant, rpp);
            return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.I_AM_A_TEAPOT);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/associated/logbook")
    public ResponseEntity<Object> getAssociatedLogbook(@RequestParam("logbook_id") Integer logbook, @RequestParam("participant_id") Integer participant, HttpServletRequest request) {
        try {
            AssociatedDocument response = monitoringService.getAssociatedRpp(participant, logbook);
            return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.I_AM_A_TEAPOT);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/associated/self-assessment")
    public ResponseEntity<Object> getAssociatedSelfAssessment(@RequestParam("self_assessment_id") Integer selfAssessment, @RequestParam("participant_id") Integer participant, HttpServletRequest request) {
        try {
            AssociatedDocument response = monitoringService.getAssociatedRpp(participant, selfAssessment);
            return ResponseHandler.generateResponse("get data dashboard succeed", HttpStatus.I_AM_A_TEAPOT);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get")
    public ResponseEntity<Object> verify(HttpServletRequest request) {
        try {
            List<Object> response = new ArrayList<>();
            response.add(request.getAttribute(Constant.VerifyConstant.NAME));
            response.add(request.getAttribute(Constant.VerifyConstant.ID));
            response.add(request.getAttribute(Constant.VerifyConstant.ID_ROLE));
            response.add(request.getAttribute(Constant.VerifyConstant.ID_PRODI));
            response.add(request.getAttribute(Constant.VerifyConstant.STATUS));
            response.add(request.getAttribute(Constant.VerifyConstant.SUB));
            return ResponseHandler.generateResponse("get succeed", HttpStatus.OK, response);
        } catch (HttpClientErrorException ex){
            return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
